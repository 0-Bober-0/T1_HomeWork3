package ru.t1.daev.bishopprototype.controller;

import org.springframework.web.bind.annotation.*;
import ru.t1.daev.bishopstarter.annotation.WeylandWatchingYou;
import ru.t1.daev.bishopstarter.dto.CommandRequest;
import ru.t1.daev.bishopstarter.dto.CommandResponse;
import ru.t1.daev.bishopstarter.service.CommandExecutorService;

@RestController
@RequestMapping("/api/commands")
public class CommandController {

    private final CommandExecutorService commandExecutorService;

    public CommandController(CommandExecutorService commandExecutorService) {
        this.commandExecutorService = commandExecutorService;
    }

    @PostMapping
    @WeylandWatchingYou
    public CommandResponse submitCommand(@RequestBody CommandRequest request) {
        commandExecutorService.executeCommand(
                commandExecutorService.validateAndCreateCommand(request)
        );
        return new CommandResponse("SUCCESS", "Command accepted for processing");
    }
}