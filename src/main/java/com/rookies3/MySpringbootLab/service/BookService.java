package com.rookies3.MySpringbootLab.service;

import com.rookies3.MySpringbootLab.controller.dto.BookDTO;
import com.rookies3.MySpringbootLab.controller.dto.BookDTO.BookDetailDTO;
import com.rookies3.MySpringbootLab.entity.Book;
import com.rookies3.MySpringbootLab.entity.BookDetail;
import com.rookies3.MySpringbootLab.exception.BusinessException;
import com.rookies3.MySpringbootLab.exception.ErrorCode;
import com.rookies3.MySpringbootLab.repository.BookDetailRepository;
import com.rookies3.MySpringbootLab.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "ISBN", isbn));
        return BookDTO.Response.fromEntity(book);
    }

    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        BookDetailDTO detailDto = request.getDetailRequest();
        if (detailDto != null) {
            BookDetail bookDetail = BookDetail.builder()
                    .description(detailDto.getDescription())
                    .language(detailDto.getLanguage())
                    .pageCount(detailDto.getPageCount())
                    .publisher(detailDto.getPublisher())
                    .coverImageUrl(detailDto.getCoverImageUrl())
                    .edition(detailDto.getEdition())
                    .book(book)
                    .build();
            book.setBookDetail(bookDetail);
        }

        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));

        if (!book.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        BookDetailDTO detailDto = request.getDetailRequest();
        if (detailDto != null) {
            BookDetail bookDetail = book.getBookDetail();
            if (bookDetail == null) {
                bookDetail = new BookDetail();
                bookDetail.setBook(book);
                book.setBookDetail(bookDetail);
            }

            bookDetail.setDescription(detailDto.getDescription());
            bookDetail.setLanguage(detailDto.getLanguage());
            bookDetail.setPageCount(detailDto.getPageCount());
            bookDetail.setPublisher(detailDto.getPublisher());
            bookDetail.setCoverImageUrl(detailDto.getCoverImageUrl());
            bookDetail.setEdition(detailDto.getEdition());
        }

        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id);
        }
        bookRepository.deleteById(id);
    }
}
