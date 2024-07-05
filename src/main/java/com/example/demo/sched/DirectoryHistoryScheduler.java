package com.example.demo.sched;

import com.example.demo.service.MonitoringService;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DirectoryHistoryScheduler {


    //@Scheduled(fixedDelay=5000000, initialDelay=5000)
    public void execute() {
        String path = "D:\\문서";
        try {
            File directory = new File(path);
            //System.out.println("directory = " + directory);

            FileAlterationObserver observer = new FileAlterationObserver(directory);
            FileAlterationListener listener = new MonitoringService();

            // Observer에 Listener 추가
            observer.addListener(listener);

            // FileAlterationMonitor 생성 (polling interval: 5000ms)
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
