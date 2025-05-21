package com.rookies3.MySpringbootLab.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private Integer price;

    private LocalDate publishDate;

    @OneToOne(mappedBy = "book",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private BookDetail bookDetail;

    // 🔽 새로 추가된 Publisher 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
}
