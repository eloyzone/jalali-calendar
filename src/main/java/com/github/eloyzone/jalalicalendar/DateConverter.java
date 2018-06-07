package com.github.eloyzone.jalalicalendar;

import java.time.LocalDate;
import java.time.Month;

/**
 * Created by Eloy on 4/17/2017.
 * <p>
 * This class is a main class of jeloy calendar as it converts jalali to gregorian and gregorian to jalali.
 * In order to handel gregorian's date it uses java8's LocalDate, for converted jalali date it uses JalaliDate class
 * which is a class that holds attributes of specific jalali date.
 *
 * @author Eloy (Elyas Hadizadeh Tasbiti)
 * @see LocalDate
 * @see JalaliDate
 */
public class DateConverter
{
    private double juliandayDayValue;
    private double persianaYearValue;
    private double persianaMonthSelectedIndex;
    private double persianaDayValue;
    private int persianaWeekDay;
    private double gregorianYearValue;
    private double gregorianMonthSelectedIndex;
    private double gregorianDayValue;


    /**
     * converts jalali date based on input arguments to gregorian date.
     *
     * @param jalaliDate will be converted to suitable form of gregorian date.
     * @return {@link LocalDate} version of gregorian date will be returned.
     */
    public LocalDate jalaliToGregorian(JalaliDate jalaliDate)
    {
        return jalaliToGregorian(jalaliDate.getYear(), jalaliDate.getMonthPersian().getValue(), jalaliDate.getDay());
    }

    /**
     * converts jalali date based on input arguments to gregorian date.
     *
     * @param year  positive jalali year value
     * @param month is jeloy's {@link MonthPersian} which is similar to java8's {@link Month}
     * @param day   positive jalali day, it can be more than 31 and automatically it tries to shift to next month

     * @return {@link LocalDate} version of gregorian date will be returned.
     * @throws IllegalArgumentException if values are negative.
     */
    public LocalDate jalaliToGregorian(int year, MonthPersian month, int day)
    {
        validateDateValues(year, day);
        return jalaliToGregorian(year, month.getValue(), day);
    }



    /**
     * converts jalali date based on input arguments to gregorian date.
     * <p>
     * It's smart enough to calculate target date with every input so instead of month from 1 to 12 it's possible to enter
     * 13 in this case it automatically shift to next year, it works for days in a same way. although, for your own
     * comfort it is recommended to use proper values
     *
     * @param year  positive jalali year value
     * @param month starts from 1 to 12, but it can be more than 12 as it tires to shift to next year
     * @param day   positive jalali day, it can be more than 31 and automatically it tries to shift to next month

     * @return JalaliDate object
     * @throws IllegalArgumentException if values are negative.
     * @see JalaliDate
     */
    public LocalDate jalaliToGregorian(int year, int month, int day)
    {
        validateDateValues(year, month, day);

        persianaYearValue = year;
        persianaMonthSelectedIndex = month - 1;
        persianaDayValue = day;

        setJulian(persianaToJd((persianaYearValue), persianaMonthSelectedIndex + 1, persianaDayValue) + 0.5);

        LocalDate localDate = LocalDate.of((int) gregorianYearValue, (int) gregorianMonthSelectedIndex + 1, (int) gregorianDayValue);

        return localDate;
    }



