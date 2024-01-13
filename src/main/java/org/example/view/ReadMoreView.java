package org.example.view;

//import javax.swing.*;
//import javax.swing.text.*;
//import javax.swing.text.html.HTML;
//import javax.swing.text.html.HTMLDocument;
//import javax.swing.text.html.HTMLEditorKit;
//import javax.swing.text.html.InlineView;
//import java.awt.*;
//import java.io.IOException;
//import java.net.URL;

import javax.swing.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReadMoreView extends JFrame {
    private String url;

    public ReadMoreView(String url) {
        this.url = url;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        //setTitle("HTML Display in Java Swing");

        // Create a JEditorPane
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);

        // Set the editor kit to support HTML
        editorPane.setContentType("text/html");
        //editorPane.setEditorKit(new NoAdsHTMLEditorKit());

        // Create a JScrollPane and add the JEditorPane to it
        JScrollPane scrollPane = new JScrollPane(editorPane);

        // Set the preferred size of the JScrollPane
        scrollPane.setPreferredSize(new Dimension(800, 600));

        // Add the JScrollPane to the JFrame
        add(scrollPane);

        // Load the external HTML page
        try {
            URL urlForPage = new URL(this.url); // Replace with the URL of your HTML page
            String htmlContent = preprocessHtml(urlForPage);
            editorPane.setText(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pack();
        setLocationRelativeTo(null);
    }

    private String preprocessHtml(URL url) throws IOException {
        Document document = Jsoup.parse(url, 10000);

        // Remove all <a> elements
        Elements links = document.select("a");
        for (Element link : links) {
            link.unwrap();
        }

        // Extract content within the <article> tag
        Element articleElement = document.select("article").first();
        if (articleElement != null) {
            return articleElement.html();
        } else {
            // If no <article> tag is found, return the entire HTML content
            return document.html();
        }

        // Get the modified HTML content
        //return document.html();
    }


    public static void main(String args[]) {
        JFrame frame = new ReadMoreView("https://www.nbcnews.com/politics/supreme-court/supreme-court-justices-" +
                "discuss-whether-hear-abortion-pill-showdown-rcna128579");
        //frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //frame.setVisible(true);

    }
}
