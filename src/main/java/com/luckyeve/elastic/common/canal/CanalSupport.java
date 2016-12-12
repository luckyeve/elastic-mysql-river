package com.luckyeve.elastic.common.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.luckyeve.elastic.common.conf.ConfigUtil;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by lixy on 2016/11/24.
 */
public class CanalSupport {

    private CanalConnector connector;
    private String filterRegex;

    public CanalSupport(String address, String destination, String filterRegex) {
        this.filterRegex = filterRegex;
        this.connector = getCanalConnector(address, destination, filterRegex);
    }

    /**
     * 获取连接
     * @return
     */
    public CanalConnector getConnector() {
        return connector;
    }

    /**
     * 获取消息
     * @param batchSize
     * @return
     */
    public Message get(int batchSize) {
        connector = openResource(connector, filterRegex);
        Message message = connector.get(batchSize);
        return message;
    }

    /**
     * 关闭资源
     */
    public void close() {
        try {
            if (connector != null) {
                connector.disconnect();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private CanalConnector getCanalConnector(String address, String destination, String filterRegex) {
        InetSocketAddress[] addresses = ConfigUtil.getSocketAddress(address);
        String user = null;
        String password = null;
        CanalConnector connector = CanalConnectors.newClusterConnector(Arrays.asList(addresses), destination, user, password);
        openResource(connector, filterRegex);
        return connector;
    }

    private CanalConnector openResource(CanalConnector connector, String filterRegex) {
        if (!connector.checkValid()) {
            connector.connect();
            if (filterRegex != null && filterRegex.length() > 0) {
                connector.subscribe(filterRegex);
            }
            //connector.subscribe(".*\\..*");
            //connector.subscribe(".*\\.users,.*\\.page,.*\\.friend,.*\\.group,.*\\.activity,.*\\.user_placestate");
            //connector.rollback();
        }
        return connector;
    }

}
