using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.Logic
{
    public class WebLogic : IWebLogic
    {
        Logics logics;

        public WebLogic()
        {
            logics = new Logics();
        }
    }
}
