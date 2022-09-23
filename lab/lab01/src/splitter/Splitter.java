package splitter;

import java.util.Scanner;

public class Splitter {

    public static void main(String[] args) {
        System.out.println("Enter a sentence specified by spaces only: ");
        // Add your code
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        String[] words = input.split(" ");
        for (int i = 0; i < words.length; i++) {
            System.out.println(words[i]);
        }
        scan.close();
    }
}
