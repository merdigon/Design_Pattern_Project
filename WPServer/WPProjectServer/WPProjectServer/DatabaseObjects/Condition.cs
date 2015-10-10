using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.DatabaseObjects
{
    public class Condition : DatabaseObject
    {
        Conditions Condition { get; set; }

        public override string ToString()
        {
            return Condition.ToString();
        }
    }

    public enum Conditions
    {
        Avaliable = 1,

        Missing = 2,

        Damaged = 3,

        Destroyed = 4
    }
}
