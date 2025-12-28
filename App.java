import java.util.*;
public class App {
static Scanner in = new Scanner(System.in);
static GameState state;
static GameManager game;
static CommandSystem commandSystem;
public static void main(String[] args) throws Exception {
game = new GameManager();
System.out.println("Welcome to the Time Traveler Game!");
System.out.print("Please enter your name: ");
String playerName = in.nextLine().trim();
if (playerName.isEmpty()) {
playerName = "Traveler"; 
}
state = new GameState(game, playerName);
commandSystem = new CommandSystem(state, game);
System.out.println("Hello, " + state.playerName + "! Your mission is to collect PieceA, PieceB, and PieceC by exploring locations and 
interacting with characters.");
System.out.println("Type '?' to see available commands.\n");
boolean gameRunning = true;
while (gameRunning) {
String[] command = getCommand();
if (command[0].equalsIgnoreCase("quit")) {
System.out.println("Thank you for playing the Time Traveler Game, " + state.playerName + "!");
gameRunning = false; 
break;
}
if (game.validCommand(command)) {
commandSystem.processCommand(command);
}
if (state.bossHelpGiven && state.piecesGivenToBoss == 3) {
System.out.println("Congratulations, " + state.playerName + "! You successfully returned all pieces to the Boss and saved the 
timeline!");
gameRunning = false; 
}
}
}
public static String[] getCommand() {
game.print(GameManager.lineBreak, false, false);
System.out.print("What is your next move, time traveler?\n> ");
String input = in.nextLine();
game.print("");
return input.toLowerCase().split("\\s+");
}
}
