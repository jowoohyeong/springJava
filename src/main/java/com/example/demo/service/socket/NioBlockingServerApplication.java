package com.example.demo.service.socket;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@Component
public class NioBlockingServerApplication implements ServerApplication {

    @Override
    public void service() throws IOException {
        // 서버 생성 (NIO Channel)
        ServerSocketChannel serverSocket = ServerSocketChannel.open();

        // 서버를 8080 포트에 바인딩.
        serverSocket.bind(new InetSocketAddress(8081));

        while (true) {
            // NIO의 accept() 메서드도 blocking 된다.
            SocketChannel socket = serverSocket.accept();

            handleRequest(socket);
        }
    }

    private static void handleRequest(SocketChannel socket) {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(80);

        try {
            while (socket.read(byteBuffer) != -1) {
                // 소켓으로부터 읽은 데이터를 쓰기위해 flip()
                byteBuffer.flip(); // position을 0으로 세팅한다. limit은 읽은 데이터 크기만큼.

                // 작업을 수행한다. (대문자 변환)
                toUpperCase(byteBuffer);

                // 소켓에 데이터를 쓴다.
                while (byteBuffer.hasRemaining()) {
                    socket.write(byteBuffer);
                }

                // 버퍼의 position을 다시 0으로 세팅한다.
                byteBuffer.compact();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void toUpperCase(final ByteBuffer byteBuffer) {
        // ByteBuffer내 모든 데이터를 읽어서 대문자로 변환한다.
        for (int x = 0; x < byteBuffer.limit(); x++) {
            byteBuffer.put(x, (byte) toUpperCase(byteBuffer.get(x)));
        }
    }

    private static int toUpperCase(int data) {
        return Character.isLetter(data) ? Character.toUpperCase(data) : data;
    }

}
