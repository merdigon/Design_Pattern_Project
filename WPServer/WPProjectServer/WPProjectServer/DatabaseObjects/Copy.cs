using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.DatabaseObjects
{
    public class Copy : DatabaseObject
    {
        public Book Book { get; set; }

        public Condition Condition { get; set; }

        public int Edition { get; set; }

        public int Year { get; set; }

        public Section Section { get; set; }

        public CopyStatus CopyStatus { get; set; }
    }
}
