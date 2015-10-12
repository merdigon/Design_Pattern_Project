using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.Logic
{
    public abstract class ISubsystemLogic
    {
        LogicFactory logics;

        public ISubsystemLogic()
        {
            logics = new LogicFactory();
        }
    }
}
