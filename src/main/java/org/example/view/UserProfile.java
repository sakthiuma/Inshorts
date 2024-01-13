package org.example.view;

import javax.swing.*;
import javax.xml.crypto.Data;

import db.DatabaseConnection;
import db.NewsReadData;
import db.NewsRecords;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.List;

public class UserProfile extends JFrame {

    private JLabel nl1, nl2, nl3, nl4, header, savedNewsLabel;
    private JTextArea area1, area2, area3, area4;

    private JButton r1, r2, r3, r4;

    private String username;

    private List<JLabel> newsIds;

    private List<JTextArea> newsArea;

    private List<JButton> newsButton;

    public UserProfile(String username) {
        this.username = username;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 500);

        createLabel();
        createNewsArea();
        createButtons();
        createMainPanel();

        newsIds = List.of(nl1, nl2, nl3, nl4);
        newsArea = List.of(area1, area2, area3, area4);
        newsButton = List.of(r1, r2, r3, r4);

        populateSaveLaterNews();
    }

    public void createLabel() {
        nl1 = new JLabel();
        nl1.setVisible(false);
        nl2 = new JLabel();
        nl2.setVisible(false);
        nl3 = new JLabel();
        nl3.setVisible(false);
        nl4 = new JLabel();
        nl4.setVisible(false);

        header = new JLabel();
        header.setText("Hi " + this.username);
        header.setFont(new Font("Serif", Font.BOLD, 20));
        header.setHorizontalAlignment(SwingConstants.LEFT);

        savedNewsLabel = new JLabel("News marked for save later");
        savedNewsLabel.setFont(new Font("Serif", Font.BOLD, 20));


    }

    public void createNewsArea() {
        area1 = new JTextArea(3, 40);
        area1.setLineWrap(true);
        area2 = new JTextArea(3, 40);
        area2.setLineWrap(true);
        area3 = new JTextArea(3, 40);
        area3.setLineWrap(true);
        area4 = new JTextArea(3, 40);
        area4.setLineWrap(true);
    }

    private void createButtons() {
        r1 = new JButton("Read more");
        r1.addActionListener((event) -> {
            int newsId = Integer.valueOf(nl1.getText());
            DatabaseConnection db = new DatabaseConnection();
            db.recordReadMore(username, newsId);
            String newsLink = db.getLinkForNews(newsId);
            db.closeConnection();

            if (newsLink != null) {
                ReadMoreView rmv = new ReadMoreView(newsLink);
                rmv.show();
            }
        });

        r2 = new JButton("Read more");
        r2.addActionListener((event) -> {
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

        r3 = new JButton("Read more");
        r3.addActionListener((event) -> {
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

        r4 = new JButton("Read more");
        r4.addActionListener((event) -> {
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
    }

    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(header);
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel dispPanel = new JPanel(new GridLayout(5, 1));
        dispPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dispPanel.setPreferredSize(new Dimension(537, 50));
        //dispPanel.add(new JTextArea("disp panel"));

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePanel.add(savedNewsLabel);

        JPanel news1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        news1.add(area1);
        news1.add(nl1);
        news1.add(r1);

        JPanel news2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        news2.add(area2);
        news2.add(nl2);
        news2.add(r2);

        JPanel news3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        news3.add(area3);
        news3.add(nl3);
        news3.add(r3);

        JPanel news4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        news4.add(area4);
        news4.add(nl4);
        news4.add(r4);

        dispPanel.add(savePanel);
        dispPanel.add(news1);
        dispPanel.add(news2);
        dispPanel.add(news3);
        dispPanel.add(news4);

        JPanel chartPanelDisp = new JPanel();
        chartPanelDisp.add(createChartPanel());
        chartPanelDisp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chartPanelDisp.setPreferredSize(new Dimension(463, 50));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(dispPanel, BorderLayout.WEST);
        mainPanel.add(chartPanelDisp, BorderLayout.EAST);

        add(mainPanel);

    }

    private void populateSaveLaterNews() {
        DatabaseConnection db = new DatabaseConnection();
        List<NewsRecords> recs = db.getNewsMarkedForSaveLater(this.username);
        db.closeConnection();

        for (int i=0;i<recs.size(); i++) {
            newsArea.get(i).setText(recs.get(i).getLink() + "\n" + recs.get(i).getSummary());
            newsIds.get(i).setText(recs.get(i).getId());
            newsIds.get(i).setVisible(false);
            //newsButton.get(i).setVisible(false);
        }

        if (recs.size()<4) {
            for (int i = recs.size(); i <4; i++) {
                newsArea.get(i).setVisible(false);
                newsButton.get(i).setVisible(false);

            }
        }
    }

    public ChartPanel createChartPanel() {
        DatabaseConnection db = new DatabaseConnection();
        List<NewsReadData> dataList = db.getNewsReadCountByGenre(this.username);
        db.closeConnection();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (NewsReadData data : dataList) {
            System.out.println("Username: " + data.getUsername() + ", Genre: " + data.getGenre() + ", Read Count: " + data.getReadCount()); // Debugging line
            dataset.addValue(data.getReadCount(), data.getGenre(), data.getUsername());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "News Read Count by Genre",
                "Genre",
                "Read Count",
                dataset
        );
        CategoryPlot catPlot = barChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) catPlot.getRenderer();
        renderer.setItemMargin(0.3);

        ChartPanel cp = new ChartPanel(barChart);
        cp.setPreferredSize(new Dimension(400, 400));
        return cp;
    }

    public static void main(String[] args) {
        UserProfile userProfile = new UserProfile("Sakthi Uma maheswari");
        userProfile.setVisible(true);
//        XYSeries series = new XYSeries("asdf");
//        for (int i = 0; i < 100; i++)
//            series.add(i, Math.random());
//        XYSeriesCollection dataset = new XYSeriesCollection(series);
//        JFreeChart chart = ChartFactory.createXYLineChart(null, null, null, dataset, PlotOrientation.HORIZONTAL, true, true, true);
//        ChartPanel chartpanel = new ChartPanel(chart);
//        chartpanel.setDomainZoomable(true);
//
//        JPanel jPanel4 = new JPanel();
//        jPanel4.setLayout(new BorderLayout());
//        jPanel4.add(chartpanel, BorderLayout.NORTH);
//
//        JFrame frame = new JFrame();
//        frame.add(jPanel4);
//        frame.pack();
//        frame.setVisible(true);
    }
}
