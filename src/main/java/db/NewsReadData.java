package db;

public class NewsReadData {
    private String username;
    private String genre;
    private int readCount;

    public NewsReadData(String username, String genre, int readCount) {
        this.username = username;
        this.genre = genre;
        this.readCount = readCount;
    }

    public String getUsername() { return username; }
    public String getGenre() { return genre; }
    public int getReadCount() { return readCount; }
}

