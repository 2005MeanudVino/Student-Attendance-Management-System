package edu.ijse.mvc.view;

import edu.ijse.mvc.controller.LoginController;
import javax.swing.*;

public class LoginView extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private LoginController controller;

    public LoginView() {
        controller = new LoginController();
        setTitle("SAMS Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(30, 30, 80, 25);
        add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(110, 30, 130, 25);
        add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(30, 70, 80, 25);
        add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(110, 70, 130, 25);
        add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(110, 110, 100, 25);
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = String.valueOf(txtPassword.getPassword());
            controller.handleLogin(user, pass);
        });

        setVisible(true);
    }
}
