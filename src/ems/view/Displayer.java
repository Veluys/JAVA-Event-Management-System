package ems.view;

import java.util.ArrayList;

public class Displayer {
    protected final int displayWidth = 120;

    private void displayHeader(final String header,
                               final char upperLeftChar,
                               final char upperRightChar,
                               final char middleChar,
                               final char centerChar,
                               final char lowerLeftChar,
                               final char lowerRightChar)
    {
        final int leftPadding = (displayWidth - header.length() - 2) / 2;
        final int rightPadding = displayWidth - leftPadding - header.length() - 2;
        final String centered = centerChar + " ".repeat(leftPadding) + header +
                " ".repeat(rightPadding) + centerChar;
        final String upperBorder = upperLeftChar + String.valueOf(middleChar).repeat(displayWidth - 2) +
                upperRightChar;
        final String lowerBorder = lowerLeftChar + String.valueOf(middleChar).repeat(displayWidth - 2) +
                lowerRightChar;

        System.out.println(upperBorder);
        System.out.println(centered);
        System.out.println(lowerBorder);
    }

    public void displayHeader(final String header){
        displayHeader(header, '╔', '╗', '═','║', '╚','╝');
    }

    public void displaySubheader(final String header){
        displayHeader(header, '+', '+', '-','|', '+', '+');
    }

    public void showMenu(final String initialPrompt, final ArrayList<String> options){
        System.out.println(initialPrompt);

        for(int i = 0; i< options.size(); i++){
            System.out.println("\t[" + (i+1) + "] " + options.get(i));
        }
    }

    public void showPrompt(final String prompt){
        System.out.print(prompt);
    }

    public void centerAlignRow(final ArrayList<String> columnValues) {
        final int tableWidth = displayWidth - columnValues.size();
        final int columnWidth = tableWidth / columnValues.size();

        for (int i = 0; i < columnValues.size(); i++) {
            String columnValue = columnValues.get(i);
            int totalPadding = columnWidth - columnValue.length();
            int paddingStart = totalPadding / 2;
            int paddingEnd = totalPadding - paddingStart;

            String centered = " ".repeat(paddingStart) + columnValue + " ".repeat(paddingEnd);
            System.out.print(centered + "|");
        }
        System.out.println();
        System.out.println("-".repeat(120));
    }
}
