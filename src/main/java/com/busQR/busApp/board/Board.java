package com.busQR.busApp.board;

import com.busQR.busApp.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "board", indexes = {
        @Index(name = "ix_board_visibility_sort", columnList = "visibility, sortOrder")
}, uniqueConstraints = @UniqueConstraint(name = "uk_board_code", columnNames = "code"))
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String code;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private BoardVisibility visibility = BoardVisibility.PUBLIC;

    private Integer sortOrder = 0;
}
