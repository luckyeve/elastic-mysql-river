package com.luckyeve.elastic.common.bean;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.luckyeve.elastic.common.es.DocumentModel;

/**
 * Created by lixy on 2016/11/24.
 */
public class MessageEntry {

    private CanalEntry.Header header;
    private CanalEntry.RowData rowData;
    private DocumentModel doc;

    public MessageEntry() {
    }

    public MessageEntry(CanalEntry.Header header, CanalEntry.RowData rowData, DocumentModel doc) {
        this.header = header;
        this.rowData = rowData;
        this.doc = doc;
    }

    public CanalEntry.Header getHeader() {
        return header;
    }

    public void setHeader(CanalEntry.Header header) {
        this.header = header;
    }

    public DocumentModel getDoc() {
        return doc;
    }

    public void setDoc(DocumentModel doc) {
        this.doc = doc;
    }

    public CanalEntry.RowData getRowData() {
        return rowData;
    }

    public void setRowData(CanalEntry.RowData rowData) {
        this.rowData = rowData;
    }
}
