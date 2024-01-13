package org.example.view;

import db.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class SignUp extends JFrame {

    private JLabel usernameLabel, passLabel, confPassLabel, signinLabel, errorLabel;
    private JTextField usernameText, passText, confPassText;
    private JButton signup, login;

    public SignUp() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        createLabel();
        createButton();
        createTextField();
        createPanel();
        setSize(500, 500);
    }

    private void createLabel() {
        signinLabel = new JLabel("Sign Up", SwingConstants.CENTER);
        signinLabel.setFont(new Font("Serif", Font.BOLD, 25));
        signinLabel.setBackground(Color.WHITE);
        signinLabel.setForeground(Color.BLACK);
        signinLabel.setBorder(BorderFactory.createEmptyBorder());
        signinLabel.setOpaque(true);

        usernameLabel = new JLabel("", SwingConstants.CENTER);
        //String labelText = String.format("<html><div WIDTH=%d>%s</div></html>", 100, );
        usernameLabel.setText("Username");
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setBackground(Color.WHITE);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder());
        usernameLabel.setOpaque(true);

        passLabel = new JLabel("", SwingConstants.CENTER);
        //labelText = String.format("<html><div WIDTH=%d>%s</div></html>", 150, );
        passLabel.setText("Password");
        passLabel.setBackground(Color.WHITE);
        passLabel.setForeground(Color.BLACK);
        passLabel.setBorder(BorderFactory.createEmptyBorder());
        passLabel.setOpaque(true);

        confPassLabel = new JLabel("", SwingConstants.CENTER);
        confPassLabel.setText("Confirm password");
        confPassLabel.setBorder(BorderFactory.createEmptyBorder());
        confPassLabel.setForeground(Color.BLACK);
        confPassLabel.setBackground(Color.WHITE);
        confPassLabel.setOpaque(true);

        errorLabel = new JLabel();
        errorLabel.setVisible(false);
    }

    private void createButton() {
        signup = new JButton();
        signup.setText("Register");
        signup.setFont(new Font("Serif", Font.BOLD, 20));
        signup.setBackground(Color.WHITE);
        signup.setForeground(Color.BLACK);
        signup.setBorder(BorderFactory.createMatteBorder(5,1,5,1, Color.BLACK));


        signup.addActionListener((event) -> {
            String username = usernameText.getText();
            String password = passText.getText();
            String confPass = confPassText.getText();

            if (confPass.equals(password)) {
                DatabaseConnection db = new DatabaseConnection();
                if (!db.isDuplicateUserRec(username)) {
                    db.insertNewUserRec(username, password);
                    db.closeConnection();
                    NewsDisplay newsDisplay = new NewsDisplay(username);
                    newsDisplay.show();
                    dispose();
                } else {
                    db.closeConnection();
                    errorLabel.setVisible(true);
                    errorLabel.setText("User record is present. Try logging in");
                    passText.setText("");
                    confPassText.setText("");
                }

            } else {
                errorLabel.setVisible(true);
                errorLabel.setText("Passwords dont match");
                passText.setText("");
                confPassText.setText("");
            }


        });

        login = new JButton();
        login.setText("Log in for existing user");
        login.setFont(new Font("Serif", Font.BOLD, 20));
        login.setBackground(Color.WHITE);
        login.setForeground(Color.BLACK);
        login.setBorder(BorderFactory.createMatteBorder(5,1,5,1, Color.BLACK));

        login.addActionListener((event) -> {
            LogIn logIn = new LogIn();
            logIn.show();
            dispose();
        });


    }



    private void createTextField() {
        usernameText = new JTextField(24);
        usernameText.setHorizontalAlignment(SwingConstants.RIGHT);
        usernameText.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        usernameText.setBackground(Color.WHITE);
        usernameText.setForeground(Color.BLACK);

        passText = new JTextField(24);
        passText.setHorizontalAlignment(SwingConstants.RIGHT);
        passText.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.black));
        passText.setBackground(Color.WHITE);
        passText.setForeground(Color.BLACK);

        //passText.setBackground(Color.LIGHT_GRAY);

        confPassText = new JTextField(24);
        confPassText.setHorizontalAlignment(SwingConstants.RIGHT);
        confPassText.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        confPassText.setBackground(Color.WHITE);
        confPassText.setForeground(Color.BLACK);
        //confPassText.setBackground(Color.LIGHT_GRAY);

    }

    private void createPanel() {
        GridLayout layout = new GridLayout(6, 1);
        layout.setVgap(20);
        JPanel mainPanel = new JPanel(layout);
        //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel signPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0 ,0));
        signPanel.add(signinLabel);
        signPanel.setBackground(Color.WHITE);

        JPanel userNamePanel = new JPanel(new GridLayout(1,2));
        userNamePanel.add(usernameLabel);
        userNamePanel.add(usernameText);

        JPanel passPanel = new JPanel(new GridLayout(1,2));
        passPanel.add(passLabel);
        passPanel.add(passText);

        JPanel confPassPanel = new JPanel(new GridLayout(1,2));
        confPassPanel.add(confPassLabel);
        confPassPanel.add(confPassText);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0 ,0));
        buttonPanel.add(signup);
        buttonPanel.add(errorLabel);
        buttonPanel.setBackground(Color.WHITE);

        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        loginPanel.add(login);
        loginPanel.setBackground(Color.WHITE);


        mainPanel.setBackground(Color.WHITE);
        mainPanel.setOpaque(true);
        mainPanel.add(signPanel);

        mainPanel.add(userNamePanel);
        //mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(passPanel);
        //mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(confPassPanel);
        //mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);
        mainPanel.add(loginPanel);

        //mainPanel.setBackground(Color.BLACK);

        this.add(mainPanel);
        //this.setBackground(Color.BLACK);
    }

    public static void main(String args[]) {
        JFrame frame = new SignUp();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
