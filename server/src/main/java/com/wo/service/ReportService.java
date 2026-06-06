package com.wo.service;

import com.wo.dto.request.ReportReq;

public interface ReportService {
    void submit(Long userId, ReportReq req);
}
