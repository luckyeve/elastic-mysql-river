## 名称：elastic-mysql-river

## 描述：
 - 环境 jdk 1.8、elasticsearch 5.0.1、canal 1.0.12、mysql 5.1.39
 - 指定要过滤的数据库和表
 - 创建elasticsearch中表映射
 - 启动canal服务，参考alibaba/canal.git
 - 启动MYSQL-RIVER。接收canal消息，根据filter和mapping指定的表字段过滤后，写入es。

## 启动
- 配置文件：lrconfig.properties
- 创建库： curl -XPUT localhost:9200/yourdb
- 创建同步的字段： curl -XPUT localhost:9200/yourdb/_mapping/tableName -d '{...}'
- 编译： mvn clean package
- **同步**： java -Dconfig=local -jar mysql-river.jar
- **初始化**： java -Dconfig=local -jar mysql-init.jar yourdb.tableName.field>12345

## lrconfig-{}.properties
    # es
    es.cluster.name=cluster5
    es.transport.address=139.xxx.xxx.80:9300

    # canal
    canal.address=192.168.1.12:11111
    canal.destination=canal2
    canal.filter=yourdb\\.tableName,yourdb\\.friends_*

    # mysql
    jdbc.url=jdbc:mysql://192.168.1.12:3306/yourdb
    jdbc.user=root
    jdbc.password=yourpasswd

    # neo4j relation map
    #neo4j.serverRootUrl=http://192.168.1.12:7474/db/data/
    #neo4j.userName=neo4j
    #neo4j.password=123456

## log4j2.properties、logback.xml

    日志

## meta.dat

	作用：canal服务的config文件夹下，记录最后一次bin-log同步的位置，参考alibaba/canal.git

