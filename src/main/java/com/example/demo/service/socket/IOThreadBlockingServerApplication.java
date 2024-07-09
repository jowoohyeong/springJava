package com.example.demo.service.socket;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class IOThreadBlockingServerApplication {

    public void service() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8082);

        while(true) {
            // blocking call (새로운 클라이언트가 접속할 때까지 blocking 된다.)
            Socket socket = serverSocket.accept();

            new Thread(()-> handleRequest(socket)).start();

        }
    }

    private static void handleRequest(Socket socket) {

        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {
            int data;

            // read() 메서드는 데이터를 읽을 때까지 blocking 된다. 만약 소켓이 닫혀 더이상 읽을 게 없다면 -1을 리턴한다.
            while((data = in.read()) != -1){
                data = Character.isLetter(data) ? toUpperCase(data) : data;
                out.write(data);
            }

        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    private static int toUpperCase(int data){
        return Character.toUpperCase(data);
    }
}
