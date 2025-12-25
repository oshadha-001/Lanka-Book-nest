package com.gtp.gtpproject.service;

import com.gtp.gtpproject.model.Book;
import com.gtp.gtpproject.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book searchFirstByQuery(String query) {
        String sanitized = sanitizeQuery(query);
        List<Book> results = bookRepository.searchBooks(sanitized);
        if (results.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        return results.get(0);
    }

    private String sanitizeQuery(String input) {
        if (input == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Query is required");
        }
        String trimmed = input.trim();
        if (trimmed.length() > 255) {
            trimmed = trimmed.substring(0, 255);
        }
        return trimmed;
    }
}

