package ru.t1.daev.bishopstarter.task;


import ru.t1.daev.bishopstarter.model.Command;

import java.util.function.Consumer;

public class CommandExecutionTask implements Runnable {

    private final Command command;
    private final Consumer<Command> completionCallback;

    public CommandExecutionTask(Command command, Consumer<Command> completionCallback) {
        this.command = command;
        this.completionCallback = completionCallback;
    }

    @Override
    public void run() {
        try {
            System.out.printf(
                    "[EXECUTING] %s command from %s: %s%n",
                    command.getPriority(),
                    command.getAuthor(),
                    command.getDescription()
            );

            Thread.sleep(500);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            completionCallback.accept(command);
        }
    }
}