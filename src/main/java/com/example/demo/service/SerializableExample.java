package com.example.demo.service;

import com.example.demo.dto.MyObjectDTO;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class SerializableExample {


    public void service() throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("test.dat");
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);

        MyObjectDTO obj1 = new MyObjectDTO("jwh", "테스트");
        MyObjectDTO obj2 = new MyObjectDTO("cls", "테스트2");
        MyObjectDTO obj3 = new MyObjectDTO("pcw", "테스트3");

        outputStream.writeObject(obj1);
        outputStream.writeObject(obj2);
        outputStream.writeObject(obj3);

        System.out.println("obj1 = " + obj1);
        System.out.println("obj2 = " + obj2);
        System.out.println("obj3 = " + obj3);

        outputStream.close();

        FileInputStream fileInputStream = new FileInputStream("test.dat");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        //객체
         MyObjectDTO exObj1 = (MyObjectDTO) objectInputStream.readObject();
         MyObjectDTO exObj2 = (MyObjectDTO) objectInputStream.readObject();
         MyObjectDTO exObj3 = (MyObjectDTO) objectInputStream.readObject();

        System.out.println("exObj1 = " + exObj1);
        System.out.println("exObj2 = " + exObj2);
        System.out.println("exObj3 = " + exObj3);
        objectInputStream.close();

    }

}
