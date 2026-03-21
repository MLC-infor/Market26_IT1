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
        setBounds(100, 100, 450, 400);
        getContentPane().setLayout(null);
        
        JLabel lblName = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Name"));
        lblName.setBounds(50, 40, 100, 20);
        getContentPane().add(lblName);
        
        nameField = new JTextField();
        nameField.setBounds(180, 40, 200, 20);
        getContentPane().add(nameField);
        
        JLabel lblEmail = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Email"));
        lblEmail.setBounds(50, 90, 100, 20);
        getContentPane().add(lblEmail);
        
        emailField = new JTextField();
        emailField.setBounds(180, 90, 200, 20);
        getContentPane().add(emailField);
        
        JLabel lblPass = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Password"));
        lblPass.setBounds(50, 140, 100, 20);
        getContentPane().add(lblPass);
        
        passField = new JPasswordField();
        passField.setBounds(180, 140, 200, 20);
        getContentPane().add(passField);
        
        JLabel lblRepeatPass = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.RepeatPassword"));
        lblRepeatPass.setBounds(50, 190, 130, 20);
        getContentPane().add(lblRepeatPass);
        
        repeatPassField = new JPasswordField();
        repeatPassField.setBounds(180, 190, 200, 20);
        getContentPane().add(repeatPassField);
        
        rdbtnSeller = new JRadioButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Seller"));
        rdbtnSeller.setBounds(80, 240, 100, 20);
        getContentPane().add(rdbtnSeller);
        
        rdbtnBuyer = new JRadioButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Buyer"));
        rdbtnBuyer.setBounds(230, 240, 100, 20);
        rdbtnBuyer.setSelected(true); 
        getContentPane().add(rdbtnBuyer);
        
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(rdbtnSeller);
        bgroup.add(rdbtnBuyer);
        
        JButton btnRegister = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.RegisterButton"));
        btnRegister.setBounds(160, 290, 120, 30);
        getContentPane().add(btnRegister);
        
        lblMessage = new JLabel("");
        lblMessage.setBounds(50, 330, 350, 20);
        lblMessage.setForeground(Color.RED);
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
                    lblMessage.setForeground(new Color(0, 153, 0)); // Verde oscuro más legible
                    lblMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Success"));
                    nameField.setText("");
                    emailField.setText("");
                    passField.setText("");
                    repeatPassField.setText("");
                } else {
                    lblMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.ErrorEmail"));
                }
            }
        });
    }
}