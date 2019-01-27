package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<Long, List<Book>> availableBooks = new HashMap<>();
    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    /**
     * Add all books from the list in the Map of availableBooks
     * @param books
     */
    public void addBooks(List<Book> books) {
        for (Book book : books) {
            addBook(book);
        }
    }
    
    /**
     * Add one book in the Map of availableBooks
     * @param books
     */
    public void addBook(Book book) {
        List<Book> lst = availableBooks.get(book.isbn.getIsbnCode());
        if(lst == null) {
            lst = new ArrayList<>();
        }
        lst.add(book);
        availableBooks.put(book.isbn.getIsbnCode(), lst);
    }
    
    /**
     * Remove one book from the Map of availableBooks
     * @param isbnCode of the book as a long
     */
    public Book removeBook(long isbnCode) {
        ISBN tmpISBN = new ISBN(isbnCode);
        return removeBook(tmpISBN);
    }
    
    /**
     * Remove one book from the Map of availableBooks
     * @param isbnCode of the book
     */
    public Book removeBook(ISBN isbnCode) {
        List<Book> lst = availableBooks.get(isbnCode.getIsbnCode());
//      displayAvailableBooks();
//      System.out.println("search : " + isbnCode.getIsbnCode());
        if(lst == null || lst.isEmpty()) {
            return null;
        }
        Book res = lst.remove(0);
        return res;
    }

    /**
     * Check if the books with isbnCode is available
     * @param isbnCode of the book
     * @return Books with isbnCode
     */
    public List<Book> findBook(long isbnCode) {
        return availableBooks.get(isbnCode);
    }

    /**
     * Check if the books with isbnCode is available
     * @param isbnCode of the book
     * @return Books with isbnCode
     */
    public void saveBookBorrow(Book book, LocalDate borrowedAt) {
        borrowedBooks.put(book, borrowedAt);
    }

    /**
     * Find the date of the borrowed book
     * @param book who have been borrowed
     * @return LocalDate when the book have been borrowed
     */
    public LocalDate findBorrowedBookDate(Book book) {
        return borrowedBooks.get(book);
    }
    
    /**
     * Remove from the list of borrowedBooks the book
     * @param book who will be remove
     * @return the date
     */
    public LocalDate returnBorrowedBookDate(Book book) {
        return borrowedBooks.remove(book);
    }
    

    public Map<Long, List<Book>> getAvailableBooks() {
        return availableBooks;
    }
    
    
    /**
     * Display all Available books in console
     */
    public void displayAvailableBooks() {
        for(Long key : availableBooks.keySet()) {
            for(Book book : availableBooks.get(key)) {
                System.out.println("book : " + book.getIsbn().getIsbnCode());
            }
        }
    }
}
