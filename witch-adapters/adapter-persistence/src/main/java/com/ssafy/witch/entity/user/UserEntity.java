package com.ssafy.witch.entity.user;

import com.ssafy.witch.entity.audit.MutableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`user`")
@Entity
public class UserEntity extends MutableBaseEntity {

  @Id
  @Column(unique = true, nullable = false)
  private String userId;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String nickname;

  private String profileImageUrl;

  public UserEntity(String userId, String email, String password, String nickname,
      String profileImageUrl) {
    this.userId = userId;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.profileImageUrl = profileImageUrl;
  }
}
