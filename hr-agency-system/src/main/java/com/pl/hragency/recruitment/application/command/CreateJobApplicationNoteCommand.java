package com.pl.hragency.recruitment.application.command;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateJobApplicationNoteCommand(
        @NotBlank(message = "Content is required")
        @Size(min = 10, max = 500, message = "Content must not exceed 500 characters and should be at least 10 characters long")
        String content){
}
