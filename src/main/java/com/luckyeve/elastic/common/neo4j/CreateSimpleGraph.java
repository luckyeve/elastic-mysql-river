package com.luckyeve.elastic.common.neo4j;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luckyeve.elastic.common.util.StringUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * rest api
 * Created by lixy on 2016/7/18.
 */
public class CreateSimpleGraph {

    private static final Logger logger = LoggerFactory.getLogger(CreateSimpleGraph.class);

    private String SERVER_ROOT_URI;
    private String USER_NAME;
    private String PASSWORD;

    public CreateSimpleGraph(String serverRootUrl, String userName, String password) {
        this.SERVER_ROOT_URI = serverRootUrl;
        this.USER_NAME = userName;
        this.PASSWORD = password;
        if (StringUtils.isEmpty(SERVER_ROOT_URI)) {
            logger.error("neo4j.serverRootUrl is null");
            SERVER_ROOT_URI = "http://localhost:7474/db/data/";
            USER_NAME = "neo4j";
            PASSWORD = "123456";
            logger.error("neo4j use [" + SERVER_ROOT_URI + "]");
        } else if (!SERVER_ROOT_URI.endsWith("/")){
            SERVER_ROOT_URI += SERVER_ROOT_URI + "/";
        }

        if (StringUtils.isEmpty(USER_NAME)) {
            USER_NAME = null;
        }
        if (StringUtil.isEmpty(PASSWORD)) {
            PASSWORD = null;
        }
    }

//    public static void main(String[] args) throws URISyntaxException {
//        // 数据库检查
//        checkDatabaseIsRunning();
//        // 创建节点1
//        Map<String, Object> user = new HashMap<>();
//        user.put("uname", 1);
//        user.put("nickname", "tom");
//        user.put("sex", 0);
//        user.put("birthday", "1998-05-01");
//        user.put("city", 23);
//        URI node1 = createNode(user);
//        setNodeLabel(node1, "User");
//        // 创建节点2
//        user.put("uname", 2);
//        user.put("nickname", "jerry");
//        user.put("sex", 0);
//        user.put("birthday", "1996-03-02");
//        user.put("city", 23);
//        URI node2 = createNode(user);
//        setNodeLabel(node2, "User");
//        // 创建关系
//        try {
//            addRelationship(node1, node2, "FRIEND", null);
//            addRelationship(node2, node1, "FRIEND", null);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        // 任意查询
//        JSONObject json = cypherData("match(n) where n.uname = 1 return n;");
//        System.out.println(json);
//        // 查询首节点
//        URI node3 = getFirstNode("match(n) where n.uname = 2 return n;");
//        URI node4 = getFirstNode("match(n) where n.uname = 1 return n;");
//        System.out.println(node3);
//        System.out.println(node4);
//        // 修改关系
//        addRelationship(node3, node4, "FRIEND", null);
//    }

    /**
     * 获取首节点
     * @param query
     * @return
     */
    public URI getFirstNode(String query) {
        URI uri = null;
        JSONObject json = cypherData(query);
        Object data = json.get("data");
        if (data instanceof JSONArray) {
            JSONArray arry = (JSONArray) data;
            if (arry.size() == 0) {
                return uri;
            }
            Object node = arry.get(0);
            if (node instanceof JSONArray) {
                node = ((JSONArray) node).get(0);
            }
            if (node instanceof JSONObject) {
                JSONObject nodeo = (JSONObject) node;
                String self = nodeo.getString("self");
                if (!StringUtil.isEmpty(self)) {
                    uri = URI.create(self);
                }
            } else if (node instanceof Number){
                uri = URI.create(SERVER_ROOT_URI + "node/" + node);
            }
        }
        return uri;
    }


