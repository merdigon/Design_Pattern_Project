package src.main.java.controllers.SubLogic;

import src.main.java.controllers.LogicFactory;
import src.main.java.models.*;

/**
 * Created by Szymon on 2015-10-13.
 */
public class DataPullSubLogic extends ISubsystemLogic
{
        public DataPullSubLogic(LogicFactory lgF)
        {
                super(lgF);
        }

        public CopyStatus GetCopyStatus(Copy copy)
        {
                return new CopyStatus();
        }

        public Copy GetCopy(String code)
        {
                return new Copy();
        }

        public Copy[] GetAllCopies(Book book)
        {
                return new Copy[0];
        }

        public User GetUser(String code)
        {
                return new User();
        }

        public Book GetBook(String Code)
        {
                return new Book();
        }

        public Book[] GetBooks(Author author)
        {
                return new Book[0];
        }

        public Book[] GetBooks(String title)
        {
                return new Book[0];
        }


}
