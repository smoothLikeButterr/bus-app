package com.busQR.busApp.post;

import com.busQR.busApp.board.Board;
import com.busQR.busApp.common.BaseTimeEntity;
import com.busQR.busApp.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "post",
        indexes = {
                @Index(name = "ix_post_board_status_pin_created",
                        columnList = "board_id, status, pinnedUntil DESC, createdAt DESC")
        })
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(length = 200, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content; // CLOB(TEXT) 매핑

    @Enumerated(EnumType.STRING)
    @Column(length = 12, nullable = false)
    private PostStatus status = PostStatus.PUBLISHED;

    private LocalDateTime pinnedUntil;

    private Integer viewCount = 0;
    private Integer likeCount = 0;
    private Integer dislikeCount = 0;
    private Integer commentCount = 0;

    private LocalDateTime deletedAt;

    // getters/setters…
}