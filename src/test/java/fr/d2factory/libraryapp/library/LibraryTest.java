package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class LibraryTest {
    private LibraryService library ;
    private BookRepository bookRepository;

    @Before
    public void setup(){
        // instantiate the library and the repository
        bookRepository = new BookRepository();
        library = new Library(bookRepository);

        // add some test books (use BookRepository#addBooks)    
        Book book1 = new Book("Call of Cthulhu", "Howard Phillips Lovecraft", 9788416080687L);
        Book book2 = new Book("The Jungle Book", "Rudyard Kipling", 9788379030651L);
        Book book3 = new Book("Poesies", "Arthur Rimbaud", 9780460879842L);
        ArrayList<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book1);
        books.add(book2);
        books.add(book3);
        bookRepository.addBooks(books);
        
        // to help you a file called books.json is available in src/test/resources      
        try {
            String content = readFile("src/test/resources/books.json", StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(content);
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                String title = jsonObject.getString("title");
                String author = jsonObject.getString("author");
                Long isbn = jsonObject.getJSONObject("isbn").getLong("isbnCode");
                Book book = new Book(title, author, isbn);
                bookRepository.addBook(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    

    @Test
    public void member_can_borrow_a_book_if_book_is_available(){
        Member JohnDoe = new Resident("John Doe", 10.0f);
        Book book = library.borrowBook(46578964513L, JohnDoe);
        
        if (book == null) {
            fail("Can't retrieve book");
        }
    }

    @Test
    public void borrowed_book_is_no_longer_available(){
        Member johnDoe = new Resident("John Doe", 10.0f);
        Book book = library.borrowBook(46578964513L, johnDoe);
        if (book == null) {
            fail("Can't retrieve book");
        }
        
        Member harryPotter = new Student("Harry Potter", 5.0f, true);
        book = library.borrowBook(46578964513L, harryPotter);
        if (book != null) {
            fail("Book hasn't been removed from map");
        }
    }
    
    @Test
    public void borrowed_two_exemple_of_a_book() {
        Member johnDoe = new Resident("John Doe", 10.0f);
        Book book = library.borrowBook(9788416080687L, johnDoe);
        if (book == null) {
            fail("Can't retrieve book");
        }
        
        Member harryPotter = new Student("Harry Potter", 5.0f, true);
        book = library.borrowBook(9788416080687L, harryPotter);
        if (book == null) {
            fail("Can't retrieve book");
        }
    }

    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
        LocalDate borrowedDay = LocalDate.of(2019, Month.JANUARY, 1);
        LocalDate returnedDay = LocalDate.of(2019, Month.JANUARY, 31);
        
        float initWallet = 50.0f;
        Member johnDoe = new Resident("John Doe", 50.0f);
        Book book = library.borrowBook(46578964513L, johnDoe, borrowedDay);
        if (book == null) {
            fail("Can't retrieve book");
        }
        library.returnBook(book, johnDoe, returnedDay);
        
        float theoricRes = (initWallet - (0.10f * 30));
        if(johnDoe.getWallet() != theoricRes) {
            fail("Wallet not suppose to be : " + johnDoe.getWallet() + 
                    " It suppose to be : " + theoricRes);
        }
    }

    @Test
    public void students_pay_10_cents_the_first_30days(){
        LocalDate borrowedDay = LocalDate.of(2019, Month.JANUARY, 1);
        LocalDate returnedDay = LocalDate.of(2019, Month.JANUARY, 31);
        
        float initWallet = 5.0f;
        Member harryPotter = new Student("Harry Potter", initWallet, false);
        Book book = library.borrowBook(46578964513L, harryPotter, borrowedDay);
        if (book == null) {
            fail("Can't retrieve book");
        }
        library.returnBook(book, harryPotter, returnedDay);
        
        float theoricRes = (initWallet - (0.10f * 30));
        if(harryPotter.getWallet() != theoricRes) {
            fail("Wallet not suppose to be : " + harryPotter.getWallet() + 
                    " It suppose to be : " + theoricRes);
        }
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){
        LocalDate borrowedDay = LocalDate.of(2019, Month.JANUARY, 1);
        LocalDate returnedDay = LocalDate.of(2019, Month.JANUARY, 31);
        
        float initWallet = 5.0f;
        Member harryPotter = new Student("Harry Potter", initWallet, true);
        Book book = library.borrowBook(46578964513L, harryPotter, borrowedDay);
        if (book == null) {
            fail("Can't retrieve book");
        }
        library.returnBook(book, harryPotter, returnedDay);
        
        float theoricRes = (initWallet - (0.10f * 15));
        if(harryPotter.getWallet() != theoricRes) {
            fail("Wallet not suppose to be : " + harryPotter.getWallet() + 
                    " It suppose to be : " + theoricRes);

        }
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
        LocalDate borrowedDay = LocalDate.of(2019, Month.JANUARY, 1);
        LocalDate returnedDay = LocalDate.of(2019, Month.FEBRUARY, 14);
        
        float initWallet = 10.0f;
        Member harryPotter = new Student("Harry Potter", initWallet, false);
        Book book = library.borrowBook(46578964513L, harryPotter, borrowedDay);
        if (book == null) {
            fail("Can't retrieve book");
        }
        library.returnBook(book, harryPotter, returnedDay);
        
        float theoricRes = (initWallet - ((0.10f * 30) + (0.15f * 14)));
        if(harryPotter.getWallet() != theoricRes) {
            fail("Wallet not suppose to be : " + harryPotter.getWallet() + 
                    " It suppose to be : " + theoricRes);
        }
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
        LocalDate borrowedDay = LocalDate.of(2019, Month.MARCH, 1);
        LocalDate returnedDay = LocalDate.of(2019, Month.JULY, 1);
        
        float initWallet = 50.0f;
        Member johnDoe = new Resident("John Doe", 50.0f);
        Book book = library.borrowBook(46578964513L, johnDoe, borrowedDay);
        if (book == null) {
            fail("Can't retrieve book");
        }
        library.returnBook(book, johnDoe, returnedDay);
        
        float theoricRes = (initWallet - ((0.10f * 60) + (0.20f * 62)));
        if(johnDoe.getWallet() != theoricRes) {
            fail("Wallet not suppose to be : " + johnDoe.getWallet() + 
                    " It suppose to be : " + theoricRes);
        }
    }

    @Test
    public void members_cannot_borrow_book_if_they_have_late_books(){
        LocalDate borrowedDay1 = LocalDate.of(2018, Month.JANUARY, 1);
        Member JohnDoe = new Resident("John Doe", 50.0f);
        Book book = library.borrowBook(46578964513L, JohnDoe, borrowedDay1);
        if (book == null) {
            fail("Can't retrieve book");
        }

        LocalDate borrowedDay2 = LocalDate.of(2019, Month.FEBRUARY, 14);
        book = null;
        try {
            book = library.borrowBook(3326456467846L, JohnDoe, borrowedDay2);
        } catch (HasLateBooksException hasLateBooksException) {}
        if (book != null) {
            fail("Shouldn't be able to retrieve book");
        }
    }
}
