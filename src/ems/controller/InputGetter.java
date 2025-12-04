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
    private static final Scanner scanner = new Scanner(System.in);

    public static int getNumberOption(int highest, boolean allowBlank){
        while(true){
            System.out.print("Enter the number of your option: ");
            String user_input = scanner.nextLine().trim();

            if(allowBlank && user_input.isBlank()) {
                return -1;
            }

            int num;
            try{
                num = Integer.parseInt(user_input);

                if(num < 1 || num > highest){
                    throw new Exception(String.format("Input invalid! Only positive integers between 1 and %d are allowed", highest));
                }
            }catch (Exception e){
                Displayer.show_error(e);
                continue;
            }
            System.out.println();
            return num;
        }
    }

    public static int getNumberOption(final int highest){
        return getNumberOption(highest, false);
    }

    public static String getLine(String prompt, boolean allowBlank){
        while (true){
            System.out.print(prompt);
            String user_input = scanner.nextLine().trim();

            if(!user_input.isBlank()){
                return user_input;
            }else{
                if(allowBlank){
                    return " ";
                }
            }
        }
    }

    public static String getLine(String prompt){
        return getLine(prompt,false);
    }

    public static LocalDate getDate(String prompt, boolean allowBlank){
        DateTimeFormatter shortDateFormat = DateTimeFormatter.ofPattern("MMM d, uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        DateTimeFormatter longDateFormat = DateTimeFormatter.ofPattern("MMMM d, uuuu")
                .withResolverStyle(ResolverStyle.STRICT);


        while (true) {
            Displayer.showPrompt(prompt + " (Ex. January 1, 2001 or Jan 1, 2001): ");
            String date = scanner.nextLine().trim();

            if (date.isBlank()) {
                if (allowBlank) return null;
                else System.out.println("Input can't be blank!");
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

    public static LocalDate getDate(String prompt){
        return getDate(prompt,false);
    }

    public static LocalTime getTime(String prompt, boolean allowBlank){
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h[:mm] a", Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT);

        while (true) {
            Displayer.showPrompt(prompt + " (Ex. 7:00 am or 7 am): ");
            String time = scanner.nextLine().trim().toUpperCase().replace(".", "");

            if (time.isBlank()) {
                if (allowBlank) return null;
                else System.out.println("Input can't be blank!");
            } else {
                try {
                    return LocalTime.parse(time, timeFormat);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid time, please try again.\n");
                }
            }
        }
    }

    public static LocalTime getTime(String prompt){
        return getTime(prompt,false);
    }
}
