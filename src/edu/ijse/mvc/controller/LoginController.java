package edu.ijse.mvc.controller;

import edu.ijse.mvc.dto.UserDto;
import edu.ijse.mvc.model.LoginModel;
import edu.ijse.mvc.view.AdminView;
import edu.ijse.mvc.view.LecturerMenu;
import edu.ijse.mvc.view.LoginView;

import javax.swing.*;

public class LoginController {

    private final LoginModel model = new LoginModel();

    public void handleLogin(String username, String password) {
        UserDto user = model.validateUser(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(null, "Login Success! Role: " + user.getRole());
            if (user.getRole().equalsIgnoreCase("admin")) {
                new AdminView();
            } else if (user.getRole().equalsIgnoreCase("lecturer")) {
                System.out.println("Opening LecturerMenu"); // Debug print
                new LecturerMenu();  // Show lecturer menu after login
            } else {
                JOptionPane.showMessageDialog(null, "Role not recognized!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid credentials");
        }
    }
}
