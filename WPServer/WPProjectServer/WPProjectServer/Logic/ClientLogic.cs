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
        public CopyStatus RentCopy(string copyCode, string userCode)
        {
            return LogicFactory.GetLogic<BookIOSubLogic>().RentCopy(copyCode, userCode);
        }

        public CopyStatus ReturnCopy(string copyCode)
        {
            return LogicFactory.GetLogic<BookIOSubLogic>().Return(copyCode);
        }
    }
}
