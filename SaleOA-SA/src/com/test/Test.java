package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {
	public static void main(String[] args) {
		/*Properties properties = System.getProperties();
		Set<Entry<Object, Object>> props = properties.entrySet();
		Iterator<Entry<Object, Object>> iter = props.iterator();
		while(iter.hasNext()) {
			Entry<Object, Object> entry = iter.next();
			System.out.println(entry.getKey().toString()+"="+entry.getValue().toString());
		}*/
		
		/*Map<String, String> envMap = System.getenv();
		Set<Entry<String, String>> envSet = envMap.entrySet();
		Iterator<Entry<String,String>> iter2 = envSet.iterator();
		while(iter2.hasNext()) {
			Entry<String, String> entry = iter2.next();
			System.out.println(entry.getKey()+"="+entry.getValue());
		}*/
		connect();
		
	}
	
	public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:java-sqlite.db";
            String sql = "CREATE TABLE IF NOT EXISTS employees (\n" + " id integer PRIMARY KEY,\n"
                    + " name text NOT NULL,\n" + " capacity real\n" + ");";
            String selectSql = "SELECT * FROM employees;";
            String insertSql = "INSERT INTO employees(id, name, capacity) VALUES(1, 'abc', 'def')";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            /*Statement stmt = conn.createStatement();
            stmt.execute(sql);
            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.executeUpdate();*/
            PreparedStatement ps = conn.prepareStatement(selectSql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	System.out.println(rs.getInt("id"));
            	System.out.println(rs.getString("name"));
            }
            System.out.println("Create table finished.");
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
