package com.busQR.busApp.user;

import com.busQR.busApp.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "users", uniqueConstraints =  {
        @UniqueConstraint(name = "uk_user_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_user_nickname", columnNames = "nickname")
})
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String passwordHash;

    @Column(length = 60, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(length = 12, nullable = false)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(length = 12, nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    private LocalDateTime deletedAt;
}
