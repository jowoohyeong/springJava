package com.example.demo.web.service;

import com.example.demo.domain.entity.Projects;
import com.example.demo.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {
    private final ProjectRepository projectRepository;

    public List<Projects> list() {
        return projectRepository.findAll();
    }

}
