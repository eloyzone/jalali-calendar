package com.github.eloyzone.jalalicalendar;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created By Eloy on 5/6/18.
 *
 * This class can be used for formatting an existing {@link JalaliDate}
 * <p>
 * Note: for formatting gregorian date based on {@link java.time.LocalDate} you have to use {@link DateTimeFormatter}.
 *
 * @author Eloy (Elyas Hadizadeh Tasbiti)
 */
public class JalaliDateFormatter
{
    private String pattern;
    private int formatLanguage;
    private JalaliDate jalaliDate;

    /**
     * a flag for configuring the language of formatting.
     *
     * <p>
     * you can call it from outside of the class for setting formatting language. usually can be used in constructor
     */
    public static int FORMAT_IN_PERSIAN = 1;

    /**
     * a flag for configuring the language of formatting.
     *
     * <p>
     * you can call it from outside of the class for setting formatting language. usually can be used in constructor
     */
    public static int FORMAT_IN_ENGLISH = 2;

    /**
     * constructor that sets english as a default formatting language.
     *
     * @param pattern desired pattern.
     */
    public JalaliDateFormatter(String pattern)
    {
        this.pattern = pattern;
        this.formatLanguage = FORMAT_IN_ENGLISH;
    }

    /**
     * constructor that sets desired language for formatting
     *
     * @param pattern        desired pattern.
     * @param formatLanguage shows the selected language which 1 is persian and 2 is english.
     */
    public JalaliDateFormatter(String pattern, int formatLanguage)
    {
        this.pattern = pattern;
        this.formatLanguage = formatLanguage;
    }

    /**
     * Returns a string that shows given jalali date in a proper formatting, it first parses the given pattern and
     * creates some place holders for future values then it applies format.
     *
     * @param jalaliDate date that will be formatted in a proper way.
     * @return a formatted date in a string that can be persian or english
     */
    public String format(JalaliDate jalaliDate)
    {
        this.jalaliDate = jalaliDate;

        parsePattern(pattern);

        String result = "";


        if (formatLanguage == FORMAT_IN_PERSIAN && pattern.contains("M"))
        {
            for (int i = dateFormatterTemporalFields.size() - 1; i >= 0; i--)
                result = result + applyFormat(i);
        } else
        {
            for (int i = 0; i < dateFormatterTemporalFields.size(); i++)
                result = result + applyFormat(i);
        }


        if (formatLanguage == FORMAT_IN_PERSIAN)
        {
            char[] resultChars = result.toCharArray();
            for (int i = 0; i < resultChars.length; i++)
            {
                resultChars[i] = replaceWithPersian(resultChars[i]);
            }
            result = String.valueOf(resultChars);
        }
        return result;
    }

    /*
        Creates string format of date based on list of temporal fields.
     */
    private String applyFormat(int index)
    {
        String result = "";
        String temp = "";

        DateFormatterTemporalField dateFormatterTemporalField = dateFormatterTemporalFields.get(index);

        switch (dateFormatterTemporalField.getType())
        {
            case DateFormatterTemporalField.YEAR_FIElD:
                if (dateFormatterTemporalField.getCount() == 2)
                {
                    temp = jalaliDate.getYear() + "";
                    result = result + temp.substring(1, 3);
                } else
                {
                    result = result + jalaliDate.getYear();
                }
                break;
            case DateFormatterTemporalField.MONTH_FIElD:


                int monthValue = jalaliDate.getMonthPersian().getValue();

                if (dateFormatterTemporalField.getCount() == 2)
                {
                    if (monthValue < 10)
                    {
                        temp = "0";
                    }
                    result = result + temp + monthValue;
                } else
                {
                    if (monthValue < 10)
                    {
                        throw new IllegalArgumentException("format of month can not match with value");
                    } else
                    {
                        result = result + temp + monthValue;
                    }
                }
                break;
            case DateFormatterTemporalField.DAY_FIElD:
                if (dateFormatterTemporalField.getCount() == 2)
                {
                    int dayValue = jalaliDate.getDay();
                    if (dayValue < 10)
                    {
                        temp = "0";
                    }
                    result = result + temp + dayValue;
                } else // count equals to one
                {
                    int dayValue = jalaliDate.getDay();
                    if (dayValue < 10)
                    {
                        throw new IllegalArgumentException("format of day can not match with value");
                    } else
                    {
                        result = result + temp + dayValue;
                    }
                }
                break;
            case DateFormatterTemporalField.SPACE_FIElD:
                for (int i = 0; i < dateFormatterTemporalField.getCount(); i++)
                    result = result + " ";
                break;
            case DateFormatterTemporalField.DASH_FIElD:
                for (int i = 0; i < dateFormatterTemporalField.getCount(); i++)
                    result = result + "-";
                break;
            case DateFormatterTemporalField.SLASH_FIElD:
                for (int i = 0; i < dateFormatterTemporalField.getCount(); i++)
                    result = result + "/";
                break;
            case DateFormatterTemporalField.MONTH_STRING_FIElD:
                String monthString = formatLanguage == FORMAT_IN_PERSIAN ? jalaliDate.getMonthPersian().getStringInPersian() : jalaliDate.getMonthPersian().getStringInEnglish();
                result = result + monthString;
                break;
        }
        return result;
    }

