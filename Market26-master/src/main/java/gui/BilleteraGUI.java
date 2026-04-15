package gui;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import domain.Transaccion;

public class BilleteraGUI extends JFrame {
    
    private JLabel lblSaldoValor;

    public BilleteraGUI(String email) {
        setTitle("Mi Billetera - " + email);
        setSize(450, 500);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblSaldoTexto = new JLabel("Saldo Actual:");
        lblSaldoTexto.setBounds(250, 20, 100, 25);
        lblSaldoTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
        add(lblSaldoTexto);

        lblSaldoValor = new JLabel("0.0€");
        lblSaldoValor.setBounds(350, 20, 80, 25);
        lblSaldoValor.setForeground(new Color(0, 102, 0));
        lblSaldoValor.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblSaldoValor);

        JLabel lblCantidad = new JLabel("Cantidad (€):");
        lblCantidad.setBounds(20, 20, 100, 25);
        add(lblCantidad);

        JTextField txtCantidad = new JTextField();
        txtCantidad.setBounds(120, 20, 100, 25);
        add(txtCantidad);

        JButton btnRecarga = new JButton("Recargar");
        btnRecarga.setBounds(20, 60, 120, 40);
        add(btnRecarga);

        JButton btnRetirar = new JButton("Retirar");
        btnRetirar.setBounds(150, 60, 120, 40);
        add(btnRetirar);

        JButton btnVer = new JButton("Actualizar");
        btnVer.setBounds(280, 60, 130, 40);
        add(btnVer);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds(20, 120, 390, 300);
        add(scroll);

        // --- LÓGICA DE ACTUALIZACIÓN SEGURA ---
        btnVer.addActionListener(e -> {
            try {
                List<Transaccion> hist = MainGUI.getBusinessLogic().getHistorial(email);
                area.setText("--- HISTORIAL DE MOVIMIENTOS ---\n\n");
                
                float saldoCalculado = 0;

                if (hist == null || hist.isEmpty()) {
                    area.append("  Sin movimientos registrados.\n");
                } else {
                    for (Transaccion t : hist) {
                        // Cortamos la fecha para que no ocupe tanto
                        String fechaCorta = t.getFecha().toString().substring(0, 10);
                        area.append(" > [" + t.getTipo() + "] " + t.getImporte() + "€ (" + fechaCorta + ")\n");
                        
                        // Calculamos el saldo al vuelo leyendo el historial
                     // Calculamos el saldo al vuelo leyendo el historial
                        if (t.getTipo().equals("RECARGA") || t.getTipo().equals("INGRESO") || t.getTipo().equals("VENTA")) {
                            saldoCalculado += t.getImporte();
                        } else if (t.getTipo().equals("RETIRADA") || t.getTipo().equals("GASTO") || t.getTipo().equals("COMPRA")) {
                            saldoCalculado -= t.getImporte();
                        }
                    }
                }
                lblSaldoValor.setText(saldoCalculado + "€");
            } catch (Exception ex) {
                ex.printStackTrace();
                area.setText("Error al cargar el historial. Revisa la consola de Eclipse.");
            }
        });

        // --- ACCIÓN RECARGAR ---
        btnRecarga.addActionListener(e -> {
            try {
                float cant = Float.parseFloat(txtCantidad.getText().replace(",", "."));
                if (cant <= 0) throw new NumberFormatException();
                
                MainGUI.getBusinessLogic().recargarSaldo(email, cant);
                txtCantidad.setText("");
                btnVer.doClick(); // Clic automático para refrescar
                JOptionPane.showMessageDialog(this, "¡Recarga exitosa!");
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "Asegúrate de escribir un número (Ej: 50.0)"); 
            }
        });

        // --- ACCIÓN RETIRAR ---
        btnRetirar.addActionListener(e -> {
            try {
                float cant = Float.parseFloat(txtCantidad.getText().replace(",", "."));
                if (cant <= 0) throw new NumberFormatException();
                
                if(MainGUI.getBusinessLogic().retirarFondos(email, cant)) {
                    txtCantidad.setText("");
                    btnVer.doClick(); // Clic automático para refrescar
                    JOptionPane.showMessageDialog(this, "¡Retirada exitosa!");
                } else { 
                    JOptionPane.showMessageDialog(this, "No tienes suficiente saldo."); 
                }
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "Asegúrate de escribir un número (Ej: 50.0)"); 
            }
        });

        // Cargamos los datos nada más abrir la ventana
        btnVer.doClick();
    }
}