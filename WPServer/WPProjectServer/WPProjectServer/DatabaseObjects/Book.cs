﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.DatabaseObjects
{
    public class Book : DatabaseObject
    {
        public string Code { get; set; }

        public string Title { get; set; }

        public Author Author { get; set; }

        public TypeOfBook BookType { get; set; }
    }
}
