package com.models;

import java.util.Date;

/**
 * Created by Szymon on 2015-10-13.
 */
public class CopyStatus extends DatabaseObject
{
        public Copy copy;

        public UserModel rentBy;

        public Date rentTime;

        public Date returnTime;

        public double daysDelay;
}