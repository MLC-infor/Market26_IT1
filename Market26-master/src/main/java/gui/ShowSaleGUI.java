package gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

import domain.Sale;
import domain.Bid;
import domain.Buyer;
import domain.Pregunta;

public class ShowSaleGUI extends JFrame {
    private JFrame thisFrame;
    private JTextArea areaPreguntas; 

    public ShowSaleGUI(Sale sale, String emailLogueado) {
        thisFrame = this;
        setTitle("Detalle de Producto");
        setSize(480, 520); // Un poco más alto y ancho para que respire
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        // ============================================================
        // ZONA SUPERIOR: TARJETA DE PRODUCTO MODERNA
        // ============================================================
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(null);
        panelHeader.setBounds(15, 15, 435, 120);
        panelHeader.setBackground(new Color(250, 250, 250)); // Gris muy clarito
        panelHeader.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        getContentPane().add(panelHeader);

        // 1. ESPACIO PARA LA IMAGEN
        JLabel lblImagen = new JLabel("Cargando...", SwingConstants.CENTER);
        lblImagen.setBounds(10, 10, 100, 100);
        lblImagen.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panelHeader.add(lblImagen);

        // Hilo en segundo plano para que la ventana no se congele si la imagen tarda en cargar por internet
        new Thread(() -> {
            try {
                String urlStr = sale.getUrlImagen();
                if (urlStr != null && !urlStr.isEmpty()) {
                    Image img = new ImageIcon(new URL(urlStr)).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    lblImagen.setIcon(new ImageIcon(img));
                    lblImagen.setText("");
                } else {
                    lblImagen.setText("Sin Foto");
                }
            } catch (Exception ex) {
                lblImagen.setText("Sin Foto");
            }
        }).start();

        // 2. INFO DEL PRODUCTO
        JLabel lblTitle = new JLabel(sale.getTitulo());
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(new Color(50, 50, 50));
        lblTitle.setBounds(125, 10, 300, 25);
        panelHeader.add(lblTitle);

        JLabel lblPrice = new JLabel(sale.getPrecioOriginal() + " €");
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblPrice.setForeground(new Color(0, 128, 0)); // Verde dinero
        lblPrice.setBounds(125, 40, 300, 30);
        panelHeader.add(lblPrice);

        // 3. VALORACIÓN DEL VENDEDOR ARREGLADA (Con estrella ★)
        JLabel lblValoracion = new JLabel("Valoración Vendedor: Cargando...");
        lblValoracion.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblValoracion.setBounds(125, 85, 300, 20); 
        
        try {
            String emailDueño = sale.getVendedor().getEmail();
            float media = MainGUI.getBusinessLogic().getValoracionMedia(emailDueño);
            if (media == 0.0f) {
                lblValoracion.setText("★ Vendedor sin valoraciones");
                lblValoracion.setForeground(Color.GRAY);
            } else {
                lblValoracion.setText("★ Valoración del vendedor: " + String.format("%.1f", media) + " / 5.0");
                lblValoracion.setForeground(new Color(218, 165, 32)); // Dorado
            }
        } catch (Exception e) {
            lblValoracion.setText("No se pudo cargar la valoración.");
        }
        panelHeader.add(lblValoracion);

        // ============================================================
        // ZONA MEDIA: OFERTAS Y GESTIÓN
        // ============================================================
        if (emailLogueado.equals(sale.getVendedor().getEmail())) {
            JButton btnGestionar = new JButton("Gestionar Venta y Ver Ofertas");
            btnGestionar.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnGestionar.setBounds(95, 155, 280, 40); 
            btnGestionar.setBackground(new Color(70, 130, 180)); // Azul acero
            btnGestionar.setForeground(Color.WHITE);
            getContentPane().add(btnGestionar);

            btnGestionar.addActionListener(e -> {
                JFrame g = new CerrarVentaGUI(sale, emailLogueado); 
                g.setVisible(true);
                thisFrame.dispose();
            });

        } else {
            JLabel lblTuOferta = new JLabel("Tu Oferta (€):");
            lblTuOferta.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblTuOferta.setBounds(40, 160, 100, 25); 
            getContentPane().add(lblTuOferta);

            JTextField fieldOffer = new JTextField();
            fieldOffer.setFont(new Font("SansSerif", Font.PLAIN, 14));
            fieldOffer.setBounds(140, 155, 90, 35); 
            getContentPane().add(fieldOffer);

            JButton btnMakeOffer = new JButton("Hacer Oferta");
            btnMakeOffer.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnMakeOffer.setBounds(245, 155, 140, 35); 
            btnMakeOffer.setBackground(new Color(60, 179, 113)); // Verde mar
            btnMakeOffer.setForeground(Color.WHITE);
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
                        JOptionPane.showMessageDialog(thisFrame, "Error: No tienes saldo suficiente.", "Saldo Insuficiente", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(thisFrame, "Error: Introduce un precio válido.");
                }
            });
        }

        // ============================================================
        // ZONA INFERIOR: PREGUNTAS Y RESPUESTAS 
        // ============================================================
        JSeparator separator = new JSeparator();
        separator.setBounds(15, 215, 435, 2);
        getContentPane().add(separator);

        JLabel lblPreguntas = new JLabel("Preguntas y Respuestas:");
        lblPreguntas.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPreguntas.setBounds(20, 225, 200, 20);
        getContentPane().add(lblPreguntas);

        areaPreguntas = new JTextArea();
        areaPreguntas.setEditable(false);
        areaPreguntas.setFont(new Font("SansSerif", Font.PLAIN, 12));
        areaPreguntas.setBackground(new Color(245, 245, 245));
        areaPreguntas.setMargin(new Insets(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(areaPreguntas);
        scrollPane.setBounds(20, 250, 425, 130);
        getContentPane().add(scrollPane);

        cargarHistorialPreguntas(sale); 

        JTextField fieldNuevaPregunta = new JTextField();
        fieldNuevaPregunta.setFont(new Font("SansSerif", Font.PLAIN, 13));
        fieldNuevaPregunta.setBounds(20, 395, 310, 35);
        getContentPane().add(fieldNuevaPregunta);

        JButton btnAccionPregunta = new JButton();
        btnAccionPregunta.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnAccionPregunta.setBounds(335, 395, 110, 35);
        getContentPane().add(btnAccionPregunta);

        if (emailLogueado.equals(sale.getVendedor().getEmail())) {
            fieldNuevaPregunta.setToolTipText("Escribe tu respuesta...");
            btnAccionPregunta.setText("Responder");
            btnAccionPregunta.setBackground(new Color(173, 216, 230)); 
            
            btnAccionPregunta.addActionListener(e -> {
                String respuesta = fieldNuevaPregunta.getText();
                if (respuesta.isEmpty()) return;

                Pregunta preguntaSinResponder = null;
                for (Pregunta p : sale.getPreguntas()) {
                    if (p.getTextoRespuesta() == null || p.getTextoRespuesta().trim().isEmpty()) {
                        preguntaSinResponder = p;
                        break;
                    }
                }

                if (preguntaSinResponder != null) {
                    boolean exito = MainGUI.getBusinessLogic().responderPregunta(preguntaSinResponder.getIdPregunta(), respuesta);
                    if (exito) {
                        preguntaSinResponder.setTextoRespuesta(respuesta); 
                        fieldNuevaPregunta.setText("");
                        cargarHistorialPreguntas(sale); 
                    }
                } else {
                    JOptionPane.showMessageDialog(thisFrame, "No hay preguntas pendientes de responder.");
                }
            });
        } else {
            fieldNuevaPregunta.setToolTipText("Escribe tu duda...");
            btnAccionPregunta.setText("Preguntar");
            btnAccionPregunta.setBackground(new Color(255, 250, 205)); 
            
            btnAccionPregunta.addActionListener(e -> {
                String duda = fieldNuevaPregunta.getText();
                if (duda.isEmpty()) return;

                Pregunta p = MainGUI.getBusinessLogic().hacerPregunta(duda, sale.getIdSale(), emailLogueado);
                if (p != null) {
                    sale.addPregunta(p); 
                    fieldNuevaPregunta.setText("");
                    cargarHistorialPreguntas(sale); 
                } else {
                    JOptionPane.showMessageDialog(thisFrame, "Error al enviar la pregunta.");
                }
            });
        }
    }

    private void cargarHistorialPreguntas(Sale sale) {
        areaPreguntas.setText("");
        if (sale.getPreguntas() == null || sale.getPreguntas().isEmpty()) {
            areaPreguntas.append("Todavía no hay preguntas sobre este producto.\n");
            return;
        }

        for (Pregunta p : sale.getPreguntas()) {
            areaPreguntas.append("Q: " + p.getTextoDuda() + " (" + p.getComprador().getEmail() + ")\n");
            if (p.getTextoRespuesta() != null && !p.getTextoRespuesta().trim().isEmpty()) {
                areaPreguntas.append("A: " + p.getTextoRespuesta() + "\n");
            } else {
                areaPreguntas.append("A: [El vendedor aún no ha respondido]\n");
            }
            areaPreguntas.append("--------------------------------------------------\n");
        }
        areaPreguntas.setCaretPosition(areaPreguntas.getDocument().getLength());
    }
}