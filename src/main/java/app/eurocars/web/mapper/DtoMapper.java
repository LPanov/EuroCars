package app.web.mapper;

import app.eurocars.notifications.model.Notification;
import app.eurocars.notifications.model.NotificationPreference;
import app.eurocars.notifications.model.NotificationType;
import app.eurocars.web.dto.NotificationPreferenceResponse;
import app.eurocars.web.dto.NotificationResponse;
import app.eurocars.web.dto.NotificationTypeRequest;
import lombok.experimental.UtilityClass;

import static app.eurocars.notifications.model.NotificationType.EMAIL;

@UtilityClass
public class DtoMapper {

    // Mapping logic: прехвърляме един тип данни към друг
    public static NotificationType fromNotificationTypeRequest(NotificationTypeRequest dto) {

        return switch (dto) {
            case EMAIL -> EMAIL;
        };
    }

    // Build dto from entity
    public static NotificationPreferenceResponse fromNotificationPreference(NotificationPreference entity) {

        return NotificationPreferenceResponse.builder()
                .id(entity.getId())
                .type(entity.getType())
                .contactInfo(entity.getContactInfo())
                .enabled(entity.isEnabled())
                .userId(entity.getUserId())
                .build();
    }

    public static NotificationResponse fromNotification(Notification entity) {

        // DTO building!
        return NotificationResponse.builder()
                .subject(entity.getSubject())
                .status(entity.getStatus())
                .createdOn(entity.getCreatedOn())
                .type(entity.getType())
                .build();
    }
}