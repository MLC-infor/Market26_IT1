package gui;

import businessLogic.BLFacade;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class RegisterGUI extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passField;
    private JPasswordField repeatPassField;
    private JRadioButton rdbtnSeller;
    private JRadioButton rdbtnBuyer;
    private JLabel lblMessage;

    public RegisterGUI() {
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Title"));
        setBounds(100, 100, 420, 520); // Ventana más alta
        setLocationRelativeTo(null); // Centrar
        getContentPane().setBackground(Color.WHITE); // Fondo blanco
        getContentPane().setLayout(null);
        
        // Título interior
        JLabel lblTitulo = new JLabel("Crear una Cuenta");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(50, 50, 50));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(0, 20, 400, 30);
        getContentPane().add(lblTitulo);
        
        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        Color labelColor = Color.DARK_GRAY;
        
        JLabel lblName = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Name"));
        lblName.setFont(labelFont);
        lblName.setForeground(labelColor);
        lblName.setBounds(60, 70, 300, 20);
        getContentPane().add(lblName);
        
        nameField = new JTextField();
        nameField.setBounds(60, 95, 280, 30);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        getContentPane().add(nameField);
        
        JLabel lblEmail = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Email"));
        lblEmail.setFont(labelFont);
        lblEmail.setForeground(labelColor);
        lblEmail.setBounds(60, 135, 300, 20);
        getContentPane().add(lblEmail);
        
        emailField = new JTextField();
        emailField.setBounds(60, 160, 280, 30);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        getContentPane().add(emailField);
        
        JLabel lblPass = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Password"));
        lblPass.setFont(labelFont);
        lblPass.setForeground(labelColor);
        lblPass.setBounds(60, 200, 300, 20);
        getContentPane().add(lblPass);
        
        passField = new JPasswordField();
        passField.setBounds(60, 225, 280, 30);
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        getContentPane().add(passField);
        
        JLabel lblRepeatPass = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.RepeatPassword"));
        lblRepeatPass.setFont(labelFont);
        lblRepeatPass.setForeground(labelColor);
        lblRepeatPass.setBounds(60, 265, 300, 20);
        getContentPane().add(lblRepeatPass);
        
        repeatPassField = new JPasswordField();
        repeatPassField.setBounds(60, 290, 280, 30);
        repeatPassField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        getContentPane().add(repeatPassField);
        
        // RadioButtons
        rdbtnSeller = new JRadioButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Seller"));
        rdbtnSeller.setBackground(Color.WHITE);
        rdbtnSeller.setFont(labelFont);
        rdbtnSeller.setBounds(80, 340, 100, 20);
        getContentPane().add(rdbtnSeller);
        
        rdbtnBuyer = new JRadioButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Buyer"));
        rdbtnBuyer.setBackground(Color.WHITE);
        rdbtnBuyer.setFont(labelFont);
        rdbtnBuyer.setBounds(220, 340, 100, 20);
        rdbtnBuyer.setSelected(true); 
        getContentPane().add(rdbtnBuyer);
        
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(rdbtnSeller);
        bgroup.add(rdbtnBuyer);
        
        // Botón Registrarse
        JButton btnRegister = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.RegisterButton"));
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegister.setBackground(new Color(60, 179, 113)); // Verde mar moderno
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setBounds(60, 385, 280, 40);
        getContentPane().add(btnRegister);
        
        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMessage.setBounds(30, 435, 340, 20);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(lblMessage);
        
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblMessage.setText(""); 
                lblMessage.setForeground(Color.RED);
                
                String name = nameField.getText();
                String email = emailField.getText();
                String pass = new String(passField.getPassword());
                String pass2 = new String(repeatPassField.getPassword());
                boolean isSeller = rdbtnSeller.isSelected();
                
                if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    lblMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.ErrorEmpty"));
                    return;
                }
                
                if (!pass.equals(pass2)) {
                    lblMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.ErrorPass"));
                    return;
                }
                
                BLFacade facade = MainGUI.getBusinessLogic();
                boolean exito = facade.registrarUsuario(name, email, pass, isSeller);
                
                if (exito) {
                    JOptionPane.showMessageDialog(null, 
                        "Cuenta creada exitosamente.\nYa puedes iniciar sesión.", 
                        "Registro Completo", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Cerramos la ventana de registro automáticamente para más comodidad
                } else {
                    lblMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.ErrorEmail"));
                }
            }
        });
    }
}