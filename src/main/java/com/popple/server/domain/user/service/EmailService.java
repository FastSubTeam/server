package com.popple.server.domain.user.service;

import com.popple.server.domain.user.dto.EmailSource;
import com.popple.server.domain.user.exception.UserBadRequestException;
import com.popple.server.domain.user.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendRegisterMail(EmailSource emailSource, String token) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailSource.getTo());
            mimeMessageHelper.setSubject(emailSource.getSubject());
            mimeMessageHelper.setText(getJoinAuthenticationHtmlFormat(token), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new UserBadRequestException(UserErrorCode.SEND_MAIL_ERROR);
        }

    }

    public void sendRandomPasswordMail(EmailSource emailSource, String randomPassword) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailSource.getTo());
            mimeMessageHelper.setSubject(emailSource.getSubject());
            mimeMessageHelper.setText(getRandomPasswordHtmlFormat(randomPassword), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new UserBadRequestException(UserErrorCode.SEND_MAIL_ERROR);
        }

    }

    public EmailSource getEmailSource(String email, String subject) {
        return EmailSource.builder()
                .to(email)
                .subject(subject)
                .build();
    }

    private String getJoinAuthenticationHtmlFormat(String token) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<body>\n" +
                "<div style=\"margin:100px;\">\n" +
                "    <h1> 안녕하세요.</h1>\n" +
                "    <h1> 팝업스토어를 사랑하는 사람들을 위한 플랫폼 Popple 입니다.</h1>\n" +
                "    <br>\n" +
                "    <p> 아래 코드를 회원가입 창으로 돌아가 입력해주세요.</p>\n" +
                "    <br>\n" +
                "\n" +
                "    <div align=\"center\" style=\"border:1px solid black; font-family:verdana;\">\n" +
                "        <h3 style=\"color:blue\"> 회원가입 인증 코드 입니다. </h3>\n" +
                "        <div style=\"font-size:130%\">" + token + "</div>\n" +
                "    </div>\n" +
                "    <br/>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

    private String getRandomPasswordHtmlFormat(String randomPassword) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<body>\n" +
                "<div style=\"margin:100px;\">\n" +
                "    <h1> 안녕하세요.</h1>\n" +
                "    <h1> 팝업스토어를 사랑하는 사람들을 위한 플랫폼 Popple 입니다.</h1>\n" +
                "    <br>\n" +
                "    <p> 로그인 후 패스워드를 수정해주세요.</p>\n" +
                "    <br>\n" +
                "\n" +
                "    <div align=\"center\" style=\"border:1px solid black; font-family:verdana;\">\n" +
                "        <h3 style=\"color:blue\"> 무작위로 수정된 비밀번호입니다. </h3>\n" +
                "        <div style=\"font-size:130%\">" + randomPassword + "</div>\n" +
                "    </div>\n" +
                "    <br/>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }
}
