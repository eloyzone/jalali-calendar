[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.eloyzone/jalali-calendar/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.eloyzone/jalali-calendar)

# jalali-calendar
A java Library that converts Gregorian to Jalali (or Jalali to Gregorian)
For Gregorian date, it uses Java8's date and for Jalali date has its own codes which are inspired from Java8.


# How can I use jalali-calendar?
You can download source code directly to your existing projects and then use it or you can download it from maven or other repositories.

## Maven
### How to Include In Maven Project
```xml
<dependency>
    <groupId>com.github.eloyzone</groupId>
    <artifactId>jalali-calendar</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Some Basic Codes:
```java

// Create an object of DateConverter, its the main class that converts calendars
DateConverter dateConverter = new DateConverter();

// Convert Jalali date to Gregorian
LocalDate localdate1 = dateConverter.jalaliToGregorian(1370, 11, 28);
LocalDate localdate2 = dateConverter.jalaliToGregorian(1386, MonthPersian.ESFAND, 29);

// Convert Gregorian date to Jalali
JalaliDate jalaliDate1 = dateConverter.gregorianToJalali(1992, 2, 17);
JalaliDate jalaliDate2 = dateConverter.gregorianToJalali(2019, 3, 20);

// checking for leapyer of Jalali Date
boolean leapyer1 = new JalaliDate(1370, 11, 28).isLeapYear());
boolean leapyer2 = dateConverter.gregorianToJalali(1992, 2, 17).isLeapYear();

// Day of week
String dayOfWeek1 = new JalaliDate(1370, 11, 28).getDayOfWeek().getStringInPersian(); // Doshanbe
String dayOfWeek2 = new JalaliDate(1370, 11, 28).getDayOfWeek().getStringInEnglish(); // دوشنبه
```

## Date Formatter:
For Gregorian date no formatter is provided by this lib as Java8 has got a good formatter but for JalaliDate You can use `JalaliDateFormatter` class like below codes
The Jalai's formatter not only provide English but also Persian. Its default form is English.

```java
DateConverter dateConverter = new DateConverter();
JalaliDate jalaliDate = dateConverter.gregorianToJalali(1992, Month.FEBRUARY, 17);
String result = jalaliDate.format(new JalaliDateFormatter("yyyy/mm/dd", JalaliDateFormatter.FORMAT_IN_PERSIAN);
// result will be: ١٣٧٠/١١/٢٨

String result2 = jalaliDate.format(new JalaliDateFormatter("yyyy- M dd", JalaliDateFormatter.FORMAT_IN_PERSIAN);
// result2 will be: ٢٨ بهمن -١٣٧٠
```

For more options it's highly recomended to see unit tests of this library, it's covered most possible options and can be good guide for you.

