package com.luckyeve.elastic.sql2es.config;

import com.luckyeve.elastic.common.conf.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lixy on 2016/11/15.
 */
public class SqlConfigManager {

    public static final Logger logger = LoggerFactory.getLogger(SqlConfigManager.class);

    // mongo
    public static final String mongoAddress;
    public static final String mongoDbName;
    public static final String mongoUsername;
    public static final String mongoPassword;
    // mongodb-sync-filter
    public static final String nosql2esFilter;

    // es
    public static final String esCluster;
    public static final String esTransportAddress;

    // canal
    public static final String canalAddress;
    public static final String canalDestination;
    public static final String canalFilter;

    // mysql
    public static final String jdbcUrl;
    public static final String jdbcUser;
    public static final String jdbcPassword;

    // neo4j
    public static final String neo4jServerRootUrl;
    public static final String neo4jUserName;
    public static final String neo4jPassword;

    // rabbitmq
    public static final String rabbitAddress;
    public static final String rabbitUsername;
    public static final String rabbitPassword;

    public static Lock lock = new ReentrantLock();

    static {
        Properties properties = ConfigUtil.getProperties("lrconfig-{}.properties");
        // mongo
        mongoAddress = properties.getProperty("mongodb.address");
        mongoDbName = properties.getProperty("mongodb.dbName");
        mongoUsername = properties.getProperty("mongodb.username");
        mongoPassword = properties.getProperty("mongodb.password");
        // nosql2es.filter
        nosql2esFilter = properties.getProperty("mongodb.filter");
        // es
        esCluster = properties.getProperty("es.cluster.name");
        esTransportAddress = properties.getProperty("es.transport.address");
        // canal
        canalAddress = properties.getProperty("canal.address");
        canalDestination = properties.getProperty("canal.destination");
        canalFilter = properties.getProperty("canal.filter");
        // mysql
        jdbcUrl = properties.getProperty("jdbc.url");
        jdbcUser = properties.getProperty("jdbc.user");
        jdbcPassword = properties.getProperty("jdbc.password");
        // neo4j
        neo4jServerRootUrl = properties.getProperty("neo4j.serverRootUrl");
        neo4jUserName = properties.getProperty("neo4j.userName");
        neo4jPassword = properties.getProperty("neo4j.password");
        // rabbitmq
        rabbitAddress = properties.getProperty("rabbitmq.adddress");
        rabbitUsername = properties.getProperty("rabbitmq.username");
        rabbitPassword = properties.getProperty("rabbitmq.password");
    }


}
