package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import businessLogic.BLFacade;
import domain.Sale;
import java.util.List;
import java.util.Vector;

public class VerOfertasAceptadasGUI extends JFrame {
    private JTable table = new JTable();
    private DefaultTableModel model = new DefaultTableModel(null, new String[]{"Título", "Precio Final", "Estado"});

    public VerOfertasAceptadasGUI() {
        setTitle("Mis Ventas Aceptadas");
        setSize(500, 300);
        JScrollPane scroll = new JScrollPane(table);
        table.setModel(model);
        getContentPane().add(scroll);

        BLFacade facade = MainGUI.getBusinessLogic();
        // Consultamos las ventas de seller1@gmail.com (el de prueba)
        List<Sale> aceptadas = facade.getOfertasAceptadas("seller1@gmail.com");

        for (Sale s : aceptadas) {
            Vector<Object> row = new Vector<>();
            row.add(s.getTitulo());
            row.add(s.getPrecioOriginal()); // En una oferta aceptada, es el precio final
            row.add(s.getEstado());
            model.addRow(row);
        }
        setVisible(true);
    }
}