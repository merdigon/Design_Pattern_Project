﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPProjectServer.DatabaseObjects
{
    public class Section : DatabaseObject
    {
        public string Code { get; set; }
        public string Name { get; set; }
    }
}