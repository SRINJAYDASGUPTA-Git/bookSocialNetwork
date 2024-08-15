package com.srinjay.book_network.book;

import com.srinjay.book_network.common.PageResponse;
import com.srinjay.book_network.exceptions.OperationNotPermittedException;
import com.srinjay.book_network.file.FileStorageService;
import com.srinjay.book_network.history.BookTransactionHistory;
import com.srinjay.book_network.history.BookTransactionHistoryRepository;
import com.srinjay.book_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.srinjay.book_network.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookTransactionHistoryRepository transactionHistoryRepository;
    private final FileStorageService fileStorageService;
    public Long saveBook(BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request, user);
        book.setOwner (user);

        return bookRepository.save(book).getId ();
    }

    public BookResponse findBookById(Long bookId) {
        return bookRepository.findById (bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow (()-> new EntityNotFoundException("No book found with id " + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by ("createdDate").descending ());
        Page<Book> books = bookRepository.findAllDisplayableBooks (user.getId (), pageable);

        // Print user ID to console before processing books
        System.out.println("User ID: " + user.getId());

        List<BookResponse> bookResponses = books.stream ()
                .map(bookMapper::toBookResponse).toList ();
        return new PageResponse<> (
                bookResponses,
                books.getNumber (),
                books.getSize (),
                books.getTotalElements (),
                books.getTotalPages (),
                books.isFirst (),
                books.isLast ()
        );
    }


    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by ("createdDate").descending ());
        Page<Book> books = bookRepository.findAll(withOwnerId (user.getId ()), pageable);
        List<BookResponse> bookResponses = books.stream ()
                .map(bookMapper::toBookResponse).toList ();
        return new PageResponse<> (
                bookResponses,
                books.getNumber (),
                books.getSize (),
                books.getTotalElements (),
                books.getTotalPages (),
                books.isFirst (),
                books.isLast ()
        );
    }


    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by ("createdDate").descending ());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks (user.getId (), pageable);
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream ()
                .map(bookMapper::toBorrowedBookResponse).toList ();
        return new PageResponse<> (
                bookResponses,
                allBorrowedBooks.getNumber (),
                allBorrowedBooks.getSize (),
                allBorrowedBooks.getTotalElements (),
                allBorrowedBooks.getTotalPages (),
                allBorrowedBooks.isFirst (),
                allBorrowedBooks.isLast ()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by ("createdDate").descending ());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllReturnedBooks (user.getId (), pageable);
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream ()
                .map(bookMapper::toBorrowedBookResponse).toList ();
        return new PageResponse<> (
                bookResponses,
                allBorrowedBooks.getNumber (),
                allBorrowedBooks.getSize (),
                allBorrowedBooks.getTotalElements (),
                allBorrowedBooks.getTotalPages (),
                allBorrowedBooks.isFirst (),
                allBorrowedBooks.isLast ()
        );
    }

    public Long updateShareableStatus(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById (bookId)
                .orElseThrow (()-> new EntityNotFoundException("No book found with id " + bookId));
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals (book.getOwner ().getId (), user.getId ())) {
           throw new OperationNotPermittedException ("You cannot Update Book's Shareable status");
        }
        book.setShareable (!book.isShareable ());
        bookRepository.save (book);
        return book.getId ();
    }

    public Long updateArchivedStatus(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById (bookId)
                .orElseThrow (()-> new EntityNotFoundException("No book found with id " + bookId));
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals (book.getOwner ().getId (), user.getId ())) {
            throw new OperationNotPermittedException ("You cannot Update Book's Archive status");
        }
        book.setArchived (!book.isArchived ());
        bookRepository.save (book);
        return book.getId ();
    }

    public Long borrowBook(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById (bookId)
                .orElseThrow (()-> new EntityNotFoundException("No book found with id " + bookId));
        if (!book.isShareable () || book.isArchived ()) {
            throw new OperationNotPermittedException ("This book cannot be borrowed as it is not shareable or archived");
        }

        User user = (User) connectedUser.getPrincipal();
        if (Objects.equals (book.getOwner ().getId (), user.getId ())) {
            throw new OperationNotPermittedException ("You cannot borrow your own book");
        }
        final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser (book.getId (), user.getId ());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException ("The requested book is already borrowed");
        }
        BookTransactionHistory history = BookTransactionHistory.builder ()
                .book (book)
                .user (user)
                .returned (false)
                .returnApproved (false)
                .build ();

        return transactionHistoryRepository.save (history).getId ();

    }

    public Long returnBorrowedBook(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById (bookId)
                .orElseThrow (()-> new EntityNotFoundException("No book found with id " + bookId));
        User user = (User) connectedUser.getPrincipal();

        if(book.isArchived () || !book.isShareable ()) {
            throw new OperationNotPermittedException ("This book cannot be returned as it is archived or not shareable");
        }

        if(Objects.equals (book.getOwner ().getId (), user.getId ())) {
            throw new OperationNotPermittedException ("You cannot return your own book");
        }
        BookTransactionHistory history = transactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId ())
                .orElseThrow (()-> new OperationNotPermittedException ("You have not borrowed this book"));
        history.setReturned (true);
        return transactionHistoryRepository.save (history).getId ();
    }

    public Long approveReturnBorrowedBook(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById (bookId)
                .orElseThrow (()-> new EntityNotFoundException("No book found with id " + bookId));
        User user = (User) connectedUser.getPrincipal();

        if(book.isArchived () || !book.isShareable ()) {
            throw new OperationNotPermittedException ("This book cannot be returned as it is archived or not shareable");
        }

        if(!Objects.equals (book.getOwner ().getId (), user.getId ())) {
            throw new OperationNotPermittedException ("You cannot return a book you do not own");
        }

        BookTransactionHistory history = transactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId ())
                .orElseThrow (()-> new OperationNotPermittedException ("The Book is not returned by the user"));
        history.setReturnApproved (true);
        return transactionHistoryRepository.save (history).getId ();
    }

    public void uploadBookCoverPicture(Long bookId, MultipartFile cover, Authentication connectedUser) {
        Book book = bookRepository.findById (bookId)
                .orElseThrow (()-> new EntityNotFoundException("No book found with id " + bookId));
        User user = (User) connectedUser.getPrincipal();
        var bookCover = fileStorageService.saveFile(cover, user.getId ());
        book.setBookCover (bookCover);
        bookRepository.save (book);
    }
}