    /**
     * converts gregorian date based on input arguments to jalali date.
     * It's smart enough to calculate target date with every input so instead of month from 1 to 12 it's possible to enter
     * 13 in this case it automatically shift to next year, it works for days in a same way. although, for your own
     * comfort it is recommended to use proper values
     *
     * @param year  gregorian positive year
     * @param month starts from 1 to 12, but it can be more than 12 as it tires to shift to next year
     * @param day   positive gregorian day, it can be more than 31 and automatically it tries to shift to next month
     *
     * @throws IllegalArgumentException if values are negative.
     * @return JalaliDate object
     * @see JalaliDate
     */
    public JalaliDate gregorianToJalali(int year, int month, int day)
    {
        validateDateValues(year,month,day);

        updateFromGregorian(year, month - 1, day);

        int persianYear = (int) persianaYearValue;
        int persianMonth = (int) (persianaMonthSelectedIndex);
        int persianDay = (int) persianaDayValue;

        JalaliDate jalaliDate = new JalaliDate();
        jalaliDate.setYear(persianYear);
        jalaliDate.setMonthPersian(MonthPersian.getEnum(persianMonth));
        jalaliDate.setDay(persianDay);
        jalaliDate.setDayOfWeekPersian(DayOfWeekPersian.getEnum(persianaWeekDay));

        return jalaliDate;
    }

    protected DayOfWeekPersian getDayOfWeekPersian(JalaliDate jalaliDate)
    {
        return DayOfWeekPersian.getEnum(jalaliToGregorian(jalaliDate).getDayOfWeek().getValue());
    }

    /**
     * converts gregorian date based on input arguments to jalali date.
     *
     * @param year  gregorian positive year
     * @param month is Java8's {@link Month} object
     * @param day   positive gregorian day, it can be more than 31 and automatically it tries to shift to next month
     *
     * @throws IllegalArgumentException if values are negative.
     * @return JalaliDate object
     * @see JalaliDate
     */
    public JalaliDate gregorianToJalali(int year, Month month, int day)
    {
        validateDateValues(year,day);
        return gregorianToJalali(year, month.getValue(), day);
    }

    /**
     * returns current date based on jalali Date.
     * <p>
     * This method works based on {@link LocalDate}, it first gets current date of os-system as
     * gregorian and then casts it to Jalali.
     *
     * @return today's JalaliDate
     * @see JalaliDate
     */
    public JalaliDate nowAsJalali()
    {
        LocalDate localDate = LocalDate.now();
        return gregorianToJalali(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth());
    }

    /**
     * returns current gregorian date based on Java8's LocalDate.
     * <p>
     * This method created just for convenient, of-course it is possible to directly use LocalDate class.
     *
     * @return LocalDate object
     * @see LocalDate
     */
    public LocalDate nowAsGregorian()
    {
        return LocalDate.now();
    }


    private void validateDateValues(int year, int month, int day)
    {
        if (year < 0 || month < 0 || day < 0)
            throw new IllegalArgumentException("Wrong value(s). date's year-month-day can not be negative");
    }

    private void validateDateValues(int year, int day)
    {
        if (year < 0 || day < 0)
            throw new IllegalArgumentException("Wrong value(s). date's year-month-day can not be negative");
    }

    private String NormLeap[] = {"Normal year", "Leap year"};

    private double jwday(double j)
    {
        return mod(Math.floor(j + 1.5), 7);
    }

    private double mod(double a, double b)
    {
        return a - (b * Math.floor(a / b));
    }

    private boolean leapGregorian(double year)
    {
        return ((year % 4) == 0) && (!(((year % 100) == 0) && ((year % 400) != 0)));
    }

    double GREGORIAN_EPOCH = 1721425.5;

    private double gregorianToJd(double year, double month, double day)
    {
        return (GREGORIAN_EPOCH - 1) +
                (365 * (year - 1)) +
                Math.floor((year - 1) / 4) +
                (-Math.floor((year - 1) / 100)) +
                Math.floor((year - 1) / 400) +
                Math.floor((((367 * month) - 362) / 12) +
                        ((month <= 2) ? 0 :
                                (leapGregorian(year) ? -1 : -2)
                        ) +
                        day);
    }

