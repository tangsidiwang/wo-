package com.wo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentCreateReq {
    @NotBlank
    @Size(max = 500)
    private String content;

    private Long parentId;
    private Long replyToUid;
}
