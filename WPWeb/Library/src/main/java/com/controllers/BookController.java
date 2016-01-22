package com.controllers;

import com.LibraryConfiguration.LibraryConfiguration;
import com.configuration.Mail;
import com.models.*;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;


@Controller
public class BookController extends BaseController {

    @Autowired
    Mail mail;

    @RequestMapping(value = {"/admin/addBook"}, method = RequestMethod.GET)
    public String addBook(Model model) {
        model.addAttribute("sections", sectionDAO.getAll());
        model.addAttribute("typesOfBooks", typeOfBookDAO.getAll());
        return "addBook";
    }

    @RequestMapping(value = {"/admin/getBookProperties"}, method = RequestMethod.GET, headers = "Accept=application/json" )
    @ResponseBody
    public String getBookProperties() {
        return "{" +
                "\"sections\":" +sectionDAO.getAll() +
                ", \"types\":" + typeOfBookDAO.getAll() +
                '}';
    }

    @RequestMapping(value = "/admin/saveBook", method = RequestMethod.POST)
    @ResponseBody
    public String saveBook(@RequestBody Book book) {

        book.setAuthors(authorDAO.saveIfNotInDB(book.getAuthors()));
        book.setCondition(conditionDAO.saveIfNotInDB(book.getCondition()));
        book.setIsInventoried(false);
        bookDAO.save(book);
        return "Success";
    }

    @RequestMapping(value = {"/showBooks"}, method = RequestMethod.GET)
    public String showBook(Model model) {

        model.addAttribute("books", bookDAO.getAll());
        model.addAttribute("allBooksSize", bookDAO.getAll().size());
        return "showBooks";
    }

