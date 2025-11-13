package capstone.demo.domain.user.entity;

import capstone.demo.domain.bookmark.Bookmark;
import capstone.demo.domain.user.LoginType;
import capstone.demo.domain.user.UserRole;
import capstone.demo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false,length = 20)
    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Bookmark> bookmarks = new ArrayList<>();
}
