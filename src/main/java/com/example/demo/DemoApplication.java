package com.example.demo;

import com.example.demo.sched.DirectoryHistoryScheduler;
import com.example.demo.service.EventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@AllArgsConstructor
public class DemoApplication implements CommandLineRunner {

	private final EventPublisher eventPublisher;

	private final DirectoryHistoryScheduler directoryHistoryScheduler;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		eventPublisher.publishEvent("Hello, this is a custom event!");
		directoryHistoryScheduler.execute();

	}
}
