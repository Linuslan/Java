package com.saleoa.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.saleoa.common.annotation.Column;
import com.saleoa.common.annotation.Table;
import com.saleoa.model.Employee;
import com.saleoa.model.Level;
import com.saleoa.model.Salary;

public class JdbcHelper {
	private static Connection conn = null;
	public static Connection getConnection() throws Exception {
		if(null == conn) {
			String url = "jdbc:sqlite:java-sqlite.db";
			conn = DriverManager.getConnection(url);
		}
		return conn;
	}
	
	public static boolean executeSql(String sql) throws Exception {
		PreparedStatement ps = getConnection().prepareStatement(sql);
		return ps.execute();
	}
	
	public static String generateTableSql(Class cls) throws Exception {
		String sql = "";
		Annotation[] annotationArr = cls.getDeclaredAnnotations();
		String tableName = "";
		for(int i = 0; i < annotationArr.length; i ++) {
			Annotation annotation = annotationArr[i];
			if(annotation.annotationType().equals(Table.class)) {
				tableName = ((Table)annotation).name();
			}
		}
		List<Annotation> annotations = new ArrayList<Annotation> ();
		Field[] fields = cls.getDeclaredFields();
		for(int i = 0; i < fields.length; i ++) {
			Field field = fields[i];
			Annotation[] fieldAnnoArr = field.getDeclaredAnnotations();
			for(int j = 0; j < fieldAnnoArr.length; j ++) {
				Annotation annotation = fieldAnnoArr[j];
				if(annotation.annotationType().equals(Column.class)) {
					annotations.add(annotation);
				}
			}
		}
		if(StringUtil.isEmpty(tableName)) {
			ExceptionUtil.throwExcep("Table name is null.");
		}
		if(annotations.isEmpty()) {
			ExceptionUtil.throwExcep("Column not found.");
		}
		sql = "CREATE TABLE IF NOT EXISTS "+tableName+"(";
		for(int i = 0; i < annotations.size(); i ++) {
			Column column = (Column) annotations.get(i);
			if(StringUtil.isEmpty(column.name())) {
				continue;
			}
			sql += column.name()+" "+column.jdbcType()+"("+column.length()+")"+(column.isNotNull() ? " NOT NULL" : "");
			sql += (StringUtil.isEmpty(column.defaultValue())?"":" default "+column.defaultValue());
			if(column.isPrimaryKey()) {
				sql += " PRIMARY KEY";
			}
			if(i+1 < annotations.size()) {
				sql += ",";
			}
		}
		sql += ")";
		return sql;
	}
	
	public static String generateInsertSql(Object obj) throws Exception {
		Class cls = obj.getClass();
		String sql = "";
		Annotation[] annotationArr = cls.getDeclaredAnnotations();
		String tableName = "";
		for(int i = 0; i < annotationArr.length; i ++) {
			Annotation annotation = annotationArr[i];
			if(annotation.annotationType().equals(Table.class)) {
				tableName = ((Table)annotation).name();
			}
		}
		List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>> ();
		Field[] fields = cls.getDeclaredFields();
		for(int i = 0; i < fields.length; i ++) {
			Field field = fields[i];
			Annotation[] fieldAnnoArr = field.getDeclaredAnnotations();
			for(int j = 0; j < fieldAnnoArr.length; j ++) {
				Annotation annotation = fieldAnnoArr[j];
				if(annotation.annotationType().equals(Column.class)) {
					String columnName = ((Column)annotation).name();
					String jdbcType = ((Column)annotation).jdbcType();
					Map<String, Object> valueMap = new HashMap<String, Object> ();
					valueMap.put("columnName", columnName);
					valueMap.put("value", BeanUtil.getValue(obj, field.getName()));
					valueMap.put("jdbcType", jdbcType);
					valueList.add(valueMap);
				}
			}
		}
		if(StringUtil.isEmpty(tableName)) {
			ExceptionUtil.throwExcep("Table name is null.");
		}
		if(valueList.isEmpty()) {
			ExceptionUtil.throwExcep("Column not found.");
		}
		sql = "INSERT INTO "+tableName;
		Iterator<Map<String, Object>> iter = valueList.iterator();
		String columnSql = "(";
		String valueSql = "(";
		while(iter.hasNext()) {
			Map<String, Object> map = iter.next();
			String columnName = map.get("columnName").toString();
			Object value = map.get("value");
			String jdbcType = map.get("jdbcType").toString();
			columnSql += columnName;
			if(JdbcType.TEXT.equals(jdbcType)) {
				if(value instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					value = sdf.format(value);
				}
				valueSql += "'"+value+"'";
			} else {
				valueSql += value;
			}
			
			if(iter.hasNext()) {
				columnSql += ",";
				valueSql += ",";
			}
		}
		columnSql += ")";
		valueSql += ")";
		sql += columnSql + " VALUES"+valueSql;
		System.out.println(sql);
		return sql;
	}
	