    private double[] jdToGregorian(double jd)
    {
        double wjd, depoch, quadricent, dqc, cent, dcent, quad, dquad, yindex, year, yearday, leapadj;

        wjd = Math.floor(jd - 0.5) + 0.5;
        depoch = wjd - GREGORIAN_EPOCH;
        quadricent = Math.floor(depoch / 146097);
        dqc = mod(depoch, 146097);
        cent = Math.floor(dqc / 36524);
        dcent = mod(dqc, 36524);
        quad = Math.floor(dcent / 1461);
        dquad = mod(dcent, 1461);
        yindex = Math.floor(dquad / 365);
        year = (quadricent * 400) + (cent * 100) + (quad * 4) + yindex;
        if (!((cent == 4) || (yindex == 4)))
        {
            year++;
        }
        yearday = wjd - gregorianToJd(year, 1, 1);
        leapadj = ((wjd < gregorianToJd(year, 3, 1)) ? 0
                :
                (leapGregorian(year) ? 1 : 2)
        );
        double month = Math.floor((((yearday + leapadj) * 12) + 373) / 367);
        double day = (wjd - gregorianToJd(year, month, 1)) + 1;

        double[] returnValue = {year, month, day};
        return returnValue;
    }

    double[][] JDE0tab1000 =

            {
                    {1721139.29189, 365242.13740, 0.06134, 0.00111, -0.00071},
                    {1721233.25401, 365241.72562, -0.05323, 0.00907, 0.00025},
                    {1721325.70455, 365242.49558, -0.11677, -0.00297, 0.00074},
                    {1721414.39987, 365242.88257, -0.00769, -0.00933, -0.00006}
            };

    double[][] JDE0tab2000 = {
            {2451623.80984, 365242.37404, 0.05169, -0.00411, -0.00057},
            {2451716.56767, 365241.62603, 0.00325, 0.00888, -0.00030},
            {2451810.21715, 365242.01767, -0.11575, 0.00337, 0.00078},
            {2451900.05952, 365242.74049, -0.06223, -0.00823, 0.00032}
    };

    private double equinox(double year, int which)
    {
        double deltaL, JDE0, JDE, S, T, W, Y;
        int i, j;
        double[][] JDE0tab;

        if (year < 1000)
        {
            JDE0tab = JDE0tab1000;
            Y = year / 1000;
        } else
        {
            JDE0tab = JDE0tab2000;
            Y = (year - 2000) / 1000;
        }

        JDE0 = JDE0tab[which][0] +
                (JDE0tab[which][1] * Y) +
                (JDE0tab[which][2] * Y * Y) +
                (JDE0tab[which][3] * Y * Y * Y) +
                (JDE0tab[which][4] * Y * Y * Y * Y);

        T = (JDE0 - 2451545.0) / 36525;
        W = (35999.373 * T) - 2.47;
        deltaL = 1 + (0.0334 * dcos(W)) + (0.0007 * dcos(2 * W));

        S = 0;
        for (i = j = 0; i < 24; i++)
        {
            S += EquinoxpTerms[j] * dcos(EquinoxpTerms[j + 1] + (EquinoxpTerms[j + 2] * T));
            j += 3;
        }


        JDE = JDE0 + ((S * 0.00001) / deltaL);

        return JDE;
    }

    double[] EquinoxpTerms = {
            485, 324.96, 1934.136,
            203, 337.23, 32964.467,
            199, 342.08, 20.186,
            182, 27.85, 445267.112,
            156, 73.14, 45036.886,
            136, 171.52, 22518.443,
            77, 222.54, 65928.934,
            74, 296.72, 3034.906,
            70, 243.58, 9037.513,
            58, 119.81, 33718.147,
            52, 297.17, 150.678,
            50, 21.02, 2281.226,
            45, 247.54, 29929.562,
            44, 325.15, 31555.956,
            29, 60.93, 4443.417,
            18, 155.12, 67555.328,
            17, 288.79, 4562.452,
            16, 198.04, 62894.029,
            14, 199.76, 31436.921,
            12, 95.39, 14577.848,
            12, 287.11, 31931.756,
            12, 320.81, 34777.259,
            9, 227.73, 1222.114,
            8, 15.45, 16859.074
    };

