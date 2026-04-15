package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import domain.Bid;
import domain.Sale;

public class CerrarVentaGUI extends JFrame {
    private Sale venta;
    private String emailLogueado;

    public CerrarVentaGUI(Sale v, String email) {
        this.venta = v;
        this.emailLogueado = email;
        setTitle("Gestionar Venta");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null); // Centrar en la pantalla

        JLabel lblInfo = new JLabel("Selecciona la oferta ganadora:");
        lblInfo.setBounds(20, 20, 340, 20);
        add(lblInfo);

        JComboBox<Bid> combo = new JComboBox<>();
        combo.setBounds(20, 50, 340, 30);
        
        // Cargamos las pujas en el desplegable
        for (Bid b : venta.getBidsRecibidas()) {
            // Un pequeño detalle pro: solo mostramos las pujas que siguen Pendientes
            if ("Pendiente".equals(b.getEstado())) {
                combo.addItem(b);
            }
        }
        add(combo);

        JButton btnCobrar = new JButton("Aceptar Oferta y Cobrar");
        btnCobrar.setBounds(20, 100, 340, 40);
        add(btnCobrar);

        btnCobrar.addActionListener(e -> {
            Bid sel = (Bid) combo.getSelectedItem();
            if (sel != null) {
                try {
                    // 1. Primer Caso de Uso: Cambiamos los estados (Aceptada / Rechazada / Cerrada)
                    MainGUI.getBusinessLogic().aceptarOferta(sel);
                    
                    // 2. Segundo Caso de Uso: Movemos el dinero de los monederos
                    MainGUI.getBusinessLogic().procesarCobro(sel);
                    
                    JOptionPane.showMessageDialog(this, "¡Venta cerrada y dinero transferido con éxito!");
                    dispose(); // Cierra la ventanita
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ha ocurrido un error al procesar el cobro.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No hay ninguna oferta seleccionada.");
            }
        });
    }
}