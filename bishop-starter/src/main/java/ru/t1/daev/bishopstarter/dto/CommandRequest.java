package ru.t1.daev.bishopstarter.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.t1.daev.bishopstarter.model.CommandPriority;
import lombok.Data;

@Data
public class CommandRequest {
    @Size(max = 1000, message = "Description must be under 1000 characters")
    private String description;

    private CommandPriority priority;

    @Size(max = 100, message = "Author must be under 100 characters")
    private String author;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$",
            message = "Time must be in ISO8601 format")
    private String time;
}