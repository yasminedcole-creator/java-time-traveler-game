import java.util.List;

public class CommandSystem {
    private GameState state;
    private GameManager game;

    public CommandSystem(GameState state, GameManager game) {
        this.state = state;
        this.game = game;

        game.addVerb("?", "Show this help screen. This gives you any commands you need.");
        game.addVerb("look", "Look around your current location.");
        game.addVerb("inventory", "View your inventory.");
        game.addVerb("talk", "Talk to a character in the location. Use: [talk character]");
        game.addVerb("move", "Move to another location. Use: [move location]");
        game.addVerb("give", "Give an object to a character.Use: [give item character]");
        game.addVerb("observe", "Observe an entity or item for more details. Use: [observe entity/item]");
        game.addVerb("play", "Play a game with an entity after being challenged.Use: [play entity]");
        game.addVerb("show", "Show an item to a character.Use: [show item character]");

    }

    public void processCommand(String[] command) {
        switch (command.length) {
            case 1:
                executeVerb(command[0]);
                break;
            case 2:
                executeVerbNoun(command[0], command[1]);
                break;
            case 3:
                executeVerbNounNoun(command[0], command[1], command[2]);
                break;
            default:
                game.print("Invalid command. Type '?' for help.");
        }
    }

    private void executeVerb(String verb) {
        switch (verb) {
            case "?":
                game.printHelp();
                break;
            case "look":
                game.print(state.currentLocation.description);
                break;
            case "inventory":
                game.print("Your inventory: " + state.inventory);
                break;
            case "quit":
                game.print("You have chosen to quit the game. Goodbye!");
                System.exit(0); // Terminate the application immediately
                break;
            default:
                game.print("Unknown command. Type '?' for help.");
        }
    }
    

    private void executeVerbNoun(String verb, String noun) {
        switch (verb) {
            case "move":
                moveToLocation(noun);
                break;
            case "talk":
                talkToEntity(noun);
                break;
            case "observe":
                observeTarget(noun);
                break;
            case "play":
                playGame(noun);
                break;
            default:
                game.print("Unknown command. Type '?' for help.");
        }
    }

    private void executeVerbNounNoun(String verb, String object1, String object2) {
        switch (verb) {
            case "give":
                giveItemToEntity(object1, object2);
                break;
            case "show":
                showItemToEntity(object1, object2);
                break;
            default:
                game.print("Unknown complex command. Type '?' for help.");
        }
    }
    private void showItemToEntity(String item, String entity) {
        // Validate the item is in the inventory
        if (!state.inventory.stream().anyMatch(i -> i.equalsIgnoreCase(item))) {
            game.print("You don't have " + item + " in your inventory.");
            return;
        }
    
        // Check if the entity is the Boss
        if (entity.equalsIgnoreCase("boss") && state.currentLocation == state.hub) {
            if (item.equalsIgnoreCase("identification")) {
                if (!state.bossHelpGiven) {
                    game.print("You show your identification to the Boss.");
                    game.print("The Boss says: 'I recognize you as the Time Traveler. Here are the tools you'll need. A leaf, Sword and Battery'");
                    // Add items to inventory
                    state.inventory.remove("identification");
                    state.inventory.add("leaf");
                    state.inventory.add("sword");
                    state.inventory.add("battery");
                    state.bossHelpGiven = true;
                } else {
                    game.print("The Boss has already given you the items.");
                }
            } else {
                game.print("The Boss has no interest in only pieces" + item + ".");
            }
        } else {
            game.print("You can't show " + item + " to " + entity + ".");
        }
    }
    

    private void moveToLocation(String locationName) {
        for (Location exit : state.currentLocation.exits) {
            if (exit.name.equalsIgnoreCase(locationName)) {
                state.currentLocation = exit;
                game.print("You moved to " + locationName + ".");
                game.print(state.currentLocation.description);
                return;
            }
        }
        game.print("You can't move to " + locationName + " from here.");
    }

