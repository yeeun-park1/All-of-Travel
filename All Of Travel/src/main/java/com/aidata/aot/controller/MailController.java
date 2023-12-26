package com.aidata.aot.controller;

import com.aidata.aot.dto.MailDto;
import com.aidata.aot.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

@Controller
public class MailController {
    @Autowired
    private MailService mServ;

    @PostMapping("mailConfirm")
    @ResponseBody
    public String mailConfirm(MailDto mail) throws MessagingException, UnsupportedEncodingException{
        String authCode = mServ.sendEmail(mail);
        return authCode;
    }
}
