package com.ziko.isaac.easysalepost.SQLite;

import android.provider.BaseColumns;

public class EasySaleContract {

    //private c-tor to prevent instance.
    private EasySaleContract() {}

    public static final class EasySaleEntry implements BaseColumns {

        public static final String TABLE_NAME = "easysaleList";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";

    }
}
