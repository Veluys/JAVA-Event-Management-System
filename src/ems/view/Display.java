package ems.view;

public class Display {
    private final int displayWidth = 120;

    void displayHeader(final String message){
        final int padding = (displayWidth - message.length()) / 2;
        final String centered = " ".repeat(padding) + message;
        final String border = "+" + "-".repeat(displayWidth - 2) + "+";

        System.out.println(border);
        System.out.println(centered);
        System.out.println(border);
    }

    void showMenu(final String initialPrompt, final String[] options){
        System.out.println(initialPrompt);

        for(int i = 0; i< options.length; i++){
            System.out.println("\t[" + (i+1) + "] " + options[i]);
        }
    }

    void showPrompt(final String prompt){
        System.out.print(prompt);
    }

    void numberedMenuPrompt(){
        showPrompt("Enter the number of your option: ");
    }
}
