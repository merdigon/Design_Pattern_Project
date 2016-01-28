package com.models;

import java.util.Date;

public class CopyStatus extends DatabaseObject
{
        public Copy copy;

        public UserModel rentBy;

        public Date rentTime;

        public Date returnTime;

        public double daysDelay;
}