using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.DatabaseObjects
{
    public class Author : DatabaseObject
    {
        public string Name { get; set; }

        public string Surname { get; set; }

        public DateTime DeathYear { get; set; }
    }
}
