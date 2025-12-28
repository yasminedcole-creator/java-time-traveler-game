public class Location {
    String name;
    String description;
    Location[] exits;

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new Location[0];
    }
}


