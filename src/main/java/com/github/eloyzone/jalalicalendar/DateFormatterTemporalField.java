package com.github.eloyzone.jalalicalendar;

/**
 * Created By Eloy on 5/7/18.
 *
 * This class is a place holder for each date pattern element, for example each yyyy is considered az a one temporal
 * field. each object of this class has two important value 'type' and 'count'. type shows type of each field and count
 * shows the number of it. for example 'mm' will be MONTH_FIELD and its count will be two.
 *
 * This class is protected by this package and just will be used by JalaliDateFormatter class.
 *
 * @author Eloy (Elyas Hadizadeh Tasbiti)
 * @see JalaliDateFormatter
 *
 */
class DateFormatterTemporalField
{
    protected static final int YEAR_FIElD = 1;
    protected static final int MONTH_FIElD = 2;
    protected static final int MONTH_STRING_FIElD = 3;
    protected static final int DAY_FIElD = 4;
    protected static final int DAY_STRING_FIElD = 5;
    protected static final int SPACE_FIElD = 6;
    protected static final int DASH_FIElD = 7;
    protected static final int SLASH_FIElD = 8;

    private int count = 0;
    private int type;

    protected DateFormatterTemporalField(int count, int type)
    {
        this.count = count;
        this.type = type;
    }

    protected int getCount()
    {
        return count;
    }

    protected int getType()
    {
        return type;
    }
}
