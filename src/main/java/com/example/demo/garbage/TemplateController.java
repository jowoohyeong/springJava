package com.example.demo.garbage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class TemplateController {

    @GetMapping("/template")
    @ResponseBody
    public String template(Model model){
        String str = "\n" +
                    "\n" +
                    "\n" +
                    "<title> 화상회의 </title>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                    "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "<table cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"margin: 0; padding: 0; width:100%;\">\n" +
                    "    <tbody>\n" +
                    "        <tr>\n" +
                    "            <td style=\"margin: 0; padding: 0;\">\n" +
                    "                <table class=\"\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin: 0 auto; padding: 0; width: 738px;\">\n" +
                    "                    <tbody>\n" +
                    "                        <tr>\n" +
                    "                            <td style=\"margin: 0; padding: 50px 30px; width:738px; border: 1px solid #e2e2e2; border-radius: 5px;\">\n" +
                    "                                <table cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"\">\n" +
                    "                                    <tbody>\n" +
                    "                                        <tr>\n" +
                    "                                            <td style=\"margin: 0; padding: 0 0 20px 0; line-height: 0;\"><img src=\"http://tigen1st.iptime.org:48080/app-assets/images/logo/logo-dark-lg.png\" alt=\"\" style=\"margin: 0; padding: 0;\"></td>\n" +
                    "                                        </tr>\n" +
                    "                                        <tr>\n" +
                    "                                            <td style=\"background: #0072f6; color: #FFF; font-size: 23px; letter-spacing: -1px; text-align: center; padding: 15px 15px; border-radius: 0 30px 0 30px; line-height: 1;\">{문구} #{항목1}</td>\n" +
                    "                                        </tr>\n" +
                    "                                        <tr>\n" +
                    "                                            <td style=\"margin: 0; padding: 35px 30px; line-height: 1.3;\"><p>방송일자 : #{항목2}<br></p></td>\n" +
                    "                                        </tr>\n" +
                    "                                        <tr>\n" +
                    "                                            <td style=\"margin: 0; padding: 35px 30px; line-height: 1.3;\"><p>회의내용 : #{항목3}<br></p></td>\n" +
                    "                                        </tr>\n" +
                    "                                        <tr>\n" +
                    "                                            <td style=\"text-align: center; margin: 0; padding: 0 0 55px 0; line-height: 1;\"><a href=\"#{단축RL}\"><button type=\"button\" style=\"background: #023b7e; color: #FFF; font-size: 16px; padding: 15px 25px; min-width: 250px; min-height: 50px; border: 0; border-radius: 100px; cursor: pointer;\">바로가기</button></a></td>\n" +
                    "                                        </tr>\n" +
                    "                                        <tr>\n" +
                    "                                            <td style=\"text-align: center; color: #888; font-size: 14px; margin: 0; padding: 30px 0 0 0; line-height: 1; border-top: 1px solid #e2e2e2;\">\n" +
                    "                                                <p style=\"margin: 0 0 14px 0; \">메일 수신을 원치 않으시면 <b>[<a href=\"#{수신거부URL}\" style=\"color: #888; text-decoration: none;\" target=\"_blank\">이곳</a>]</b>에서 수신거부를 해 주시기 바랍니다.</p>\n" +
                    "                                                <p style=\"margin: 0 0 6px 0;\">(우편번호) 주소 상호명, 대표전화 000-0000-0000</p>\n" +
                    "                                                <p style=\"margin: 0 0 0 0;\">Copyright 2023 상호명. All Rights Reserved.</p>\n" +
                    "                                            </td>\n" +
                    "                                        </tr>\n" +
                    "                                    </tbody>\n" +
                    "                                </table>\n" +
                    "                            </td>\n" +
                    "                        </tr>\n" +
                    "                    </tbody>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </tbody>\n" +
                    "</table>";
        return str;
    }
}
