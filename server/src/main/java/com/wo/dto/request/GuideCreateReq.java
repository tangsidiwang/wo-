package com.wo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class GuideCreateReq {
    @NotBlank @Size(max = 200)
    private String title;
    private String coverUrl;
    private String summary;
    private Integer gameId;
    private List<Section> sections;

    @Data
    public static class Section {
        private String subtitle;
        private String content;
        private String imageUrl;
    }
}
