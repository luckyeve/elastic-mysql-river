package com.luckyeve.elastic.common.rabbitmq;


import com.alibaba.fastjson.JSONObject;
import com.luckyeve.elastic.common.util.StringUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by lixy on 2016/11/28.
 */
public class RabbitmqSupport {

    private String address;
    private String userName;
    private String password;
    private Connection connection;
    private Channel channel;

    public RabbitmqSupport(String adddress, String userName, String password) {
        try {
            this.address = adddress;
            this.userName = userName;
            this.password = password;
            this.connection = newConnection(adddress, userName, password);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void bind(String exchangeName, String type, String queueName, String routingKey) throws Exception {
        Channel channel = getChannal();
        boolean durable = true;
        declareBind(channel, exchangeName, type, durable, queueName, routingKey);
    }

    public void convertAndSend(String exchangeName, String routingKey, Map<String, Object> message) throws Exception {
        Channel channel = getChannal();
        String body = JSONObject.toJSONString(message);
        publishJson(channel, exchangeName, routingKey, body);
    }

    public void close() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Channel getChannal() {
        try {
            if (connection == null || !connection.isOpen()) {
                connection = newConnection(address, userName, password);
            }
            if (channel == null || !channel.isOpen()) {
                channel = connection.createChannel();
            }
            return channel;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private ConnectionFactory newConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setAutomaticRecoveryEnabled(true);
        factory.setTopologyRecoveryEnabled(true);
        factory.setConnectionTimeout(30000);
        factory.setHandshakeTimeout(10000);
        factory.setNetworkRecoveryInterval(30000);
        return factory;
    }

    private Connection newConnection(String address, String userName, String password) throws IOException, TimeoutException {
        ConnectionFactory factory = newConnectionFactory();
        if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(password)) {
            factory.setUsername(userName);
            factory.setPassword(password);
        }
        Address[] addresses = Address.parseAddresses(address);
        Connection connection = factory.newConnection(addresses);
        return connection;
    }

    private void declareBind(Channel channel, String exchangeName, String type, Boolean durable, String queueName, String routingKey) throws IOException {
        channel.exchangeDeclare(exchangeName, type, durable);
        boolean exclusive = false; // 不仅限此连接
        boolean autoDelete = false; // 长时间不用不删除
        channel.queueDeclare(queueName, durable, exclusive, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
    }

    private void publishJson(Channel channel, String exchangeName, String routingKey, String json) throws IOException {
        byte[] body = json.getBytes();
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .contentEncoding("utf-8")
                .contentType("application/json")
                .build();
        channel.basicPublish(exchangeName, routingKey, properties, body);
    }

}
