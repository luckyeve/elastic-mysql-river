package com.luckyeve.elastic.sql2es.custom;

import com.luckyeve.elastic.common.bean.MessageEntry;
import com.luckyeve.elastic.sql2es.handle.IMessageHandler;

import java.util.List;

/**
 * 文档提交到elastic后的处理
 * Created by lxy on 2016/12/1.
 */
public class MyAfterHandler implements IMessageHandler {

    public MyAfterHandler() {
    }

    @Override
    public void handle(List<MessageEntry> changes) {
//        // 写入到用户关系图（users修改时，更新到关系图属性；friends修改更新关系图路径）
//        sendUserAndFriendsGraph(users, friends);
//        // 发送性别更新的mq
//        sendUserSexUpdateMq(users, rabbit);
    }

//    private void sendUserAndFriendsGraph(Map<String, MessageEntry> users, Map<String, MessageEntry> friends) {
//        try {
//            // 用户节点添加、修改
//            List<Map<String, Object>> userNodes = new ArrayList<Map<String, Object>>();
//            users.forEach((k, v) -> {
//                DocumentModel doc = v.getDoc();
//                if (doc == null || !RequestType.ADD.equals(doc.getRequestType()))
//                    return;
//                Long uname = StringUtil.parseLong(doc.getId(), null);
//                if (uname == null) return;
//                String nickname = StringUtil.parseString(doc.getSource().get("nickname"), null);
//                Integer sex = StringUtil.parseInteger(doc.getSource().get("Sex"), null);
//                Integer zone = StringUtil.parseInteger(doc.getSource().get("zone"), null);
//                Long pageId = StringUtil.parseLong(doc.getSource().get("v3_page_id"), null);
//                Map<String, Object> user = new HashMap<String, Object>();
//                user.put("uname", uname);
//                user.put("nickname", nickname);
//                user.put("sex", sex);
//                user.put("zone", zone);
//                user.put("v3_page_id", pageId);
//                userNodes.add(user);
//            });
//            FriendRecommendComp.addUsers(userNodes, graph);
//            logger.debug("addUserGraph [{}]", JSONObject.toJSON(userNodes));
//            // 关系增加修改
//            List<Map<String, Long>> relations = new ArrayList<Map<String, Long>>();
//            friends.forEach((k, v) -> {
//                // 好友添加删除
//                DocumentModel doc = v.getDoc();
//                if (doc == null) return;
//                Long uname = StringUtil.parseLong(doc.getSource().get("Uname"), null);
//                Long funame = StringUtil.parseLong(doc.getSource().get("funame"), null);
//                Long type = StringUtil.parseLong(doc.getSource().get("type"), FriendRecommendComp.USER_FRIEND_TYPE_NOT_FRIEND);
//                if (uname == null || funame == null) return;
//                Map<String, Long> relation = new HashMap<String, Long>();
//                relation.put("uname", uname);
//                relation.put("funame", funame);
//                if (RequestType.DELETE.equals(doc.getRequestType())) {
//                    relation.put("type", FriendRecommendComp.USER_FRIEND_TYPE_NOT_FRIEND);
//                } else {
//                    relation.put("type", type);
//                }
//                relations.add(relation);
//            });
//            logger.debug("addFriendRelationGraph [{}]", JSONObject.toJSON(relations));
//            FriendRecommendComp.addFriendRelations(relations, graph);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
//
//    private void sendUserSexUpdateMq(Map<String, MessageEntry> users, RabbitmqSupport rabbit) {
//        try {
//            Map<Long, Map> updates = new HashMap<Long, Map>();
//            users.forEach((k, v) -> {
//                if (!CanalEntry.EventType.UPDATE.equals(v.getHeader().getEventType())) {
//                    return;
//                }
//                CanalEntry.RowData row = v.getRowData();
//                Long uname = StringUtil.parseLong(v.getDoc().getId(), null);
//                Integer sex = null;
//                for (CanalEntry.Column column: row.getAfterColumnsList()) {
//                    if (column.getUpdated() && "Sex".equals(column.getName())) {
//                        sex = StringUtil.parseInteger(column.getValue(), null);
//                    }
//                }
//                if (uname != null && sex != null) {
//                    Map<String, Object> user = new HashMap<String, Object>();
//                    user.put("uname", uname);
//                    user.put("sex", sex);
//                    updates.put(uname, user);
//                }
//            });
//            for (Map event: updates.values()) {
//                logger.info("send event " + JSONObject.toJSONString(event));
//                rabbit.convertAndSend("canal-data-exchange", "", event);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
}
