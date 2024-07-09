package com.example.demo.service;

import com.example.demo.service.socket.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class SocketOpenService {
    private final IOSimpleBlockingServerApplication ioSimpleBlockingServerApplication;
   	private final IOThreadBlockingServerApplication ioThreadBlockingServerApplication;
   	private final IOThreadPoolBlockingServerApplication ioThreadPoolBlockingServerApplication;
   	private final ServerApplication nioBlockingServerApplication;
   	private final ServerApplication nioNonBlockingServerApplication;
   	private final ServerApplication nioNonBlockingSelectorServerApplication;

    public void service() throws IOException {
        //	ioSimpleBlockingServerApplication.service();
       	//	ioThreadBlockingServerApplication.service();
       	//	ioThreadPoolBlockingServerApplication.service();
		nioNonBlockingSelectorServerApplication.service();

    }

}
