package db;

public class NewsRecords {

    private int id;

    private String link, genre, summary;

    public NewsRecords(int id, String genre, String link, String summary) {
        this.id = id;
        this.genre = genre;
        this.link = link;
        this.summary = summary;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