    private double tehranEquinox(double year)
    {
        double equJED, equJD, equAPP, equTehran, dtTehran;

        equJED = equinox(year, 0);

        equJD = equJED - (deltat(year) / (24 * 60 * 60));

        equAPP = equJD + equationOfTime(equJED);

        dtTehran = (52 + (30 / 60.0) + (0 / (60.0 * 60.0))) / 360;
        equTehran = equAPP + dtTehran;

        return equTehran;
    }

    private double deltat(double year)
    {
        double dt, f, t;
        int i;

        if ((year >= 1620) && (year <= 2000))
        {
            i = (int) Math.floor((year - 1620) / 2);
            f = ((year - 1620) / 2) - i;
            dt = deltaTtab[i] + ((deltaTtab[i + 1] - deltaTtab[i]) * f);
        } else
        {
            t = (year - 2000) / 100;
            if (year < 948)
            {
                dt = 2177 + (497 * t) + (44.1 * t * t);
            } else
            {
                dt = 102 + (102 * t) + (25.3 * t * t);
                if ((year > 2000) && (year < 2100))
                {
                    dt += 0.37 * (year - 2100);
                }
            }
        }
        return dt;
    }

    private double deltaTtab[] = {
            121, 112, 103, 95, 88, 82, 77, 72, 68, 63, 60, 56, 53, 51, 48, 46,
            44, 42, 40, 38, 35, 33, 31, 29, 26, 24, 22, 20, 18, 16, 14, 12,
            11, 10, 9, 8, 7, 7, 7, 7, 7, 7, 8, 8, 9, 9, 9, 9, 9, 10, 10, 10,
            10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13,
            13, 14, 14, 14, 14, 15, 15, 15, 15, 15, 16, 16, 16, 16, 16, 16,
            16, 16, 15, 15, 14, 13, 13.1, 12.5, 12.2, 12, 12, 12, 12, 12, 12,
            11.9, 11.6, 11, 10.2, 9.2, 8.2, 7.1, 6.2, 5.6, 5.4, 5.3, 5.4, 5.6,
            5.9, 6.2, 6.5, 6.8, 7.1, 7.3, 7.5, 7.6, 7.7, 7.3, 6.2, 5.2, 2.7,
            1.4, -1.2, -2.8, -3.8, -4.8, -5.5, -5.3, -5.6, -5.7, -5.9, -6,
            -6.3, -6.5, -6.2, -4.7, -2.8, -0.1, 2.6, 5.3, 7.7, 10.4, 13.3, 16,
            18.2, 20.2, 21.1, 22.4, 23.5, 23.8, 24.3, 24, 23.9, 23.9, 23.7,
            24, 24.3, 25.3, 26.2, 27.3, 28.2, 29.1, 30, 30.7, 31.4, 32.2,
            33.1, 34, 35, 36.5, 38.3, 40.2, 42.2, 44.5, 46.5, 48.5, 50.5,
            52.2, 53.8, 54.9, 55.8, 56.9, 58.3, 60, 61.6, 63, 65, 66.6
    };


    private double J2000 = 2451545.0;
    private double JulianCentury = 36525.0;
    private double JulianMillennium = (JulianCentury * 10);

    private double equationOfTime(double jd)
    {
        double alpha, deltaPsi, E, epsilon, L0, tau;

        tau = (jd - J2000) / JulianMillennium;
        L0 = 280.4664567 + (360007.6982779 * tau) +
                (0.03032028 * tau * tau) +
                ((tau * tau * tau) / 49931) +
                (-((tau * tau * tau * tau) / 15300)) +
                (-((tau * tau * tau * tau * tau) / 2000000));
        L0 = fixangle(L0);
        alpha = sunpos(jd)[10];
        deltaPsi = nutation(jd)[0];
        epsilon = obliqeq(jd) + nutation(jd)[1];
        E = L0 + (-0.0057183) + (-alpha) + (deltaPsi * dcos(epsilon));
        E = E - 20.0 * (Math.floor(E / 20.0));
        E = E / (24 * 60);
        return E;
    }


