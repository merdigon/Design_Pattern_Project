using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPProjectServer.DatabaseObjects;

namespace WPProjectServer.Logic.SubsystemLogic
{
    public class DataPullSubLogic : ISubsystemLogic
    {
        public CopyStatus GetCopyStatus(Copy copy)
        {
            return new CopyStatus();
        }

        public Copy GetCopy(string code)
        {
            return new Copy();
        }

        public Copy[] GetAllCopies(Book book)
        {
            return new Copy[0];
        }

        public User GetUser(string code)
        {
            return new User();
        }

        public Book GetBook(string Code)
        {
            return new Book();
        }

        public Book[] GetBooks(Author author)
        {
            return new Book[0];
        }

        public Book[] GetBooks(string title)
        {
            return new Book[0];
        }


    }
}
