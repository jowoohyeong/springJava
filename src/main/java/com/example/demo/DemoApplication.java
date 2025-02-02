package com.example.demo;

import com.example.demo.sched.DirectoryHistoryScheduler;
import com.example.demo.service.EventPublisher;
import com.example.demo.service.SocketOpenService;
import com.example.demo.service.netty.NettyServer;
import com.example.demo.service.socket.IOSimpleBlockingServerApplication;
import com.example.demo.service.SerializableExample;
import com.example.demo.service.socket.IOThreadBlockingServerApplication;
import com.example.demo.service.socket.IOThreadPoolBlockingServerApplication;
import com.example.demo.service.socket.NioBlockingServerApplication;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@AllArgsConstructor
@ComponentScan(basePackages = "com.example")
public class DemoApplication implements CommandLineRunner {

	private final EventPublisher eventPublisher;

	private final DirectoryHistoryScheduler directoryHistoryScheduler;

	private final SerializableExample serializableExample;

	private final SocketOpenService socketOpenService;
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(DemoApplication.class, args);
		/*
		켜져 있으면 어떻게 할건지 동작 필요!
		int port = 9005;
		new NettyServer(port).start();*/
	}

	@Override
	public void run(String... args) throws Exception {
		eventPublisher.publishEvent("Hello, this is a custom event!");
		directoryHistoryScheduler.execute();


//		serializableExample.service();
		socketOpenService.service();
	}
}
