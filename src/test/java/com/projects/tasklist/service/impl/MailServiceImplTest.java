package com.projects.tasklist.service.impl;

import com.projects.tasklist.config.TestConfig;
import com.projects.tasklist.domain.MailType;
import com.projects.tasklist.domain.user.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Properties;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {

    @MockBean
    private Configuration configuration;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private MailServiceImpl mailService;

    @Test
    void sendEmailForRegistration() {
        try {
            // given
            String name = "name";
            String username = "username";
            User user = new User();
            user.setUsername(name);
            user.setUsername(username);

            Mockito.when(javaMailSender.createMimeMessage())
                    .thenReturn(Mockito.mock(MimeMessage.class));
            Mockito.when(configuration.getTemplate("register.ftlh"))
                    .thenReturn(Mockito.mock(Template.class));

            // when
            mailService.sendEmail(
                    user, MailType.REGISTRATION, new Properties()
            );

            // then
            Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
            Mockito.verify(configuration).getTemplate("register.ftlh");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sendEmailForReminder() {
        try {
            // given
            String name = "name";
            String username = "username";
            User user = new User();
            user.setUsername(name);
            user.setUsername(username);

            Mockito.when(javaMailSender.createMimeMessage())
                    .thenReturn(Mockito.mock(MimeMessage.class));
            Mockito.when(configuration.getTemplate("reminder.ftlh"))
                    .thenReturn(Mockito.mock(Template.class));

            // when
            mailService.sendEmail(user, MailType.REMINDER, new Properties());

            // then
            Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
            Mockito.verify(configuration).getTemplate("reminder.ftlh");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
