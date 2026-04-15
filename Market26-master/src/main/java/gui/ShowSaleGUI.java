package gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import domain.Sale;
import domain.Bid;
import domain.Buyer;
import businessLogic.BLFacade;

public class ShowSaleGUI extends JFrame {
    private JFrame thisFrame;

    // Recibimos la venta en la que han hecho clic y el email del que está mirando la pantalla
    public ShowSaleGUI(Sale sale, String emailLogueado) {
        thisFrame = this;
        setTitle("Detalle de Producto");
        setSize(400, 250);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblInfo = new JLabel("Producto: " + sale.getTitulo() + " - Precio Original: " + sale.getPrecioOriginal() + "€");
        lblInfo.setBounds(20, 20, 350, 20);
        lblInfo.setFont(new Font("Tahoma", Font.BOLD, 12));
        getContentPane().add(lblInfo);

        // 🎓 MAGIA DE UX: Comparamos el email logueado con el del dueño de la venta
        if (emailLogueado.equals(sale.getVendedor().getEmail())) {
            
            // --- ERES EL VENDEDOR (Dueño) ---
            JLabel lblAviso = new JLabel("¡Eres el creador de esta venta!");
            lblAviso.setForeground(Color.BLUE);
            lblAviso.setBounds(100, 60, 200, 20);
            getContentPane().add(lblAviso);

            JButton btnGestionar = new JButton("Gestionar Venta y Ver Ofertas");
            btnGestionar.setBounds(75, 100, 250, 40);
            btnGestionar.setBackground(new Color(255, 200, 200)); // Rojito
            getContentPane().add(btnGestionar);

            btnGestionar.addActionListener(e -> {
                // Le pasamos su email correcto
                JFrame g = new CerrarVentaGUI(sale, emailLogueado); 
                g.setVisible(true);
                thisFrame.dispose();
            });

        } else {
            
            // --- ERES UN COMPRADOR (No eres el dueño) ---
            JLabel lblTuOferta = new JLabel("Tu Oferta (€):");
            lblTuOferta.setBounds(20, 100, 100, 20);
            getContentPane().add(lblTuOferta);

            JTextField fieldOffer = new JTextField();
            fieldOffer.setBounds(130, 100, 80, 25);
            getContentPane().add(fieldOffer);

            JButton btnMakeOffer = new JButton("Hacer Oferta");
            btnMakeOffer.setBounds(220, 95, 140, 35);
            btnMakeOffer.setBackground(new Color(200, 255, 200)); // Verdecito
            getContentPane().add(btnMakeOffer);

            btnMakeOffer.addActionListener(e -> {
                try {
                    float precio = Float.parseFloat(fieldOffer.getText().replace(",", "."));
                    if(precio <= 0) throw new NumberFormatException();

                    Buyer compradorActual = new Buyer(emailLogueado, "", ""); 
                    Bid b = MainGUI.getBusinessLogic().crearBid(precio, compradorActual, sale);
                    
                    if (b != null) {
                        JOptionPane.showMessageDialog(thisFrame, "¡Oferta enviada correctamente!");
                        thisFrame.dispose();
                    } else {
                        // 🎓 AVISO AL USUARIO
                        JOptionPane.showMessageDialog(thisFrame, "Error: No tienes saldo suficiente en tu Billetera para hacer esta oferta.", "Saldo Insuficiente", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(thisFrame, "Error: Introduce un precio válido.");
                }
            });
        }
    }
}