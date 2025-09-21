package com.busQR.busApp.post;

import com.busQR.busApp.comment.CommentService;
import com.busQR.busApp.post.dto.PostCreateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

//    @GetMapping("/posts/{code}")
//    public String list(@PathVariable String code,
//                       @RequestParam(defaultValue = "0") int page,
//                       @RequestParam(defaultValue = "20") int size,
//                       Model model) {
//        List<Post> posts = postService.list(code, page, size);
//        long total = postService.count(code);
//        long totalPages = (long) Math.ceil((double) total / size);
//
//        model.addAttribute("posts", posts);
//        model.addAttribute("boardCode", code);
//        model.addAttribute("page", page);
//        model.addAttribute("size", size);
//        model.addAttribute("total", total);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("hasPrev", page > 0);
//        model.addAttribute("hasNext", page + 1 < totalPages);
//        return "post/list";
//    }
//
//    @GetMapping("/boards/{code}/write")
//    public String writeForm(@PathVariable String code, Model model) {
//        model.addAttribute("boardCode", code);
//        model.addAttribute("form", new PostCreateForm("", ""));
//        return "post/form";
//    }
//
//    @PostMapping("/boards/{code}/write")
//    public String write(@PathVariable String code, @Valid @ModelAttribute("form") PostCreateForm form) {
//        // TODO: Security 도입 전까지 임시 사용자
//        Long authorId = 1L;
//        Long postId = postService.create(code, authorId, form);
//        return "redirect:/posts/" + postId;
//    }

    @GetMapping("/posts/{id}")
    public String detail(@PathVariable Long id,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size,
                         Model model) {
        Post post = postService.get(id);
        model.addAttribute("post", post);
        model.addAttribute("boardCode", post.getBoard().getCode());

        // 댓글 목록 + 작성 폼
        model.addAttribute("comments", commentService.listByPost(id));
        model.addAttribute("commentForm", new com.busQR.busApp.comment.dto.CommentCreateForm(null, ""));

        // ★ 목록 복귀용 파라미터
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "post/detail";
    }



}
