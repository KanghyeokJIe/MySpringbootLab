package com.rookies3.MySpringbootLab.controller;

import com.rookies3.MySpringbootLab.controller.dto.BookDTO;
import com.rookies3.MySpringbootLab.controller.dto.PublisherDTO;
import com.rookies3.MySpringbootLab.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    // 전체 출판사 조회 (도서 수 포함)
    @GetMapping
    public List<PublisherDTO.SimpleResponse> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    // ID로 출판사 조회 (도서 포함)
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    // 이름으로 출판사 조회
    @GetMapping("/name/{name}")
    public ResponseEntity<PublisherDTO.Response> getPublisherByName(@PathVariable String name) {
        return ResponseEntity.ok(publisherService.getPublisherByName(name));
    }

    // 출판사 생성
    @PostMapping
    public ResponseEntity<PublisherDTO.Response> createPublisher(@RequestBody PublisherDTO.Request request) {
        return ResponseEntity.ok(publisherService.createPublisher(request));
    }

    // 출판사 수정
    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> updatePublisher(
            @PathVariable Long id,
            @RequestBody PublisherDTO.Request request) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, request));
    }

    // 출판사 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ 출판사 ID로 해당 출판사의 도서 목록 조회
    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO.Response>> getBooksByPublisher(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getBooksByPublisher(id));
    }
}
