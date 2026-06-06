package com.wo.service.impl;

import com.wo.common.exception.BusinessException;
import com.wo.dto.request.ReportReq;
import com.wo.entity.Report;
import com.wo.mapper.ReportMapper;
import com.wo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    @Override
    public void submit(Long userId, ReportReq req) {
        // 检查5分钟内是否已有相同举报
        Report report = new Report();
        report.setReporterId(userId);
        report.setTargetType(req.getTargetType());
        report.setTargetId(req.getTargetId());
        report.setReasonType(req.getReasonType());
        report.setDescription(req.getDescription() != null ? req.getDescription() : "");
        report.setStatus(1);
        reportMapper.insert(report);
    }
}
