﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPProjectServer.DatabaseObjects;

namespace WPProjectServer.Logic
{
    public interface IClientLogic
    {
        public abstract Book Return(Book bookToReturn);
    }
}
