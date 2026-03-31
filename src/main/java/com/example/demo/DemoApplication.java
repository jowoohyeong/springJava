package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.example")
@ConfigurationPropertiesScan
public class DemoApplication {
	public static void main(String[] args){
		// H2 서버가 꺼져 있는지 미리 체크 (포트 9092)
		if (!isPortOpen("localhost", 9092)) {
			System.err.println("\n" + "=".repeat(60));
			System.err.println("[안내] H2 데이터베이스 서버(포트ese 9092)가 꺼져 있습니다.");
			System.err.println("원격 접속(TCP) 모드를 사용 중이므로 H2 서버를 먼저 실행해야 합니다.");
			System.err.println("=".repeat(60) + "\n");
			System.exit(1);
		}

		try {
			SpringApplication.run(DemoApplication.class, args);
		} catch (Exception e) {
			// 기타 예상치 못한 오류 처리
			throw e;
		}
	}

	private static boolean isPortOpen(String host, int port) {
		try (java.net.Socket socket = new java.net.Socket()) {
			socket.connect(new java.net.InetSocketAddress(host, port), 200); // 200ms 타임아웃
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
