package fr.d2factory.libraryapp.book;

/**
 * A simple representation of a book
 */
public class Book {
    String title;
    String author;
    ISBN isbn;

    public Book(String title, String author, ISBN isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }
    
    public Book(String title, String author, Long isbn) {
        this.title = title;
        this.author = author;
        this.isbn = new ISBN(isbn);
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public ISBN getIsbn() {
        return isbn;
    }
    
}
