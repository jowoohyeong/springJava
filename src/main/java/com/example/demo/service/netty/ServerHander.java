package com.example.demo.service.netty;

import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class ServerHander extends SimpleChannelInboundHandler<String> {

    // 응답 받을 때
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("ServerHander.channelRead0");

        // 클라이언트에 응답 메시지 전송
        ctx.writeAndFlush("echo: " + msg + "\n");

        if ("quit".equals(msg)) {
            ctx.close();
        }
    }


    // channel 활성화시 호출
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ServerHander.channelRegistered");
        super.channelRegistered(ctx);
    }

    // channel이 eventLoop 에서 해제되고 입출력 처리 불가시 호출
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ServerHander.channelUnregistered");
        super.channelUnregistered(ctx);
    }

    // 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ChannelId channelId = ctx.channel().id();
        System.out.println("ServerHander.channelActive channelId: " + channelId);
        super.channelActive(ctx);
    }

    // 읽기 작업 발생시마다 호출
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ServerHander.channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelId channelId = ctx.channel().id();
        System.out.println("ServerHander.channelInactive channelId: " + channelId);
        super.channelInactive(ctx);
    }

    // 예외 발생
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
