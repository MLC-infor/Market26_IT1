package gui;

import businessLogic.BLFacade;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setBounds(100, 100, 420, 380); // Ventana un poco más grande
        setLocationRelativeTo(null); // Centrar en pantalla
        getContentPane().setBackground(Color.WHITE); // Fondo blanco moderno
        getContentPane().setLayout(null);
        
        // Título interior
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(50, 50, 50));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(0, 20, 400, 30);
        getContentPane().add(lblTitulo);
        
        // Etiquetas y campos más grandes y limpios
        JLabel lblEmail = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Email"));
        lblEmail.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblEmail.setForeground(Color.DARK_GRAY);
        lblEmail.setBounds(60, 80, 300, 20);
        getContentPane().add(lblEmail);
        
        emailField = new JTextField();
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailField.setBounds(60, 105, 280, 35); // Más alto (35px)
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Padding interior
        getContentPane().add(emailField);
        
        JLabel lblPass = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Password"));
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPass.setForeground(Color.DARK_GRAY);
        lblPass.setBounds(60, 160, 300, 20);
        getContentPane().add(lblPass);
        
        passField = new JPasswordField();
        passField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passField.setBounds(60, 185, 280, 35);
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        getContentPane().add(passField);
        
        // Botón con estilo moderno
        JButton btnLogin = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.LoginButton"));
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setBackground(new Color(70, 130, 180)); // Azul acero elegante
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBounds(60, 245, 280, 40); // Botón ancho
        getContentPane().add(btnLogin);
        
        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMessage.setBounds(30, 295, 340, 20);
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
                    JOptionPane.showMessageDialog(null, 
                        "¡Bienvenido/a " + usuario.getName() + "!\nHas iniciado sesión correctamente.", 
                        "Login Exitoso", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    MainGUI nuevoMain = new MainGUI(usuario.getEmail());
                    nuevoMain.setVisible(true);
                    dispose(); 
                } else {
                    lblMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.ErrorLogin"));
                }
            }
        });
    }
}
