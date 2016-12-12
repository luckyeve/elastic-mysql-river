package com.luckyeve.elastic.sql2es;

import com.luckyeve.elastic.common.canal.CanalSupport;
import com.luckyeve.elastic.common.es.ElasticSupport;
import com.luckyeve.elastic.common.mysql.MysqlSupport;
import com.luckyeve.elastic.sql2es.config.SqlConfigManager;
import com.luckyeve.elastic.sql2es.custom.MyAfterHandler;
import com.luckyeve.elastic.sql2es.custom.MyBeforeHandler;
import com.luckyeve.elastic.sql2es.handle.MessageReceiverThread;
import com.luckyeve.elastic.sql2es.handle.MysqlReader;

import static com.luckyeve.elastic.sql2es.config.SqlConfigManager.*;

/**
 * Created by lxy on 2016/12/1.
 * @init args=dbName.tableName.uniqueField gte='5263463439' lte=
 * @sync args=null
 */
public class Sql2esBootstrap {

    public static void main(String[] args) {
        if (args.length > 0) {
            init(args);
            return;
        }
        sync();
    }

    /**
     * 同步
     */
    public static void sync() {
        logger.info("sync start");
        // tools
        ElasticSupport elastic = new ElasticSupport(esCluster, esTransportAddress);
        CanalSupport canal = new CanalSupport(canalAddress, canalDestination, canalFilter);
        MysqlSupport mysql = new MysqlSupport(jdbcUrl, jdbcUser, jdbcPassword);
        // receiver
        MessageReceiverThread receiver = new MessageReceiverThread(elastic, canal, SqlConfigManager.canalFilter);
        receiver.addHandler(new MyBeforeHandler(mysql, elastic)); // 提交到elastic前的处理
        receiver.addAfterHandler(new MyAfterHandler()); // 提交后的处理
        receiver.start();
        // 关闭
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                canal.close();
                mysql.close();
                elastic.close();
                logger.info("sync exit");
                System.exit(0);
            }
        });
    }

    /**
     * 初始化
     * @param args dbName.tableName.uniqueField gte='5263463439' lte=
     */
    public static void init(String[] args) {
        logger.info("init start");
        // tools
        ElasticSupport elastic = new ElasticSupport(esCluster, esTransportAddress);
        MysqlSupport mysql = new MysqlSupport(jdbcUrl, jdbcUser, jdbcPassword);
        // init thread
        MysqlReader init = new MysqlReader(mysql, elastic, args);
        init.addHandler(new MyBeforeHandler(mysql, elastic)); // 提交到elastic前的处理
        init.addAfterHandler(new MyAfterHandler()); // 提交后的处理
        // ShutdownHook
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                mysql.close();
                elastic.close();
                logger.info("init exit");
                System.exit(0);
            }
        });
        init.start();
    }

}
