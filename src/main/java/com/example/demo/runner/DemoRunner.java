package com.example.demo.runner;

import com.example.demo.monitor.DirectoryEventMonitor;
import com.example.demo.service.EventPublisher;
import com.example.demo.service.SerializableExample;
import com.example.demo.service.SocketOpenService;
import com.example.demo.service.netty.NettyServerRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("main")
@RequiredArgsConstructor
public class DemoRunner implements CommandLineRunner {

    private final EventPublisher eventPublisher;

    private final DirectoryEventMonitor directoryEventMonitor;

    private final SerializableExample serializableExample;

    private final SocketOpenService socketOpenService;
    @Override
    public void run(String... args) throws Exception {
        /* 이벤트 처리 */
        eventPublisher.publishEvent("Hello, this is a custom event Message!!");

        /* 정적 경로의 디렉터리 변동 확인 이벤트 */
        directoryEventMonitor.execute();

        Thread nettyThread = new Thread(new NettyServerRunner(), "netty-server-thread");
        nettyThread.start();
//		serializableExample.service();
//		socketOpenService.service();
    }
}
