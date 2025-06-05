package com.ssafy.witch.repository.group;

import static com.ssafy.witch.entity.group.QGroupEntity.groupEntity;
import static com.ssafy.witch.entity.group.QGroupMemberEntity.groupMemberEntity;
import static com.ssafy.witch.entity.notification.QFcmTokenEntity.fcmTokenEntity;
import static com.ssafy.witch.entity.user.QUserEntity.userEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.witch.entity.group.GroupEntity;
import com.ssafy.witch.entity.group.QGroupEntity;
import com.ssafy.witch.entity.group.QGroupMemberEntity;
import com.ssafy.witch.entity.notification.QFcmTokenEntity;
import com.ssafy.witch.entity.user.QUserEntity;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.group.Group;
import com.ssafy.witch.group.GroupMember;
import com.ssafy.witch.group.GroupMemberUser;
import com.ssafy.witch.group.GroupWithMemberUsers;
import com.ssafy.witch.group.model.GroupDetailProjection;
import com.ssafy.witch.group.model.GroupWithLeaderProjection;
import com.ssafy.witch.user.User;
import com.ssafy.witch.user.UserWithFcmToken;
import com.ssafy.witch.user.model.UserBasicProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupCustomRepositoryImpl implements GroupCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<GroupWithLeaderProjection> findGroupListReadModelsByUserId(String userId) {
    QGroupEntity group = groupEntity;
    QGroupMemberEntity groupMember = groupMemberEntity;
    QGroupMemberEntity leaderMember = new QGroupMemberEntity("leaderMember");
    QUserEntity leaderUser = new QUserEntity("leaderUser");

    return queryFactory
        .select(Projections.constructor(
            GroupWithLeaderProjection.class,
            group.groupId,
            group.name,
            group.groupImageUrl,
            group.createdAt,
            Projections.constructor(
                UserBasicProjection.class,
                leaderUser.userId,
                leaderUser.nickname,
                leaderUser.profileImageUrl
            )
        ))
        .from(group)
        .join(groupMember).on(group.groupId.eq(groupMember.groupId))
        .join(leaderMember)
        .on(group.groupId.eq(leaderMember.groupId).and(leaderMember.isLeader.isTrue()))
        .join(leaderUser).on(leaderMember.userId.eq(leaderUser.userId))
        .where(groupMember.userId.eq(userId))
        .fetch();
  }

  @Override
  public GroupDetailProjection readGroupDetail(String userId, String groupId) {

    return queryFactory
        .select(Projections.constructor(
            GroupDetailProjection.class,
            groupEntity.groupId,
            groupEntity.name,
            groupEntity.groupImageUrl,
            groupMemberEntity.isLeader,
            groupMemberEntity.cntLateArrival)
        )
        .from(groupEntity)
        .join(groupMemberEntity).on(groupEntity.groupId.eq(groupMemberEntity.groupId))
        .join(userEntity).on(groupMemberEntity.userId.eq(userEntity.userId))
        .where(groupEntity.groupId.eq(groupId).and(groupMemberEntity.userId.eq(userId)))
        .fetchOne();
  }

  @Override
  public GroupWithMemberUsers readGroupWithMemberUsers(String groupId) {
    QGroupEntity group = groupEntity;
    QGroupMemberEntity groupMember = groupMemberEntity;
    QUserEntity user = userEntity;
    QFcmTokenEntity fcmToken = fcmTokenEntity;

    GroupEntity targetGroup = queryFactory.selectFrom(group).where(group.groupId.eq(groupId))
        .fetchOne();

    if (targetGroup == null) {
      throw new GroupNotFoundException();
    }

    List<GroupMemberUser> groupMemberUsers = queryFactory.select(
            Projections.constructor(GroupMemberUser.class,
                Projections.constructor(GroupMember.class,
                    groupMember.groupMemberId,
                    groupMember.userId,
                    groupMember.groupId,
                    groupMember.isLeader,
                    groupMember.cntLateArrival),
                Projections.constructor(UserWithFcmToken.class,
                    Projections.constructor(User.class,
                        user.userId,
                        user.email,
                        user.password,
                        user.nickname,
                        user.profileImageUrl),
                    fcmToken.fcmToken
                )
            )
        )
        .from(group)
        .leftJoin(groupMember)
        .on(group.groupId.eq(groupMember.groupId))
        .leftJoin(user)
        .on(groupMember.userId.eq(user.userId))
        .leftJoin(fcmToken)
        .on(user.userId.eq(fcmToken.userId))
        .where(group.groupId.eq(groupId))
        .fetch();

    Group groupRet = new Group(groupId, targetGroup.getName(),
        targetGroup.getGroupImageUrl());
    return new GroupWithMemberUsers(groupRet, groupMemberUsers);
  }
}
