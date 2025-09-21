package com.busQR.busApp.reaction;

import com.busQR.busApp.comment.Comment;
import com.busQR.busApp.common.BaseTimeEntity;
import com.busQR.busApp.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "comment_reaction",
        uniqueConstraints = @UniqueConstraint(name = "uk_comment_reaction_user_target", columnNames = {"comment_id","user_id"}),
        indexes = {
                @Index(name = "ix_comment_reaction_comment", columnList = "comment_id"),
                @Index(name = "ix_comment_reaction_user", columnList = "user_id")
        })
public class CommentReaction extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING) @Column(length = 10, nullable = false)
    private ReactionType type = ReactionType.LIKE;

    // getters/setters...
}