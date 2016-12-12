package com.luckyeve.elastic.common.es;

import com.luckyeve.elastic.common.conf.ConfigUtil;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixy on 2016/11/24.
 */
public class ElasticSupport {

    private ImmutableOpenMap<String, IndexMetaData> indices;

    private TransportClient client;

    public ElasticSupport(String clusterName, String address) {
        this.client = getEsTransportClient(clusterName, address);
    }

    /**
     *  获取连接
     * @return
     */
    public TransportClient getTransportClient() {
        return client;
    }

    public void close() {
        if (client != null) {

        }
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
    }

    /**
     * 获取连接
     * @return
     */
    private TransportClient getEsTransportClient(String clusterName, String address) {
        // 获取地址
        InetSocketAddress[] arr = ConfigUtil.getSocketAddress(address);
        List<InetSocketTransportAddress> addresses = new ArrayList<InetSocketTransportAddress>();
        for (InetSocketAddress ar: arr) {
            addresses.add(new InetSocketTransportAddress(ar.getAddress(), ar.getPort()));
        }
        if (addresses.size() == 0) {
            throw new IllegalArgumentException("address is null");
        }
        // 配置管理
        Settings settings = Settings.builder().put("client.transport.sniff", true).put("cluster.name", clusterName).build();
        TransportClient client = new PreBuiltTransportClient(settings);
        for (InetSocketTransportAddress add : addresses) {
            client.addTransportAddresses(add);
        }
        return client;
    }

    /**
     * 批量提交文档（添加或删除）
     * @param docs
     */
    public BulkResponse addDocs(List<DocumentModel> docs) {
        if (docs == null || docs.size() == 0) {
            return null;
        }
        BulkRequestBuilder bulk = client.prepareBulk();
        for (DocumentModel doc: docs) {
            if (doc == null) continue;
            // 文档必须有索引和主键
            if (doc.getIndex() == null || doc.getType() == null || doc.getId() == null) {
                continue;
            }
            if (doc.getRequestType() == null || doc.getRequestType() == RequestType.ADD) {
                // 更新全部或添加文档
                if (doc.getSource() == null) {
                    continue;
                }
                IndexRequestBuilder indexRequest = client.prepareIndex(doc.getIndex(), doc.getType()).setSource(doc.getSource()).setId(doc.getId());
                bulk.add(indexRequest);
            } else if(doc.getRequestType() == RequestType.UPDATE) {
                if (doc.getSource() == null) {
                    continue;
                }
                // 局部更新
                UpdateRequestBuilder updateRequest = client.prepareUpdate(doc.getIndex(), doc.getType(), doc.getId()).setDoc(doc.getSource());
                bulk.add(updateRequest);
            } else if (doc.getRequestType() == RequestType.DELETE) {
                // 删除文档
                DeleteRequestBuilder deleteRequest = client.prepareDelete(doc.getIndex(), doc.getType(), doc.getId());//.setOperationThreaded(false);
                bulk.add(deleteRequest);
            }
        }
        try {
            BulkResponse bulkResponse = bulk.execute().actionGet();
            return bulkResponse;
        } catch (Exception e) {
            //logger.error(docs.size() + "---{}", (docs.size() > 0 ? JSONObject.toJSONString(docs.get(0)) : ""));
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取mapping的属性
     * @param index
     * @param type
     * @return
     */
    public Map<String, Object> getMappingProperties(String index, String type) {
        try {
            MappingMetaData metaData = getMappingMetaData(client, index, type);
            if (metaData != null) {
                Object properties = metaData.getSourceAsMap().get("properties");
                if (properties instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) properties;
                    return map;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>();
    }

    private MappingMetaData getMappingMetaData(TransportClient client, String index, String type) {
        if (index == null || type == null) {
            return null;
        }
        if (indices == null) {
            ClusterStateResponse response = client.admin().cluster().prepareState().execute().actionGet();
            indices = response.getState().getMetaData().getIndices();
        }
        if (indices.get(index) != null) {
            MappingMetaData metaData = indices.get(index).getMappings().get(type);
            return metaData;
        }
        return null;
    }


}
