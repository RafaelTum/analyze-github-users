package com.example.analizegithubusers.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParser {

    public static LocalDate parse(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date.substring(0,10), inputFormatter);
    }
}
