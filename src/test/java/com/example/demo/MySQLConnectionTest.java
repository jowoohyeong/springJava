package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootTest
public class MySQLConnectionTest {
    @Test
    void main() {
        String url = "jdbc:mysql://localhost:3306/board?serverTimezone=Asia/Seoul";
        String username = "root";
        String password = "1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ MySQL 연결 성공!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.err.println("❌ 드라이버 로딩 실패: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ DB 연결 실패: " + e.getMessage());
        }
    }
}
