package br.com.veltrium.finance.controller;

import br.com.veltrium.finance.dto.notification.NotificationResponse;
import br.com.veltrium.finance.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> list(@RequestParam Long householdId) {
        return notificationService.list(householdId);
    }

    @PatchMapping("/{id}/read")
    public NotificationResponse markAsRead(@PathVariable Long id, @RequestParam Long householdId) {
        return notificationService.markAsRead(id, householdId);
    }

    @PostMapping("/generate")
    public Map<String, Integer> generate(@RequestParam Long householdId) {
        return Map.of("created", notificationService.generateForHousehold(householdId));
    }
}
