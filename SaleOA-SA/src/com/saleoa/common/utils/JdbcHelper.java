package com.saleoa.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.saleoa.common.constant.JdbcType;
import com.saleoa.model.BalanceLevel;
import com.saleoa.model.Department;
import com.saleoa.model.Employee;
import com.saleoa.model.EmployeeRole;
import com.saleoa.model.Level;
import com.saleoa.model.ManagerLevel;
import com.saleoa.model.Salary;
import com.saleoa.model.SalaryConfig;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleLog;
import com.saleoa.model.SaleSalary;

public class JdbcHelper {
	private static Connection conn = null;
	public static Connection getConnection() throws Exception {
		if(null == conn) {
			//String url = "jdbc:sqlite:java-sqlite.db";
			String url = "jdbc:sqlite:lib/slf4j.jar";
			conn = DriverManager.getConnection(url);
		}
		return conn;
	}
	
	public static void openTransaction() {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void commitTransaction() {
		try {
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean close(ResultSet rs, PreparedStatement ps, Connection conn) {
		boolean success = false;
		try {
			if(null != rs) {
				rs.close();
				rs = null;
			}
			
			if(null != ps) {
				ps.close();
				ps = null;
			}
			
			if(null != conn) {
				conn.close();
				conn = null;
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
	
	public static Long id(Class cls) throws Exception {
		String tableName = AnnotationUtil.getTableName(cls);
		String sql = "SELECT MAX(id) id FROM "+tableName;
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		Long nextId = null;
		if(rs.next()) {
			Long maxId = rs.getLong("id");
			nextId = maxId + 1;
		}
		return nextId;
	}
	
	public static boolean executeSql(String sql) throws Exception {
		PreparedStatement ps = getConnection().prepareStatement(sql);
		return ps.executeUpdate() > 0 ? true : false;
	}
	
	public static String tableSql(Class cls) throws Exception {
		String sql = "";
		String tableName = AnnotationUtil.getTableName(cls);
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
	
	public static String tableIndexSql(Class cls) throws Exception {
		String sql = "";
		String tableName = AnnotationUtil.getTableName(cls);
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
		sql = "CREATE INDEX IF NOT EXISTS id_index ON "+tableName+" (id)";
		return sql;
	}
	
	public static String insertSqlPrepared(Object obj, List<Object> values) throws Exception {
		Class cls = obj.getClass();
		String sql = "";
		String tableName = AnnotationUtil.getTableName(cls);
		List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>> ();
		Field[] fields = cls.getDeclaredFields();
		for(int i = 0; i < fields.length; i ++) {
			Field field = fields[i];
			Object value = BeanUtil.getValue(obj, field.getName());
			if(null == value) {
				continue;
			}
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
			valueSql += "?";
			if(JdbcType.TEXT.equals(jdbcType)) {
				if(value instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					value = sdf.format(value);
				}
			}
			values.add(value);
			
			if(iter.hasNext()) {
				columnSql += ",";
				valueSql += ",";
			}
		}
		columnSql += ")";
		valueSql += ")";
		sql += columnSql + " VALUES"+valueSql;
		return sql;
	}
	
	public static String insertSql(Object obj) throws Exception {
		Class cls = obj.getClass();
		String sql = "";
		String tableName = AnnotationUtil.getTableName(cls);
		List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>> ();
		Field[] fields = cls.getDeclaredFields();
		for(int i = 0; i < fields.length; i ++) {
			Field field = fields[i];
			Object value = BeanUtil.getValue(obj, field.getName());
			if(null == value) {
				continue;
			}
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
		//System.out.println(sql);
		return sql;
	}
	
	public static boolean insertBatch(List<? extends Object> list) throws Exception {
		boolean success = false;
		List<Object> values = new ArrayList<Object> ();
		
		Iterator<? extends Object> iter = list.iterator();
		PreparedStatement ps = null;
		while(iter.hasNext()) {
			Object obj = iter.next();
			values.clear();
			try {
				String sql = insertSqlPrepared(obj, values);
				if(null == ps) {
					ps = getConnection().prepareStatement(sql);
				}
				if(values.isEmpty()) {
					continue;
				}
				Iterator<Object> iter2 = values.iterator();
				int idx = 0;
				while(iter2.hasNext()) {
					idx ++;
					Object value = iter2.next();
					if(value instanceof String) {
						ps.setString(idx, value.toString());
					} else if(value instanceof Integer) {
						ps.setInt(idx, (Integer)value);
					} else if(value instanceof Long) {
						ps.setLong(idx, (Long)value);
					} else if(value instanceof Float) {
						ps.setFloat(idx, (Float)value);
					} else if(value instanceof Double) {
						ps.setDouble(idx, (Double)value);
					}
				}
				ps.addBatch();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(null != ps) {
			ps.executeBatch();
		}
		success = true;
		return success;
	}
	
	public static String updateSql(Object obj) throws Exception {
		Class cls = obj.getClass();
		Long id = BeanUtil.parseLong(BeanUtil.getValue(obj, "id"));
		if(null == id) {
			ExceptionUtil.throwExcep("id is null");
		}
		String sql = "";
		String tableName = AnnotationUtil.getTableName(cls);
		List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>> ();
		Field[] fields = cls.getDeclaredFields();
		for(int i = 0; i < fields.length; i ++) {
			Field field = fields[i];
			Object value = BeanUtil.getValue(obj, field.getName());
			if(null != value && !"id".equals(field.getName())) {
				Annotation[] fieldAnnoArr = field.getDeclaredAnnotations();
				for(int j = 0; j < fieldAnnoArr.length; j ++) {
					Annotation annotation = fieldAnnoArr[j];
					if(annotation.annotationType().equals(Column.class)) {
						String columnName = ((Column)annotation).name();
						String jdbcType = ((Column)annotation).jdbcType();
						Map<String, Object> valueMap = new HashMap<String, Object> ();
						valueMap.put("columnName", columnName);
						valueMap.put("value", value);
						valueMap.put("jdbcType", jdbcType);
						valueList.add(valueMap);
					}
				}
			}
		}
		if(StringUtil.isEmpty(tableName)) {
			ExceptionUtil.throwExcep("Table name is null.");
		}
		if(valueList.isEmpty()) {
			ExceptionUtil.throwExcep("Column not found.");
		}
		sql = "UPDATE "+tableName+" SET ";
		Iterator<Map<String, Object>> iter = valueList.iterator();
		while(iter.hasNext()) {
			Map<String, Object> map = iter.next();
			String columnName = map.get("columnName").toString();
			Object value = map.get("value");
			String jdbcType = map.get("jdbcType").toString();
			sql += columnName;
			if(JdbcType.TEXT.equals(jdbcType)) {
				if(value instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					value = sdf.format(value);
				}
				sql += "='"+value+"'";
			} else {
				sql += "="+value;
			}
			
			if(iter.hasNext()) {
				sql += ",";
			}
		}
		
		sql += " WHERE id="+id;
		//System.out.println(sql);
		return sql;
	}
	
	private static String updateSql(Object obj, List<Object> values) throws Exception {
		Class cls = obj.getClass();
		Long id = BeanUtil.parseLong(BeanUtil.getValue(obj, "id"));
		if(null == id) {
			ExceptionUtil.throwExcep("id is null");
		}
		String sql = "";
		String tableName = AnnotationUtil.getTableName(cls);
		List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>> ();
		Field[] fields = cls.getDeclaredFields();
		for(int i = 0; i < fields.length; i ++) {
			Field field = fields[i];
			Object value = BeanUtil.getValue(obj, field.getName());
			if(null != value && !"id".equals(field.getName())) {
				Annotation[] fieldAnnoArr = field.getDeclaredAnnotations();
				for(int j = 0; j < fieldAnnoArr.length; j ++) {
					Annotation annotation = fieldAnnoArr[j];
					if(annotation.annotationType().equals(Column.class)) {
						String columnName = ((Column)annotation).name();
						String jdbcType = ((Column)annotation).jdbcType();
						Map<String, Object> valueMap = new HashMap<String, Object> ();
						valueMap.put("columnName", columnName);
						valueMap.put("value", value);
						valueMap.put("jdbcType", jdbcType);
						valueList.add(valueMap);
					}
				}
			}
		}
		if(StringUtil.isEmpty(tableName)) {
			ExceptionUtil.throwExcep("Table name is null.");
		}
		if(valueList.isEmpty()) {
			ExceptionUtil.throwExcep("Column not found.");
		}
		sql = "UPDATE "+tableName+" SET ";
		Iterator<Map<String, Object>> iter = valueList.iterator();
		while(iter.hasNext()) {
			Map<String, Object> map = iter.next();
			String columnName = map.get("columnName").toString();
			Object value = map.get("value");
			String jdbcType = map.get("jdbcType").toString();
			sql += columnName+= "=?";
			if(JdbcType.TEXT.equals(jdbcType)) {
				if(value instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					value = sdf.format(value);
				}
			}
			values.add(value);
			if(iter.hasNext()) {
				sql += ",";
			}
		}
		
		sql += " WHERE id=?";
		values.add(BeanUtil.getValue(obj, "id"));
		//System.out.println(sql);
		return sql;
	}
	
	private static boolean updatePrepared(List<Object> values, PreparedStatement ps) throws Exception {
		boolean success = false;
		Iterator<Object> valueIter = values.iterator();
		int idx = 0;
		while(valueIter.hasNext()) {
			idx ++;
			Object value = valueIter.next();
			if(value instanceof String) {
				ps.setString(idx, value.toString());
			} else if(value instanceof Integer) {
				ps.setInt(idx, (Integer)value);
			} else if(value instanceof Long) {
				ps.setLong(idx, (Long)value);
			} else if(value instanceof Float) {
				ps.setFloat(idx, (Float)value);
			} else if(value instanceof Double) {
				ps.setDouble(idx, (Double)value);
			}
		}
		ps.addBatch();
		success = true;
		return success;
	}
	
	public static boolean update(Object object) throws Exception {
		boolean success = false;
		List<Object> values = new ArrayList<Object> ();
		String sql = updateSql(object, values);
		PreparedStatement ps = getConnection().prepareStatement(sql);
		updatePrepared(values, ps);
		int[] i = ps.executeBatch();
		success = true;
		return success;
	}
	
	public static boolean updateBatch(List<? extends Object> objects) throws Exception {
		boolean success = false;
		List<List<Object>> valuesList = new ArrayList<List<Object>> ();
		PreparedStatement ps = null;
		for(int i = 0; i < objects.size(); i ++) {
			List<Object> values = new ArrayList<Object> ();
			Object object = objects.get(i);
			String sql = updateSql(object, values);
			if(null == ps) {
				ps = getConnection().prepareStatement(sql);
			}
			valuesList.add(values);
		}
		for(int i = 0; i < valuesList.size(); i ++) {
			updatePrepared(valuesList.get(i), ps);
		}
		
		ps.executeBatch();
		success = true;
		return success;
	}
	
	public static String deleteSql(Object obj) throws Exception {
		Class cls = obj.getClass();
		Long id = BeanUtil.parseLong(BeanUtil.getValue(obj, "id"));
		if(null == id) {
			ExceptionUtil.throwExcep("id is null");
		}
		String sql = "";
		String tableName = AnnotationUtil.getTableName(cls);
		sql = "DELETE FROM "+tableName+" WHERE id="+id;
		//System.out.println(sql);
		return sql;
	}
	
	public static String selectSql(Class cls, Map<String, Object> paramMap, boolean count, Long page, Integer limit) throws Exception {
		String sql = "";
		String tableName = AnnotationUtil.getTableName(cls);
		sql += "SELECT "+(count ? "COUNT(*) total_count" : "*")+" FROM "+tableName;
		try {
			if(null == paramMap || paramMap.isEmpty()) {
				ExceptionUtil.throwExcep("Condition map is null.");
			}
			String orderby = " ORDER BY id ASC";
			if(null != paramMap.get("orderby")) {
				orderby = paramMap.get("orderby").toString();
				paramMap.remove("orderby");
			}
			sql += " WHERE";
			Field[] fields = cls.getDeclaredFields();
			Map<String, Object> param = new HashMap<String, Object> ();
			Iterator<Entry<String, Object>> paramIter = paramMap.entrySet().iterator();
			while(paramIter.hasNext()) {
				Entry<String, Object> entry = paramIter.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				for(int i = 0; i < fields.length; i ++) {
					Field field = fields[i];
					String fieldName = field.getName();
					String tail = "";
					if(key.indexOf(fieldName)>0 || key.indexOf(fieldName) < 0) {
						continue;
					}
					tail = key.substring(fieldName.length());
					Annotation[] fieldAnnoArr = field.getDeclaredAnnotations();
					for(int j = 0; j < fieldAnnoArr.length; j ++) {
						Annotation annotation = fieldAnnoArr[j];
						if(annotation.annotationType().equals(Column.class)) {
							String name = ((Column)annotation).name();
							Class type = field.getType();
							String nameKey = name;
							if(type.equals(String.class)) {
								value = "'"+value+"'";
							} else if(type.equals(Date.class)) {
								//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								//String date = sdf.format(value.toString());
								value = "DATETIME('"+value+"')";
								nameKey = "DATETIME("+name+")";
							}
							nameKey += StringUtil.isEmpty(tail)?"=":tail;
							param.put(nameKey, value);
						}
					}
					break;
				}
			}
			
			Set<Entry<String, Object>> entrySet = param.entrySet();
			Iterator<Entry<String, Object>> iter = entrySet.iterator();
			while(iter.hasNext()) {
				Entry<String, Object> entry = iter.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				sql += " "+key+value;
				if(iter.hasNext()) {
					sql += " AND";
				}
			}
			sql += orderby;
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(!count) {
				if(null != limit) {
					long limitStart = 0;
					if(null != page) {
						limitStart = (page-1)*limit;
					}
					sql += " LIMIT "+limitStart+","+limit;
				}
			}
		}
		//System.out.println(sql);
		return sql;
	}
	
	public static List select(String sql, Class cls) throws Exception {
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
					String date = rs.getString(columnName);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					BeanUtil.setValue(obj, fieldName, sdf.parse(date));
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
		Class<Sale> saleCls = Sale.class;
		Class<SaleSalary> saleSalaryCls = SaleSalary.class;
		Class<EmployeeRole> employeeRoleCls = EmployeeRole.class;
		Class<Department> departmentCls = Department.class;
		Class<ManagerLevel> managerLevelCls = ManagerLevel.class;
		Class<SalaryConfig> salaryConfigCls = SalaryConfig.class;
		Class<SaleLog> saleLogCls = SaleLog.class;
		Class<BalanceLevel> balanceLevelCls = BalanceLevel.class;
		String sql = tableSql(employeeCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(employeeCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableSql(levelCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(levelCls);
		System.out.println(sql);
		executeSql(sql);
		sql = tableSql(salaryCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(salaryCls);
		System.out.println(sql);
		executeSql(sql);
		sql = tableSql(saleCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(saleCls);
		System.out.println(sql);
		executeSql(sql);
		sql = tableSql(saleSalaryCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(saleSalaryCls);
		System.out.println(sql);
		executeSql(sql);
		sql = tableSql(employeeRoleCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(employeeRoleCls);
		System.out.println(sql);
		executeSql(sql);
		sql = tableSql(departmentCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(departmentCls);
		System.out.println(sql);
		executeSql(sql);
		sql = tableSql(managerLevelCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(managerLevelCls);
		System.out.println(sql);
		executeSql(sql);
		sql = tableSql(salaryConfigCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(salaryConfigCls);
		System.out.println(sql);
		executeSql(sql);
		sql = tableSql(saleLogCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(saleLogCls);
		System.out.println(sql);
		executeSql(sql);
		sql = tableSql(balanceLevelCls);
		executeSql(sql);
		System.out.println(sql);
		sql = tableIndexSql(balanceLevelCls);
		System.out.println(sql);
		executeSql(sql);
		return isSuccess;
	}
	
	/*public static void initEmployee() {
		try {
			Employee employee = new Employee();
			employee.setId(0L);
			employee.setCreateDate(new Date());
			employee.setIsDelete(0);
			employee.setName("无");
			employee.setNameEn("wu");
			employee.setRegisterDate(new Date());
			employee.setStatus(0);
			employee.setUpdateDate(new Date());
			employee.setLeaderId(0L);
			employee.setLeaderName("");
			employee.setAddress("");
			employee.setDepartmentId(null);
			employee.setDepartmentName("");
			employee.setEmployeeRoleId(null);
			employee.setEmployeeRoleName("");
			employee.setIdNumber("");
			employee.setInheritor("");
			employee.setInheritorPhone("");
			employee.setFireDate(DateUtil.parseFullDate("1000-01-01 00:00:00"));
			String insertSql = JdbcHelper.insertSql(employee);
			JdbcHelper.executeSql(insertSql);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}*/
	
	public static void initSale() {
		try {
			Sale sale = new Sale();
			sale.setId(0L);
			sale.setCreateDate(new Date());
			sale.setIsDelete(0);
			sale.setLevelId(50L);
			sale.setLevelName("最高级");
			sale.setName("无");
			sale.setNameEn("wu");
			sale.setSaleNo(1);
			sale.setSaleDate(new Date());
			sale.setRewardPoints(10000L);
			sale.setSalary(0L);
			sale.setUpdateDate(new Date());
			sale.setLastSaleId(0L);
			sale.setLastSaleName("");
			sale.setEmployeeId(0L);
			sale.setEmployeeName("");
			sale.setDepartmentId(0L);
			sale.setDepartmentName("");
			sale.setSaleDate(DateUtil.parseFullDate("1000-01-01 00:00:00"));
			String insertSql = JdbcHelper.insertSql(sale);
			JdbcHelper.executeSql(insertSql);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			initTable();
			//initEmployee();
			//initSale();
			/*Level level = new Level();
			level.setId(1L);
			level.setName("二级");
			level.setRewardPoints(5l);
			level.setCreateDate(new Date());
			//String sql = insertSql(level);
			//String sql = deleteSql(level);
			//executeSql(sql);
			//List<Level> levels = (List<Level>) select("SELECT * FROM tbl_oa_level", Level.class);
//			System.out.println(levels.get(0).getId());
//			System.out.println(levels.get(0).getName());
//			System.out.println(levels.get(0).getRewardPoints());*/
			
			/*Map<String, Object> map = new HashMap<String, Object> ();
			map.put("name", "二级");
			//map.put("id", 1L);
			//map.put("createDate>=", "2018-08-16 00:00:00");
			//map.put("createDate<=", "2018-08-16 23:59:59");
			//String selectSql = selectSql(Level.class, map);
			ILevelDao dao = new ILevelDaoImpl();
			List<Level> levels = dao.select(map);
			System.out.println(levels.size());*/
			//String updateSql = "UPDATE tbl_oa_employee SET NAME='无' WHERE id=0";
			//executeSql(updateSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
