using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPProjectServer.Logic.SubsystemLogic;

namespace WPProjectServer.Logic
{
    public class LogicFactory
    {
        public static T GetLogic<T>() where T : ISubsystemLogic, new()
        {
                return new T();
        }
    }
}
