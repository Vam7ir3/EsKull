package se.ki.education.nkcx.repo.projection;

import se.ki.education.nkcx.enums.IOSNotificationMode;

public interface DeviceProjection {
    String getToken();

    IOSNotificationMode getIosNotificationMode();

    Integer getBadgeCount();
}
