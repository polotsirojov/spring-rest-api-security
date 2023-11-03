package com.polot.gym.client.report;

import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.payload.response.ReportResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GymReportServiceFallback implements ReportServiceClient {
    @Override
    public List<ReportResponse> getAll() {
        return List.of();
    }

    @Override
    public void postReport(ReportRequest request) {
    }
}
