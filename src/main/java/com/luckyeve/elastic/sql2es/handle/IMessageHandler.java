package com.luckyeve.elastic.sql2es.handle;

import com.luckyeve.elastic.common.bean.MessageEntry;

import java.util.List;

/**
 * Created by lixy on 2016/11/24.
 */
public interface IMessageHandler {

    void handle(List<MessageEntry> changes);

}