    public URI createNode(Map<String, Object> nodeData) {
        final String nodeEntryPointUri = SERVER_ROOT_URI + "node";//"node"; cypher
        WebResource resource = Client.create().resource(nodeEntryPointUri);
        auth(resource);
        // POST {} to the node entry point URI
        nodeData = nodeData == null? new HashMap<String, Object>(): nodeData;
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(JSONObject.toJSONString(nodeData))
                .post(ClientResponse.class);
        final URI location = response.getLocation();
        logger.debug(String.format("POST to [%s], status code [%d], location header [%s]", nodeEntryPointUri, response.getStatus(), location == null? null: location.toString()));
        response.close();
        return location;
    }

    public boolean executeBatch(JSONArray batchData) {
        if (batchData == null || batchData.size() == 0) {
            return true;
        }
        final String nodeEntryPointUri = SERVER_ROOT_URI + "batch";
        WebResource resource = Client.create().resource(nodeEntryPointUri);
        auth(resource);
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(batchData.toJSONString())
                .post(ClientResponse.class);
        int status = response.getStatus();
        logger.debug(String.format("POST batch, status code [%d]", response.getStatus()));
        response.close();
        if (status == 200) {
            return true;
        }
        return false;
    }


    /**
     * 添加节点属性
     * @param nodeUri
     * @param nodeData
     */
    public boolean addNodeProperties(URI nodeUri, Map<String, Object> nodeData) {
        String propertyUri = nodeUri.toString() + "/properties";
        WebResource resource = Client.create().resource(propertyUri);
        auth(resource);
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(JSONObject.toJSONString(nodeData))
                .put(ClientResponse.class);
        logger.debug(String.format("PUT to [%s], status code [%d]", propertyUri, response.getStatus()));
        response.close();
        if (response.getStatus() == 200 || response.getStatus() == 204) {
            return true;
        }
        return false;
    }

    public boolean setNodeLabel(URI uri, String label) {
        final String nodeEntryPointUri = uri.toString() + "/labels";
        WebResource resource = Client.create().resource(nodeEntryPointUri);
        auth(resource);
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity("[\""+label+"\"]")
                .post(ClientResponse.class);
        response.close();
        if (response.getStatus() == 200 || response.getStatus() == 204) {
            return true;
        }
        return false;
    }

    /**
     * 检查数据库运行状态
     *
     * @return
     */
    public boolean checkDatabaseIsRunning() {
        WebResource resource = Client.create().resource(SERVER_ROOT_URI);
        auth(resource);
        ClientResponse response = resource.get(ClientResponse.class);
        response.close();
        logger.debug(String.format("GET on [%s], status code [%d]", SERVER_ROOT_URI, response.getStatus()));
        if (response.getStatus() == 200) {
            return true;
        }
        return false;
    }

    /**
     * 添加节点关系
     * @param startNode
     * @param endNode
     * @param relationshipType
     * @param jsonAttributes
     * @return
     * @throws URISyntaxException
     */
    public URI addRelationship(URI startNode, URI endNode, String relationshipType, String jsonAttributes) throws URISyntaxException {
        URI fromUri = new URI(startNode.toString() + "/relationships");
        String relationshipJson = generateJsonRelationship(endNode, relationshipType, jsonAttributes);
        WebResource resource = Client.create().resource(fromUri);
        auth(resource);
        // POST JSON to the relationships URI
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(relationshipJson)
                .post(ClientResponse.class);
        final URI location = response.getLocation();
        System.out.println(String.format("POST to [%s], status code [%d], location header [%s]", fromUri, response.getStatus(), location == null? null: location.toString()));
        response.close();
        return location;
    }

    /**
     * cypher查询
     * @param query
     * @return
     * @throws URISyntaxException
     */
    public JSONObject cypherData(String query) {
        final String nodeEntryPointUri = SERVER_ROOT_URI + "cypher";
        WebResource resource = Client.create().resource(nodeEntryPointUri);
        auth(resource);
        // POST {} to the node entry point URI
        JSONObject entity = new JSONObject();
        entity.put("query", query);
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(entity.toJSONString())
                .post(ClientResponse.class);
        String rs = response.getEntity(String.class);
        JSONObject result = JSONObject.parseObject(rs);
        logger.debug(String.format("POST to [%s], query [%s], status code [%d], location header [%s]", nodeEntryPointUri, query, response.getStatus(), result));
        response.close();
        return result;
    }

