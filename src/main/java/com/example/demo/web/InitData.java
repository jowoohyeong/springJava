package com.example.demo.web;

import com.example.demo.domain.entity.Projects;
import com.example.demo.repository.ProjectRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class InitData {
    private final InitDataService initDataService;

    @PostConstruct
    void init() {
        initDataService.save();
    }

    @Service
    @RequiredArgsConstructor
    static class InitDataService {
        private final ProjectRepository projectRepository;

        public void save() {
//            projectRepository.save(new Projects("MCMS", "세스코"));
        }
    }
}
