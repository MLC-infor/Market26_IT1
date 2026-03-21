package gui;

import businessLogic.BLFacade;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class LoginGUI extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JTextField emailField;
    private JPasswordField passField;
    private JLabel lblMessage;

    public LoginGUI() {
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Title"));
        setBounds(100, 100, 400, 300);
        getContentPane().setLayout(null);
        
        JLabel lblEmail = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Email"));
        lblEmail.setBounds(50, 50, 100, 20);
        getContentPane().add(lblEmail);
        
        emailField = new JTextField();
        emailField.setBounds(150, 50, 180, 20);
        getContentPane().add(emailField);
        
        JLabel lblPass = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Password"));
        lblPass.setBounds(50, 100, 100, 20);
        getContentPane().add(lblPass);
        
        passField = new JPasswordField();
        passField.setBounds(150, 100, 180, 20);
        getContentPane().add(passField);
        
        JButton btnLogin = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.LoginButton"));
        btnLogin.setBounds(130, 160, 120, 30);
        getContentPane().add(btnLogin);
        
        lblMessage = new JLabel("");
        lblMessage.setBounds(30, 210, 320, 20);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(lblMessage);
        
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblMessage.setText(""); 
                lblMessage.setForeground(Color.RED);
                
                String email = emailField.getText();
                String pass = new String(passField.getPassword());
                
                if (email.isEmpty() || pass.isEmpty()) {
                    lblMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.ErrorEmpty"));
                    return;
                }
                
                BLFacade facade = MainGUI.getBusinessLogic();
                domain.User usuario = facade.hacerLogin(email, pass);
                
                if (usuario != null) {
                    lblMessage.setForeground(new Color(0, 153, 0)); // Verde
                    lblMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Success") + " " + usuario.getName());
                    
                    // Aquí en el futuro podrías abrir la ventana de Comprador o Vendedor
                    // y cerrar esta con: this.setVisible(false);
                } else {
                    lblMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.ErrorLogin"));
                }
            }
        });
    }
}
