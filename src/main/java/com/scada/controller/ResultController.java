package com.scada.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.scada.entities.ResultRecord;
import com.scada.service.ResultService;
import com.scada.sess.AdminSessionTracker;

import java.util.List;

@RestController
@RequestMapping("/api/result")
@CrossOrigin(origins = {"https://scada.pratikshat.com", "http://198.7.114.147:8888", "http://207.180.233.141:8888", "http://localhost:8888", "http://localhost:4200"})
public class ResultController {

    @Autowired
    private ResultService service;

@PostMapping("/save")
public ResultRecord saveResult(@RequestBody ResultRecord record) {

    String admin = AdminSessionTracker.getCurrentAdmin();
    record.setAdminName(admin != null ? admin : "NO_ADMIN");

    long now = System.currentTimeMillis();

    if (record.getStartTime() == null)
        record.setStartTime(now);

    if (record.getEndTime() == null)
        record.setEndTime(now);

    if (record.getTimeTakenMs() == null)
        record.setTimeTakenMs(record.getEndTime() - record.getStartTime());

    return service.save(record);
}


    @GetMapping("/user/{username}")
    public List<ResultRecord> getByUser(@PathVariable("username") String username) {
        return service.findByUser(username);
    }

    @GetMapping("/{id}")
    public ResultRecord getById(@PathVariable("id") Long id) {
        return service.findById(id).orElse(null);
    }

    @GetMapping("/all")
    public List<ResultRecord> getAll() {
        return service.findAll();
    }
    @GetMapping("/session/{sessionId}")
    public ResultRecord getResultBySession(@PathVariable String sessionId) {
        return service.findBySessionId(sessionId);
          
    }

}
