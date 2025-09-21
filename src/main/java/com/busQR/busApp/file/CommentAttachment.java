package com.busQR.busApp.file;

import com.busQR.busApp.comment.Comment;
import com.busQR.busApp.common.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "comment_attachment",
        indexes = @Index(name = "ix_comment_attachment_comment", columnList = "comment_id"))
public class CommentAttachment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Column(length = 255, nullable = false) private String originalName;
    @Column(length = 255, nullable = false) private String storedName;
    @Column(length = 100) private String contentType;
    @Column(nullable = false) private Long sizeBytes;
    @Column(length = 500, nullable = false) private String url;

    // getters/setters...
}