package com.example.demo.service.netty;

public class NettyServerRunner implements Runnable{
    @Override
    public void run() {
        try {
            //		TODO 켜져 있으면 어떻게 할건지 동작 필요!
            new NettyServer(9005).start();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
