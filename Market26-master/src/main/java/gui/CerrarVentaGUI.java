package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        setSize(400, 250);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        setLocationRelativeTo(null); 

        JLabel lblInfo = new JLabel("Selecciona la oferta ganadora:");
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblInfo.setBounds(30, 20, 340, 20);
        add(lblInfo);

        JComboBox<Bid> combo = new JComboBox<>();
        combo.setBounds(30, 55, 320, 35);
        combo.setBackground(Color.WHITE);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // ------------------------------------------------------------
        // LÓGICA VIP 1: ORDENAR LAS OFERTAS (VIPs PRIMERO)
        // ------------------------------------------------------------
        List<Bid> pujasPendientes = new ArrayList<>();
        for (Bid b : venta.getBidsRecibidas()) {
            if ("Pendiente".equals(b.getEstado())) {
                pujasPendientes.add(b);
            }
        }
        
        Collections.sort(pujasPendientes, (b1, b2) -> {
            boolean vip1 = b1.getComprador().esVIP();
            boolean vip2 = b2.getComprador().esVIP();
            
            if (vip1 && !vip2) return -1; // b1 va primero
            if (!vip1 && vip2) return 1;  // b2 va primero
            
            // Si ambos son o no son VIP, ordenamos por dinero de mayor a menor
            return Float.compare(b2.getImporte(), b1.getImporte());
        });

        for (Bid b : pujasPendientes) {
            combo.addItem(b);
        }

        // ------------------------------------------------------------
        // LÓGICA VIP 2: PINTAR LOS VIP DE DORADO EN EL DESPLEGABLE
        // ------------------------------------------------------------
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Bid) {
                    Bid b = (Bid) value;
                    if (b.getComprador().esVIP()) {
                        setText("<html><font color='#DAA520'><b>⭐ " + b.getComprador().getEmail() + "</b></font> - " + b.getImporte() + "€</html>");
                    } else {
                        setText(b.getComprador().getEmail() + " - " + b.getImporte() + "€");
                    }
                }
                return this;
            }
        });

        add(combo);

        JButton btnCobrar = new JButton("Aceptar Oferta y Cobrar");
        btnCobrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCobrar.setBackground(new Color(60, 179, 113)); // Verde oscuro
        btnCobrar.setForeground(Color.WHITE);
        btnCobrar.setFocusPainted(false);
        btnCobrar.setBounds(30, 115, 320, 40);
        add(btnCobrar);

        btnCobrar.addActionListener(e -> {
            Bid sel = (Bid) combo.getSelectedItem();
            if (sel != null) {
                try {
                    MainGUI.getBusinessLogic().aceptarOferta(sel);
                    MainGUI.getBusinessLogic().procesarCobro(sel);
                    
                    JOptionPane.showMessageDialog(this, "¡Venta cerrada y dinero transferido con éxito!");
                    dispose(); 
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