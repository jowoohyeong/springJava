package com.example.demo.web.service;

import com.example.demo.web.domain.NoticeEntity;
import com.example.demo.web.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public List<NoticeEntity> getNoticeList(){
        return noticeRepository.findAll();
    }
}
