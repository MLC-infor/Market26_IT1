package gui;

import javax.swing.*;
import java.awt.*;
import domain.Sale;

public class ValorarGUI extends JFrame {

    public ValorarGUI(Sale venta, String emailComprador) {
        setTitle("Valorar Transacción");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null); // Centrar en pantalla

        String emailVendedor = venta.getVendedor().getEmail();

        JLabel lblTitulo = new JLabel("Valora al vendedor: " + venta.getVendedor().getName());
        lblTitulo.setBounds(20, 20, 350, 20);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblTitulo);

        JLabel lblNota = new JLabel("Puntuación (1-5):");
        lblNota.setBounds(20, 60, 150, 20);
        add(lblNota);

        // Desplegable de 1 a 5 estrellas
        Integer[] notas = {5, 4, 3, 2, 1};
        JComboBox<Integer> comboNota = new JComboBox<>(notas);
        comboNota.setBounds(150, 60, 50, 25);
        add(comboNota);

        JLabel lblComentario = new JLabel("Comentario:");
        lblComentario.setBounds(20, 100, 100, 20);
        add(lblComentario);

        JTextArea txtComentario = new JTextArea();
        txtComentario.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(txtComentario);
        scroll.setBounds(20, 130, 340, 70);
        add(scroll);

        JButton btnEnviar = new JButton("Enviar Valoración");
        btnEnviar.setBounds(100, 215, 200, 35);
        btnEnviar.setBackground(new Color(200, 255, 200));
        add(btnEnviar);

        btnEnviar.addActionListener(e -> {
            int nota = (Integer) comboNota.getSelectedItem();
            String comentario = txtComentario.getText();

            boolean exito = MainGUI.getBusinessLogic().valorarVendedor(nota, comentario, emailComprador, emailVendedor, venta);

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Gracias por tu valoración!");
                dispose(); // Cierra la ventana
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un error al enviar la valoración.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}