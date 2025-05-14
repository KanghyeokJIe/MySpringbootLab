package com.rookies3.MySpringbootLab.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDetailPatchRequest {
    private String description;
    private String language;
    private Integer pageCount;
    private String publisher;
    private String coverImageUrl;
    private String edition;
}
