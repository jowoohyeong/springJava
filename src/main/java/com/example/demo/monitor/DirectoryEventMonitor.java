package com.example.demo.monitor;

import com.example.demo.service.MonitoringService;
import com.example.demo.domain.PathProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

@Slf4j
@Component
public class DirectoryEventMonitor {

    private final ApplicationEventPublisher eventPublisher;
    private final PathProperties pathProperties;

    public DirectoryEventMonitor(ApplicationEventPublisher eventPublisher, PathProperties pathProperties) {
        this.eventPublisher = eventPublisher;
        this.pathProperties = Objects.requireNonNull(pathProperties);
    }


    public void execute() {
        String path = pathProperties.getEventDir();
        try {
            File directory = new File(path);
            log.info("{} :: path monitoring ,,,, ", path);

            FileAlterationObserver observer = new FileAlterationObserver(directory);
            FileAlterationListener listener = new MonitoringService(eventPublisher);

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
