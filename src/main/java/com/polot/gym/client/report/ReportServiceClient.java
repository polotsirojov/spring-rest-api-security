package com.polot.gym.client.report;

import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.payload.response.ReportResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "GYM-REPORT-SERVICE", fallback = GymReportServiceFallback.class)
public interface ReportServiceClient {

    @GetMapping("/api/v1/report")
    List<ReportResponse> getAll();

    @PostMapping("/api/v1/report")
    void postReport(@Valid @RequestBody ReportRequest request);
}
