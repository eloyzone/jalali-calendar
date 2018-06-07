package com.github.eloyzone.jalalicalendar;

import java.time.DateTimeException;

/**
 * Created By Eloy on 5/6/18.
 *
 *  This enum contains all months-of-jalali-year in both persian and english, this class mainly used in JalaliDate.
 *
 *  Notice: FARVARDIN has got the numberic value of 1 and Esfand has got 12, so year starts with FARVARDIN
 *
 * @author Eloy (Elyas Hadizadeh Tasbiti)
 */
public enum MonthPersian
{
    /**
     * The singleton instance for the month of Farvardin with 31 days.
     * This has the numeric value of {@code 1}.
     */
    FARVARDIN,
    /**
     * The singleton instance for the month of Ordibehesht with 31 days.
     * This has the numeric value of {@code 2}.
     */
    ORDIBEHESHT,
    /**
     * The singleton instance for the month of Khordad with 31 days.
     * This has the numeric value of {@code 3}.
     */
    KHORDAD,
    /**
     * The singleton instance for the month of Tir with 31 days.
     * This has the numeric value of {@code 4}.
     */
    TIR,
    /**
     * The singleton instance for the month of Mordad with 31 days.
     * This has the numeric value of {@code 5}.
     */
    MORDAD,
    /**
     * The singleton instance for the month of Shahrivar with 31 days.
     * This has the numeric value of {@code 6}.
     */
    SHAHRIVAR,
    /**
     * The singleton instance for the month of Mehr with 30 days.
     * This has the numeric value of {@code 7}.
     */
    MEHR,
    /**
     * The singleton instance for the month of Aban with 30 days.
     * This has the numeric value of {@code 8}.
     */
    ABAN,
    /**
     * The singleton instance for the month of Azar with 30 days.
     * This has the numeric value of {@code 9}.
     */
    AZAR,
    /**
     * The singleton instance for the month of Day with 30 days.
     * This has the numeric value of {@code 10}.
     */
    DAY,
    /**
     * The singleton instance for the month of Bahman with 30 days.
     * This has the numeric value of {@code 11}.
     */
    BAHMAN,
    /**
     * The singleton instance for the month of Esfand with 29 days, or 30 in a leap year.
     * This has the numeric value of {@code 12}.
     */
    ESFAND;
    /**
     * Private cache of all the constants.
     */

    private static final MonthPersian[] ENUMS = MonthPersian.values();

    final static String[] PERSIAN_MONTHS_EN = {"Farvardin", "Ordibehesht", "Khordad", "Tir", "Mordad", "Shahrivar", "Mehr", "Aban", "Azar", "Day", "Bahmand", "Esfand"};
    final static String[] PERSIAN_MONTHS_FA = {"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};



    //-----------------------------------------------------------------------

    /**
     * Gets the month-of-year {@code int} value.
     * <p>
     * The values are numbered following the ISO-8601 standard,
     * from 1 (Farvardin) to 12 (Esfand).
     *
     * @return the month-of-year, from 1 (Farvardin) to 12 (Esfand)
     */
    public int getValue() {
        return ordinal() + 1;
    }


    public static MonthPersian getEnum(int value)
    {
        for (MonthPersian monthPersian : MonthPersian.values())
        {
            if (monthPersian.ordinal() == value)
                return monthPersian;
        }
        return null; //For values out of enum scope
    }

    /**
     * Gets the month-of-year in persian string format.
     * <p>
     * The string are persian values
     *
     * @return the month-of-year, from فروردین to اسفند
     */
    public String getStringInPersian()
    {
        return PERSIAN_MONTHS_FA[ordinal()];
    }

    /**
     * Gets the month-of-year in english string format.
     * <p>
     * The string are english values
     *
     * @return the month-of-year, from farvardin to esfand
     */
    public String getStringInEnglish()
    {
        return PERSIAN_MONTHS_EN[ordinal()];
    }


    /**
     * Obtains an instance of {@code MonthPersian} from an {@code int} value.
     * <p>
     * {@code MonthPersian} is an enum representing the 12 months of the year.
     * This factory allows the enum to be obtained from the {@code int} value.
     * The {@code int} value will be from 1 (Farvardin) to 12 (Ordibehesht).
     *
     * @param month  the month-of-year to represent, from 1 (Farvardin) to 12 (Ordibehesht)
     * @return the month-of-year, not null
     * @throws DateTimeException if the month-of-year is invalid
     */
    public static MonthPersian of(int month) {
        if (month < 1 || month > 12) {
            throw new DateTimeException("Invalid value for MonthOfYear: " + month);
        }
        return ENUMS[month - 1];
    }
}
