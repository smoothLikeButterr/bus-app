package com.busQR.busApp.comment;

import com.busQR.busApp.common.BaseTimeEntity;
import com.busQR.busApp.post.Post;
import com.busQR.busApp.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "comment",
        indexes = {
                @Index(name = "ix_comment_post_sortkey", columnList = "post_id, sort_key"),
                @Index(name = "ix_comment_post_parent_created", columnList = "post_id, parent_id, created_at")
        })
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 12, nullable = false)
    private CommentStatus status = CommentStatus.PUBLISHED;

    @Column(nullable = false)
    private int depth = 0; // 0=루트

    @Column(length = 255)
    private String sortKey; // PATH or MPTT 등 계층 정렬 키

    private LocalDateTime deletedAt;

    // getters/setters…
}
