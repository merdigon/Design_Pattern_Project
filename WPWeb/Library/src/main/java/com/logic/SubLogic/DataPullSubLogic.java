//package com.logic.SubLogic;
//
//
//import com.dao.AuthorDAO;
//import com.dao.BookDAO;
//import com.logic.LogicFactory;
//import com.models.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.LinkedHashSet;
//import java.util.List;
//
///**
// * Created by Szymon on 2015-10-13.
// */
//@Component
//public class DataPullSubLogic extends ISubsystemLogic
//{
//        @Autowired
//        BookDAO bookDAO;
//
//        @Autowired
//        AuthorDAO authorDAO;
//
//        public DataPullSubLogic(LogicFactory lgF)
//        {
//               // super(lgF);
//        }
//
//        public Book getBook(String bookUuid)
//        {
//                return bookDAO.get(bookUuid);
//        }
//
//        public List<Book> getAllBooks()
//        {
//                return bookDAO.getAll();
//        }
//
//        public UserModel getUser(String code)
//        {
//                return new UserModel();
//        }
//
//        public List<Book> getBooksByAuthor(Author author)
//        {
//                return bookDAO.getAllByAuthor(author);
//        }
//
//        public LinkedHashSet<Book> getBooksByAuthor(String name, String surname, String year) {
//                List<Book> books = new ArrayList<Book>();
//                for (Author author : authorDAO.get(name, surname, year)) {
//                        books.addAll(getBooksByAuthor(author));
//                }
//                return new LinkedHashSet<>(books);
//        }
//
//        public List<Book> getBooksByYear(String year)
//        {
//                return bookDAO.getAllByYear(year);
//        }
//
//        public List<Book> getBooksByTitle(String title)
//        {
//                return bookDAO.findByColumn("title", title);
//        }
//
//        public List<Book> getBooksByCondition(Condition condition){
//                return bookDAO.getAllByCondition(condition);
//        }
//}
