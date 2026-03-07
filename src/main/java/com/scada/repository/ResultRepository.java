
package com.scada.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.scada.entities.ResultRecord;

public interface ResultRepository extends JpaRepository<ResultRecord, Long> {
    List<ResultRecord> findByUserNameOrderByStartTimeDesc(String userName);
    Optional<ResultRecord> findBySessionId(String sessionId);

}
