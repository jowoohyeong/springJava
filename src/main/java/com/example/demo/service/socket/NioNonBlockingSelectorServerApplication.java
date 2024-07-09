package com.example.demo.service.socket;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NioNonBlockingSelectorServerApplication implements ServerApplication {

    private static final Map<SocketChannel, ByteBuffer> sockets = new ConcurrentHashMap<>();

    @Override
    public void service() throws IOException {
        System.out.println("NioNonBlockingSelectorServerApplication.service");
        // 서버 생성
        ServerSocketChannel serverSocket = ServerSocketChannel.open();

        // 서버를 8080 포트에 바인딩.
        serverSocket.bind(new InetSocketAddress(8081));

        // Non-Blocking 모드로 전환.
        serverSocket.configureBlocking(false);

        // 채널 관리자 (Selector) 생성
        try (Selector selector = Selector.open()) {

            // 채널 관리자 (Selector)에 채널 (ServerSocketChannel) 등록.
            // Accept connection에만 관심이 있으므로 OP_ACCEPT를 등록한다.
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // 채널 관리자 (Selector)에 등록된 채널들의 이벤트를 감지한다. (이벤트가 발생하기전까지 blocking 된다.)
                selector.select();

                // 채널 관리자 (Selector)에 등록된 채널들의 이벤트를 순회한다.
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                for (Iterator<SelectionKey> it = selectionKeys.iterator(); it.hasNext(); ) {
                    SelectionKey key = it.next();

                    try {
                        if (key.isValid()) {
                            if (key.isAcceptable()) {
                                handleAcceptEvent(key); // 연결이 들어온 경우
                            } else if (key.isReadable()) {
                                handleReadEvent(key); // 읽기 이벤트가 발생한 경우
                            } else if (key.isWritable()) {
                                handleWriteEvent(key); // 쓰기 이벤트가 발생한 경우
                            }
                        }
                    } catch (ClosedChannelException e) {
                        closeSocket((SocketChannel) key.channel());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }

                    // 동일한 key가 두번 이상 처리되지않도록, 한번 처리한 key는 Iterator에서 제거한다.
                    it.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleAcceptEvent(SelectionKey key) throws IOException {
        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socket = socketChannel.accept();

        socket.configureBlocking(false);

        socket.register(key.selector(), SelectionKey.OP_READ);

        // 매 Socket마다 하나의 ByteBuffer를 할당한다.
        sockets.put(socket, ByteBuffer.allocateDirect(80));
    }

    private static void handleReadEvent(SelectionKey key) throws IOException {
        SocketChannel socket = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = sockets.get(socket);

        int data = socket.read(byteBuffer);

        if (data == -1) {
            closeSocket(socket);
            sockets.remove(socket);
        }

        // position = 0으로 함으로써 읽기 모드로 전환한다.
        byteBuffer.flip();

        // 작업을 수행한다. (대문자 변환)
        toUpperCase(byteBuffer);

        socket.configureBlocking(false);

        key.interestOps(SelectionKey.OP_WRITE);
    }

    private static void handleWriteEvent(SelectionKey key) throws IOException {
        SocketChannel socket = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = sockets.get(socket);

        socket.write(byteBuffer);

        // 전부 다 write한 경우
        while (!byteBuffer.hasRemaining()) {
            byteBuffer.compact();
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    private static void closeSocket(SocketChannel socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
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
