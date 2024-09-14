package com.projects.tasklist.service.impl;

import com.projects.tasklist.domain.MailType;
import com.projects.tasklist.domain.task.Task;
import com.projects.tasklist.domain.user.User;
import com.projects.tasklist.service.MailService;
import com.projects.tasklist.service.Reminder;
import com.projects.tasklist.service.TaskService;
import com.projects.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder {

    private final TaskService taskService;
    private final UserService userService;
    private final MailService mailService;
    private final Duration duration = Duration.ofHours(1);


    @Scheduled(cron = "0 0 * * * *")
    @Override
    public void reminderForTask() {
        List<Task> tasks = taskService.getAllSoonTasks(duration);
        tasks.forEach(task -> {
            User user = userService.getTaskAuthor(task.getId());
            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription());
            mailService.sendEmail(user, MailType.REMINDER, properties);
        });
    }
}
