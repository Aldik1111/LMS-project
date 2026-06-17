package com.example.lms.backend.presentation.controller;


import com.example.lms.backend.application.dto.TestResultDto;
import com.example.lms.backend.application.service.StatisticsService;
import com.example.lms.backend.domain.entity.Student;
import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final SecurityUtils securityUtils;

    @GetMapping("test/{testId}")
    public ResponseEntity<List<TestResultDto>> getResultByTest(@PathVariable Long testId){
        return ResponseEntity.ok(statisticsService.getResultsByTest(testId));
    }

    // Get /api/statistics/student/{studentId}
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TestResultDto>> getResultByStudent(@PathVariable Long studentId){
        User currentUser = securityUtils.getCurrentUser();

        if(currentUser instanceof Student && !currentUser.getId().equals(studentId)){
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(statisticsService.getResultsByStudent(studentId));
    }

    // GET /api/statistics/overall
    @GetMapping("/overall")
    public ResponseEntity<List<StatisticsService.TestSummaryDto>> getOverall() {
        return ResponseEntity.ok(statisticsService.getOverallStatistics());
    }

    // GET /api/statistics/my
    @GetMapping("/my")
    public ResponseEntity<List<TestResultDto>> getMyResults(){
        User currentUser = securityUtils.getCurrentUser();
        return ResponseEntity.ok(statisticsService.getResultsByStudent(currentUser.getId()));
    }
}
