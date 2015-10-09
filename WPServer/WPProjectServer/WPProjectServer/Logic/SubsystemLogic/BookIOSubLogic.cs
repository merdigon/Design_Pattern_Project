using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPProjectServer.DatabaseObjects;

namespace WPProjectServer.Logic.SubsystemLogic
{
    public class BookIOSubLogic : ISubsystemLogic
    {
        public Book Return(Book book)
        {
            return book;
        }
    }
}
