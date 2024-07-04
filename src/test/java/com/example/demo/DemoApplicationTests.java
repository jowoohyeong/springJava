package com.example.demo;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void fileObjectTest() {

		String path = "D:\\문서\\동영상";
		try {
			File directory = new File(path);
			System.out.println("directory = " + directory);

			FileAlterationObserver observer = new FileAlterationObserver(directory);


			// FileAlterationListener 구현
			FileAlterationListener listener = new FileAlterationListener() {
				@Override
				public void onStart(FileAlterationObserver observer) {
					System.out.println("Monitoring started");
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
					System.out.println("Monitoring stopped");
				}
			};

			// Observer에 Listener 추가
			observer.addListener(listener);

			// FileAlterationMonitor 생성 (polling interval: 5000ms)
			FileAlterationMonitor monitor = new FileAlterationMonitor(5000, observer);

			// 모니터 시작
			monitor.start();

			Thread.sleep(60000);
			monitor.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