    private double[] nutation(double jd)
    {
        double deltaPsi, deltaEpsilon;
        int i, j;
        double t = (jd - 2451545.0) / 36525.0, t2, t3, to10,
                dp = 0, de = 0, ang;
        double ta[] = new double[5];


        t3 = t * (t2 = t * t);

        ta[0] = dtr(297.850363 + 445267.11148 * t - 0.0019142 * t2 +
                t3 / 189474.0);
        ta[1] = dtr(357.52772 + 35999.05034 * t - 0.0001603 * t2 -
                t3 / 300000.0);
        ta[2] = dtr(134.96298 + 477198.867398 * t + 0.0086972 * t2 +
                t3 / 56250.0);
        ta[3] = dtr(93.27191 + 483202.017538 * t - 0.0036825 * t2 +
                t3 / 327270);
        ta[4] = dtr(125.04452 - 1934.136261 * t + 0.0020708 * t2 +
                t3 / 450000.0);

        for (i = 0; i < 5; i++)
        {
            ta[i] = fixangr(ta[i]);
        }

        to10 = t / 10.0;
        for (i = 0; i < 63; i++)
        {
            ang = 0;
            for (j = 0; j < 5; j++)
            {
                if (nutArgMult[(i * 5) + j] != 0)
                {
                    ang += nutArgMult[(i * 5) + j] * ta[j];
                }
            }
            dp += (nutArgCoeff[(i * 4) + 0] + nutArgCoeff[(i * 4) + 1] * to10) * Math.sin(ang);
            de += (nutArgCoeff[(i * 4) + 2] + nutArgCoeff[(i * 4) + 3] * to10) * Math.cos(ang);
        }

        deltaPsi = dp / (3600.0 * 10000.0);
        deltaEpsilon = de / (3600.0 * 10000.0);

        double[] returnValue = {deltaPsi, deltaEpsilon};
        return returnValue;
    }


    private double[] nutArgCoeff = {
            -171996, -1742, 92095, 89,
            -13187, -16, 5736, -31,
            -2274, -2, 977, -5,
            2062, 2, -895, 5,
            1426, -34, 54, -1,
            712, 1, -7, 0,
            -517, 12, 224, -6,
            -386, -4, 200, 0,
            -301, 0, 129, -1,
            217, -5, -95, 3,
            -158, 0, 0, 0,
            129, 1, -70, 0,
            123, 0, -53, 0,
            63, 0, 0, 0,
            63, 1, -33, 0,
            -59, 0, 26, 0,
            -58, -1, 32, 0,
            -51, 0, 27, 0,
            48, 0, 0, 0,
            46, 0, -24, 0,
            -38, 0, 16, 0,
            -31, 0, 13, 0,
            29, 0, 0, 0,
            29, 0, -12, 0,
            26, 0, 0, 0,
            -22, 0, 0, 0,
            21, 0, -10, 0,
            17, -1, 0, 0,
            16, 0, -8, 0,
            -16, 1, 7, 0,
            -15, 0, 9, 0,
            -13, 0, 7, 0,
            -12, 0, 6, 0,
            11, 0, 0, 0,
            -10, 0, 5, 0,
            -8, 0, 3, 0,
            7, 0, -3, 0,
            -7, 0, 0, 0,
            -7, 0, 3, 0,
            -7, 0, 3, 0,
            6, 0, 0, 0,
            6, 0, -3, 0,
            6, 0, -3, 0,
            -6, 0, 3, 0,
            -6, 0, 3, 0,
            5, 0, 0, 0,
            -5, 0, 3, 0,
            -5, 0, 3, 0,
            -5, 0, 3, 0,
            4, 0, 0, 0,
            4, 0, 0, 0,
            4, 0, 0, 0,
            -4, 0, 0, 0,
            -4, 0, 0, 0,
            -4, 0, 0, 0,
            3, 0, 0, 0,
            -3, 0, 0, 0,
            -3, 0, 0, 0,
            -3, 0, 0, 0,
            -3, 0, 0, 0,
            -3, 0, 0, 0,
            -3, 0, 0, 0,
            -3, 0, 0, 0
    };


