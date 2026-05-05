package org.example.functions;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

public class TodoNotificationFunction {

    @FunctionName("TodoCompletionNotifier")
    public void run(
            @ServiceBusQueueTrigger(
                    name = "message",
                    queueName = "%AZURE_SERVICEBUS_QUEUE_NAME%",
                    connection = "AZURE_SERVICEBUS_CONNECTION_STRING"
            ) String messageBody,
            final ExecutionContext context) {

        context.getLogger().info("TodoCompletionNotifier triggered. Message: " + messageBody);

        // In a real scenario here would be sending an email
        // For the demo we just log the received payload.
        context.getLogger().info("TODO completed notification processed successfully.");
    }
}
