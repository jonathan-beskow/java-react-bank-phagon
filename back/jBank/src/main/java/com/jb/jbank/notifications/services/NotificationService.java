package com.jb.jbank.notifications.services;

import com.jb.jbank.auth_users.entity.User;
import com.jb.jbank.notifications.dto.NotificationDTO;

public interface NotificationService {

    void sendEmail(NotificationDTO notificationDTO, User user);

}
