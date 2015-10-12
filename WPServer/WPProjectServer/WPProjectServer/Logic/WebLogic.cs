using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.Logic
{
    public class WebLogic : IWebLogic
    {
        LogicFactory logics;

        public WebLogic()
        {
            logics = new LogicFactory();
        }
    }
}
