package fr.d2factory.libraryapp.member;

import java.util.ArrayList;
import java.util.List;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.library.LibraryService;

/**
 * A member is a person who can borrow and return books to a {@link LibraryService}
 * A member can be either a student or a resident
 */
public abstract class Member {
    /**
     * An initial sum of money the member has
     */
    protected String name;
    protected float wallet;
    protected List<Book> borrowedBooks;
    
    public Member(String name, float wallet) {
        this.wallet = wallet;
        borrowedBooks = new ArrayList<>();
    }

    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays);
    
    public abstract int getDAYLIMITONBORROWING();

    public String getName() {
        return name;
    }

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }
    
    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    
    public void addBorrowedBook(Book book) {
        borrowedBooks.add(book);
    }
    
    public boolean removeBorrowedBook(Book book) {
        return borrowedBooks.remove(book);
    }
}
