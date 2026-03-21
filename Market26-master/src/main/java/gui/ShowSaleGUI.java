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
    private JTextField fieldOffer = new JTextField();
    private JButton btnMakeOffer = new JButton("Hacer Oferta");
    private JFrame thisFrame;

    public ShowSaleGUI(Sale sale) {
        thisFrame = this;
        setTitle("Detalle de Venta");
        setSize(450, 400);
        getContentPane().setLayout(null);

        JLabel lblInfo = new JLabel("Producto: " + sale.getTitulo() + " - Precio: " + sale.getPrecioOriginal() + "€");
        lblInfo.setBounds(20, 20, 400, 20);
        getContentPane().add(lblInfo);

        JLabel lblTuOferta = new JLabel("Tu Oferta (€):");
        lblTuOferta.setBounds(20, 100, 100, 20);
        getContentPane().add(lblTuOferta);

        fieldOffer.setBounds(130, 100, 80, 20);
        getContentPane().add(fieldOffer);

        btnMakeOffer.setBounds(220, 95, 150, 30);
        getContentPane().add(btnMakeOffer);

        btnMakeOffer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    float precio = Float.parseFloat(fieldOffer.getText());
                    BLFacade facade = MainGUI.getBusinessLogic();
                    // Usamos el comprador de prueba
                    Buyer comprador = new Buyer("comprador1@gmail.com", "1234", "Iker");
                    Bid b = facade.crearBid(precio, comprador, sale);
                    if (b != null) {
                        JOptionPane.showMessageDialog(null, "Oferta enviada. Estado: " + b.getEstado());
                        thisFrame.setVisible(false);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error en el precio");
                }
            }
        });
        setVisible(true);
    }
}