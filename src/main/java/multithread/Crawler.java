package multithread;

import db.DatabaseConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Crawler {

    private Map<String, String> newsLinkToText;
    private JPanel newsPanel;
    private int totalNews = 12;

    private String genre = "";


    public Crawler(final String genre) {
        //super("News Crawler");

        this.genre = genre;
        newsLinkToText = new HashMap<>();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Crawler("").crawlNews());
    }

    public boolean crawlNews() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (String website : websites) {
            executorService.execute(() -> {
                try {
                    crawlWebsite(website+this.genre);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            DatabaseConnection db = new DatabaseConnection();
            // add code to write to the database
            synchronized (db) {
                if (db.insertNews(newsLinkToText, this.genre)) {
                    return true;
                }
            }
            db.closeConnection();
            return false;

        } catch (InterruptedException e) {
            return false;
        }



    }

    private void crawlWebsite(String website) throws IOException {
        System.out.println("Crawling news from: " + website);

        try {
            Document document = Jsoup.connect(website).get();
            synchronized (document) {


                Elements newsElements = document.select("h2.styles_headline__ice3t");

                for (int i = 0; i < Math.min(totalNews, newsElements.size()); i++) {
                    Element newsElement = newsElements.get(i);
                    String newsText = newsElement.text();
                    String link = newsElement.select("a").attr("href");

                    if (link != null) {
                        newsLinkToText.put(link, newsText);
                    }

                }
            }
        } catch (IOException e) {
            System.err.println("Error crawling " + website + ": " + e.getMessage());
            throw e;
        }
        System.out.println("here");
    }


    private static final String[] websites = {
            "https://www.nbcnews.com/",
            //"https://time.com/" --> can we remove this?
    };
}

