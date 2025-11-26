package com.example.demo.service.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class NettyServer {
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();     // 클라이언트 연결을 수락하는 스레드 그룹
        EventLoopGroup workerGroup = new NioEventLoopGroup();   // 데이터 읽기/쓰기 및 비즈니스 로직 처리를 담당하는 스레드 그룹

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)      // NIO 전송 채널을 사용하도록 설정
                    .childHandler(new ServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)          // 연결 요청 큐의 크기 설정
                    .childOption(ChannelOption.SO_KEEPALIVE, true);     // KEEPALIVE 설정

            // 서버를 바인딩 하고 시작
            ChannelFuture f = b.bind(port).sync();
            System.out.println("Server started on port = " + port);

            // 채널이 닫힐 때까지 대기
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
