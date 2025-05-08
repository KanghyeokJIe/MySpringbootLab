package com.rookies3.MySpringbootLab.controller;

import com.rookies3.MySpringbootLab.entity.Book;
import com.rookies3.MySpringbootLab.exception.BusinessException;
import com.rookies3.MySpringbootLab.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    /**
     * 새 도서 등록
     * POST /api/books
     * 요청 body에 담긴 Book 데이터를 저장하고, 저장된 Book을 응답으로 반환
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookRepository.save(book));
    }

    /**
     * 전체 도서 목록 조회
     * GET /api/books
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * ID로 도서 조회
     * GET /api/books/{id}
     * - 존재하지 않으면 404 Not Found 응답
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());  // 404 처리
    }

    /**
     * SBN으로 도서 조회
     * GET /api/books/isbn/{isbn}
     * - 존재하지 않으면 BusinessException 예외 발생 → 404 응답 처리됨
     */
    @GetMapping("/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("ISBN에 해당하는 도서를 찾을 수 없습니다."));
    }

    /**
     * 저자명으로 도서 목록 조회
     * GET /api/books/author/{author}
     */
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }

    /**
     * 도서 정보 수정
     * PUT /api/books/{id}
     * - 해당 ID의 도서를 찾아 요청 body의 내용으로 업데이트
     * - 존재하지 않으면 404 반환
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setIsbn(updatedBook.getIsbn());
                    book.setPrice(updatedBook.getPrice());
                    book.setPublishDate(updatedBook.getPublishDate());
                    return ResponseEntity.ok(bookRepository.save(book));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 도서 삭제
     * DELETE /api/books/{id}
     * - 존재하지 않으면 404 반환
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.<Void>noContent().build();  // 삭제 성공: 204 No Content
                })
                .orElse(ResponseEntity.notFound().build());  // 삭제 대상 없음: 404
    }
}
