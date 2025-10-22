package com.example.demo.monitor;

import com.example.demo.service.MonitoringService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class DirectoryEventMonitor {

    @Value("${changeFilePath}")
    String filePath;

    public void execute() {
        String path = filePath;
        try {
            File directory = new File(path);
            log.info("{} :: path monitoring ,,,, ", filePath);

            FileAlterationObserver observer = new FileAlterationObserver(directory);
            FileAlterationListener listener = new MonitoringService();

            // Observer에 Listener 추가
            observer.addListener(listener);

            // FileAlterationMonitor 생성 (polling interval: 5000ms) 주기 5초
            FileAlterationMonitor monitor = new FileAlterationMonitor(5000, observer);

            try {
                // 모니터 시작
                monitor.start();
            } catch (Exception e) {
                e.printStackTrace();

                try {
                    monitor.stop();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