	public static List<? extends Object> select(String sql, Class cls) throws Exception {
		List<Object> list = new ArrayList<Object>();
		Field[] fields = cls.getDeclaredFields();
		Map<String, String> columnMap = new HashMap<String, String> ();
		for(int i = 0; i < fields.length; i ++) {
			Field field = fields[i];
			Annotation[] fieldAnnoArr = field.getDeclaredAnnotations();
			for(int j = 0; j < fieldAnnoArr.length; j ++) {
				Annotation annotation = fieldAnnoArr[j];
				if(annotation.annotationType().equals(Column.class)) {
					String name = ((Column)annotation).name();
					columnMap.put(name, field.getName());
				}
			}
		}
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Object obj = cls.newInstance();
			Set<Entry<String, String>> entrySet = columnMap.entrySet();
			Iterator<Entry<String, String>> iter = entrySet.iterator();
			while(iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				String columnName = entry.getKey();
				String fieldName = entry.getValue();
				Class type = BeanUtil.getFieldType(cls, fieldName);
				if(type.equals(String.class)) {
					BeanUtil.setValue(obj, fieldName, rs.getString(columnName));
				} else if(type.equals(Integer.class)) {
					BeanUtil.setValue(obj, fieldName, rs.getInt(columnName));
				} else if(type.equals(Long.class)) {
					BeanUtil.setValue(obj, fieldName, rs.getLong(columnName));
				} else if(type.equals(Float.class)) {
					BeanUtil.setValue(obj, fieldName, rs.getFloat(columnName));
				} else if(type.equals(Double.class)) {
					BeanUtil.setValue(obj, fieldName, rs.getDouble(columnName));
				} else if(type.equals(Date.class)) {
					BeanUtil.setValue(obj, fieldName, rs.getDate(columnName));
				}
			}
			list.add(obj);
		}
		return list;
	}
	
	public static boolean initTable() throws Exception {
		boolean isSuccess = false;
		Class<Employee> employeeCls = Employee.class;
		Class<Level> levelCls = Level.class;
		Class<Salary> salaryCls = Salary.class;
		String sql = generateTableSql(employeeCls);
		executeSql(sql);
		System.out.println(sql);
		sql = generateTableSql(levelCls);
		executeSql(sql);
		System.out.println(sql);
		sql = generateTableSql(salaryCls);
		executeSql(sql);
		System.out.println(sql);
		return isSuccess;
	}
	
	public static void main(String[] args) {
		try {
			initTable();
			Level level = new Level();
			level.setId(1L);
			level.setName("¶þ¼¶");
			level.setRewardPoints(5l);
			level.setCreateDate(new Date());
			String sql = generateInsertSql(level);
			//executeSql(sql);
			List<Level> levels = (List<Level>) select("SELECT * FROM tbl_oa_level", Level.class);
			System.out.println(levels.get(0).getId());
			System.out.println(levels.get(0).getName());
			System.out.println(levels.get(0).getRewardPoints());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
