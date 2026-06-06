package com.wo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class PostCreateReq {
    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    private String content;

    private Integer categoryId;
    private List<String> tagNames;
    private String coverUrl;
    private List<String> imageUrls;
}
