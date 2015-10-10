using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPProjectServer.DatabaseObjects;
using WPProjectServer.Logic.SubsystemLogic;

namespace WPProjectServer.Logic
{
    public class ClientLogic : IClientLogic
    {
        Logics logics;

        public ClientLogic()
        {
            logics = new Logics();
        }

        public Book Return(Book bookToReturn)
        {
            return logics.GetLogic<BookIOSubLogic>().Return(bookToReturn);
        }
    }
}