    @RequestMapping(value = {"/showBooks", "/user/showBooks", "/admin/showBooks"}, method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Book> showBooks(HttpServletRequest request,Model model) {
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int numberOfBookOnPage = Integer.parseInt(request.getParameter("numberOfBookOnPage"));
        return bookDAO.getOnPage(currentPage,numberOfBookOnPage);
    }

    @RequestMapping(value = {"/searchBooks"}, method = RequestMethod.GET)
    public String searchBook(Model model) {
        model.addAttribute("conditions", conditionDAO.getAll());
        return "searchBooks";
    }

    @RequestMapping(value = "/searchBooks", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public Collection<Book> searchBookAjax(@RequestBody SearchBook searchBook) {

        if (searchBook.getSearchType().equals("author")) {
            return new LinkedHashSet<>(bookDAO.getAllBySimilarAuthor(searchBook.getBook().getAuthors().get(0)));
        } else if (searchBook.getSearchType().equals("title")) {
            return bookDAO.findByColumn("title", searchBook.getBook().getTitle());
        } else if (searchBook.getSearchType().equals("year")) {
            return bookDAO.getAllByYear(Integer.toString(searchBook.getBook().getYear()));
        }
        List<Book> books = bookDAO.getAllBySimilarAuthor(searchBook.getBook().getAuthors().get(0));
        books.retainAll(bookDAO.findByColumn("title", searchBook.getBook().getTitle()));
        books.retainAll(bookDAO.getAllByYear(Integer.toString(searchBook.getBook().getYear())));
        return new LinkedHashSet<>(books);
    }




    @RequestMapping(value = "/admin/borrowBook", method = RequestMethod.POST)
    @ResponseBody
    public String borrowBook(@RequestParam("userUuid") String userUuid,
                             @RequestParam("bookUuid") String bookUuid) {

        UserModel user = userModelDAO.get(userUuid);
        Book book = bookDAO.get(bookUuid);

        if (user.getBooks().size() >= LibraryConfiguration.getInstance().getMaxBorrowedBooks()) return "Failure: you borrowed too many books";
        if (!book.getCondition().equals(Conditions.valueOf("Available"))) return "Failure: book is not available";

        LocalDate planningReturnDate = new LocalDate().plusDays(LibraryConfiguration.getInstance().getBorrowedDays());
        BookDate date = new BookDate();
        date.setBorrowedDate(new LocalDate());
        date.setPlanningReturnDate(planningReturnDate);
        date.setLogin(user.getLogin());
        Condition condition = new Condition(Conditions.valueOf("Borrowed"));
        book.setCondition(conditionDAO.saveIfNotInDB(condition));
        book.addDate(dateDAO.saveIfNotInDB(date));
        bookDAO.update(book);
        userModelDAO.addBook(user, book);
        return "success";
    }

    @RequestMapping(value = "/admin/returnBook", method = RequestMethod.POST)
    @ResponseBody
    public String returnBook(@RequestParam("uuidUser") String userUuid,
                             @RequestParam("uuidBook") String bookUuid) {

        Book book = bookDAO.get(bookUuid);
        UserModel user = userModelDAO.get(userUuid);

        if (!user.getBooks().contains(book)) return "Failure: user doesn't borrowed this book";

        BookDate date = bookDAO.getLastDate(book);
        double debt = 0;
        if (date.getPlanningReturnDate().isBefore(new LocalDate())) {
            debt = Days.daysBetween(date.getPlanningReturnDate(), new LocalDate()).getDays() * LibraryConfiguration.getInstance().getInterests();
        }

        user.addDebt(debt);
        user.removeBook(book);
        date.setReturnDate(new LocalDate());
        dateDAO.updateDate(date);
        Condition condition = new Condition(Conditions.valueOf("Available"));
        condition = conditionDAO.saveIfNotInDB(condition);
        book.setCondition(condition);
        bookDAO.update(book);
        userModelDAO.update(user);
        return "success";
    }

    @RequestMapping(value = "/admin/editBook/{uuid}", method = RequestMethod.GET)
    public String editBook(@PathVariable("uuid") String uuid, Model model) {

        model.addAttribute("book", bookDAO.get(uuid));
        model.addAttribute("conditions", conditionDAO.getAll());
        model.addAttribute("types", typeOfBookDAO.getAll());
        model.addAttribute("sections", sectionDAO.getAll());
        return "editBook";

    }

    @RequestMapping(value = "/admin/editBook", method = RequestMethod.POST)
    @ResponseBody
    public String editBook(@RequestBody Book book) {

        book.setAuthors(authorDAO.saveIfNotInDB(book.getAuthors()));
        book.setCondition(conditionDAO.saveIfNotInDB(book.getCondition()));
        book.setTypeOfBook(typeOfBookDAO.saveIfNotInDB(typeOfBookDAO.get(book.getTypeOfBook().getUuid())));
        book.setSection(sectionDAO.saveIfNotInDB(sectionDAO.get(book.getSection().getUuid())));
        book.setDates(bookDAO.get(book.getUuid()).getDates());
        bookDAO.update(book);
        return "success";
    }

    @RequestMapping(value = "/reserveBook", method = RequestMethod.POST)
    @ResponseBody
    public String reserveBook(@RequestParam("userUuid") String userUuid,
                              @RequestParam("bookUuid") String bookUuid) {

        UserModel user;
        Book book = bookDAO.get(bookUuid);

        if (userUuid == "") {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user = userModelDAO.getByLogin(userDetails.getUsername());
        } else
            user = userModelDAO.get(userUuid);

        if (book == null) return "Failure: there is no such book";
        if (user == null) return "Failure: there is no such user";
        if (user.getReservedBooks().size()> LibraryConfiguration.getInstance().getMaxReservedBooks()) return "Failure: you reserved too many books";
        if (!book.getCondition().equals(Conditions.valueOf("Available"))) return "Failure: book is not available";

        book.setCondition(conditionDAO.saveIfNotInDB(new Condition(Conditions.valueOf("Reserved"))));
        bookDAO.update(book);
        userModelDAO.addReservedBook(user, book);
        return "success";
    }

    @RequestMapping(value = "/cancelReservedBook", method = RequestMethod.POST)
    @ResponseBody
    public String cancelReserveBook(@RequestParam("userUuid") String userUuid,
                                    @RequestParam("bookUuid") String bookUuid) {

        UserModel user;
        if (userUuid == "") {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user = userModelDAO.getByLogin(userDetails.getUsername());
        } else
            user = userModelDAO.get(userUuid);

        Book book = bookDAO.get(bookUuid);
        if (book == null) return "Failure: there is no such book";
        if (user == null) return "Failure: there is no such user";
        if (!book.getCondition().equals(Conditions.valueOf("Reserved"))) return "Failure: book is not reserved";

        Condition condition = new Condition(Conditions.valueOf("Available"));
        book.setCondition(conditionDAO.saveIfNotInDB(condition));
        bookDAO.update(book);
        userModelDAO.removeReservedBook(user, book);
        return "success";
    }

    @RequestMapping(value = "/admin/qrGenerate/", method = RequestMethod.GET)
    @ResponseBody
    public String getQRImage(@RequestParam("uuid") String uuid) {

        ByteArrayOutputStream out = QRCode.from(uuid).to(ImageType.PNG).stream();
        return new String(Base64.encodeBase64(out.toByteArray()));
    }

    @RequestMapping(value = "/admin/getDataEditBook/", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public String getDataEditBook(@RequestParam("uuid") String uuid) {
        List<String> data = new ArrayList<>();
        data.add(sectionDAO.getAll().toString());
        data.add(typeOfBookDAO.getAll().toString());
        data.add(bookDAO.get(uuid).toString());
        return data.toString();
    }



}