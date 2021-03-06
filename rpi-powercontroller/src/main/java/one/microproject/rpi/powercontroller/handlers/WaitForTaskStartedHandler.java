package one.microproject.rpi.powercontroller.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import one.microproject.rpi.powercontroller.dto.TaskId;
import one.microproject.rpi.powercontroller.services.AAService;
import one.microproject.rpi.powercontroller.services.TaskManagerService;

import java.io.InputStream;

public class WaitForTaskStartedHandler implements HttpHandler {

    private final AAService aaService;
    private final ObjectMapper mapper;
    private final TaskManagerService taskManagerService;

    public WaitForTaskStartedHandler(ObjectMapper mapper, AAService aaService, TaskManagerService taskManagerService) {
        this.aaService = aaService;
        this.mapper = mapper;
        this.taskManagerService = taskManagerService;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (!HandlerUtils.validateRequest(aaService, exchange)) {
            exchange.setStatusCode(HandlerUtils.FORBIDDEN);
            return;
        }
        HttpString requestMethod = exchange.getRequestMethod();
        if (HandlerUtils.METHOD_PUT.equals(requestMethod.toString())) {
            exchange.startBlocking();
            InputStream is = exchange.getInputStream();
            TaskId taskId = mapper.readValue(is, TaskId.class);
            taskManagerService.waitForStarted(taskId);
            exchange.setStatusCode(HandlerUtils.OK);
        } else {
            exchange.setStatusCode(HandlerUtils.METHOD_NOT_ALLOWED);
        }
    }

}