    /**
     * 获取map列表
     * @param cypherResult
     * @return
     */
    public List<JSONObject> toMapList(JSONObject cypherResult) {
        List<JSONObject> result = new ArrayList<JSONObject>();
        if (cypherResult == null) {
            return result;
        }
        Object cols = cypherResult.get("columns");
        if (!(cols instanceof JSONArray)) {
            return result;
        }
        String[] columns = ((JSONArray) cols).toArray(new String[]{});
        Object data = cypherResult.get("data");
        if (!(data instanceof JSONArray)) {
            return result;
        }
        for (int i = 0; i < ((JSONArray) data).size(); i++) {
            JSONArray row = ((JSONArray) data).getJSONArray(i);
            JSONObject map = new JSONObject();
            for (int j = 0; j < row.size(); j++) {
                String name = columns[j];
                Object value = row.get(j);
                if (value instanceof JSONArray) {
                    value = ((JSONArray) value).toArray();
                }
                map.put(name, value);
            }
            result.add(map);
        }
        return result;
    }

    private String generateJsonRelationship(URI endNode, String relationshipType, String... jsonAttributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"to\" : \"");
        sb.append(endNode.toString());
        sb.append("\", ");
        sb.append("\"type\" : \"");
        sb.append(relationshipType);
        if (jsonAttributes == null || jsonAttributes.length < 1) {
            sb.append("\"");
        } else {
            sb.append("\", \"data\" : ");
            for (int i = 0; i < jsonAttributes.length; i++) {
                sb.append(jsonAttributes[i]);
                if (i < jsonAttributes.length - 1) { // Miss off the final comma
                    sb.append(", ");
                }
            }
        }
        sb.append(" }");
        return sb.toString();
    }

    /**
     * 密码认证
     * @param resource
     */
    private void auth(WebResource resource) {
        if (!StringUtils.isEmpty(USER_NAME) && !StringUtil.isEmpty(PASSWORD)) {
            resource.addFilter(new HTTPBasicAuthFilter(USER_NAME, PASSWORD));
        }
    }

    public String toJsonNameValuePairs(Map<String, Object> data) {
        if (data == null) {
            return "{}";
        }
        StringBuffer str = new StringBuffer();
        for (Map.Entry<String, Object> entry: data.entrySet()) {
            String value = entry.getValue() == null? null: entry.getValue().toString();
            if (!(entry.getValue() instanceof Number)) {
                value = "\"" + value + "\"";
            }
            str.append(entry.getKey()).append(":").append(value).append(",");
        }
        if (str.length() > 0) {
            str.deleteCharAt(str.length() - 1);
        }
        return "{" + str + "}";
    }

    public String toStringNameValuePairs(Map<String, Object> data) {
        if (data == null || data.size() == 0) {
            return "accessTime=timestamp()";
        }
        StringBuffer str = new StringBuffer();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            if (entry.getValue() instanceof List || entry.getValue() instanceof Array) {
                value = entry.getValue().toString();
            } else if (!(entry.getValue() instanceof Number) && value != null) {
                value = "\"" + value + "\"";
            }
            str.append("n." + entry.getKey()).append("=").append(value).append(",");
        }
        if (str.length() > 0) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString() + " ";
    }

    public List<Long> parseLongList(Object obj) {
        List<Long> result = new ArrayList<Long>();
        if (obj == null) return result;
        if (obj instanceof List) {
            List array = (List) obj;
            if (array != null) {
                for (Object ar: array) {
                    if (ar == null) result.add(null);
                    else result.add(Long.parseLong(ar.toString()));
                }
            }
        } else {
            Object[] array = (Object[]) obj;
            if (array != null) {
                for (Object ar: array) {
                    if (ar == null) result.add(null);
                    else result.add(Long.parseLong(ar.toString()));
                }
            }
        }
        return result;
    }


}
