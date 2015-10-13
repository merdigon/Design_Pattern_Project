package src.main.java.controllers;

import src.main.java.models.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
public class HibernateOperations {

    public static void save(Book book){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(book);
        session.getTransaction().commit();
    }

    public static Book get(int key){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Book book = (Book)session.get(Book.class, key);
        session.getTransaction().commit();
        return book;
    }

    public static List<Book> getAll(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Book");
        List<Book> list = query.list();
        session.getTransaction().commit();
        return list;
    }


//
//    public static void main(String[] args){
//        Book book = new Book();
//        book.setAuthor("autor");
//        book.setTitle("tytul");
//        book.setYear("rok");
//        save(book);
//    }

}
