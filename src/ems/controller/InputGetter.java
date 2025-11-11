package ems.controller;

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
        while(true){
            String text = scanner.nextLine();

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

}
