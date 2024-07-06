package com.chwipoClova.article.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedCategoryRes {

    @Schema(description = "CategoryID", example = "CD1", name = "categoryId")
    private String categoryId;

    @Schema(description = "categoryName", example = "개발", name = "categoryName")
    private String categoryName;

}
