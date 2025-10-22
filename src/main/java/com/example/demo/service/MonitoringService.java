package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

@Slf4j
public class MonitoringService implements FileAlterationListener {
    @Override
    public void onStart(FileAlterationObserver observer) {
//        log.info("Monitoring started");
    }

    @Override
    public void onDirectoryCreate(File directory) {
        log.info("Directory created: " + directory.getName());
    }

    @Override
    public void onDirectoryChange(File directory) {
        log.info("Directory changed: " + directory.getName());
    }

    @Override
    public void onDirectoryDelete(File directory) {
        log.info("Directory deleted: " + directory.getName());
    }

    @Override
    public void onFileCreate(File file) {
        log.info("File created: " + file.getName());
    }

    @Override
    public void onFileChange(File file) {
        log.info("File changed: " + file.getName());
    }

    @Override
    public void onFileDelete(File file) {
        log.info("File deleted: " + file.getName());
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
//        log.info("Monitoring stopped");
    }
}
