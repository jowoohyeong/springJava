package com.example.demo.service;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

public class MonitoringService implements FileAlterationListener {
    @Override
    public void onStart(FileAlterationObserver observer) {
//        System.out.println("Monitoring started");
    }

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("Directory created: " + directory.getName());
    }

    @Override
    public void onDirectoryChange(File directory) {
        System.out.println("Directory changed: " + directory.getName());
    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("Directory deleted: " + directory.getName());
    }

    @Override
    public void onFileCreate(File file) {
        System.out.println("File created: " + file.getName());
    }

    @Override
    public void onFileChange(File file) {
        System.out.println("File changed: " + file.getName());
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("File deleted: " + file.getName());
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
//        System.out.println("Monitoring stopped");
    }
}
