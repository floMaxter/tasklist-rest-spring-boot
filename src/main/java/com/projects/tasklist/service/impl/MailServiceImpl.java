package com.projects.tasklist.service.impl;

import com.projects.tasklist.domain.MailType;
import com.projects.tasklist.domain.user.User;
import com.projects.tasklist.service.MailService;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final Configuration configuration;

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(final User user,
                          final MailType mailType,
                          final Properties properties) {
        switch (mailType) {
            case REGISTRATION -> sendRegistrationEmail(user);
            case REMINDER -> sendReminderEmail(user, properties);
            default -> {
            }
        }
    }

    @SneakyThrows
    private void sendRegistrationEmail(final User user) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                false,
                "UTF-8");
        helper.setSubject("Thank you for registration, " + user.getName());
        helper.setTo(user.getUsername());
        String emailContent = getRegistrationEmailContent(user);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(final User user) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        configuration.getTemplate("register.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private void sendReminderEmail(final User user,
                                   final Properties properties) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                false,
                "UTF-8");
        helper.setSubject("You have a task is due in an 1 hour");
        helper.setTo(user.getUsername());
        String emailContent = getReminderEmailContent(user, properties);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private String getReminderEmailContent(final User user,
                                           final Properties properties) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("title", properties.getProperty("task.title"));
        model.put("description", properties.getProperty("task.description"));
        configuration.getTemplate("reminder.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }
}
