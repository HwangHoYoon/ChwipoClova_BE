package com.chwipoClova.article.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleListRes {

    @Schema(description = "id", example = "id", name = "id")
    private Long id;

    @Schema(description = "title", example = "title", name = "title")
    private String title;

    @Schema(description = "description", example = "description", name = "description")
    private String description;

    @Schema(description = "link", example = "link", name = "link")
    private String link;

    @Schema(description = "thumbnail", example = "thumbnail", name = "thumbnail")
    private String thumbnail;

    @Schema(description = "published", example = "published", name = "published")
    private String published;

    @Schema(description = "guid", example = "guid", name = "guid")
    private String guid;

    @Schema(description = "companyName", example = "companyName", name = "companyName")
    private String companyName;

    @Schema(description = "createdAt", example = "createdAt", name = "createdAt")
    private String createdAt;

}
