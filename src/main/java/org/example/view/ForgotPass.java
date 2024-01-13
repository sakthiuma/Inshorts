package org.example.view;

import db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;

public class ForgotPass extends JFrame {

    private JLabel newPassLabel, confPassLabel, resetLabel, userLabel, errLabel;

    private JTextField newPass, confPass, username;

    private JButton resetPass;

    public ForgotPass() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        createLabel();
        createTextFields();
        createResetButton();
        createMainPanel();
        setSize(500, 400);


    }

    public void createLabel() {
        resetLabel = new JLabel("Reset your Password", SwingConstants.CENTER);
        resetLabel.setFont(new Font("Serif", Font.BOLD, 25));
        resetLabel.setBackground(Color.WHITE);
        resetLabel.setForeground(Color.BLACK);
        resetLabel.setBorder(BorderFactory.createEmptyBorder());
        resetLabel.setOpaque(true);

        userLabel = new JLabel("User Name", SwingConstants.CENTER);
        userLabel.setBackground(Color.WHITE);
        userLabel.setForeground(Color.BLACK);
        userLabel.setBorder(BorderFactory.createEmptyBorder());
        userLabel.setOpaque(true);

        newPassLabel = new JLabel("New password", SwingConstants.CENTER);
        newPassLabel.setBackground(Color.WHITE);
        newPassLabel.setForeground(Color.BLACK);
        newPassLabel.setBorder(BorderFactory.createEmptyBorder());
        newPassLabel.setOpaque(true);

        confPassLabel = new JLabel("Confirm password", SwingConstants.CENTER);
        confPassLabel.setBorder(BorderFactory.createEmptyBorder());
        confPassLabel.setForeground(Color.BLACK);
        confPassLabel.setBackground(Color.WHITE);
        confPassLabel.setOpaque(true);

        errLabel = new JLabel();
        errLabel.setVisible(false);
    }

    public void createTextFields() {
        username = new JTextField(24);
        username.setHorizontalAlignment(SwingConstants.RIGHT);
        username.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        username.setBackground(Color.WHITE);
        username.setForeground(Color.BLACK);

        newPass = new JTextField(24);
        newPass.setHorizontalAlignment(SwingConstants.RIGHT);
        newPass.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        newPass.setBackground(Color.WHITE);
        newPass.setForeground(Color.BLACK);

        confPass = new JTextField(24);
        confPass.setHorizontalAlignment(SwingConstants.RIGHT);
        confPass.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        confPass.setBackground(Color.WHITE);
        confPass.setForeground(Color.BLACK);
    }

    public void createResetButton() {
        resetPass = new JButton();
        resetPass.setText("Reset");
        resetPass.setFont(new Font("Serif", Font.BOLD, 20));
        resetPass.setBackground(Color.WHITE);
        resetPass.setForeground(Color.BLACK);
        resetPass.setBorder(BorderFactory.createMatteBorder(5,1,5,1, Color.BLACK));

        resetPass.addActionListener((event) -> {
            String name = username.getText();
            String pass = newPass.getText();
            String confPassVal = confPass.getText();

            if (confPassVal.equals(pass)) {

                DatabaseConnection db = new DatabaseConnection();
                db.updateUserPass(name, pass);
                db.closeConnection();

                NewsDisplay newsDisplay = new NewsDisplay(name);
                newsDisplay.show();
                dispose();
            } else {
                errLabel.setText("Passwords dont match");
                errLabel.setVisible(true);
                newPass.setText("");
                confPass.setText("");
            }

        });
    }

    public void createMainPanel() {
        GridLayout layout = new GridLayout(6, 1);
        layout.setVgap(20);
        JPanel mainPanel = new JPanel(layout);

        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0 ,0));
        resetPanel.add(resetLabel);
        resetPanel.setBackground(Color.WHITE);

        JPanel userPanel = new JPanel(new GridLayout(1, 2));
        userPanel.add(userLabel);
        userPanel.add(username);

        JPanel passPanel = new JPanel(new GridLayout(1, 2));
        passPanel.add(newPassLabel);
        passPanel.add(newPass);

        JPanel confPassPanel = new JPanel(new GridLayout(1, 2));
        confPassPanel.add(confPassLabel);
        confPassPanel.add(confPass);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0 ,0));
        buttonPanel.add(resetPass);
        buttonPanel.setBackground(Color.WHITE);

        mainPanel.add(resetPanel);
        mainPanel.add(userPanel);
        mainPanel.add(passPanel);
        mainPanel.add(confPassPanel);
        mainPanel.add(buttonPanel);

        mainPanel.setBackground(Color.WHITE);
        mainPanel.setOpaque(true);

        add(mainPanel);

    }

    public static void main(String args[]) {
        JFrame frame = new ForgotPass();
        frame.setVisible(true);
    }
}
