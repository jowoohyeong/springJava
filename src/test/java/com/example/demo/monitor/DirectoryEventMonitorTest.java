package com.example.demo.monitor;

import com.example.demo.domain.entity.Directory;
import com.example.demo.repository.jpa.DirectoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class DirectoryEventMonitorTest {
//    private final EntityManager em;
    @Autowired DirectoryRepository directoryRepository;
    static String storagePath = "F:/개인자료/";

    @BeforeEach
    @DisplayName("데이터 삽입")
    void save() {
        directoryRepository.save(new Directory("프로젝트"));
        directoryRepository.save(new Directory("모아보기"));
        directoryRepository.save(new Directory("무폴더"));
    }

    @Test
    void directorySearch() {
        String targetPath = "C:/Users/TIGEN0802/Desktop/target/";

        File target = new File(targetPath);
        File storage = new File(storagePath);
        System.out.println("target.isDirectory = " + target.isDirectory());
        System.out.println("storage.isDirectory = " + storage.isDirectory());
        
        File[] targetInDirs = target.listFiles();

        if (storage.isDirectory()) {
            for (File targetInDir : targetInDirs) {
                String targetDirectoryName = targetInDir.getName();
                System.out.println("targetDirectoryName = " + targetDirectoryName);

                File[] subDirectory = targetInDir.listFiles();
                for (File movedFile : subDirectory) {
                    Path destFile = Path.of(storagePath + targetDirectoryName + "/" +movedFile.getName());
                    extracted(movedFile, destFile);
                }

            }
        }
        

    }

    private static void extracted(File movedFile, Path destFile) {
        try {
            log.info("파일 이동: " + movedFile);
            Files.move(movedFile.toPath(), destFile);
            log.info("✅ 파일 이동 성공: └─-> " + destFile);
//        } catch (DirectoryNotEmptyException directoryNotEmptyException) {
        } catch (FileAlreadyExistsException e) {
            log.error("❌ 중복 파일명 존재 └─-> {}", destFile, e);
        } catch (DirectoryNotEmptyException e) {
            log.error("❌ 디렉터리가 비어있지 않습니다 - {}", destFile, e);
        } catch (IOException e) {
            log.error("파일 이동 중 IO 오류 발생 - {} -> {}", movedFile, destFile, e);
        }catch (Exception e) {
            log.error("파일 이동 중 알 수 없는 오류 발생 - {} -> {}", movedFile, destFile, e);
            e.printStackTrace();
        }
    }
}