import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    public static int DISPLAY_WIDTH = 100;
    public static String lineBreak = "";

    private List<String> verbs = new ArrayList<>();
    private List<String> verbDescription = new ArrayList<>();
    private List<String> nouns = new ArrayList<>();

    public GameManager() {
        createHorizontalRule();
    }

    public boolean validCommand(String[] command) {
        if (command.length < 1 || command.length > 3) {
            print("Unknown command. Type '?' for help.");
            return false;
        }

        if (command[0].equalsIgnoreCase("quit")) {
            return true; // Allow the command to be processed
        }
        
        if (!verbs.contains(command[0])) {
            reportUnknownCommand(command[0]);
            return false;
        }

        for (int i = 1; i < command.length; i++) {
            if (!nouns.contains(command[i])) {
                reportUnknownCommand(command[i]);
                return false;
            }
        }
        return true;
    }

    public void addNoun(String string) {
        if (!nouns.contains(string.toLowerCase())) {
            nouns.add(string.toLowerCase());
        }
    }

    public void addVerb(String verb, String description) {
        verbs.add(verb.toLowerCase());
        verbDescription.add(description);
    }

    public void printHelp() {
        System.out.printf("\n\n" + lineBreak + "\n%" + ((DISPLAY_WIDTH / 2) + 4) + "s\n" + lineBreak + "\n", " Commands ");

        for (String verb : verbs) {
            System.out.printf("%-8s  %s", verb, formatHelpMenuDescriptionString(verbDescription.get(verbs.indexOf(verb))));
        }
    }

    public void print(String longString) {
        print(longString, true, true);
    }

    public void print(String longString, boolean addBrackets, boolean formatWidth) {
        if (addBrackets) {
            longString = addNounBrackets(longString);
        }
        if (formatWidth) {
            longString = formatWidth(longString);
        }
        System.out.println(longString);
    }

    private static void createHorizontalRule() {
        int count = 0;
        while (count++ < DISPLAY_WIDTH) {
            lineBreak += "-";
        }
    }

    private void reportUnknownCommand(String input) {
        print("I don't understand '" + input + "'. Type '?' for help.");
    }

    private String addNounBrackets(String longString) {
        String[] lines = longString.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String[] words = line.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                String word = words[i].replaceAll("\\p{Punct}+$", "");
                String punct = words[i].substring(word.length());
                for (String noun : nouns) {
                    if (word.equalsIgnoreCase(noun)) {
                        words[i] = "[" + word + "]" + punct;
                        break;
                    }
                }
            }
            result.append(String.join(" ", words)).append("\n");
        }
        return result.toString();
    }

    private String formatWidth(String longString) {
        String[] lines = longString.split("\n");
        StringBuilder result = new StringBuilder();
        int charLength = 0;

        for (String line : lines) {
            String[] words = line.split("\\s+");
            for (String word : words) {
                if (charLength + word.length() > DISPLAY_WIDTH - 6) {
                    result.append("\n");
                    charLength = 0;
                }
                result.append(word).append(" ");
                charLength += word.length() + 1;
            }
            result.append("\n");
            charLength = 0;
        }
        return result.toString();
    }

    private String formatHelpMenuDescriptionString(String longString) {
        StringBuilder result = new StringBuilder();
        String[] words = longString.split(" ");
        int charLength = 0;

        for (String word : words) {
            charLength += word.length();
            result.append(word).append(" ");
            if (charLength >= DISPLAY_WIDTH - 35) {
                result.append("\n          ");
                charLength = 0;
            }
        }
        return result.append("\n\n").toString();
    }
}
