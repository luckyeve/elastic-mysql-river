package com.luckyeve.elastic.common.mysql;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Map;
import java.util.TreeMap;

/**
 * jdbc类型 与 java类型转换 工具类 
 * @author lixy
 *
 */
public class JDBCTypesUtils {

	private static Map<Integer, Class<?>> jdbcTypes;
	private static Map<Integer, Class<?>> jdbcTypes2;
	
	
	static {
		jdbcTypes = new TreeMap<Integer, Class<?>>();
		jdbcTypes.put(Types.INTEGER, Integer.class);
		jdbcTypes.put(Types.SMALLINT, Short.class);
		jdbcTypes.put(Types.DOUBLE, Double.class);
		jdbcTypes.put(Types.TIMESTAMP, Timestamp.class);
		jdbcTypes.put(Types.BIT, Boolean.class);
		jdbcTypes.put(Types.TINYINT, Byte.class);
		jdbcTypes.put(Types.BIGINT, Long.class);
		jdbcTypes.put(Types.VARCHAR, String.class);
		jdbcTypes.put(Types.DATE, Date.class);
		jdbcTypes.put(Types.LONGVARCHAR, String.class);
		jdbcTypes.put(Types.DECIMAL, BigDecimal.class);
	}

	static {
		jdbcTypes2 = new TreeMap<Integer, Class<?>>();
		jdbcTypes2.put(Types.INTEGER, Integer.class);
		jdbcTypes2.put(Types.SMALLINT, Short.class);
		jdbcTypes2.put(Types.DOUBLE, Double.class);
		jdbcTypes2.put(Types.TIMESTAMP, Timestamp.class);
		jdbcTypes2.put(Types.BIT, Boolean.class);
		jdbcTypes2.put(Types.TINYINT, Byte.class);
		jdbcTypes2.put(Types.BIGINT, Long.class);
		jdbcTypes2.put(Types.VARCHAR, String.class);
		jdbcTypes2.put(Types.DATE, Date.class);
		jdbcTypes2.put(Types.DECIMAL, BigDecimal.class);
	}
	
	
	public static Class<?> getJdbcClass(int jdbcType) {
		return jdbcTypes.get(jdbcType);
	}

	public static int getSqlType(Class<?> jdbcClass) {
		for (Map.Entry<Integer, Class<?>> entry: jdbcTypes2.entrySet()) {
			if (jdbcClass.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return Types.VARCHAR;
	}
	
}
