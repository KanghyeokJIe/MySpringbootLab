package com.rookies3.MySpringbootLab.controller.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class PatchRequest {
    private String title;
    private String author;
    private String isbn;
    private Integer price;
    private LocalDate publishDate;

    private BookDetailPatchRequest detailRequest;
}
