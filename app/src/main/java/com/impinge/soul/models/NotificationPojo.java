package com.impinge.soul.models;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificationPojo implements Serializable {

    ArrayList<NotificationData> notifications;

    public ArrayList<NotificationData> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<NotificationData> notifications) {
        this.notifications = notifications;
    }
}
