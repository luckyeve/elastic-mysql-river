package com.luckyeve.elastic.sql2es.handle;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.luckyeve.elastic.common.bean.MessageEntry;
import com.luckyeve.elastic.common.es.ElasticSupport;
import com.luckyeve.elastic.common.mysql.JDBCTypesUtils;
import com.luckyeve.elastic.common.mysql.MysqlSupport;
import com.luckyeve.elastic.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by lxy on 2016/12/7.
 */
public class MysqlReader {

    private static final Logger logger = LoggerFactory.getLogger(MysqlReader.class);

    private MysqlSupport mysql;
    private ElasticSupport elastic;

    private MessagePoccessor processor;
    private List<IMessageHandler> handlers;
    private List<IMessageHandler> afterHandlers;

    private String[] params;

    public MysqlReader(MysqlSupport mysql, ElasticSupport elastic, String[] args) {
        this.mysql = mysql;
        this.elastic = elastic;
        this.processor = new MessagePoccessor();
        this.handlers = new ArrayList<IMessageHandler>();
        this.afterHandlers = new ArrayList<IMessageHandler>();
        this.params = parseArg(args);
        if (this.params.length == 0) {
            throw new IllegalArgumentException("program arguments error");
        }
    }

    public void start() {
        read(params, mysql);
    }

    public void addHandler(IMessageHandler handler) {
        handlers.add(handler);
    }

    public void addAfterHandler(IMessageHandler handler) {
        afterHandlers.add(handler);
    }

    private Message parseMessage(List<Map<String, Object>> rows, String database, String table, Set<String> primaryKeys) {
        CanalEntry.Entry.Builder entry = CanalEntry.Entry.newBuilder();
        entry.setEntryType(CanalEntry.EntryType.ROWDATA);
        CanalEntry.Header.Builder header = CanalEntry.Header.newBuilder();
        header.setSchemaName(database);
        header.setTableName(table);
        header.setEventType(CanalEntry.EventType.INSERT);
        entry.setHeader(header);

        CanalEntry.RowChange.Builder rowChange = CanalEntry.RowChange.newBuilder();
        rowChange.setEventType(CanalEntry.EventType.INSERT);

        for (Map<String, Object> row: rows) {
            if (row.size() == 0) continue;
            // make rowdata
            CanalEntry.RowData.Builder rowData = CanalEntry.RowData.newBuilder();
            row.forEach((k, v) -> {
                if (v == null) return;
                CanalEntry.Column.Builder column = CanalEntry.Column.newBuilder();
                column.setName(k);
                column.setValue(v.toString());
                column.setSqlType(JDBCTypesUtils.getSqlType(v.getClass()));
                if (primaryKeys.contains(k)) {
                    column.setIsKey(true);
                }
                rowData.addAfterColumns(column);
            });
            rowChange.addRowDatas(rowData);
        }

        entry.setStoreValue(rowChange.build().toByteString());

        Message message = new Message(1);
        message.addEntry(entry.build());
        return message;
    }

    private void process(Message message) {
        List<MessageEntry> changes = processor.process(elastic, message);
        for (IMessageHandler handler : handlers) {
            handler.handle(changes);
        }
        processor.write(elastic, changes);
        for (IMessageHandler handler : afterHandlers) {
            handler.handle(changes);
        }
    }

    private void read(String[] params, MysqlSupport mysql) {
        logger.info("start");
        String sql0 = makeSql0("*", params[0], params[1], params[2], params[3], params[4]);
        Object lastCursor = null;
        long count = 0;
        int size = 100;
        Set<String> primaryKeys = mysql.selectPrimaryKeys(params[0], params[1]);
        do {
            long curr = System.currentTimeMillis();
            String sql = makeSql(sql0, params[2], lastCursor, size);
            List<Map<String, Object>> list = mysql.select(sql, null);
            count += list.size();
            logger.debug(sql + ";" + count + ";+" + (System.currentTimeMillis() - curr));
            if (list.size() == 0) break;
            lastCursor = list.get(list.size() - 1).get(params[2]);
            Message message = parseMessage(list, params[0], params[1], primaryKeys);
            process(message);
        } while(lastCursor != null);
        logger.info("success");
    }

    /**
     * 解析初始化参数
     * @param args yourdb.tableName.uniqueField gte='5263463439' lte='935263463439'
     * @return params
     */
    private static String[] parseArg(String[] args) {
        if (args.length < 1) return new String[0];
        String[] arr = args[0].split("\\.");
        String[] params = new String[5];
        String database = arr[0].trim();
        String tableName = arr[1].trim();
        String fieldName = arr[2].trim();
        String gte = null;
        String lte = null;
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            String[] arr2 = arg.split("=");
            if (arr2.length != 2) continue;
            if (arg.contains("gte")) {
                gte = arr2[1].trim();
            } else if (arg.contains("lte")) {
                lte = arr2[1].trim();
            }
        }
        params[0] = database;
        params[1] = tableName;
        params[2] = fieldName;
        params[3] = gte;
        params[4] = lte;
        return params;
    }

    private static String makeSql0(String fields, String database, String tableName, String fieldName, String gte, String lte) {
        String sql = "select " + fields + " from " + database + "." + tableName + " where 1=1 ";
        if (gte != null) {
            sql += " and " + fieldName + ">=" + gte;
        }
        if (lte != null) {
            sql += " and " + fieldName + "<=" + lte;
        }
        return sql;
    }

    private static String replace(Object value) {
        if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Timestamp) {
            value = DateUtil.TimestampFormat.format(new Date(((Timestamp) value).getTime()));
            return "'" + value + "'";
        } else if (value instanceof Date) {
            value = DateUtil.TimestampFormat.format((Date) value);
            return "'" + value + "'";
        } else {
            return "'" + value + "'";
        }
    }

    private static String makeSql(String sql, String sortField, Object lastCursor, int limit) {
        String condition2 = "";
        if (lastCursor != null) {
            condition2 = "and " + sortField + " > " + replace(lastCursor);
        }
        String sql0 = sql + condition2 + " order by " + sortField + " asc limit " + limit;
        return sql0;
    }


}
