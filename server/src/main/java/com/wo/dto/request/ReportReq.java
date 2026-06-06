package com.wo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReportReq {
    @NotBlank
    private String targetType;
    private Long targetId;
    @NotBlank
    private String reasonType;
    @Size(max = 500)
    private String description;
}
