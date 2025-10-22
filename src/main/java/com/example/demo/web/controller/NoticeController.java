package com.example.demo.web.controller;

import com.example.demo.web.domain.Notice;
import com.example.demo.web.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("")
    @Operation(summary = "공지사항 목록 조회",  description = "공지사항 전체 목록을 조회합니다.")
    public String notice(Model model){
        List<Notice> noticeList = noticeService.getNoticeList();
        model.addAttribute("list", noticeList);

        return "notice/notice-list";
    }
}
