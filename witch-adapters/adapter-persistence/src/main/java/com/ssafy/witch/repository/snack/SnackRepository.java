package com.ssafy.witch.repository.snack;

import com.ssafy.witch.mapper.snack.SnackMapper;
import com.ssafy.witch.mapper.snack.SnackProjectionMapper;
import com.ssafy.witch.snack.Snack;
import com.ssafy.witch.snack.SnackPort;
import com.ssafy.witch.snack.SnackReadPort;
import com.ssafy.witch.snack.model.SnackDetailProjection;
import com.ssafy.witch.snack.model.SnackProjection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SnackRepository implements SnackPort, SnackReadPort {

  private final SnackJpaRepository snackJpaRepository;

  private final SnackMapper snackMapper;
  private final SnackProjectionMapper snackProjectionMapper;

  @Override
  public Snack save(Snack snack) {
    return snackMapper.toDomain(snackJpaRepository.save(snackMapper.toEntity(snack)));
  }

  @Override
  public boolean existsById(String snackId) {
    return snackJpaRepository.existsById(snackId);
  }

  @Override
  public Optional<Snack> findById(String snackId) {
    return snackJpaRepository.findById(snackId)
        .map(snackMapper::toDomain);
  }

  @Override
  public void deleteById(String snackId) {
    snackJpaRepository.deleteById(snackId);
  }

  @Override
  public boolean isOwnerByUserIdAndSnackId(String userId, String snackId) {
    return snackJpaRepository.isOwnerByUserIdAndSnackId(userId, snackId);
  }

  public List<SnackProjection> getSnacks(String userId, String appointmentId) {
    return snackJpaRepository.getSnacks(userId, appointmentId)
        .stream()
        .map(snackProjectionMapper::toProjection)
        .toList();
  }

}
