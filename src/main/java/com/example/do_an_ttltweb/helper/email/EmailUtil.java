package com.example.do_an_ttltweb.helper.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailUtil {

    private static final String FROM_EMAIL = "nguyenhuybaolegit@gmail.com";
    private static final String PASSWORD = "deus bqxf hwqe jwol"; // bí mật

    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void sendEmailAsync(String toEmail, String subject, String body) {
        executor.submit(() -> {
            try {
                sendEmail(toEmail, subject, body);
                System.out.println("Gửi email thành công tới: " + toEmail);
            } catch (MessagingException e) {
                System.err.println("Lỗi khi gửi email tới " + toEmail + ": " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public static void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject, "UTF-8");
        message.setContent(body, "text/html; charset=UTF-8");

        Transport.send(message);
    }

    public static String buildReplyEmailTemplate(String toName, String content) {
        return "<h3>Xin chào " + toName + ",</h3>"
                + "<p>Cảm ơn bạn đã liên hệ với Aroma Café. Đây là phản hồi từ chúng tôi:</p>"
                + "<div style='background-color: #f0f0f0; padding: 15px; border-left: 5px solid #c76739; margin: 10px 0;'>"
                + content.replace("\n", "<br>")
                + "</div>"
                + "<p>Trân trọng,<br><b>Aroma Café Support Team</b></p>";
    }
}