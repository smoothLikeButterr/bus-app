package com.busQR.busApp.bookmark;

import com.busQR.busApp.post.Post;
import com.busQR.busApp.user.User;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bookmark",
        indexes = @Index(name = "ix_bookmark_post_user", columnList = "post_id, user_id"))
public class Bookmark {

    @EmbeddedId
    private Id id = new Id();

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Embeddable
    public static class Id implements Serializable {
        private Long userId;
        private Long postId;

        public Id() {}
        public Id(Long userId, Long postId) { this.userId = userId; this.postId = postId; }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;
            Id that = (Id) o;
            return Objects.equals(userId, that.userId) && Objects.equals(postId, that.postId);
        }
        @Override public int hashCode() { return Objects.hash(userId, postId); }
    }

    // getters/setters…
}