package ems.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Scanner;

public class InputGetter {
    private final Scanner scanner = new Scanner(System.in);

    public int getInt(final int lowest, final int highest){
        int num;

        try{
            num = Integer.parseInt(scanner.nextLine());
            if(num < lowest || num > highest){
                throw new NumberFormatException();
            }
            return num;
        }catch (NumberFormatException e){
            System.out.println("Invalid input! Valid inputs are whole numbers including and between " + lowest + " and " + highest + "\n");
            return -1;
        }
    }

    public int getPositiveInt(final int highest){
        return getInt(1, highest);
    }

    public String getLine(boolean allowBlank){
        String text;
        while(true){
            text = scanner.nextLine();

            if(!allowBlank){
                if(!text.isBlank()) return text;
            }else{
                return text;
            }
        }
    }

    public String getLine(){
        return getLine(false);
    }

    public String getDate(boolean allowBlank){
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MMMM dd, uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        while (true) {
            String input = scanner.nextLine();

            if(allowBlank) return input;

            try {
                return LocalDate.parse(input, inputFormat).toString();
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date, please try again.");
            }
        }
    }

    public String getDate(){
        return getDate(false);
    }

    public String getTime(boolean allowBlank){
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT);

        while (true) {
            String input = scanner.nextLine().toUpperCase().replace(".", "");

            if(allowBlank) return input;

            try {
                return LocalTime.parse(input, timeFormat).toString();
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format, please try again.");
            }
        }
    }

    public String getTime(){
        return getTime(false);
    }
}
