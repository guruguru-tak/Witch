package com.ssafy.witch.repository.snack;

import com.ssafy.witch.entity.Snack.SnackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnackJpaRepository extends JpaRepository<SnackEntity, String >, SnackCustomRepository {

}
