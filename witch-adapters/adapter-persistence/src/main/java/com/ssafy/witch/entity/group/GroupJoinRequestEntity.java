package com.ssafy.witch.entity.group;

import com.ssafy.witch.entity.audit.MutableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "group_join_request")
@Entity
public class GroupJoinRequestEntity extends MutableBaseEntity {

  @Id
  @Column(unique = true, nullable = false)
  private String groupJoinRequestId;

  @JoinColumn(nullable = false)
  private String userId;

  @JoinColumn(nullable = false)
  private String groupId;

  public GroupJoinRequestEntity(String groupJoinRequestId, String userId, String groupId) {
    this.groupJoinRequestId = groupJoinRequestId;
    this.userId = userId;
    this.groupId = groupId;
  }
}
