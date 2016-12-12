package com.luckyeve.elastic.sql2es.handle;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.luckyeve.elastic.common.bean.MessageEntry;
import com.luckyeve.elastic.common.es.DocumentModel;
import com.luckyeve.elastic.common.es.ElasticSupport;
import com.luckyeve.elastic.common.es.RequestType;
import com.luckyeve.elastic.common.util.StringUtil;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixy on 2016/11/24.
 */
class MessagePoccessor {

    private static final Logger logger = LoggerFactory.getLogger(MessagePoccessor.class);

    List<MessageEntry> process(ElasticSupport elastic, Message message) {
        try {
            List<MessageEntry> changes = new ArrayList<MessageEntry>();
            // changes updates
            for (CanalEntry.Entry entry: message.getEntries()) {
                if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                    continue;
                }
                // 库名 表名
                CanalEntry.Header header = entry.getHeader();
                if (header == null || header.getSchemaName() == null || header.getTableName() == null || header.getTableName().length() == 0) continue;
                /* 获取行 */
                CanalEntry.RowChange rchange = null;
                try {
                    rchange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                } catch (Exception e) {
                    logger.error("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
                    continue;
                }
                // mapping properties
                Map<String, Object> properties = elastic.getMappingProperties(header.getSchemaName(), header.getTableName());
                if (properties.size() == 0) continue;
                // 获取有变化的行
                CanalEntry.EventType eventType = rchange.getEventType();
                for (CanalEntry.RowData rowData: rchange.getRowDatasList()) {
                    if (eventType == CanalEntry.EventType.DELETE) {
                        List<CanalEntry.Column> columns = rowData.getBeforeColumnsList();
                        DocumentModel doc = getUpdateDoc(columns, properties, header.getSchemaName(), header.getTableName(), RequestType.DELETE);
                        MessageEntry change = new MessageEntry(header, rowData, doc);
                        changes.add(change);
                    } else if (eventType == CanalEntry.EventType.INSERT || eventType == CanalEntry.EventType.UPDATE) {
                        List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
                        DocumentModel doc = getUpdateDoc(columns, properties, header.getSchemaName(), header.getTableName(), RequestType.ADD);
                        MessageEntry change = new MessageEntry(header, rowData, doc);
                        changes.add(change);
                    }
                }
            }
            return changes;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    void write(ElasticSupport elastic, List<MessageEntry> changes) {
        List<DocumentModel> docs = new ArrayList<DocumentModel>();
        for (MessageEntry change: changes) {
            if (change.getDoc() != null) {
                docs.add(change.getDoc());
            }
        }
        elastic.addDocs(docs);
    }

    /**
     * 解析文档
     * @param columns
     * @param properties
     * @param index
     * @param type
     * @param requestType
     * @return
     */
    private DocumentModel getUpdateDoc(List<CanalEntry.Column> columns, Map<String, Object> properties, String index, String type, RequestType requestType) {
        if (columns == null) return null;
        DocumentModel doc = new DocumentModel();
        StringBuffer key = new StringBuffer();
        Double longitude = null;
        Double latitude = null;
        Map<String, Object> changes = new HashMap<String, Object>();
        for (CanalEntry.Column column: columns) {
            if (column.getIsKey()) {

                key.append(column.getValue()).append("_");
            }
            if ("longitude".equals(column.getName())) {
                longitude = StringUtil.parseDouble(column.getValue(), null);
            } else if ("latitude".equals(column.getName())) {
                latitude = StringUtil.parseDouble(column.getValue(), null);
            }
            if (properties.keySet().contains(column.getName())) {
                changes.put(column.getName(), column.getValue());
            }
        }
        if (longitude != null && latitude != null) {
            GeoPoint point = new GeoPoint(latitude, longitude);
            properties.forEach((k, v) -> {
                if (v instanceof Map && "geo_point".equals(((Map)v).get("type"))) {
                    changes.put(k, point);
                    return;
                }
            });
        }
        if (key.length() > 1) {
            key.deleteCharAt(key.length() - 1);
            doc.setIndex(index);
            doc.setType(type);
            doc.setRequestType(requestType);
            doc.setId(key.toString());
            doc.setSource(changes);
            return doc;
        }
        return null;
    }

}
