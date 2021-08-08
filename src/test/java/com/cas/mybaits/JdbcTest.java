package com.cas.mybaits;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2021/8/7 4:23 下午
 * @desc
 */
public class JdbcTest {

    private static final String URL = "jdbc:mysql://localhost:3306/cas";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("select * from goods where id='100'");
        while (rs.next()) {
            System.out.println(rs.getInt("surplus"));
        }

    }

}
