package com.ssafy.witch.entity.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class MutableBaseEntity extends BaseEntity {

  @LastModifiedDate
  @Column(name = "modified_at", nullable = false)
  private LocalDateTime modifiedAt;

}
