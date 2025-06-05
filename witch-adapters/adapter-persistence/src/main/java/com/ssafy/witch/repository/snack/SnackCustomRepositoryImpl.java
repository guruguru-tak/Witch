package com.ssafy.witch.repository.snack;

import static com.querydsl.core.types.Projections.constructor;
import static com.ssafy.witch.entity.Snack.QSnackEntity.snackEntity;
import static com.ssafy.witch.entity.user.QUserEntity.userEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.witch.entity.Snack.SnackEntityProjection;
import com.ssafy.witch.user.model.UserBasicProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SnackCustomRepositoryImpl implements SnackCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public boolean isOwnerByUserIdAndSnackId(String userId, String snackId) {
    Integer fetchOne = queryFactory
        .selectOne()
        .from(snackEntity)
        .where(
            snackEntity.userId.eq(userId),
            snackEntity.snackId.eq(snackId)
        )
        .fetchFirst();
    return fetchOne != null;
  }

  public List<SnackEntityProjection> getSnacks(String userId, String appointmentId) {

    return queryFactory
        .select(constructor(SnackEntityProjection.class,
            snackEntity.snackId,
            snackEntity.longitude,
            snackEntity.latitude,
            snackEntity.snackImageUrl,
            snackEntity.snackSoundUrl,
            snackEntity.createdAt,
            constructor(UserBasicProjection.class,
                userEntity.userId,
                userEntity.nickname,
                userEntity.profileImageUrl)))
        .from(snackEntity)
        .innerJoin(userEntity)
        .on(snackEntity.userId.eq(userEntity.userId))
        // 스낵의 약속 id가 조회한 id랑 같은 스낵들 조회
        .where(snackEntity.appointmentId.eq(appointmentId))
        // 최근 생성된 스낵이 위로 오도록 정렬 (내림차순)
        .orderBy(snackEntity.createdAt.desc())
        .fetch();
  }

}
