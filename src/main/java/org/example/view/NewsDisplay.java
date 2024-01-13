package org.example.view;

import db.DatabaseConnection;
import db.NewsRecords;
import multithread.Crawler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NewsDisplay extends JFrame {

    private static int refG1 = 0, refG2 = 0, refG3 = 0;

    private String username;

    private JLabel headerLabel, nl1, nl2, nl3, nl4;

    private JTextArea area1, area2, area3, area4;

    private JButton genre1, genre2, genre3, sl1, sl2, sl3, sl4, rm1, rm2, rm3, rm4, rf1, rf2, rf3, profile;

    private Crawler obj1=null, obj2=null, obj3=null;

    private List<String> news =  new ArrayList<>(Arrays.asList("news1", "news2", "news3"));

    private List<JTextArea> newsAreas;

    private List<JLabel> newsLabel;

    public NewsDisplay(String name) {
        this.username = name;

        createHeaderLabel();
        createGenreButton();
        createRefreshButton();
        createNewsFields();
        createProfileButton();
        createMainPanel();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 500);

        newsAreas = List.of(area1, area2, area3, area4);
        newsLabel = List.of(nl1, nl2, nl3, nl4);
    }

    private void createHeaderLabel() {
        headerLabel = new JLabel();
        headerLabel.setText("Welcome " + this.username);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 20));
        headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        //headerLabel.setBorder(BorderFactory.createMatteBorder(0,1,1,1, Color.BLACK));
        //headerLabel.setBorder();
    }

    private void createGenreButton() {
        genre1 = new JButton("World");
        genre2 = new JButton("Business");
        genre3 = new JButton("Politics");



        genre1.addActionListener((event) -> {
            clearTextArea();
            Crawler obj = new Crawler(genre1.getText().toLowerCase());
            int newsCount = 4;
            if (obj.crawlNews()) {
                DatabaseConnection db = new DatabaseConnection();
                List<NewsRecords> vals = db.getNews(4, genre1.getText().toLowerCase());

                for (int i = 0; i < vals.size(); i++) {
                    newsAreas.get(i).setText(vals.get(i).getLink() + "\n" + vals.get(i).getSummary());
                    newsLabel.get(i).setText(vals.get(i).getId());
                    newsLabel.get(i).setVisible(false);
                }

                db.closeConnection();

            }
        });

        genre2.addActionListener((event) -> {
            clearTextArea();
            Crawler obj = new Crawler(genre2.getText().toLowerCase());
            //obj.crawlNews();
            if (obj.crawlNews()) {
                DatabaseConnection db = new DatabaseConnection();
                List<NewsRecords> vals = db.getNews(4, genre2.getText().toLowerCase());

                for (int i = 0; i < vals.size(); i++) {
                    newsAreas.get(i).setText(vals.get(i).getLink() + "\n" + vals.get(i).getSummary());
                    newsLabel.get(i).setText(vals.get(i).getId());
                }

                db.closeConnection();
            }
        });

        genre3.addActionListener((event) -> {
            clearTextArea();
            Crawler obj = new Crawler(genre3.getText().toLowerCase());
            if (obj.crawlNews()) {
                DatabaseConnection db = new DatabaseConnection();
                List<NewsRecords> vals = db.getNews(4, genre3.getText().toLowerCase());

                for (int i = 0; i < vals.size(); i++) {
                    newsAreas.get(i).setText(vals.get(i).getLink() + "\n" + vals.get(i).getSummary());
                    newsLabel.get(i).setText(vals.get(i).getId());
                }

                db.closeConnection();

            }
        });

    }

    public void createRefreshButton() {
        rf1 = new JButton("Refresh");
        rf1.addActionListener((event) -> {
            DatabaseConnection db = new DatabaseConnection();
            refG1 += 4;
            List<NewsRecords> vals = db.getNewsBasedOnOffset(4, genre1.getText().toLowerCase(), refG1);

            for (int i = 0; i < vals.size(); i++) {
                newsAreas.get(i).setText(vals.get(i).getLink() + "\n" + vals.get(i).getSummary());
                newsLabel.get(i).setText(vals.get(i).getId());
            }

            db.closeConnection();
        });

        rf2 = new JButton("Refresh");
        rf2.addActionListener((event) -> {
            refG2 += 4;
            DatabaseConnection db = new DatabaseConnection();
            List<NewsRecords> vals = db.getNewsBasedOnOffset(4, genre2.getText().toLowerCase(), refG2);

            for (int i = 0; i < vals.size(); i++) {
                newsAreas.get(i).setText(vals.get(i).getLink() + "\n" + vals.get(i).getSummary());
                newsLabel.get(i).setText(vals.get(i).getId());
            }

            db.closeConnection();
        });

        rf3 = new JButton("Refresh");
        rf3.addActionListener((event) -> {
            refG3 += 4;
            DatabaseConnection db = new DatabaseConnection();
            List<NewsRecords> vals = db.getNewsBasedOnOffset(4, genre3.getText().toLowerCase(), refG3);

            for (int i = 0; i < vals.size(); i++) {
                newsAreas.get(i).setText(vals.get(i).getLink() + "\n" + vals.get(i).getSummary());
                newsLabel.get(i).setText(vals.get(i).getId());
            }

            db.closeConnection();
        });
    }

    public void clearTextArea() {
        area1.selectAll();
        area1.replaceSelection("");

        area2.selectAll();
        area2.replaceSelection("");

        area3.selectAll();
        area3.replaceSelection("");

        area4.selectAll();
        area4.replaceSelection("");
    }

    private void createNewsFields() {
        area1 = new JTextArea(3, 80);
        area1.setText("");
        area2 = new JTextArea(3, 80);
        area2.setText("");
        area3 = new JTextArea(3, 80);
        area3.setText("");
        area4 = new JTextArea(3, 80);
        area4.setText("");

        sl1 = new JButton("Save for later");
        sl2 = new JButton("Save for later");
        sl3 = new JButton("Save for later");
        sl4 = new JButton("Save for later");

        sl1.addActionListener((event) -> {
            DatabaseConnection db = new DatabaseConnection();
            db.recordSaveForLater(username, Integer.valueOf(nl1.getText()));
            db.closeConnection();
        });

        sl2.addActionListener((event) -> {
            DatabaseConnection db = new DatabaseConnection();
            db.recordSaveForLater(username, Integer.valueOf(nl2.getText()));
            db.closeConnection();
        });

        sl3.addActionListener((event) -> {
            DatabaseConnection db = new DatabaseConnection();
            db.recordSaveForLater(username, Integer.valueOf(nl3.getText()));
            db.closeConnection();
        });

        sl4.addActionListener((event) -> {
            DatabaseConnection db = new DatabaseConnection();
            db.recordSaveForLater(username, Integer.valueOf(nl4.getText()));
            db.closeConnection();
        });


        rm1 = new JButton("Read more");
        rm2 = new JButton("Read more");
        rm3 = new JButton("Read more");
        rm4 = new JButton("Read more");

        rm1.addActionListener((event) -> {
            int newsId = Integer.valueOf(nl1.getText());
            DatabaseConnection db = new DatabaseConnection();
            db.recordReadMore(username, newsId);
            String newsLink = db.getLinkForNews(newsId);
            db.closeConnection();

            System.out.println(newsLink);

            if (newsLink != null) {

                ReadMoreView rmv = new ReadMoreView(newsLink);
                rmv.show();
            }
        });

        rm2.addActionListener((event) -> {
            int newsId = Integer.valueOf(nl2.getText());
            DatabaseConnection db = new DatabaseConnection();
            db.recordReadMore(username, newsId);
            String newsLink = db.getLinkForNews(newsId);
            db.closeConnection();

            if (newsLink != null) {

                ReadMoreView rmv = new ReadMoreView(newsLink);
                rmv.show();
            }
        });

        rm3.addActionListener((event) -> {
            int newsId = Integer.valueOf(nl3.getText());
            DatabaseConnection db = new DatabaseConnection();
            db.recordReadMore(username, newsId);
            String newsLink = db.getLinkForNews(newsId);
            db.closeConnection();

            if (newsLink != null) {

                ReadMoreView rmv = new ReadMoreView(newsLink);
                rmv.show();
            }
        });

        rm4.addActionListener((event) -> {
            int newsId = Integer.valueOf(nl4.getText());
            DatabaseConnection db = new DatabaseConnection();
            db.recordReadMore(username, newsId);
            String newsLink = db.getLinkForNews(newsId);
            db.closeConnection();

            if (newsLink != null) {

                ReadMoreView rmv = new ReadMoreView(newsLink);
                rmv.show();
            }
        });

        nl1 = new JLabel("1");
        nl2 = new JLabel("2");
        nl3 = new JLabel("3");
        nl4 = new JLabel("4");


    }

    public void createProfileButton() {
        profile = new JButton("Go to profile");
        profile.setBackground(Color.WHITE);
        profile.setForeground(Color.BLACK);
        profile.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK));

        profile.addActionListener((event) -> {
            UserProfile up = new UserProfile(this.username);
            up.show();
            //dispose();
        });
    }
    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(headerLabel);
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        headerPanel.add(Box.createHorizontalStrut(550));
        headerPanel.add(profile);
        //headerPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        //headerPanel.setPreferredSize(new Dimension(600, 50));
        headerPanel.setBounds(20, 0, 400, 50);

        JPanel buttonControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonControlPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        buttonControlPanel.setPreferredSize(new Dimension(250, 50));
        GridLayout gl = new GridLayout(3, 2);
        gl.setVgap(50);
        JPanel buttonPanel = new JPanel(gl);
        buttonPanel.add(genre1);
        buttonPanel.add(rf1);
        buttonPanel.add(genre2);
        buttonPanel.add(rf2);
        buttonPanel.add(genre3);
        buttonPanel.add(rf3);
        buttonControlPanel.add(buttonPanel);

        JPanel dispPanel = new JPanel(new GridLayout(4, 1));
        dispPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dispPanel.setPreferredSize(new Dimension(737, 50));
        //dispPanel.add(new JTextArea("disp panel"));

        JPanel news1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        news1.add(area1);
        news1.add(nl1);
        news1.add(sl1);
        news1.add(rm1);

        JPanel news2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        news2.add(area2);
        news2.add(nl2);
        news2.add(sl2);
        news2.add(rm2);

        JPanel news3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        news3.add(area3);
        news3.add(nl3);
        news3.add(sl3);
        news3.add(rm3);

        JPanel news4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        news4.add(area4);
        news4.add(nl4);
        news4.add(sl4);
        news4.add(rm4);



        dispPanel.add(news1);
        dispPanel.add(news2);
        dispPanel.add(news3);
        dispPanel.add(news4);

        //JPanel contentPanel = new JPanel(new GridLayout(1, 2));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonControlPanel, BorderLayout.WEST);
        mainPanel.add(dispPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    public static void main(String args[]) {
        JFrame frame = new NewsDisplay("Sakthi Uma maheswari");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
