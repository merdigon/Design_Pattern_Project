using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.DatabaseObjects
{
    public class User : DatabaseObject
    {
        public string Code { get; set; }

        public string Login { get; set; }

        public string Password { get; set; }

        public string Name { get; set; }

        public string Surname { get; set; }

        public decimal Debt { get; set; }
    }
}
