package com.luckyeve.elastic.common.mysql;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;


/**
 * dao support
 * @author lixy
 *
 */
public class MysqlSupport  { // extends ResKey
	
	private static Logger logger = LoggerFactory.getLogger(MysqlSupport.class);

	private String driverClassName;

	private String url;

	private String username;

	private String password;

	private DataSource dataSource;

	public MysqlSupport(String url, String username, String password) {
		this.url = url;
		this.driverClassName = "com.mysql.jdbc.Driver";
		this.username = username;
		this.password = password;
		this.dataSource = getDataSource();
	}
	/**
	 * 获取源
	 * @return
	 */
	private DataSource getDataSource() {
		if (dataSource == null) {
			if (dataSource != null) {
				return dataSource;
			}
			DataSource dataSource = new DataSource();
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			dataSource.setMaxActive(30);
			dataSource.setMaxIdle(30);
			dataSource.setMinIdle(10);
			dataSource.setInitialSize(10);
			dataSource.setValidationQuery("SELECT 1");
			dataSource.setTestOnBorrow(true);
			dataSource.setTestOnReturn(true);
			dataSource.setTestWhileIdle(true);
			this.dataSource = dataSource;
		}
		return dataSource;
	}

	private void close(Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close(Connection conn) {
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close(DataSource dataSource) {
		if (dataSource != null) {
			dataSource.close();
		}
	}

	/**
	 * 关闭连接
	 */
	public void close() {
		close(dataSource);
	}

	/**
	 * 查询记录
	 * @param sql
	 * @param args
	 * @return 有错误会抛出
	 */
	public List<Map<String, Object>> select(String sql, Object[] args) {
		logger.debug("select sql : " + sql);
		logger.debug("select args: " + Arrays.toString(args));

		List<Map<String, Object>> maps = new LinkedList<Map<String, Object>>();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;

		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			stmt = conn.prepareStatement(sql);
			if(args != null && args.length > 0){
				for(int i = 0; i < args.length; i++){
					stmt.setObject(i + 1, args[i]);
				}
			}

			rs = stmt.executeQuery();
			meta = rs.getMetaData();
			int count = meta.getColumnCount();

			while(rs.next()){
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for(int i = 0; i < count; i++){
					String columnLabal = meta.getColumnLabel(i + 1);
					String columnName = meta.getColumnName(i + 1);
					if (columnLabal != null && columnLabal.length() > 0) {
						map.put(columnLabal, rs.getObject(i+1));
					} else {
						map.put(columnName, rs.getObject(i+1));
					}
				}
				maps.add(map);
			}

		} catch (Exception e){
			throw new RuntimeException(e);
		} finally {
			this.close(rs);
			this.close(stmt);
			this.close(conn);
		}
		return maps;
	}

	/**
	 * 获取表字段属性
	 * @param database
	 * @param tableName
     * @return
     */
	public Map<String, Object> selectColumns(String database, String tableName) {
		Map<String, Object> columns = new LinkedHashMap<String, Object>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			stmt = conn.prepareStatement("select 1 from " + database + "." + tableName);

			rs = stmt.executeQuery();
			meta = rs.getMetaData();
			int count = meta.getColumnCount();

			for(int i = 0; i < count; i++){
				String columnLabal = meta.getColumnLabel(i + 1);
				String columnName = meta.getColumnName(i + 1);
				Class columnClass = JDBCTypesUtils.getJdbcClass(meta.getColumnType(i + 1));
				if (columnClass == null) continue;
				if (columnLabal != null && columnLabal.length() > 0) {
					columns.put(columnLabal, columnClass);
				} else {
					columns.put(columnName, columnClass);
				}
			}
		} catch (Exception e){
			logger.error("table not exists {}.{}", database, tableName);
		} finally {
			this.close(rs);
			this.close(stmt);
			this.close(conn);
		}
		return columns;
	}

	/**
	 * 获取主键
	 * @param database
	 * @param tableName
	 * @return
	 */
	public Set<String> selectPrimaryKeys(String database, String tableName) {
		Set<String> pks = new TreeSet<String>();
		Map<String, Object> columns = new LinkedHashMap<String, Object>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DatabaseMetaData meta = null;
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			meta = conn.getMetaData();
			rs = meta.getPrimaryKeys(null, database, tableName); // catalog：mysql、oracle应该不支持，db2 Sybase支持
			while (rs.next()) {
				String pk = rs.getString("COLUMN_NAME");
				if (pk != null && pk.length() > 0)
					pks.add(pk);
			}
		} catch (Exception e){
			logger.error("table not exists {}.{}", database, tableName);
		} finally {
			this.close(rs);
			this.close(stmt);
			this.close(conn);
		}
		return pks;
	}

	/**
	 * 查询数据库名
	 * @return
     */
	public String queryDbName() {
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			String catalog = conn.getCatalog();
			if (catalog == null || catalog.trim().length() == 0) {
				return null;
			}
			return catalog;
		} catch (Exception e){
			e.printStackTrace();
			logger.error("select error!", e);
		} finally {
			this.close(conn);
		}
		return null;
	}

	@Override
	public String toString() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", url);
		map.put("username", username);
		map.put("password", password);
		return map.toString();
	}
}

