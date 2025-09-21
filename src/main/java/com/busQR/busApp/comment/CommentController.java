package com.busQR.busApp.comment;

import com.busQR.busApp.comment.dto.CommentCreateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public String add(@PathVariable Long postId,
                      @Valid @ModelAttribute("commentForm") CommentCreateForm form) {
        // TODO: 보안 적용 전 임시 사용자
        Long authorId = 1L;
        commentService.add(postId, authorId, form);
        return "redirect:/posts/" + postId;
    }

}
