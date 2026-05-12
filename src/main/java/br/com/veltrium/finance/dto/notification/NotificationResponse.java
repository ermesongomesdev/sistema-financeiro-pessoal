package br.com.veltrium.finance.dto.notification;

import br.com.veltrium.finance.model.enums.NotificationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String title,
        String message,
        LocalDate dueDate,
        NotificationStatus status,
        LocalDateTime createdAt
) {}
