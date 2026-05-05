package org.example.service;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.ServiceBusMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.example.model.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TodoNotificationService {

    private static final Logger log = LoggerFactory.getLogger(TodoNotificationService.class);

    @Value("${azure.servicebus.connection-string:}")
    private String connectionString;

    @Value("${azure.servicebus.queue-name:todo-notifications}")
    private String queueName;

    private ServiceBusSenderClient sender;

    @PostConstruct
    void init() {
        if (connectionString == null || connectionString.isBlank()) {
            log.warn("Azure Service Bus connection string not configured — notifications disabled");
            return;
        }
        sender = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();
        log.info("Service Bus sender initialised for queue '{}'", queueName);
    }

    public void sendCompletionNotification(Todo todo) {
        if (sender == null) {
            return;
        }
        String body = String.format(
                "{\"id\":%d,\"title\":\"%s\",\"completedAt\":\"%s\"}",
                todo.getId(),
                todo.getTitle().replace("\"", "\\\""),
                java.time.Instant.now()
        );
        try {
            sender.sendMessage(new ServiceBusMessage(body));
            log.info("Notification sent for todo id={}", todo.getId());
        } catch (Exception e) {
            log.error("Failed to send Service Bus notification for todo id={}: {}", todo.getId(), e.getMessage());
        }
    }

    @PreDestroy
    void close() {
        if (sender != null) {
            sender.close();
        }
    }
}
