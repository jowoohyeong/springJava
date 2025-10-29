package com.example.demo.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.context.ApplicationEventPublisher;

import java.io.File;

@Slf4j
@AllArgsConstructor
public class MonitoringService implements FileAlterationListener {

    private final ApplicationEventPublisher eventPublisher;
    @Override
    public void onStart(FileAlterationObserver observer) {
//        log.info("Monitoring started");
    }

    @Override
    public void onDirectoryCreate(File directory) {
        log.info("Directory created: " + directory.getName());
        eventPublisher.publishEvent(directory);
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
        eventPublisher.publishEvent(file);
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
