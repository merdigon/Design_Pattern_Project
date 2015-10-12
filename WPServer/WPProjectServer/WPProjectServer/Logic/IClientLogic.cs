using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPProjectServer.DatabaseObjects;

namespace WPProjectServer.Logic
{
    public interface IClientLogic
    {
        CopyStatus ReturnCopy(string copyCode);

        CopyStatus RentCopy(string copyCode, string userCode);

        bool RegisterUser(User user);
    }
}
