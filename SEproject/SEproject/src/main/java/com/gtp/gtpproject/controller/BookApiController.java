package com.gtp.gtpproject.controller;

import com.gtp.gtpproject.controller.dto.BookResponse;
import com.gtp.gtpproject.model.Book;
import com.gtp.gtpproject.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookApiController {
    private final BookService bookService;

    public BookApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/search")
    public ResponseEntity<BookResponse> search(@RequestParam("query") String query) {
        Book book = bookService.searchFirstByQuery(query);
        BookResponse resp = new BookResponse(book.getTitle(), book.getAuthor(), book.getPrice(), book.getQuantity());
        return ResponseEntity.ok(resp);
    }
}

