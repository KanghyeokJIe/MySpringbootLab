package com.rookies3.MySpringbootLab.controller;

import com.rookies3.MySpringbootLab.controller.dto.BookDTO;
import com.rookies3.MySpringbootLab.controller.dto.BookDetailPatchRequest;
import com.rookies3.MySpringbootLab.controller.dto.PatchRequest;
import com.rookies3.MySpringbootLab.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAllBooks() {
        List<BookDTO.Response> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id) {
        BookDTO.Response book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getBookByIsbn(@PathVariable String isbn) {
        BookDTO.Response book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO.Response>> getBooksByAuthor(@RequestParam String author) {
        List<BookDTO.Response> books = bookService.getBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO.Response>> getBooksByTitle(@RequestParam String title) {
        List<BookDTO.Response> books = bookService.getBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(@Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response createdBook = bookService.createBook(request);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response updatedBook = bookService.updateBook(id, request);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO.Response> patchBook(@PathVariable Long id,
                                                      @RequestBody PatchRequest request) {
        BookDTO.Response response = bookService.patchBook(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/detail")
    public ResponseEntity<BookDTO.Response> patchBookDetail(@PathVariable Long id,
                                                            @RequestBody BookDetailPatchRequest request) {
        // BookDetail만 따로 수정할 수 있도록 PatchRequest에 감싸서 호출
        PatchRequest patchRequest = new PatchRequest();
        patchRequest.setDetailRequest(request);

        BookDTO.Response response = bookService.patchBook(id, patchRequest);
        return ResponseEntity.ok(response);
    }

}