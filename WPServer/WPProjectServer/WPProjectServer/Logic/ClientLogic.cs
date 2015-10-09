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
        List<ISubsystemLogic> ServerLogics;

        public ClientLogic()
        {
            ServerLogics = new List<ISubsystemLogic>();
            LoadLogics();
        }

        void LoadLogics()
        {
            ServerLogics.Add(new BookIOSubLogic());
        }

        public Book Return(Book bookToReturn)
        {
            foreach(ISubsystemLogic subLogic in ServerLogics)
            {
                if(subLogic is BookIOSubLogic)
                {
                    return ((BookIOSubLogic)subLogic).Return(bookToReturn);
                }
            }
            return null;
        }
    }
}
