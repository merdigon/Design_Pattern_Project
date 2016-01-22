//package com.logic.SubLogic;
//
//
//import com.LibraryConfiguration.LibraryConfiguration;
//import com.dao.*;
//import com.logic.LogicFactory;
//import com.models.*;
//import org.joda.time.Days;
//import org.joda.time.LocalDate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Szymon on 2015-10-13.
// */
//@Component
//public class BookIOSubLogic extends DataPullSubLogic {
//
//    @Autowired
//    ConditionDAO conditionDAO;
//
//    @Autowired
//    TypeOfBookDAO typeOfBookDAO;
//
//    @Autowired
//    SectionDAO sectionDAO;
//
//    @Autowired
//    UserModelDAO userModelDAO;
//
//    @Autowired
//    DateDAO dateDAO;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(BookIOSubLogic.class);
//
//    public BookIOSubLogic(LogicFactory lgF) {
//        super(lgF);
//    }
//
//
//
//
//
//    public String saveBook(String authorData, String title, String conditionData, String uuidSection, String uuidType, String bornYear) {
//
//
//        int year;
//        try {
//            year = Integer.parseInt(bornYear);
//        }catch(NumberFormatException nfe){
//            return "Failure: year of book is not a numeric type";
//        }
//
//        String[] authorsString = authorData.split((";"));
//        List<Author> authors = new ArrayList<Author>();
//        Condition condition = conditionDAO.saveIfNotInDB(new Condition(Conditions.valueOf(conditionData)));
//        for (String authorString : authorsString) {
//            if (authorString.split(" ").length != 3)
//                return "Failure: bad author";
//            try {
//                Author author = new Author(authorString.split(" ")[0], authorString.split(" ")[1], Integer.parseInt(authorString.split(" ")[2]));
//                authors.add(authorDAO.saveIfNotInDB(author));
//            } catch (NumberFormatException nfe) {
//                return "Failure: year of author in not a numeric type";
//            }
//        }
//
//        Book book = new Book(authors, title, year, condition, typeOfBookDAO.get(uuidType), sectionDAO.get(uuidSection));
//        bookDAO.save(book);
//        return "Success";
//    }
//
//    public String borrowBook(String userUuid, String bookUuid){
//        UserModel user = userModelDAO.get(userUuid);
//        Book book = bookDAO.get(bookUuid);
//
//        if (user.getBooks().size() + 1 >= LibraryConfiguration.getMaxBorrowedBooks())
//            return "Failure: you borrowed too many books";
//
//        if (!book.getCondition().equals(Conditions.valueOf("Available")))
//            return "Failure: book is not available";
//
//
//        LocalDate planningReturnDate = new LocalDate().plusDays(LibraryConfiguration.getBorrowedDays());
//
//
//        BookDate date = new BookDate();
//        date.setBorrowedDate(new LocalDate());
//        date.setPlanningReturnDate(planningReturnDate);
//        date.setLogin(user.getLogin());
//
//        Condition condition = new Condition(Conditions.valueOf("Borrowed"));
//
//        book.setCondition(conditionDAO.saveIfNotInDB(condition));
//        book.addDate(dateDAO.saveIfNotInDB(date));
//        bookDAO.update(book);
//        userModelDAO.addBook(user, book);
//        return "success";
//    }
//
//    public String returnBook(String userUuid, String bookUuid){
//        Book book = bookDAO.get(bookUuid);
//        UserModel user = userModelDAO.get(userUuid);
//
//        if (!user.getBooks().contains(book))
//            return "Failure: user doesn't borrowed this book";
//
//        BookDate date = bookDAO.getLastDate(book);
//
//        double debt = 0;
//        if (date.getPlanningReturnDate().isBefore(new LocalDate())) {
//            debt = Days.daysBetween(date.getPlanningReturnDate(), new LocalDate()).getDays() * LibraryConfiguration.getInterests();
//        }
//
//        user.addDebt(debt);
//        user.removeBook(book);
//
//        date.setReturnDate(new LocalDate());
//        dateDAO.updateDate(date);
//
//        Condition condition = new Condition(Conditions.valueOf("Available"));
//        condition = conditionDAO.saveIfNotInDB(condition);
//        book.setCondition(condition);
//        bookDAO.update(book);
//        userModelDAO.update(user);
//        return "success";
//    }
//
//    public String editBook(String authorData, String uuid, String title, String conditionData, String uuidSection, String uuidType, int year){
//        Book book = bookDAO.get(uuid);
//
//        String[] authorsString = authorData.split((","));
//        List<Author> authors = new ArrayList<Author>();
//        for (String authorString : authorsString) {
//            Author author = new Author(authorString.split(" ")[0], authorString.split(" ")[1], Integer.parseInt(authorString.split(" ")[2]));
//            authors.add(authorDAO.saveIfNotInDB(author));
//        }
//        book.setAuthors(authors);
//        Condition condition = conditionDAO.saveIfNotInDB(new Condition(Conditions.valueOf(conditionData)));
//        book.setCondition(condition);
//        book.setTitle(title);
//        book.setSection(sectionDAO.get(uuidSection));
//        book.setTypeOfBook(typeOfBookDAO.get(uuidType));
//        book.setYear(year);
//        bookDAO.update(book);
//        return "success";
//    }
//
//    public String reserveBook(String userUuid, String bookUuid){
//        UserModel user;
//        Book book = bookDAO.get(bookUuid);
//
//        if (userUuid == "") {
//            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            user = userModelDAO.getByLogin(userDetails.getUsername());
//        } else
//            user = userModelDAO.get(userUuid);
//
//        if (book == null)
//            return "Failure: there is no such book";
//        if (user == null)
//            return "Failure: there is no such user";
//
//        if (user.getReservedBooks().size() + 1 >= LibraryConfiguration.getMaxReservedBooks())
//            return "Failure: you reserved too many books";
//
//        if (!book.getCondition().equals(Conditions.valueOf("Available")))
//            return "Failure: book is not available";
//
//        Condition condition = new Condition(Conditions.valueOf("Reserved"));
//        book.setCondition(conditionDAO.saveIfNotInDB(condition));
//        bookDAO.update(book);
//        userModelDAO.addReservedBook(user, book);
//        return "success";
//    }
//
//    public String cancelReserveBook(String userUuid, String bookUuid){
//        UserModel user;
//        if (userUuid == "") {
//            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            user = userModelDAO.getByLogin(userDetails.getUsername());
//        } else
//            user = userModelDAO.get(userUuid);
//
//        Book book = bookDAO.get(bookUuid);
//        if (book == null)
//            return "Failure: there is no such book";
//        if (user == null)
//            return "Failure: there is no such user";
//
//        if (!book.getCondition().equals(Conditions.valueOf("Reserved")))
//            return "Failure: book is not reserved";
//
//        Condition condition = new Condition(Conditions.valueOf("Available"));
//        book.setCondition(conditionDAO.saveIfNotInDB(condition));
//        bookDAO.update(book);
//        userModelDAO.removeReservedBook(user, book);
//
//        return "success";
//    }
//
//}
