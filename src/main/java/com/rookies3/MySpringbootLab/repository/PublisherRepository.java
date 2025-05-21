package com.rookies3.MySpringbootLab.repository;

import com.rookies3.MySpringbootLab.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    /**
     * 출판사 이름으로 조회
     */
    Optional<Publisher> findByName(String name);

    /**
     * 출판사 ID로 조회하면서 해당 출판사의 책들을 즉시 로딩 (Fetch Join)
     */
    @Query("SELECT p FROM Publisher p LEFT JOIN FETCH p.books WHERE p.id = :id")
    Optional<Publisher> findByIdWithBooks(@Param("id") Long id);

    /**
     * 출판사 이름 중복 확인
     */
    boolean existsByName(String name);
}
