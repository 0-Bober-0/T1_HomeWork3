package ru.t1.daev.bishopstarter.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.t1.daev.bishopstarter.dto.CommandRequest;
import ru.t1.daev.bishopstarter.exception.InvalidCommandException;
import ru.t1.daev.bishopstarter.model.Command;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@Service
@Validated
public class CommandExecutorService {

    private final CommandQueueService commandQueueService;

    public CommandExecutorService(CommandQueueService commandQueueService) {
        this.commandQueueService = commandQueueService;
    }

    public Command validateAndCreateCommand(@Valid CommandRequest request) {
        try {
            return new Command(
                    request.getDescription(),
                    request.getPriority(),
                    request.getAuthor(),
                    Instant.parse(request.getTime())
            );
        } catch (DateTimeParseException e) {
            throw new InvalidCommandException("Invalid time format");
        }
    }

    public void executeCommand(Command command) {
        commandQueueService.addCommand(command);
    }
}
