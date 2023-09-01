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
        return randomToken;
    }
}
