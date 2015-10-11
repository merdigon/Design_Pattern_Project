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

        public User GetUser(string code)
        {
            return new User();
        }
    }
}
