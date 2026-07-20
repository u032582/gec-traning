package com.example.training.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record TaskRequest(
        @NotBlank(message = "titleは必須です")
        @Size(max = 100, message = "titleは100文字以内で入力してください")
        String title,

        @Size(max = 500, message = "descriptionは500文字以内で入力してください")
        String description,

        @NotBlank(message = "statusは必須です")
        @Pattern(regexp = "todo|doing|done", message = "statusは todo / doing / done のいずれかです")
        String status,

        LocalDate dueDate
) {}