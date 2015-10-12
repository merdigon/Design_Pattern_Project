using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.DatabaseObjects
{
    public class Condition : DatabaseObject
    {
        Conditions condition { get; set; }

        public override string ToString()
        {
            return condition.ToString();
        }

        public override bool Equals(object obj)
        {
            if(obj is Condition)
                return condition.Equals(((Condition)obj).condition);
            else if(obj is Conditions)
                return condition.Equals((Conditions)obj);
            return false;
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
