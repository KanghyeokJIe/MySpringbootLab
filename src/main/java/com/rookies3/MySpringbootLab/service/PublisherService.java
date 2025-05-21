package com.rookies3.MySpringbootLab.service;

import com.rookies3.MySpringbootLab.controller.dto.BookDTO;
import com.rookies3.MySpringbootLab.controller.dto.PublisherDTO;
import com.rookies3.MySpringbootLab.entity.Book;
import com.rookies3.MySpringbootLab.entity.Publisher;
import com.rookies3.MySpringbootLab.exception.BusinessException;
import com.rookies3.MySpringbootLab.exception.ErrorCode;
import com.rookies3.MySpringbootLab.repository.BookRepository;
import com.rookies3.MySpringbootLab.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    /**
     * 모든 출판사를 조회하며, 각 출판사의 도서 수를 포함합니다.
     */
    public List<PublisherDTO.SimpleResponse> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(p -> PublisherDTO.SimpleResponse.fromEntityWithCount(
                        p, bookRepository.countByPublisherId(p.getId()))
                )
                .collect(Collectors.toList());
    }

    /**
     * ID로 특정 출판사를 조회하며, 해당 출판사의 모든 도서 정보를 포함합니다.
     */
    public PublisherDTO.Response getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PUBLISHER_NOT_FOUND));

        return PublisherDTO.Response.fromEntity(publisher);
    }

    /**
     * 이름으로 특정 출판사를 조회합니다.
     */
    public PublisherDTO.Response getPublisherByName(String name) {
        Publisher publisher = publisherRepository.findByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.PUBLISHER_NOT_FOUND));

        return PublisherDTO.Response.fromEntity(publisher);
    }

    /**
     * 특정 출판사의 도서 목록을 조회합니다.
     */
    public List<BookDTO.Response> getBooksByPublisher(Long publisherId) {
        // 출판사 존재 확인
        if (!publisherRepository.existsById(publisherId)) {
            throw new BusinessException(ErrorCode.PUBLISHER_NOT_FOUND);
        }

        return bookRepository.findByPublisherId(publisherId).stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 새로운 출판사를 생성합니다. 이름 중복을 검증합니다.
     */
    @Transactional
    public PublisherDTO.Response createPublisher(PublisherDTO.Request request) {
        if (publisherRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.PUBLISHER_NAME_DUPLICATE);
        }

        Publisher newPublisher = Publisher.builder()
                .name(request.getName())
                .establishedDate(request.getEstablishedDate())
                .address(request.getAddress())
                .build();

        Publisher saved = publisherRepository.save(newPublisher);
        return PublisherDTO.Response.fromEntity(saved);
    }

    /**
     * 기존 출판사 정보를 수정합니다. 이름 중복(자신 제외)을 검증합니다.
     */
    @Transactional
    public PublisherDTO.Response updatePublisher(Long id, PublisherDTO.Request request) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PUBLISHER_NOT_FOUND));

        boolean isNameChanged = !publisher.getName().equals(request.getName());
        boolean isNameDuplicated = publisherRepository.existsByName(request.getName());

        if (isNameChanged && isNameDuplicated) {
            throw new BusinessException(ErrorCode.PUBLISHER_NAME_DUPLICATE);
        }

        publisher.setName(request.getName());
        publisher.setEstablishedDate(request.getEstablishedDate());
        publisher.setAddress(request.getAddress());

        return PublisherDTO.Response.fromEntity(publisher);
    }

    /**
     * 출판사를 삭제합니다. 해당 출판사에 도서가 있는 경우 삭제를 거부합니다.
     */
    @Transactional
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PUBLISHER_NOT_FOUND));

        long bookCount = bookRepository.countByPublisherId(id);
        if (bookCount > 0) {
            throw new BusinessException(ErrorCode.PUBLISHER_HAS_BOOKS);
        }

        publisherRepository.delete(publisher);
    }
}
