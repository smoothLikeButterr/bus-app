package com.busQR.busApp.reaction;

import com.busQR.busApp.common.BaseTimeEntity;
import com.busQR.busApp.post.Post;
import com.busQR.busApp.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_reaction",
        uniqueConstraints = @UniqueConstraint(name = "uk_post_reaction_user_target", columnNames = {"post_id","user_id"}),
        indexes = {
                @Index(name = "ix_post_reaction_post", columnList = "post_id"),
                @Index(name = "ix_post_reaction_user", columnList = "user_id")
        })
@Getter @Setter
public class PostReaction extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING) @Column(length = 10, nullable = false)
    private ReactionType type = ReactionType.LIKE;


    // denormalized 카운터
    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(name = "dislike_count", nullable = false)
    private int dislikeCount = 0;
    // getters/setters...
}