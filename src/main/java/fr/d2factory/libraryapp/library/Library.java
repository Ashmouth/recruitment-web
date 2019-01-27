package fr.d2factory.libraryapp.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.lang.Math.toIntExact;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;

public class Library implements LibraryService {
	
	private BookRepository bookRepository;

	public Library(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
    /**
     * A member is borrowing a book from our library.
     *
     * @param isbnCode the isbn code of the book
     * @param member the member who is borrowing the book
     * @param borrowedAt the date when the book was borrowed
     *
     * @return the book the member wishes to obtain if found
     * @throws HasLateBooksException in case the member has books that are late
     */
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
		List<Book> borrowedBooks = member.getBorrowedBooks();
		boolean canTake = true;
		for(Book borrowed : borrowedBooks) {
			LocalDate borrowedDate = bookRepository.findBorrowedBookDate(borrowed);
			long daysBetween = ChronoUnit.DAYS.between(borrowedDate, borrowedAt);
			int days = toIntExact(daysBetween);
			if (days > member.getDAYLIMITONBORROWING()) {
				canTake = false;
				throw new HasLateBooksException("Member : " + member.getName() +
						" is late on returning " + borrowed.getTitle());
			}
		}
		
		Book book = null;
		if(canTake) {
			book = bookRepository.removeBook(isbnCode);
			bookRepository.saveBookBorrow(book, borrowedAt);
			member.addBorrowedBook(book);
		}
		
		return book;
	}
	
	@Override
    /**
     * A member is borrowing a book from our library on current date.
     *
     * @param isbnCode the isbn code of the book
     * @param member the member who is borrowing the book
     *
     * @return the book the member wishes to obtain if found
     * @throws HasLateBooksException in case the member has books that are late
     */
	public Book borrowBook(long isbnCode, Member member) throws HasLateBooksException {
		LocalDate currentlocalDate = LocalDate.now();
		return borrowBook(isbnCode, member, currentlocalDate);
	}
	

	@Override
    /**
     * A member returns a book to the library.
     * We should calculate the tarif and probably charge the member
     *
     * @param book the {@link Book} they return
     * @param member the {@link Member} who is returning the book
     */
	public void returnBook(Book book, Member member) {
		LocalDate currentlocalDate = LocalDate.now();
		returnBook(book, member, currentlocalDate);
	}
	
	@Override
    /**
     * A member returns a book to the library on current date.
     * We should calculate the tarif and probably charge the member
     *
     * @param book the {@link Book} they return
     * @param member the {@link Member} who is returning the book
     * @param returnedDay the date when the book was returned
     */
	public void returnBook(Book book, Member member, LocalDate returnedDay) {
		// compute number of day and make the member pay
		LocalDate localDate = bookRepository.returnBorrowedBookDate(book);
		long daysBetween = ChronoUnit.DAYS.between(localDate, returnedDay);
		int days = toIntExact(daysBetween);
		
		member.payBook(days);
		member.removeBorrowedBook(book);
		
		bookRepository.addBook(book);
	}

}