    private double[] nutArgMult = {
            0, 0, 0, 0, 1,
            -2, 0, 0, 2, 2,
            0, 0, 0, 2, 2,
            0, 0, 0, 0, 2,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            -2, 1, 0, 2, 2,
            0, 0, 0, 2, 1,
            0, 0, 1, 2, 2,
            -2, -1, 0, 2, 2,
            -2, 0, 1, 0, 0,
            -2, 0, 0, 2, 1,
            0, 0, -1, 2, 2,
            2, 0, 0, 0, 0,
            0, 0, 1, 0, 1,
            2, 0, -1, 2, 2,
            0, 0, -1, 0, 1,
            0, 0, 1, 2, 1,
            -2, 0, 2, 0, 0,
            0, 0, -2, 2, 1,
            2, 0, 0, 2, 2,
            0, 0, 2, 2, 2,
            0, 0, 2, 0, 0,
            -2, 0, 1, 2, 2,
            0, 0, 0, 2, 0,
            -2, 0, 0, 2, 0,
            0, 0, -1, 2, 1,
            0, 2, 0, 0, 0,
            2, 0, -1, 0, 1,
            -2, 2, 0, 2, 2,
            0, 1, 0, 0, 1,
            -2, 0, 1, 0, 1,
            0, -1, 0, 0, 1,
            0, 0, 2, -2, 0,
            2, 0, -1, 2, 1,
            2, 0, 1, 2, 2,
            0, 1, 0, 2, 2,
            -2, 1, 1, 0, 0,
            0, -1, 0, 2, 2,
            2, 0, 0, 2, 1,
            2, 0, 1, 0, 0,
            -2, 0, 2, 2, 2,
            -2, 0, 1, 2, 1,
            2, 0, -2, 0, 1,
            2, 0, 0, 0, 1,
            0, -1, 1, 0, 0,
            -2, -1, 0, 2, 1,
            -2, 0, 0, 0, 1,
            0, 0, 2, 2, 1,
            -2, 0, 2, 0, 1,
            -2, 1, 0, 2, 1,
            0, 0, 1, -2, 0,
            -1, 0, 1, 0, 0,
            -2, 1, 0, 0, 0,
            1, 0, 0, 0, 0,
            0, 0, 1, 2, 0,
            -1, -1, 1, 0, 0,
            0, 1, 1, 0, 0,
            0, -1, 1, 2, 2,
            2, -1, -1, 2, 2,
            0, 0, -2, 2, 2,
            0, 0, 3, 2, 2,
            2, -1, 0, 2, 2
    };

    private double fixangr(double a)
    {
        return a - (2 * Math.PI) * (Math.floor(a / (2 * Math.PI)));
    }

