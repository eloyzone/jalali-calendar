package zone.eloy.commons.jalalicalendar;

import java.util.Objects;

/**
 * Created By Eloy on 5/6/18.
 * <p>
 * This class holds some data about specific date in Jalali's Date format like year, month, day, day of week and
 * leap year
 *
 * @author Eloy (Elyas Hadizadeh Tasbiti)
 */
public class JalaliDate
{
    private int year;
    private MonthPersian monthPersian;
    private int day;
    private DayOfWeekPersian dayOfWeekPersian;
    private boolean leapYear = false;

    /**
     * Only DateConverter is allowed to create an object of JalaliDate with empty constructor's argument
     * <p>
     * Other classes and methods must use another version.
     */
    protected JalaliDate()
    {
    }

    /**
     * creates an object of jalali date.
     *
     * @param year  jalali year
     * @param month from 1 to 12
     * @param day   from 1 to 31
     *
     * @exception IllegalArgumentException if values are negative.
     * @exception IllegalArgumentException if requested date is not possible.
     */
    public JalaliDate(int year, int month, int day)
    {
        basicArgumentValidating(year, month, day);

        setYear(year);
        this.monthPersian = MonthPersian.of(month);
        this.day = day;

        if (!leapYear && month == 12 && day >= 30)
            throw new IllegalArgumentException("Wrong value for day, just in leap year last month must be greater than 29");

    }

    private void basicArgumentValidating(int year, int month, int day)
    {
        if (month <= 0 || month > 12)
            throw new IllegalArgumentException("Wrong value for month, it must be from 1 to 12");

        if (day <= 0 || day > 32)
            throw new IllegalArgumentException("Wrong value for day, it must be from 1 to 31");

        if (year <= 0)
            throw new IllegalArgumentException("Wrong value for Year, it must be positive ");

        if (month >= 7 && month <= 12 && day == 31)
            throw new IllegalArgumentException("Wrong value for day. in second half of year, months have less than 31 days ");
    }

    /**
     * reformats jalali date with provided format. the result will be a string in either persian or english.
     *
     * @param jalaliDateFormatter an object that shows preferred format for jalali date.
     * @return string formatted date either in persian or english
     */
    public String format(JalaliDateFormatter jalaliDateFormatter)
    {
        Objects.requireNonNull(jalaliDateFormatter, "formatter");
        return jalaliDateFormatter.format(this);
    }

    /**
     * Transforms date to ISO-8601 standard contains year, month and day
     *
     * @return String format of date
     */
    @Override
    public String toString()
    {
        String date = year + "-" + monthPersian.getValue() + "-" + day;
        return date;
    }

    public int getYear()
    {
        return year;
    }

    /**
     * set year of jalali date and then checks is it a leap year or not.
     * <p>
     * Note: only classes of this package needs this method, you'll never need it. use constructor instead.
     *
     *
     * @param year input year
     */
    protected void setYear(int year)
    {
        this.year = year;
        setLeapYear(new DateConverter().leapPersiana(year));
    }

    /**
     * gets current day of week
     *
     * @return DayOfWeekPersian object will be returned that user can get its persian or english string version by
     * calling getStringInXXX() method.
     */
    public DayOfWeekPersian getDayOfWeek()
    {
        if (dayOfWeekPersian == null)
        {
            dayOfWeekPersian = new DateConverter().getDayOfWeekPersian(this);
            return dayOfWeekPersian;
        } else
            return dayOfWeekPersian;
    }

    public MonthPersian getMonthPersian()
    {
        return monthPersian;
    }

    protected void setMonthPersian(MonthPersian monthPersian)
    {
        this.monthPersian = monthPersian;
    }

    public int getDay()
    {
        return day;
    }

    protected void setDay(int day)
    {
        this.day = day;
    }

    public boolean isLeapYear()
    {
        return leapYear;
    }

    protected void setLeapYear(boolean leapYear)
    {
        this.leapYear = leapYear;
    }

    protected void setDayOfWeekPersian(DayOfWeekPersian dayOfWeekPersian)
    {
        this.dayOfWeekPersian = dayOfWeekPersian;
    }



    /**
     * Checks if this date is equal to another date.
     * <p>
     * Compares this {@link JalaliDate} with another for ensuring that the date is the same.
     * <p>
     * Only object type of {@link JalaliDate} are compared, other types return false.
     *
     * @param obj the object to check, null returns false
     * @return true if this is equal to the other date
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj instanceof JalaliDate)
            return compare((JalaliDate) obj);
        return false;
    }


    private boolean compare(JalaliDate otherDate)
    {
        if (year != otherDate.getYear() || monthPersian.getValue() != otherDate.monthPersian.getValue() ||
                day != otherDate.day || this.getDayOfWeek().getValue() != otherDate.getDayOfWeek().getValue() ||
                leapYear != otherDate.leapYear)
        {
            return false;
        } else
            return true;
    }
}
