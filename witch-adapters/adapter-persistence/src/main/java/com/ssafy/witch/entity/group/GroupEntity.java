package com.ssafy.witch.entity.group;

import com.ssafy.witch.entity.audit.MutableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "`group`")
@Entity
public class GroupEntity extends MutableBaseEntity {

  @Id
  @Column(unique = true, nullable = false)
  private String groupId;

  @Column(unique = true, nullable = false)
  private String name;

  private String groupImageUrl;

  public GroupEntity(String groupId, String name, String groupImageUrl) {
    this.groupId = groupId;
    this.name = name;
    this.groupImageUrl = groupImageUrl;
  }

}