    private double[] sunpos(double jd)
    {
        double T, T2, L0, M, e, C, sunLong, sunAnomaly, sunR,
                Omega, Lambda, epsilon, epsilon0, Alpha, Delta,
                AlphaApp, DeltaApp;

        T = (jd - J2000) / JulianCentury;
        T2 = T * T;
        L0 = 280.46646 + (36000.76983 * T) + (0.0003032 * T2);
        L0 = fixangle(L0);
        M = 357.52911 + (35999.05029 * T) + (-0.0001537 * T2);
        M = fixangle(M);
        e = 0.016708634 + (-0.000042037 * T) + (-0.0000001267 * T2);
        C = ((1.914602 + (-0.004817 * T) + (-0.000014 * T2)) * dsin(M)) +
                ((0.019993 - (0.000101 * T)) * dsin(2 * M)) +
                (0.000289 * dsin(3 * M));
        sunLong = L0 + C;
        sunAnomaly = M + C;
        sunR = (1.000001018 * (1 - (e * e))) / (1 + (e * dcos(sunAnomaly)));
        Omega = 125.04 - (1934.136 * T);
        Lambda = sunLong + (-0.00569) + (-0.00478 * dsin(Omega));
        epsilon0 = obliqeq(jd);
        epsilon = epsilon0 + (0.00256 * dcos(Omega));
        Alpha = rtd(Math.atan2(dcos(epsilon0) * dsin(sunLong), dcos(sunLong)));
        Alpha = fixangle(Alpha);
        Delta = rtd(Math.asin(dsin(epsilon0) * dsin(sunLong)));
        AlphaApp = rtd(Math.atan2(dcos(epsilon) * dsin(Lambda), dcos(Lambda)));
        AlphaApp = fixangle(AlphaApp);
        DeltaApp = rtd(Math.asin(dsin(epsilon) * dsin(Lambda)));

        double[] returnValue = {L0, M, e, C, sunLong, sunAnomaly, sunR, Lambda, Alpha, Delta, AlphaApp, DeltaApp};
        return returnValue;
    }

    private double dcos(double d)
    {
        return Math.cos(dtr(d));
    }

    private double dtr(double d)
    {
        return (d * Math.PI) / 180.0;
    }

    private double rtd(double r)
    {
        return (r * 180.0) / Math.PI;
    }

    private double dsin(double d)
    {
        return Math.sin(dtr(d));
    }

    private double obliqeq(double jd)
    {
        double eps, u, v;
        int i;

        v = u = (jd - J2000) / (JulianCentury * 100);

        eps = 23 + (26 / 60.0) + (21.448 / 3600.0);

        if (Math.abs(u) < 1.0)
        {
            for (i = 0; i < 10; i++)
            {
                eps += (oterms[i] / 3600.0) * v;
                v *= u;
            }
        }
        return eps;
    }

    private double[] oterms = {
            -4680.93,
            -1.55,
            1999.25,
            -51.38,
            -249.67,
            -39.05,
            7.12,
            27.87,
            5.79,
            2.45
    };

    private double fixangle(double a)
    {
        return a - 360.0 * (Math.floor(a / 360.0));
    }


    private double tehranEquinoxJd(double year)
    {
        double ep, epg;

        ep = tehranEquinox(year);
        epg = Math.floor(ep);

        return epg;
    }

    private  double PERSIAN_EPOCH = 1948320.5;
    private  double TropicalYear = 365.24219878;

    private double[] persianaYear(double jd)
    {
        double guess = jdToGregorian(jd)[0] - 2,
                lasteq, nexteq, adr;

        lasteq = tehranEquinoxJd(guess);
        while (lasteq > jd)
        {
            guess--;
            lasteq = tehranEquinoxJd(guess);
        }
        nexteq = lasteq - 1;
        while (!((lasteq <= jd) && (jd < nexteq)))
        {
            lasteq = nexteq;
            guess++;
            nexteq = tehranEquinoxJd(guess);
        }
        adr = Math.round((lasteq - PERSIAN_EPOCH) / TropicalYear) + 1;

        double[] returnValue = {adr, lasteq};
        return returnValue;
    }


    private double[] jdToPersiana(double jd)
    {
        double year, month, day, yday;
        double[] adr;
        jd = Math.floor(jd) + 0.5;
        adr = persianaYear(jd);
        year = adr[0];

        yday = (Math.floor(jd) - persianaToJd(year, 1, 1)) + 1;
        month = (yday <= 186) ? Math.ceil(yday / 31) : Math.ceil((yday - 6) / 30);
        day = (Math.floor(jd) - persianaToJd(year, month, 1)) + 1;

        double[] return_value = {year, month, day};
        return return_value;
    }