    /**
     * Replace english numbers with persian ones.
     * <p>
     * It changes each english number with its persian version,
     * for example 1 becomes ۱.
     *
     * @param resultChar an english number that will be changed to persian.
     * @return the persian version of number
     */
    private char replaceWithPersian(char resultChar)
    {
        if (resultChar == '0')
            return '\u06F0';
        if (resultChar == '1')
            return '\u06F1';
        if (resultChar == '2')
            return '\u06F2';
        if (resultChar == '3')
            return '\u06F3';
        if (resultChar == '4')
            return '\u06F4';
        if (resultChar == '5')
            return '\u06F5';
        if (resultChar == '6')
            return '\u06F6';
        if (resultChar == '7')
            return '\u06F7';
        if (resultChar == '8')
            return '\u06F8';
        if (resultChar == '9')
            return '\u06F9';

        return resultChar;
    }


    // validate entered pattern and then inflate the array list of place holders ( temporal fields )
    private void parsePattern(String pattern)
    {
        for (int pos = 0; pos < pattern.length(); pos++)
        {
            char cur = pattern.charAt(pos);
            if ((cur >= 'A' && cur <= 'Z') || (cur >= 'a' && cur <= 'z') || (cur == ' ') || (cur == '/') || (cur == '-'))
            {
                int start = pos++;
                for (; pos < pattern.length() && pattern.charAt(pos) == cur; pos++) ;  // short loop
                int count = pos - start;

                if (isValidPatternCharacter(String.valueOf(cur)))
                {
                    parseField(cur, count); // create each temporal field
                }
                pos--;
            } else
            {
                throw new IllegalArgumentException("Invalid character for formatting: " + cur);
            }
        }
    }


    // an array list of place holders for each pattern element, each pattern element will be mapped to one temporal field
    private ArrayList<DateFormatterTemporalField> dateFormatterTemporalFields = new ArrayList<>();

    /*
        according to position and numbers of each pattern element ( like mm ), it creates one temporal field and add it
        to list of temporal fields.
    */
    private void parseField(char cur, int count)
    {
        switch (cur)
        {
            case 'y':
                if (count == 2)
                {
                    throw new IllegalArgumentException("Not enough pattern letters: " + cur);
                }
                if (count == 2)
                {
                    dateFormatterTemporalFields.add(new DateFormatterTemporalField(2, DateFormatterTemporalField.YEAR_FIElD));
                } else if (count == 4)
                {
                    dateFormatterTemporalFields.add(new DateFormatterTemporalField(4, DateFormatterTemporalField.YEAR_FIElD));
                } else
                {
                    throw new IllegalArgumentException("Too many pattern letters: " + cur);
                }
                break;

            case 'm':
                if (count == 1)
                {
                    dateFormatterTemporalFields.add(new DateFormatterTemporalField(1, DateFormatterTemporalField.MONTH_FIElD));
                } else if (count == 2)
                {
                    dateFormatterTemporalFields.add(new DateFormatterTemporalField(2, DateFormatterTemporalField.MONTH_FIElD));
                } else
                {
                    throw new IllegalArgumentException("Too many pattern letters: " + cur);
                }
                break;

            case 'M':
                if (count == 1)
                {
                    dateFormatterTemporalFields.add(new DateFormatterTemporalField(1, DateFormatterTemporalField.MONTH_STRING_FIElD));
                } else
                {
                    throw new IllegalArgumentException("Too many pattern letters: " + cur);
                }
                break;

            case 'd':
                if (count == 1)
                {
                    dateFormatterTemporalFields.add(new DateFormatterTemporalField(1, DateFormatterTemporalField.DAY_FIElD));
                } else if (count == 2)
                {
                    dateFormatterTemporalFields.add(new DateFormatterTemporalField(2, DateFormatterTemporalField.DAY_FIElD));
                } else
                {
                    throw new IllegalArgumentException("Too many pattern letters: " + cur);
                }
                break;

            case ' ':
                dateFormatterTemporalFields.add(new DateFormatterTemporalField(count, DateFormatterTemporalField.SPACE_FIElD));
                break;
            case '-':
                dateFormatterTemporalFields.add(new DateFormatterTemporalField(count, DateFormatterTemporalField.DASH_FIElD));
                break;
            case '/':
                dateFormatterTemporalFields.add(new DateFormatterTemporalField(count, DateFormatterTemporalField.SLASH_FIElD));
                break;
        }
    }

    /**
     * Valid pattern characters that can be used for formatting jalali date
     * <p>
     * y: year              (Note: use yyyy)
     * m: month in number   (Note: use mm)
     * M: month in word     (Note: use M)
     * d: day               (Note: use dd)
     * ' ', '/' and '-' can be used as a separator.
     */
    private String[] validPatternCharacters = {"y", "m", "M", "d", " ", "/", "-"};


    private boolean isValidPatternCharacter(String character)
    {
        // for (String validPatternCharacter : validPatternCharacters)
        // {
        //     if (validPatternCharacter.equals(character))
        //     {
        //         return true;
        //     }
        // }
        // return false;

        return Arrays.stream(validPatternCharacters).anyMatch(x -> x.equals(character));
    }


}
