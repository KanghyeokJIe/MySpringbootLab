package com.rookies3.MySpringbootLab.repository;

import com.rookies3.MySpringbootLab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * ISBN으로 도서를 단순 조회 (기본)
     */
    Optional<Book> findByIsbn(String isbn); // ✅ 이거 추가!

    /**
     * 작가 이름으로 대소문자 구분 없이 검색
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);

    /**
     * 제목으로 대소문자 구분 없이 검색
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * ID로 조회하면서 BookDetail 즉시 로딩
     */
    @Query("SELECT b FROM Book b JOIN FETCH b.bookDetail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(@Param("id") Long id);

    /**
     * ISBN으로 조회하면서 BookDetail 즉시 로딩
     */
    @Query("SELECT b FROM Book b JOIN FETCH b.bookDetail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(@Param("isbn") String isbn);

    /**
     * ISBN 중복 확인
     */
    boolean existsByIsbn(String isbn);

    /**
     * 특정 출판사의 모든 도서 조회
     */
    List<Book> findByPublisherId(Long publisherId);

    /**
     * 특정 출판사의 도서 수 카운트
     */
    Long countByPublisherId(@Param("publisherId") Long publisherId);

    /**
     * BookDetail과 Publisher를 모두 즉시 로딩 (도서 상세조회용)
     */
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookDetail LEFT JOIN FETCH b.publisher WHERE b.id = :id")
    Optional<Book> findByIdWithAllDetails(@Param("id") Long id);
}
