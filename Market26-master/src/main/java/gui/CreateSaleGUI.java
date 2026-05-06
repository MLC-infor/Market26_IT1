package gui;

import java.util.*;
import javax.swing.*;
import com.toedter.calendar.JCalendar;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import businessLogic.BLFacade;
import configuration.UtilDate;

public class CreateSaleGUI extends JFrame {
    
    private static final long serialVersionUID = 1L;

    private String sellerMail;
    private JTextField fieldTitle = new JTextField();
    private JTextField fieldDescription = new JTextField();
    
    private JLabel jLabelTitle = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Title"));
    private JLabel jLabelDescription = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Description")); 
    private JLabel jLabelProductStatus = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Status"));
    private JLabel jLabelPrice = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Price"));
    private JTextField jTextFieldPrice = new JTextField();

    // NUEVO CAMPO PARA EL LINK DE LA IMAGEN
    private JLabel jLabelUrl = new JLabel("Link Imagen (Web):");
    private JTextField txtUrlImagen = new JTextField();

    private JCalendar jCalendar = new JCalendar();
    private Calendar calendarAct = null;
    private Calendar calendarAnt = null;

    private JScrollPane scrollPaneEvents = new JScrollPane();
    
    JComboBox<String> jComboBoxStatus = new JComboBox<String>();
    DefaultComboBoxModel<String> statusOptions = new DefaultComboBoxModel<String>();
    List<String> status;

    private JButton jButtonCreate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.CreateProduct"));
    private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
    private JLabel jLabelMsg = new JLabel();
    private JLabel jLabelError = new JLabel();
    private JFrame thisFrame;

    public CreateSaleGUI(String mail) {

        thisFrame = this;
        this.sellerMail = mail;
        this.getContentPane().setLayout(null);
        this.setSize(new Dimension(604, 400)); // Un pelín más alto
        this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.CreateProduct"));

        jLabelTitle.setBounds(new Rectangle(6, 24, 92, 20));
        fieldTitle.setBounds(98, 21, 250, 26);
        getContentPane().add(fieldTitle);
        this.getContentPane().add(jLabelTitle, null);

        jLabelDescription.setBounds(6, 56, 109, 16);
        fieldDescription.setBounds(98, 56, 250, 73);
        getContentPane().add(fieldDescription);
        getContentPane().add(jLabelDescription);

        jLabelPrice.setBounds(new Rectangle(6, 141, 101, 20));
        jTextFieldPrice.setBounds(new Rectangle(97, 141, 60, 20));
        this.getContentPane().add(jLabelPrice, null);
        this.getContentPane().add(jTextFieldPrice, null);
        
        jLabelProductStatus.setBounds(6, 185, 140, 25);
        getContentPane().add(jLabelProductStatus);
        
        status = Utils.getStatus();
        for(String s:status) statusOptions.addElement(s);
        jComboBoxStatus.setModel(statusOptions);
        jComboBoxStatus.setBounds(90, 183, 114, 27);
        getContentPane().add(jComboBoxStatus);

        // --- AÑADIMOS EL CAMPO DEL LINK ---
        jLabelUrl.setBounds(new Rectangle(6, 220, 120, 20));
        getContentPane().add(jLabelUrl);
        
        txtUrlImagen.setBounds(new Rectangle(130, 218, 215, 26));
        getContentPane().add(txtUrlImagen);
        // ----------------------------------

        // Botones (Movidos un poco hacia abajo para hacer sitio al link)
        jButtonCreate.setFont(new Font("Lucida Grande", Font.BOLD, 15));
        jButtonCreate.setBounds(new Rectangle(100, 265, 216, 41));

        jButtonCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jLabelMsg.setText("");
                String error = check_fields_Errors();
                if (error != null) 
                    jLabelMsg.setText(error);
                else
                    try {
                        BLFacade facade = MainGUI.getBusinessLogic();
                        float price = Float.parseFloat(jTextFieldPrice.getText());
                        int numStatus = jComboBoxStatus.getSelectedIndex();
                        
                        // Recogemos el texto que has pegado en el campo de la URL
                        String rutaImagen = txtUrlImagen.getText().trim();
                        
                        facade.createSale(fieldTitle.getText(), fieldDescription.getText(), numStatus, price,  UtilDate.trim(jCalendar.getDate()), sellerMail, rutaImagen);
                        
                        jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.ProductCreated"));
                    
                    } catch (Exception e1) {
                        jLabelMsg.setText(e1.getMessage());
                    }
            }
        });

        jButtonClose.setBounds(new Rectangle(328, 270, 101, 30));
        jButtonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                thisFrame.setVisible(false);            
            }
        });

        jLabelMsg.setBounds(new Rectangle(26, 315, 377, 20));
        jLabelMsg.setForeground(Color.red);
        this.getContentPane().add(jLabelMsg, null);

        jLabelError.setBounds(new Rectangle(16, 315, 384, 20));
        jLabelError.setForeground(Color.red);
        this.getContentPane().add(jLabelError, null);
        
        this.getContentPane().add(jButtonClose, null);
        this.getContentPane().add(jButtonCreate, null);
        
        // CALENDARIO
        jCalendar.setBounds(new Rectangle(360, 50, 225, 150));
        this.getContentPane().add(jCalendar, null);
        
        JLabel jLabelPublicationDate = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.PublicationDate"));
        jLabelPublicationDate.setBounds(360, 26, 197, 20);
        getContentPane().add(jLabelPublicationDate);

        this.jCalendar.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertychangeevent) {
                if (propertychangeevent.getPropertyName().equals("locale")) {
                    jCalendar.setLocale((Locale) propertychangeevent.getNewValue());
                } else if (propertychangeevent.getPropertyName().equals("calendar")) {
                    calendarAnt = (Calendar) propertychangeevent.getOldValue();
                    calendarAct = (Calendar) propertychangeevent.getNewValue();
                    int monthAnt = calendarAnt.get(Calendar.MONTH);
                    int monthAct = calendarAct.get(Calendar.MONTH);
                    if (monthAct != monthAnt) {
                        if (monthAct == monthAnt+2) { 
                            calendarAct.set(Calendar.MONTH, monthAnt+1);
                            calendarAct.set(Calendar.DAY_OF_MONTH, 1);
                        }
                        jCalendar.setCalendar(calendarAct);                     
                    }
                    jCalendar.setCalendar(calendarAct);
                    int offset = jCalendar.getCalendar().get(Calendar.DAY_OF_WEEK);
                    if (Locale.getDefault().equals(new Locale("es")))
                        offset += 4;
                    else
                        offset += 5;
                }
            }
        });
    }   

    private String check_fields_Errors() {
        try {
            if ((fieldTitle.getText().length() == 0) || (fieldDescription.getText().length() == 0)  || (jTextFieldPrice.getText().length() == 0))
                return ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.ErrorQuery");
            else {
                float price = Float.parseFloat(jTextFieldPrice.getText());
                if (price <= 0) 
                    return ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.PriceMustBeGreaterThan0");
                else 
                    return null;
            }
        } catch (java.lang.NumberFormatException e1) {
            return  ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.ErrorNumber");       
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
