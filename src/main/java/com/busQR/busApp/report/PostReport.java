package com.busQR.busApp.report;

import com.busQR.busApp.common.BaseTimeEntity;
import com.busQR.busApp.post.Post;
import com.busQR.busApp.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "post_report",
        indexes = {
                @Index(name = "ix_post_report_post_status", columnList = "post_id, status"),
                @Index(name = "ix_post_report_reporter", columnList = "reporter_id")
        })
public class PostReport extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Column(length = 50, nullable = false)
    private String reasonCode; // SPAM, ABUSE, AD ...

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING) @Column(length = 12, nullable = false)
    private ReportStatus status = ReportStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "handler_id")
    private User handler;

    private LocalDateTime handledAt;

    // getters/setters...
}