package ems.view;

import java.util.ArrayList;

public class Displayer {
    protected static final int displayWidth = 150;

    private static void displayHeader(final String header,
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

    public static void displayHeader(final String header){
        displayHeader(header, '╔', '╗', '═','║', '╚','╝');
    }

    public static void displaySubheader(final String header){
        displayHeader(header, '+', '+', '-','|', '+', '+');
    }

    public static void showMenu(final String initialPrompt, final ArrayList<String> options){
        System.out.println(initialPrompt);

        for(int i = 0; i< options.size(); i++){
            System.out.println("\t[" + (i+1) + "] " + options.get(i));
        }
    }

    public static void showPrompt(final String prompt){
        System.out.print(prompt);
    }

    public static void displayTable(final String tableName,
                                    final ArrayList<String> columnHeaders,
                                    final ArrayList<ArrayList<String>> records,
                                    final ArrayList<Double> columnSizes)
    {
        final int tableWidth = displayWidth - columnHeaders.size();

        final ArrayList<Integer> columnWidths = new ArrayList<>();
        int totalWidth = 0;
        for(int i = 0; i < columnSizes.size(); i++){
            if(i == columnSizes.size() - 1){
                columnWidths.add(tableWidth - totalWidth);
                break;
            }

            int colWidth = (int) (tableWidth * columnSizes.get(i));
            totalWidth+=colWidth;
            columnWidths.add(colWidth);
        }

        displaySubheader(tableName);
        System.out.print("|");
        for(int i = 0; i < columnHeaders.size(); i++){
            int totalPadding = columnWidths.get(i) - columnHeaders.get(i).length();
            if(i == 0) totalPadding--;
            int paddingStart = totalPadding / 2;
            int paddingEnd = totalPadding - paddingStart;

            String centered = " ".repeat(paddingStart) + columnHeaders.get(i) + " ".repeat(paddingEnd);
            System.out.print(centered + "|");
        }
        System.out.println("\n" +"-".repeat(displayWidth));

        for(ArrayList<String> record : records){
            System.out.print("|");
            for(int i = 0; i < columnHeaders.size(); i++){
                int totalPadding = columnWidths.get(i) - record.get(i).length();
                if(i == 0) totalPadding--;
                int paddingStart = totalPadding / 2;
                int paddingEnd = totalPadding - paddingStart;

                String centered = " ".repeat(paddingStart) + record.get(i) + " ".repeat(paddingEnd);
                System.out.print(centered + "|");
            }
            System.out.println("\n" +"-".repeat(displayWidth));
        }

        System.out.println();
    }
}