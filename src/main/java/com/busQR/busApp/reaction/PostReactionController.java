package com.busQR.busApp.reaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class PostReactionController {

    private final PostReactionService reactionService;

    @PostMapping("/posts/{postId}/reactions")
    public String toggle(@PathVariable Long postId,
                         @RequestParam(defaultValue = "LIKE") ReactionType type,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size) {
        // TODO: 로그인 붙기 전 임시 사용자
        Long userId = 1L;
        reactionService.toggle(postId, userId, type);
        return "redirect:/posts/" + postId + "?page=" + page + "&size=" + size;
    }
}
