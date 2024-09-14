package com.projects.tasklist.service;

import com.projects.tasklist.domain.MailType;
import com.projects.tasklist.domain.user.User;

import java.util.Properties;

public interface MailService {

    void sendEmail(User user, MailType mailType, Properties properties);
}
