package ems.view;

import java.util.ArrayList;

public class Displayer {
    protected final int displayWidth = 120;

    public void displayHeader(final String message){
        final int padding = (displayWidth - message.length()) / 2;
        final String centered = " ".repeat(padding) + message;
        final String border = "+" + "-".repeat(displayWidth - 2) + "+";

        System.out.println(border);
        System.out.println(centered);
        System.out.println(border);
    }

    public void showMenu(final String initialPrompt, final String[] options){
        System.out.println(initialPrompt);

        for(int i = 0; i< options.length; i++){
            System.out.println("\t[" + (i+1) + "] " + options[i]);
        }
    }

    public void showPrompt(final String prompt){
        System.out.print(prompt);
    }

    public void numberedMenuPrompt(){
        showPrompt("Enter the number of your option: ");
    }

    public void centerAlignRow(final ArrayList<String> columnValues) {
        final int tableWidth = displayWidth - 4;
        final int columnWidth = tableWidth / 4;

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

    public void rightAlignRecord(final ArrayList<String> columnValues){
        final int labelWidth = 15;
        String attributeLabel = columnValues.get(0);
        String attributeValue = columnValues.get(1);

        int paddingRight = labelWidth - attributeLabel.length();

        System.out.println(attributeLabel + ":" + " ".repeat(paddingRight) + attributeValue);
    }
}
