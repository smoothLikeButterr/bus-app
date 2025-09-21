package com.busQR.busApp.board;

import com.busQR.busApp.board.dto.BoardSearchForm;
import com.busQR.busApp.common.PageResult;
import com.busQR.busApp.post.Post;
import com.busQR.busApp.post.PostService;
import com.busQR.busApp.post.dto.PostCreateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final PostService postService;

    // ✅ 목록 + 검색
    @GetMapping("/{code}")
    public String list(@PathVariable String code,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "20") int size,
                       @ModelAttribute("search") BoardSearchForm search,
                       Model model) {

        PageResult<Post> result = postService.searchBoard(code, search, page, size);

        model.addAttribute("boardCode", code);
        model.addAttribute("posts", result.getContent());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("page", result.getPage());
        model.addAttribute("size", result.getSize());
        model.addAttribute("totalPages", result.getTotalPages());
        return "board/list";
    }

    // ✅ 글쓰기 폼
    @GetMapping("/{code}/write")
    public String writeForm(@PathVariable String code,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "20") int size,
                            Model model) {
        model.addAttribute("boardCode", code);
        model.addAttribute("form", new PostCreateForm("", ""));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "board/form";
    }

    // ✅ 글 저장
    @PostMapping("/{code}/write")
    public String write(@PathVariable String code,
                        @Valid @ModelAttribute("form") PostCreateForm form,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size) {
        // TODO: 보안 도입 전 임시 사용자
        Long authorId = 1L;
        Long postId = postService.create(code, authorId, form);
        // 상세로 이동 (목록 복귀 파라미터 유지)
        return "redirect:/posts/" + postId + "?page=" + page + "&size=" + size;
    }
}
