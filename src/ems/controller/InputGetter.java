package ems.controller;

import ems.view.Displayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Scanner;

public class InputGetter {
    private final Scanner scanner = new Scanner(System.in);
    private final Displayer displayer = new Displayer();

    public int getNumberOption(final int highest, boolean allowBlank){
        while (true){
            try{
                displayer.showPrompt("Enter the number of your option: ");
                String input = scanner.nextLine();
                System.out.println();

                if(allowBlank && input.isBlank()){
                    return -1;
                }

                int num = Integer.parseInt(input);
                if(num < 1 || num > highest){
                    throw new NumberFormatException();
                }
                return num;
            }catch (NumberFormatException e){
                System.out.println("Invalid input! Valid inputs are whole numbers including and between 1 and " + highest + "\n");
            }
        }
    }

    public int getNumberOption(final int highest){
        return getNumberOption(highest, false);
    }

    public String getLine(String prompt, boolean allowBlank) {
        while (true) {
            displayer.showPrompt(prompt);
            String text = scanner.nextLine();
            if (text.isBlank()) {
                if (allowBlank) return "";
            } else {
                return text;
            }
        }
    }

    public String getLine(String prompt){
        return getLine(prompt,false);
    }

    public LocalDate getDate(String prompt, boolean allowBlank){
        DateTimeFormatter shortDateFormat = DateTimeFormatter.ofPattern("MMM d, uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        DateTimeFormatter longDateFormat = DateTimeFormatter.ofPattern("MMMM d, uuuu")
                .withResolverStyle(ResolverStyle.STRICT);


        while (true) {
            displayer.showPrompt(prompt + " (Ex. January 1, 2001 or Jan 1, 2001): ");
            String date = scanner.nextLine();

            if (date.isBlank()) {
                if (allowBlank) return null;
            }
            try {
                return LocalDate.parse(date, shortDateFormat);
            } catch (DateTimeParseException e) {
                try {
                    return LocalDate.parse(date, longDateFormat);
                }catch (DateTimeParseException err){
                    System.out.println("Invalid date, please try again.");
                }
            }
        }
    }

    public LocalDate getDate(String prompt){
        return getDate(prompt,false);
    }

    public LocalTime getTime(String prompt, boolean allowBlank){
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h[:mm] a", Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT);

        while (true) {
            displayer.showPrompt(prompt + " (Ex. 7:00 am or 7 am): ");
            String time = scanner.nextLine().toUpperCase().replace(".", "");

            if (time.isBlank()) {
                if (allowBlank) return null;
            } else {
                try {
                    return LocalTime.parse(time, timeFormat);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid time, please try again.\n");
                }
            }
        }
    }

    public LocalTime getTime(String prompt){
        return getTime(prompt,false);
    }
}