    private void talkToEntity(String entity) {
        switch (entity.toLowerCase()) {
            case "boss":
                if (state.currentLocation == state.hub) {
                    game.print("The Boss says: 'Collect the artifacts and return them to me to save the timeline.'");
                } else {
                    game.print("The Boss is not here.");
                }
                break;
            case "chieftain":
                if (state.currentLocation == state.cave) {
                    if (!state.chieftainHelpGiven) {
                        game.print("The Chieftain says: 'Bring me a sacred Leaf and we shall talk.'");
                    } else {
                        game.print("The Chieftain challenges you to a riddle game.You should play the game with the chieftain!");
                    }
                } else {
                    game.print("The Chieftain is not here.");
                }
                break;
            case "robot":
                if (state.currentLocation == state.lab) {
                    if (!state.robotHelpGiven) {
                        game.print("The Robot says: 'I require a Battery to function. Bring it to me.'");
                    } else {
                        game.print("The Robot challenges you to a guessing game. You should play the game with robot!");
                    }
                } else {
                    game.print("The Robot is not here.");
                }
                break;
            case "knight":
                if (state.currentLocation == state.castle) {
                    if (!state.knightHelpGiven) {
                        game.print("The Knight says: 'A warrior must prove their worth. Bring me a Sword.'");
                    } else {
                        game.print("The Knight invites you to a game of Stone, Leaf, Knife. You should play the knight!");
                    }
                } else {
                    game.print("The Knight is not here.");
                }
                break;
            default:
                game.print("You can't talk to " + entity + ".");
        }
    }

    private void giveItemToEntity(String item, String entity) {
        // Validate if the item is in the inventory
        if (!state.inventory.stream().anyMatch(i -> i.equalsIgnoreCase(item))) {
            game.print("You don't have " + item + " in your inventory.");
            return;
        }
    
        // Prevent giving "identification" to the Boss
        if (item.equalsIgnoreCase("identification")) {
            if (entity.equalsIgnoreCase("boss")) {
                game.print("The Boss says: 'I don't need your identification. Just show it too me.");
            } else {
                game.print("You can't give " + item + " to " + entity + ".");
            }
            return; // Block further execution
        }
    
        // Handle giving other items
        if (entity.equalsIgnoreCase("boss") && state.currentLocation == state.hub) {
            if (item.toLowerCase().startsWith("piece")) {
                state.inventory.remove(item); // Remove the piece from inventory
                state.piecesGivenToBoss++;
                game.print("You gave " + item + " to the Boss. Pieces returned: " + state.piecesGivenToBoss + "/3.");
                if (state.piecesGivenToBoss == 3) {
                    game.print("The Boss says: 'Thank you for returning all the pieces"+ state.playerName + "!" );
                }
            } else {
                game.print("The Boss doesn't need " + item + ".");
            }
        } else if (entity.equalsIgnoreCase("chieftain") && state.currentLocation == state.cave) {
            if (item.equalsIgnoreCase("leaf")) {
                game.print("You gave the Chieftain the Leaf. The Chieftain sees you as a friend now you guys can talk");
                state.inventory.remove("leaf");
                state.chieftainHelpGiven = true;
            } else {
                game.print("The Chieftain doesn't need " + item + ".");
            }
        } else if (entity.equalsIgnoreCase("robot") && state.currentLocation == state.lab) {
            if (item.equalsIgnoreCase("battery")) {
                game.print("You gave the Robot the Battery. The robot sees you as a friend now you guys can talk.");
                state.inventory.remove("battery");
                state.robotHelpGiven = true;
            } else {
                game.print("The Robot doesn't need " + item + ".");
            }
        } else if (entity.equalsIgnoreCase("knight") && state.currentLocation == state.castle) {
            if (item.equalsIgnoreCase("sword")) {
                game.print("You gave the Knight the Sword. The Knight sees you as an ally now you guys can talk");
                state.inventory.remove("sword");
                state.knightHelpGiven = true;
            } else {
                game.print("The Knight doesn't need " + item + ".");
            }
        } else {
            game.print("You can't give " + item + " to " + entity + " here.");
        }
    }
    
    
    private void observeTarget(String target) {
        if (state.inventory.stream().anyMatch(i -> i.equalsIgnoreCase(target))) {
            game.print(target + ": " + state.getItemDescription(target));
        } else if (target.equalsIgnoreCase("boss") && state.currentLocation == state.hub) {
            game.print("Boss: " + state.getEntityDescription("boss"));
        } else if (target.equalsIgnoreCase("chieftain") && state.currentLocation == state.cave) {
            game.print("Chieftain: " + state.getEntityDescription("chieftain"));
        } else if (target.equalsIgnoreCase("robot") && state.currentLocation == state.lab) {
            game.print("Robot: " + state.getEntityDescription("robot"));
        } else if (target.equalsIgnoreCase("knight") && state.currentLocation == state.castle) {
            game.print("Knight: " + state.getEntityDescription("knight"));
        } else {
            game.print("You don't have " + target + " in your inventory, and it's not here.");
        }
    }

