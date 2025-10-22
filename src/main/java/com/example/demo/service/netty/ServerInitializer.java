package com.example.demo.service.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        // 줄 단위로 데이터를 읽기 위한 디코더 추가
        p.addLast(new LineBasedFrameDecoder(1024));

        p.addLast(new StringDecoder());     // 인바운드 처리: 바이트 데이터를 문자열로 변환
        p.addLast(new StringEncoder());     // 아웃바운드 처리: 문자열 데이터를 바이트 데이터로 변환
        p.addLast(new ServerHander());      // 비즈니스 로직을 처리하는 핸들러 추가

    }
}
