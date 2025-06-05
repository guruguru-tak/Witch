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
@Table(name = "group_member")
@Entity
public class GroupMemberEntity extends MutableBaseEntity {

  @Id
  @Column(updatable = false, nullable = false)
  private String groupMemberId;

  @JoinColumn(nullable = false)
  private String userId;

  @JoinColumn(nullable = false)
  private String groupId;

  @Column(nullable = false)
  private boolean isLeader;

  @Column(nullable = false)
  private int cntLateArrival;

  public GroupMemberEntity(String groupMemberId, String userId, String groupId, boolean isLeader,
      int cntLateArrival) {
    this.groupMemberId = groupMemberId;
    this.userId = userId;
    this.groupId = groupId;
    this.isLeader = isLeader;
    this.cntLateArrival = cntLateArrival;
  }
}
