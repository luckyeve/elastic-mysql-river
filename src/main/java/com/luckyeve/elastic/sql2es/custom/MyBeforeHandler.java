package com.luckyeve.elastic.sql2es.custom;

import com.luckyeve.elastic.common.bean.MessageEntry;
import com.luckyeve.elastic.common.es.ElasticSupport;
import com.luckyeve.elastic.common.mysql.MysqlSupport;
import com.luckyeve.elastic.sql2es.handle.IMessageHandler;

import java.util.List;

/**
 * 文档提交到elastic前的处理
 * Created by lxy on 2016/12/1.
 */
public class MyBeforeHandler implements IMessageHandler {

    private MysqlSupport mysql;
    private ElasticSupport elastic;

    public MyBeforeHandler(MysqlSupport mysql, ElasticSupport elastic) {
        this.mysql = mysql;
        this.elastic = elastic;
    }

    @Override
    public void handle(List<MessageEntry> changes) {
        // 获取可能需要处理的表记录
//        Map<String, MessageEntry> users = new HashMap<String, MessageEntry>();
//        Map<String, MessageEntry> friends = new HashMap<String, MessageEntry>();
//        for (MessageEntry change : changes) {
//            if (change.getDoc() == null) continue;
//            String table = change.getDoc().getType();
//            switch (table) {
//                case "users":
//                    users.put(change.getDoc().getId(), change);
//                    break;
//                case "friends":
//                    friends.put(change.getDoc().getId(), change);
//                    break;
//            }
//        }
//        // 添加好友性别
//        addFriendsName(friends);
//        // 更新性别
//        updateFriendName(users, elastic.getTransportClient());
    }

//    /**
//     * 添加用户昵称
//     * @param friends
//     */
//    private void addFriendsName(Map<String, MessageEntry> friends) {
//        try {
//            // funames
//            Set<String> funames = new HashSet<String>();
//            friends.forEach((k,v)->{
//                if (v.getDoc() == null || v.getDoc().getSource() == null) {
//                    return;
//                }
//                String funame = StringUtil.parseString(v.getDoc().getSource().get("funame"), null);
//                if (funame != null) {
//                    funames.add(funame);
//                }
//            });
//            // 昵称
//            Map<String, String> nicknames = MysqlQueryComp.selectUserNicknames(funames, mysql);
//            friends.forEach((k, v) -> {
//                String nickname = nicknames.get(k);
//                if (nickname != null) {
//                    v.getDoc().getSource().put("fnickname", nicknames.get(k));
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 修改用户昵称时更新到好友列表
//     * @param users
//     */
//    private void updateFriendName(Map<String, MessageEntry> users, TransportClient client) {
//        try {
//            Map<String, String> nicknames = new HashMap<String, String>();
//            final String[] indexes = new String[1];
//            users.forEach((k, v) -> {
//                if (CanalEntry.EventType.UPDATE.equals(v.getHeader().getEventType())) {
//                    indexes[0] = v.getDoc().getIndex();
//                    List<CanalEntry.Column> columns = v.getRowData().getAfterColumnsList();
//                    columns.forEach(e -> {
//                        if (e.getUpdated() && "nickname".equals(e.getName())) {
//                            String id = v.getDoc().getId();
//                            String nickname = e.getValue();
//                            if (nickname != null && id != null) {
//                                nicknames.put(id, nickname);
//                            }
//                        }
//                    });
//                }
//            });
//            if (nicknames.size() == 0 && indexes[0] != null) {
//                return;
//            }
//            nicknames.forEach((funame, fnickname) -> {
//                if (funame != null && fnickname != null) {
//                    BoolQueryBuilder query = QueryBuilders.boolQuery();
//                    query.must(QueryBuilders.termsQuery("funame", funame));
//                    Script script = new Script("ctx._source.fnickname");
//                    UpdateByQueryRequestBuilder builder = UpdateByQueryAction.INSTANCE.newRequestBuilder(client).script(script).abortOnVersionConflict(true).abortOnVersionConflict(false);
//                    builder.source().setIndices(indexes[0]).setTypes("friends").setQuery(query);
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
}
