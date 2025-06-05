package com.ssafy.witch.repository.appointment;

import static com.querydsl.core.types.Projections.constructor;
import static com.ssafy.witch.entity.appointment.QAppointmentEntity.appointmentEntity;
import static com.ssafy.witch.entity.appointment.QAppointmentMemberEntity.appointmentMemberEntity;
import static com.ssafy.witch.entity.group.QGroupEntity.groupEntity;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.witch.apoointment.model.AppointmentMemberProjection;
import com.ssafy.witch.entity.appointment.AppointmentDetailEntityProjection;
import com.ssafy.witch.entity.appointment.AppointmentEntity;
import com.ssafy.witch.entity.appointment.AppointmentEntityProjection;
import com.ssafy.witch.entity.appointment.AppointmentWithGroupEntityProjection;
import com.ssafy.witch.entity.appointment.QAppointmentEntity;
import com.ssafy.witch.entity.appointment.QAppointmentMemberEntity;
import com.ssafy.witch.entity.notification.QFcmTokenEntity;
import com.ssafy.witch.entity.user.QUserEntity;
import com.ssafy.witch.group.model.GroupProjection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AppointmentCustomRepositoryImpl implements AppointmentCustomRepository {

  private final JPAQueryFactory queryFactory;

  public boolean existsConflictAppointment(String userId, LocalDateTime appointmentTime) {
    LocalDateTime oneHourLater = appointmentTime.plusMinutes(50);

    Integer fetchOne = queryFactory
        .selectOne()
        .from(appointmentMemberEntity)
        .join(appointmentEntity)
        .on(appointmentMemberEntity.appointmentId.eq(appointmentEntity.appointmentId))
        .where(
            appointmentMemberEntity.userId.eq(userId),
            appointmentEntity.appointmentTime.between(appointmentTime, oneHourLater)
        )
        .fetchFirst();

    return fetchOne != null; // 값이 있으면 true, 없으면 false
  }

  @Override
  public List<AppointmentEntityProjection> getAppointments(String userId, String groupId) {

    return queryFactory
        .select(constructor(AppointmentEntityProjection.class,
            appointmentEntity.appointmentId,
            appointmentEntity.name,
            appointmentEntity.appointmentTime,
            appointmentEntity.status,
            appointmentMemberEntity.userId.when(userId).then(true).otherwise(false)
        ))
        .from(appointmentEntity)
        .leftJoin(appointmentMemberEntity)
        .on(appointmentEntity.appointmentId.eq(appointmentMemberEntity.appointmentId)
            .and(appointmentMemberEntity.userId.eq(userId)))
        .where(appointmentEntity.groupId.eq(groupId))
        .fetch();
  }

  @Override
  public List<AppointmentWithGroupEntityProjection> getMyAppointments(
      String userId, int year, int month) {
    LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
    LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

    return queryFactory
        .select(constructor(AppointmentWithGroupEntityProjection.class,
            appointmentEntity.appointmentId,
            appointmentEntity.name,
            appointmentEntity.appointmentTime,
            appointmentEntity.status,
            constructor(GroupProjection.class,
                groupEntity.groupId,
                groupEntity.name,
                groupEntity.groupImageUrl)))
        .from(appointmentEntity)
        .innerJoin(appointmentMemberEntity)
        .on(appointmentEntity.appointmentId.eq(appointmentMemberEntity.appointmentId))
        .innerJoin(groupEntity)
        .on(appointmentEntity.groupId.eq(groupEntity.groupId))
        .where(
            appointmentMemberEntity.userId.eq(userId)
                .and(appointmentEntity.appointmentTime.between(startOfMonth, endOfMonth))
        )
        .orderBy(appointmentEntity.appointmentTime.asc())
        .fetch();
  }

  @Override
  public AppointmentDetailEntityProjection getAppointmentDetail(String appointmentId) {
    QAppointmentEntity appointment = QAppointmentEntity.appointmentEntity;
    QAppointmentMemberEntity member = QAppointmentMemberEntity.appointmentMemberEntity;
    QUserEntity user = QUserEntity.userEntity;
    QFcmTokenEntity fcmToken = QFcmTokenEntity.fcmTokenEntity;

    Map<AppointmentEntity, List<AppointmentMemberProjection>> transform = queryFactory
        .select(appointment)
        .from(appointment)
        .leftJoin(member).on(member.appointmentId.eq(appointment.appointmentId))
        .leftJoin(user).on(member.userId.eq(user.userId))
        .leftJoin(fcmToken).on(user.userId.eq(fcmToken.userId))
        .where(appointment.appointmentId.eq(appointmentId))
        .transform(GroupBy.groupBy(appointment).as(
            GroupBy.list(
                Projections.constructor(
                    AppointmentMemberProjection.class,
                    user.userId,
                    user. nickname,
                    user.profileImageUrl,
                    member.isLeader,
                    fcmToken.fcmToken
                )
            )
        ));

    Map.Entry<AppointmentEntity, List<AppointmentMemberProjection>> entry =
        transform.entrySet().iterator().next();
    AppointmentEntity appointmentEntity = entry.getKey();
    List<AppointmentMemberProjection> members = entry.getValue();

    // 3. DTO 매핑
    return new AppointmentDetailEntityProjection(
        appointmentEntity.getAppointmentId(),
        appointmentEntity.getName(),
        appointmentEntity.getStatus(), // Enum 변환 필요 시
        appointmentEntity.getSummary(),
        appointmentEntity.getAppointmentTime(),
        appointmentEntity.getAddress(),
        appointmentEntity.getLongitude(),
        appointmentEntity.getLatitude(),
        members
    );
  }
}
