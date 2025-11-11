package ems.view;

public class EntityDisplayer extends Displayer{
    void centerAlignRow(String[] columnValues) {
        final int tableWidth = displayWidth - 4;
        final int columnWidth = tableWidth / 4;

        for (int i = 0; i < columnValues.length; i++) {
            String columnValue = columnValues[i];
            int totalPadding = columnWidth - columnValue.length();
            int paddingStart = totalPadding / 2;
            int paddingEnd = totalPadding - paddingStart;

            String centered = " ".repeat(paddingStart) + columnValue + " ".repeat(paddingEnd);
            System.out.print(centered + "|");
        }
        System.out.println();
        System.out.println("-".repeat(120));
    }

    void rightAlignRecord(String[] columnValues){
        final int labelWidth = 15;
        String attributeLabel = columnValues[0];
        String attributeValue = columnValues[1];

        int paddingRight = labelWidth - attributeLabel.length();

        System.out.println(attributeLabel + ":" + " ".repeat(paddingRight) + attributeValue);
    }
}
