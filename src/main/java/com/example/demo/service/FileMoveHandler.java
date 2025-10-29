package com.example.demo.service;

import com.example.demo.web.domain.PathProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Component
@Async
@Slf4j
public class FileMoveHandler {
    private final PathProperties pathProperties;

    public FileMoveHandler(PathProperties pathProperties) {
        this.pathProperties = Objects.requireNonNull(pathProperties);
    }

    @EventListener
    public void onMoveFileHandler(File file) {
        Path targetDirPath = Path.of(pathProperties.getUsbDir());
        Path sourcePath = file.toPath();
        Path finalTargetPath = targetDirPath.resolve(sourcePath.getFileName());

        try {
            if (Files.exists(sourcePath) && Files.exists(targetDirPath)) {

                Files.move(sourcePath, finalTargetPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("✅ 파일 이동 성공: " + sourcePath + " -> " + finalTargetPath);
            } else {
                if (!Files.exists(sourcePath)) {
                    System.out.println("❌ 원본 파일이 존재하지 않습니다: " + sourcePath);
                }
                if (!Files.exists(targetDirPath)) {
                    System.out.println("❌ 대상 디렉터리가 존재하지 않습니다: " + targetDirPath);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ 파일 이동 실패: " + sourcePath);
            e.printStackTrace();
        }
    }
}
