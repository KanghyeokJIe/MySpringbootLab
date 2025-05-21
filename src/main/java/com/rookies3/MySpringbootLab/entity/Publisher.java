package com.rookies3.MySpringbootLab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publishers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private LocalDate establishedDate;

    private String address;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Book> books = new ArrayList<>();

    // 양방향 관계를 위한 헬퍼 메서드
    public void addBook(Book book) {
        books.add(book);
        book.setPublisher(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setPublisher(null);
    }
}