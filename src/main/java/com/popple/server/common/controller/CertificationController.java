package com.popple.server.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Controller
public class CertificationController {

    @GetMapping("/.well-known/acme-challenge/{randomToken}")
    @ResponseBody
    public String serveChallenge(@PathVariable String randomToken) {
        // randomToken 값을 파일 내용으로 사용하여 파일을 생성하고 반환합니다.
        String challengeFilePath = "/var/www/letsencrypt/" + randomToken; // 파일 경로
        File challengeFile = new File(challengeFilePath);
        try {
            FileWriter fileWriter = new FileWriter(challengeFile);
            fileWriter.write(randomToken);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return randomToken;
    }
}