    private void playGame(String entity) {
        if (entity.equalsIgnoreCase("robot") && state.currentLocation == state.lab) {
            if (!state.robotHelpGiven) {
                game.print("You must first give the Robot the Battery to power it up.");
            } else if (!state.inventory.contains("pieceb")) {
                game.print("The Robot challenges you to a number-guessing game!");
                if (!numberGuessChallenge()) {
                    endGame("You lost the game with the Robot. Game Over!");
                } else {
                    state.inventory.add("PieceB");
                    game.print("You won the game and collected PieceB!");
                }
            }
        } else if (entity.equalsIgnoreCase("chieftain") && state.currentLocation == state.cave) {
            if (!state.chieftainHelpGiven) {
                game.print("You must first give the Chieftain the Leaf.");
            } else if (!state.inventory.contains("piecea")) {
                game.print("The Chieftain challenges you with a riddle!");
                if (!riddleChallenge()) {
                    endGame("You failed the Chieftain's challenge. Game Over!");
                } else {
                    state.inventory.add("PieceA");
                    game.print("You solved the riddle and collected PieceA!");
                }
            }
        } else if (entity.equalsIgnoreCase("knight") && state.currentLocation == state.castle) {
            if (!state.knightHelpGiven) {
                game.print("You must first give the Knight the Sword.");
            } else if (!state.inventory.contains("piecec")) {
                game.print("The Knight challenges you to a game of Stone, Leaf, Knife!");
                while (true) {
                    int result = stoneLeafKnifeGame();
                    if (result == 1) {
                        state.inventory.add("PieceC");
                        game.print("You won the game and collected PieceC!");
                        break;
                    } else if (result == -1) {
                        endGame("You lost the game with the Knight. Game Over!");
                        break;
                    } else {
                        game.print("It's a tie! Try again.");
                    }
                }
            }
        } else {
            game.print("You can't play a game with " + entity + ".");
        }
    }

    private boolean riddleChallenge() {
        game.print("I speak without a mouth and hear without ears. I have no body, but I come alive with the wind. What am I?");
        String answer = App.in.nextLine().trim().toLowerCase();
        return answer.equals("echo");
    }

    private boolean numberGuessChallenge() {
        int number = (int) (Math.random() * 10) + 1;
        game.print("Guess a number between 1 and 10.");
        int guess = App.in.nextInt();
        return guess == number;
    }

    private int stoneLeafKnifeGame() {
        String[] options = {"stone", "leaf", "knife"};
        String knightChoice = options[(int) (Math.random() * 3)];

        game.print("Choose: stone, leaf, or knife.");
        String playerChoice = App.in.nextLine().toLowerCase();

        game.print("You chose: " + playerChoice);
        game.print("The Knight chose: " + knightChoice);

        if (playerChoice.equals(knightChoice)) {
            return 0; // Tie
        } else if ((playerChoice.equals("stone") && knightChoice.equals("knife")) ||
                   (playerChoice.equals("leaf") && knightChoice.equals("stone")) ||
                   (playerChoice.equals("knife") && knightChoice.equals("leaf"))) {
            return 1; // Win
        } else {
            return -1; // Loss
        }
    }

    private void endGame(String message) {
        game.print(message);
        System.exit(0);
    }
}
