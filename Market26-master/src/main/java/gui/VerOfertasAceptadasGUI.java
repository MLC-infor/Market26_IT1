package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import businessLogic.BLFacade;
import domain.Sale;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

public class VerOfertasAceptadasGUI extends JFrame {
    private JTable table = new JTable();
    private DefaultTableModel model;
    private String emailLogueado;

    public VerOfertasAceptadasGUI(String email) {
        // 1. Guardamos el email del usuario que ha entrado
        this.emailLogueado = email;
        
        setTitle("Mis Ofertas Aceptadas / Compras");
        setSize(500, 300);
        setLocationRelativeTo(null); // Centrar en pantalla

        // 2. Modelo de tabla bloqueado (no editable) con una columna extra
        model = new DefaultTableModel(null, new String[]{"Título", "Precio Final", "Estado", "ObjetoVenta"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ¡MAGIA! Esto bloquea que se pueda escribir en las celdas
            }
        };
        table.setModel(model);
        
        // Ocultamos la columna 3 (donde va el objeto) para que el usuario no vea letras raras
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(3));

        JScrollPane scroll = new JScrollPane(table);
        getContentPane().add(scroll);

        // 3. Cargamos los datos reales del usuario logueado
        BLFacade facade = MainGUI.getBusinessLogic();
        List<Sale> aceptadas = facade.getOfertasAceptadas(emailLogueado);

        if (aceptadas != null) {
            for (Sale s : aceptadas) {
                Vector<Object> row = new Vector<>();
                row.add(s.getTitulo());
                row.add(s.getPrecioOriginal()); 
                row.add(s.getEstado());
                row.add(s); // Guardamos la venta en la columna invisible
                model.addRow(row);
            }
        }

        // 4. Detectar el doble clic para VALORAR
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    JTable t = (JTable) mouseEvent.getSource();
                    int row = t.rowAtPoint(mouseEvent.getPoint());
                    
                    // Recuperamos la venta de la columna invisible
                    Sale ventaSeleccionada = (Sale) model.getValueAt(row, 3);
                    
                    // Comprobamos si somos el comprador o el vendedor
                    if (!ventaSeleccionada.getVendedor().getEmail().equals(emailLogueado)) {
                        // Somos el comprador: ¡Abrimos la ventana de valorar!
                        ValorarGUI ventanaValorar = new ValorarGUI(ventaSeleccionada, emailLogueado);
                        ventanaValorar.setVisible(true);
                    } else {
                        // Somos el vendedor: Avisamos de que no puede votarse a sí mismo
                        JOptionPane.showMessageDialog(null, "Eres el vendedor de este producto. El comprador es quien debe dejar la valoración.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }
}