package org.example.view;

import db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;

public class LogIn extends JFrame {

    private JButton signin, login, forgotPass;

    private JLabel userlabel, passlabel, errorLabel, loginLabel;
    private JTextField usertext, passtext;

    public LogIn() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        createLabels();
        createTextFields();
        createLoginButton();
        createSignInButton();
        createForgotPassButton();
        createMainPanel();
        setSize(500, 400);
    }
    private void createLabels() {
        userlabel = new JLabel("Enter username", SwingConstants.CENTER);
        userlabel.setBorder(BorderFactory.createEmptyBorder());
        userlabel.setForeground(Color.BLACK);
        userlabel.setBackground(Color.WHITE);
        userlabel.setOpaque(true);

        passlabel = new JLabel("Enter password", SwingConstants.CENTER);
        passlabel.setBorder(BorderFactory.createEmptyBorder());
        passlabel.setForeground(Color.BLACK);
        passlabel.setBackground(Color.WHITE);
        passlabel.setOpaque(true);


    }

    private void createTextFields() {
        usertext = new JTextField(24);
        usertext.setHorizontalAlignment(SwingConstants.RIGHT);
        usertext.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.black));
        usertext.setBackground(Color.WHITE);
        usertext.setForeground(Color.BLACK);

        passtext = new JTextField(24);
        passtext.setHorizontalAlignment(SwingConstants.RIGHT);
        passtext.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.black));
        passtext.setBackground(Color.WHITE);
        passtext.setForeground(Color.BLACK);
    }

    private void createLoginButton() {
        loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Serif", Font.BOLD, 25));
        loginLabel.setBackground(Color.WHITE);
        loginLabel.setForeground(Color.BLACK);
        loginLabel.setBorder(BorderFactory.createEmptyBorder());
        loginLabel.setOpaque(true);


        login = new JButton("Log In");
        login.setFont(new Font("Serif", Font.BOLD, 20));
        login.setBackground(Color.WHITE);
        login.setForeground(Color.BLACK);
        login.setBorder(BorderFactory.createMatteBorder(5,1,5,1, Color.BLACK));

        errorLabel = new JLabel();
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setVisible(false);
        errorLabel.setBorder(BorderFactory.createEmptyBorder());
        errorLabel.setForeground(Color.BLACK);
        errorLabel.setBackground(Color.WHITE);
        errorLabel.setOpaque(true);

        login.addActionListener((event) -> {
            DatabaseConnection db = new DatabaseConnection();
            if (!db.isUserPresent(usertext.getText(), passtext.getText())) {
                errorLabel.setVisible(true);
                errorLabel.setText("Incorrect credentials");
                usertext.setText("");
                passtext.setText("");
            } else {
                NewsDisplay newsDisplay = new NewsDisplay(usertext.getText());
                newsDisplay.show();
                dispose();
                System.out.println("login is successful");
            }
        });
    }
    private void createSignInButton() {
        signin = new JButton("If new user sign up");
        signin.addActionListener((event) -> {
            SignUp signIn = new SignUp();
            signIn.show();

            dispose();
        });
        signin.setFont(new Font("Serif", Font.BOLD, 20));
        signin.setBackground(Color.WHITE);
        signin.setForeground(Color.BLACK);
        signin.setBorder(BorderFactory.createMatteBorder(5,1,5,1, Color.BLACK));
    }

    private void createForgotPassButton() {
        forgotPass = new JButton("Forgot Password");
        forgotPass.setFont(new Font("Serif", Font.BOLD, 20));
        forgotPass.setBackground(Color.WHITE);
        forgotPass.setForeground(Color.BLACK);
        forgotPass.setBorder(BorderFactory.createMatteBorder(5,1,5,1, Color.BLACK));
        forgotPass.addActionListener((event) -> {
            ForgotPass fp = new ForgotPass();
            fp.show();
            dispose();
        });
    }

    private void createMainPanel() {
        GridLayout layout = new GridLayout(6, 1);
        layout.setVgap(20);
        JPanel mainPanel = new JPanel(layout);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setOpaque(true);

        JPanel loginLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0 ,0));
        loginLabelPanel.add(loginLabel);
        loginLabelPanel.setBackground(Color.WHITE);

        JPanel userPanel = new JPanel(new GridLayout(1, 2));
        userPanel.add(userlabel);
        userPanel.add(usertext);

        JPanel passPanel = new JPanel(new GridLayout(1, 2));
        passPanel.add(passlabel);
        passPanel.add(passtext);

        //JPanel mainLoginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0 , 0));
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0 , 0));
        loginPanel.setBackground(Color.WHITE);
        //loginPanel.setOpaque(true);
        loginPanel.add(login);
        loginPanel.add(errorLabel);
        //mainLoginPanel.setBackground(Color.WHITE);
        //mainLoginPanel.setOpaque(true);
        //mainLoginPanel.add(loginPanel);

        JPanel signinPanel = new JPanel();
        signinPanel.setBackground(Color.WHITE);
        signinPanel.setOpaque(true);
        signinPanel.add(signin);

        JPanel forgotPassPanel = new JPanel();
        forgotPassPanel.setBackground(Color.WHITE);
        forgotPassPanel.setOpaque(true);
        forgotPassPanel.add(forgotPass);

        mainPanel.add(loginLabelPanel);
        mainPanel.add(userPanel);
        mainPanel.add(passPanel);
        mainPanel.add(loginPanel);
        mainPanel.add(signinPanel);
        mainPanel.add(forgotPassPanel);

        this.add(mainPanel);
    }

    public static void main(String args[]) {
        JFrame frame = new LogIn();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
