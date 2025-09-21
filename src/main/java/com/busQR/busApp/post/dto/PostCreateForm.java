package com.busQR.busApp.post.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCreateForm(
        @NotBlank String title,
        @NotBlank String content
) {}
