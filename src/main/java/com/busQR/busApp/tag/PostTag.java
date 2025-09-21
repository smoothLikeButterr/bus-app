package com.busQR.busApp.tag;

import com.busQR.busApp.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
@Entity
@Table(name = "post_tag",
        indexes = @Index(name = "ix_posttag_tag_post", columnList = "tag_id, post_id"))
public class PostTag {

    @EmbeddedId
    private Id id = new Id();

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Embeddable
    public static class Id implements Serializable {
        private Long postId;
        private Long tagId;

        public Id() {}
        public Id(Long postId, Long tagId) { this.postId = postId; this.tagId = tagId; }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;
            Id that = (Id) o;
            return Objects.equals(postId, that.postId) && Objects.equals(tagId, that.tagId);
        }
        @Override public int hashCode() { return Objects.hash(postId, tagId); }
    }

    // getters/setters…
}