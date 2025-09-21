package com.busQR.busApp.file;

import com.busQR.busApp.common.BaseTimeEntity;
import com.busQR.busApp.post.Post;
import jakarta.persistence.*;

@Entity
@Table(name = "post_attachment",
        indexes = @Index(name = "ix_post_attachment_post", columnList = "post_id"))
public class PostAttachment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(length = 255, nullable = false) private String originalName;
    @Column(length = 255, nullable = false) private String storedName;
    @Column(length = 100) private String contentType;
    @Column(nullable = false) private Long sizeBytes;
    @Column(length = 500, nullable = false) private String url;

    // getters/setters...
}