package ru.t1.daev.bishopstarter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.t1.daev.bishopstarter.exception.CommandQueueFullException;
import ru.t1.daev.bishopstarter.metrics.AndroidMetrics;
import ru.t1.daev.bishopstarter.model.Command;
import ru.t1.daev.bishopstarter.model.CommandPriority;
import ru.t1.daev.bishopstarter.task.CommandExecutionTask;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class CommandQueueService {

    private final ThreadPoolExecutor executor;
    private final AndroidMetrics androidMetrics;

    @Autowired
    public CommandQueueService(AndroidMetrics androidMetrics) {
        this.androidMetrics = androidMetrics;
        this.executor = new ThreadPoolExecutor(
                1,
                5,
                30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100) // task queue
        );
    }

    public void addCommand(Command command) {
        if (command.getPriority() == CommandPriority.CRITICAL) {
            executor.execute(new CommandExecutionTask(command, this::updateMetrics));
        } else {
            if (!executor.getQueue().offer(new CommandExecutionTask(command, this::updateMetrics))) {
                throw new CommandQueueFullException("Command queue is full");
            }
        }
        androidMetrics.updateQueueSize(executor.getQueue().size());
    }

    private void updateMetrics(Command command) {
        androidMetrics.updateQueueSize(executor.getQueue().size());
        androidMetrics.incrementCommandCount(command.getAuthor());
    }
}