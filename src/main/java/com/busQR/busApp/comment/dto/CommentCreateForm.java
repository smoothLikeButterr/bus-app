package com.busQR.busApp.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateForm(
        Long parentId, // 대댓글이면 부모 댓글 ID, 아니면 null
        @NotBlank String content
) {
}
