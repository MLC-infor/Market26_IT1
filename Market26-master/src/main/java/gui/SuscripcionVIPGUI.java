package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class SuscripcionVIPGUI extends JFrame {

    private JFrame thisFrame;

    public SuscripcionVIPGUI(String emailLogueado) {
        thisFrame = this;
        
        ResourceBundle rb = ResourceBundle.getBundle("Etiquetas");
        
        setTitle(rb.getString("VIP.Title"));
        setSize(380, 280);
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null); // Centrar en la pantalla

        JLabel lblTitulo = new JLabel(rb.getString("VIP.Header"));
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(218, 165, 32)); // Dorado
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(0, 20, 380, 30);
        getContentPane().add(lblTitulo);

        JLabel lblVentajas = new JLabel(rb.getString("VIP.Benefits"));
        lblVentajas.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblVentajas.setBounds(50, 60, 280, 70);
        getContentPane().add(lblVentajas);

        JLabel lblPrecio = new JLabel(rb.getString("VIP.Price"));
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPrecio.setHorizontalAlignment(SwingConstants.CENTER);
        lblPrecio.setBounds(0, 140, 380, 20);
        getContentPane().add(lblPrecio);

        JButton btnComprar = new JButton(rb.getString("VIP.BuyButton"));
        btnComprar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnComprar.setBackground(new Color(255, 215, 0)); // Dorado
        btnComprar.setForeground(Color.DARK_GRAY);
        btnComprar.setFocusPainted(false);
        btnComprar.setBorderPainted(false);
        btnComprar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnComprar.setBounds(70, 180, 240, 40);
        getContentPane().add(btnComprar);

        btnComprar.addActionListener(e -> {
            boolean exito = MainGUI.getBusinessLogic().comprarSuscripcionVIP(emailLogueado);
            
            if (exito) {
                JOptionPane.showMessageDialog(thisFrame, rb.getString("VIP.Success"), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                thisFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(thisFrame, rb.getString("VIP.Error"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
