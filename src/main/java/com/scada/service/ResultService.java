package com.scada.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.scada.entities.ResultRecord;
import com.scada.repository.ResultRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ResultService {

    @Autowired
    private ResultRepository repo;

    public ResultRecord save(ResultRecord r) {
        return repo.save(r);
    }

    public Optional<ResultRecord> findById(Long id) {
        return repo.findById(id);
    }

    public List<ResultRecord> findByUser(String username) {
        return repo.findByUserNameOrderByStartTimeDesc(username);
    }

    public List<ResultRecord> findAll() {
        return repo.findAll();
    }
  
    public ResultRecord findBySessionId(String sessionId) {
        return repo.findBySessionId(sessionId)
            .orElseThrow(() -> new RuntimeException("Result not found"));
    }

}
