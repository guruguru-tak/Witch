package com.ssafy.witch.repository.group;

import static com.ssafy.witch.entity.group.QGroupMemberEntity.groupMemberEntity;
import static com.ssafy.witch.entity.user.QUserEntity.userEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.witch.group.model.GroupMemberProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupMemberCustomRepositoryImpl implements GroupMemberCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public boolean isLeaderByUserIdAndGroupId(String userId, String groupId) {
    Integer fetchOne = queryFactory
        .selectOne()
        .from(groupMemberEntity)
        .where(
            groupMemberEntity.userId.eq(userId),
            groupMemberEntity.groupId.eq(groupId),
            groupMemberEntity.isLeader.isTrue()
        )
        .fetchFirst();

    return fetchOne != null;
  }

  @Override
  public List<GroupMemberProjection> readGroupMemberList(String groupId) {
    return queryFactory.select(Projections.constructor(GroupMemberProjection.class,
            userEntity.userId,
            userEntity.nickname,
            userEntity.profileImageUrl,
            groupMemberEntity.isLeader
        ))
        .from(groupMemberEntity)
        .join(userEntity).on(groupMemberEntity.userId.eq(userEntity.userId))
        .where(groupMemberEntity.groupId.eq(groupId)).fetch();
  }
}
