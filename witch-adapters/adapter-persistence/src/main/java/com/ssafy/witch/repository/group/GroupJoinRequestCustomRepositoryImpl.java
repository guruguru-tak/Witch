package com.ssafy.witch.repository.group;

import static com.ssafy.witch.entity.group.QGroupJoinRequestEntity.groupJoinRequestEntity;
import static com.ssafy.witch.entity.user.QUserEntity.userEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.witch.entity.group.QGroupJoinRequestEntity;
import com.ssafy.witch.entity.user.QUserEntity;
import com.ssafy.witch.group.model.GroupJoinRequestProjection;
import com.ssafy.witch.user.model.UserBasicProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupJoinRequestCustomRepositoryImpl implements GroupJoinRequestCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<GroupJoinRequestProjection> findGroupJoinRequestListReadModelsByGroupId(String groupId) {
    QUserEntity user = userEntity;
    QGroupJoinRequestEntity groupJoinRequest = groupJoinRequestEntity;

    return queryFactory
        .select(Projections.constructor(
            GroupJoinRequestProjection.class,
            groupJoinRequest.groupJoinRequestId,
            Projections.constructor(
                UserBasicProjection.class,
                user.userId,
                user.nickname,
                user.profileImageUrl
            )
        ))
        .from(groupJoinRequest)
        .join(user).on(groupJoinRequest.userId.eq(user.userId))
        .where(groupJoinRequest.groupId.eq(groupId))
        .fetch();
  }
}
