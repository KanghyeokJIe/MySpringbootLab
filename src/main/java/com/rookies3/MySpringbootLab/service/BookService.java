package com.rookies3.MySpringbootLab.service;

import com.rookies3.MySpringbootLab.controller.dto.BookDTO;
import com.rookies3.MySpringbootLab.entity.Book;
import com.rookies3.MySpringbootLab.exception.BusinessException;
import com.rookies3.MySpringbootLab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.BookResponse::from)
                .collect(Collectors.toList());
    }

    public BookDTO.BookResponse getBookById(Long id) {
        return bookRepository.findById(id)
                .map(BookDTO.BookResponse::from)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }

    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(BookDTO.BookResponse::from)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }

    public List<BookDTO.BookResponse> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author).stream()
                .map(BookDTO.BookResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        Book saved = bookRepository.save(request.toEntity());
        return BookDTO.BookResponse.from(saved);
    }

    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));

        book.setPrice(request.getPrice()); // 예: 가격만 수정
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublishDate(request.getPublishDate());

        return BookDTO.BookResponse.from(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("Book Not Found", HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }
}
