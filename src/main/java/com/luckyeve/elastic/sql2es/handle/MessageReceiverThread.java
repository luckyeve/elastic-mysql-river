package com.luckyeve.elastic.sql2es.handle;

import com.alibaba.otter.canal.protocol.Message;
import com.luckyeve.elastic.common.bean.MessageEntry;
import com.luckyeve.elastic.common.canal.CanalSupport;
import com.luckyeve.elastic.common.es.ElasticSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lixy on 2016/11/24.
 */
public class MessageReceiverThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(MessageReceiverThread.class);

    private String canalFilter;

    private CanalSupport canal;
    private ElasticSupport elastic;
    private int batchSize = 1000;

    private boolean run;

    private List<IMessageHandler> handlers;
    private List<IMessageHandler> afterHandlers;
    private MessagePoccessor processor;

    public MessageReceiverThread(ElasticSupport elastic, CanalSupport canal, String canalFilter) {
        this.elastic = elastic;
        this.canal = canal;
        this.canalFilter = canalFilter;
        this.handlers = new ArrayList<IMessageHandler>();
        this.afterHandlers = new ArrayList<IMessageHandler>();
        this.processor = new MessagePoccessor();
    }

    public void addHandler(IMessageHandler handler) {
        handlers.add(handler);
    }

    public void addAfterHandler(IMessageHandler handler) {
        afterHandlers.add(handler);
    }

    @Override
    public void run() {
        run = true;
        long emptyCount = 0;
        int tryNum = 0;
        while (run) {
            try {
                // 获取指定数量的数据
                long batchId = 0L;
                Message message = canal.get(batchSize);
                int size = message.getEntries().size();
                // 记录为空
                if (message.getId() == -1 || size == 0) {
                    emptyCount++;
                    if (emptyCount % 25 == 0) {
                        logger.info("[canal] empty count : " + emptyCount);
                        if (emptyCount >= Integer.MAX_VALUE) {
                            emptyCount = 0;
                        }
                    }
                    sleepy(1000);
                    continue;
                } else {
                    // 非空，执行handler
                    emptyCount = 0;
                    try {
                        List<MessageEntry> changes = processor.process(elastic, message);
                        for (IMessageHandler handler : handlers) {
                            handler.handle(changes);
                        }
                        processor.write(elastic, changes);
                        for (IMessageHandler handler : afterHandlers) {
                            handler.handle(changes);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        sleepy(100);
                    }
                }
                tryNum = 0;
            } catch (Exception e) {
                logger.error("time=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:dd").format(new Date()));
                logger.error(e.getMessage(), e);
                if (tryNum >= 200) { // 连续失败200次，强制结束程序
                    this.run = false;
                    logger.info("error too many times, force stop, {}", emptyCount);
                }
                sleepy(100);
            }
        }
        this.run = false;
        canal.close();
    }


    private void sleepy(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
