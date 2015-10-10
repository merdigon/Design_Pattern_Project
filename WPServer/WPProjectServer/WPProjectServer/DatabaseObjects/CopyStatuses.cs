using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.DatabaseObjects
{
    public class CopyStatuses : DatabaseObject
    {
        public Copy Copy { get; set; }

        public User RentBy { get; set; }

        public DateTime RentTime { get; set; }

        public DateTime ReturnTime { get; set; }               
    }
}