    private double persianaToJd(double year, double month, double day)
    {
        double equinox, guess, jd;
        double[] adr = {year - 1, 0};

        guess = (PERSIAN_EPOCH - 1) + (TropicalYear * ((year - 1) - 1));

        while (adr[0] < year)
        {
            adr = persianaYear(guess);
            guess = adr[1] + (TropicalYear + 2);
        }
        equinox = adr[1];

        jd = equinox +
                ((month <= 7) ?
                        ((month - 1) * 31) :
                        (((month - 1) * 30) + 6)
                ) +
                (day - 1);
        return jd;
    }

    /**
     * checks whether provided year is leap year or not.
     * <p>
     * This method should not be used by other classes or methods outside of this package.
     *
     * @param year positive jalali year
     * @return if provided year was leap it returns true
     */
    protected boolean leapPersiana(double year)
    {
        return (persianaToJd(year + 1, 1, 1) -
                persianaToJd(year, 1, 1)) > 365;
    }

    private boolean leapPersian(double year)
    {
        return ((((((year - ((year > 0) ? 474 : 473)) % 2820) + 474) + 38) * 682) % 2816) < 682;
    }

    private double persianToJd(double year, double month, double day)
    {
        double epbase, epyear;

        epbase = year - ((year >= 0) ? 474 : 473);
        epyear = 474 + mod(epbase, 2820);

        return day +
                ((month <= 7) ?
                        ((month - 1) * 31) :
                        (((month - 1) * 30) + 6)
                ) +
                Math.floor(((epyear * 682) - 110) / 2816) +
                (epyear - 1) * 365 +
                Math.floor(epbase / 2820) * 1029983 +
                (PERSIAN_EPOCH - 1);
    }


    private double[] jdToPersian(double jd)
    {
        double year, month, day, depoch, cycle, cyear, ycycle,
                aux1, aux2, yday;


        jd = Math.floor(jd) + 0.5;

        depoch = jd - persianToJd(475, 1, 1);
        cycle = Math.floor(depoch / 1029983);
        cyear = mod(depoch, 1029983);
        if (cyear == 1029982)
        {
            ycycle = 2820;
        } else
        {
            aux1 = Math.floor(cyear / 366);
            aux2 = mod(cyear, 366);
            ycycle = Math.floor(((2134 * aux1) + (2816 * aux2) + 2815) / 1028522) +
                    aux1 + 1;
        }
        year = ycycle + (2820 * cycle) + 474;
        if (year <= 0)
        {
            year--;
        }
        yday = (jd - persianToJd(year, 1, 1)) + 1;
        month = (yday <= 186) ? Math.ceil(yday / 31) : Math.ceil((yday - 6) / 30);
        day = (jd - persianToJd(year, month, 1)) + 1;
        double[] returnValue = {year, month, day};
        return returnValue;
    }

    double perscal[];

    private void updateFromGregorian(int gregorianYear, int gregorianMonth, int gregorianDay)
    {
        double j, year, mon, mday, weekday;

        double[] julcal;

        year = gregorianYearValue = gregorianYear;
        mon = gregorianMonthSelectedIndex = gregorianMonth;
        mday = gregorianDayValue + gregorianDay;

        j = gregorianToJd(year, mon + 1, mday);

        juliandayDayValue = j;

        weekday = jwday(j);
        persianaWeekDay = (int) weekday;

        perscal = jdToPersian(j);

        perscal = jdToPersiana(j);
        persianaYearValue = perscal[0];
        persianaMonthSelectedIndex = perscal[1] - 1;
        persianaDayValue = perscal[2];
    }

    private void calcJulian()
    {
        double j;
        double[] date;

        j = juliandayDayValue;
        date = jdToGregorian(j);
        gregorianYearValue = date[0];
        gregorianMonthSelectedIndex = date[1] - 1;
        gregorianDayValue = date[2];

    }

    private void setJulian(double j)
    {
        juliandayDayValue = j;
        calcJulian();
    }
}
