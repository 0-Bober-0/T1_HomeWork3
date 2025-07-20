package ru.t1.daev.bishopstarter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class Command {
    private String description;
    private CommandPriority priority;
    private String author;
    private Instant time;
}