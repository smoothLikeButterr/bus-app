package com.busQR.busApp.report;

import com.busQR.busApp.comment.Comment;
import com.busQR.busApp.common.BaseTimeEntity;
import com.busQR.busApp.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_report",
        indexes = {
                @Index(name = "ix_comment_report_comment_status", columnList = "comment_id, status"),
                @Index(name = "ix_comment_report_reporter", columnList = "reporter_id")
        })
public class CommentReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Column(length = 50, nullable = false)
    private String reasonCode;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING) @Column(length = 12, nullable = false)
    private ReportStatus status = ReportStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "handler_id")
    private User handler;

    private LocalDateTime handledAt;

    // getters/setters...
}