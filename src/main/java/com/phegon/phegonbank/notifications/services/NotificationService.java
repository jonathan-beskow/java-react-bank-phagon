package com.phegon.phegonbank.notifications.services;

import com.phegon.phegonbank.auth_users.entity.User;
import com.phegon.phegonbank.notifications.dto.NotificationDTO;

public interface NotificationService {

    void sendEmail(NotificationDTO notificationDTO, User user);

}
