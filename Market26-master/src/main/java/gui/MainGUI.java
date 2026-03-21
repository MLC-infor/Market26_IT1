package gui;

/**
 * @author Software Engineering teachers
 */

import javax.swing.*;

import businessLogic.BLFacade;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGUI extends JFrame {
	
	private String sellerMail;
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonQueryQueries = null;
	private JButton jButtonRegister = null; // NUEVO BOTÓN
	private JButton jButtonLogin = null;
	private JButton jButtonVerAceptadas = null;

	private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	
	public static void setBussinessLogic (BLFacade facade){
		appFacadeInterface=facade;
	}
	protected JLabel jLabelSelectOption;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	/**
	 * This is the default constructor
	 */
	public MainGUI(String mail) {
		super();

		this.sellerMail=mail;
		
		// He hecho la ventana un poco más alta para que quepa el nuevo botón (de 290 a 340)
		this.setSize(495, 340); 
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		
		rdbtnNewRadioButton = new JRadioButton("English");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("en"));
				paintAgain();				
			}
		});
		buttonGroup.add(rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("eus"));
				paintAgain();				
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_1);
		
		rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("es"));
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_2);
	
		panel = new JPanel();
		panel.add(rdbtnNewRadioButton_1);
		panel.add(rdbtnNewRadioButton_2);
		panel.add(rdbtnNewRadioButton);
		
		jButtonCreateQuery = new JButton();
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateSale"));
		jButtonCreateQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new CreateSaleGUI(sellerMail);
				a.setVisible(true);
			}
		});
		
		jButtonQueryQueries = new JButton();
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QuerySales"));
		jButtonQueryQueries.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new QuerySalesGUI();
				a.setVisible(true);
			}
		});
		
		// CONFIGURACIÓN DEL NUEVO BOTÓN
		jButtonRegister = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Register"));
		jButtonRegister.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new RegisterGUI();
				a.setVisible(true);
			}
		});
		
		jButtonLogin = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Login"));
		jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new LoginGUI();
				a.setVisible(true);
			}
		});
		
		// NUEVO BOTÓN: Ver Ventas Aceptadas
		// Cambia la línea de creación por esta:
		jButtonVerAceptadas = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.VerAceptadas"));
        jButtonVerAceptadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFrame a = new VerOfertasAceptadasGUI();
                a.setVisible(true);
            }
        });
		
        jContentPane = new JPanel();
        jContentPane.setLayout(new GridLayout(7, 1, 0, 0)); // Cambiado a 7 filas
        jContentPane.add(jLabelSelectOption);
        jContentPane.add(jButtonCreateQuery);
        jContentPane.add(jButtonQueryQueries);
        jContentPane.add(jButtonRegister);
        jContentPane.add(jButtonLogin);
        jContentPane.add(jButtonVerAceptadas); // Añadimos el nuevo botón al panel
        jContentPane.add(panel);
		
		setContentPane(jContentPane);
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") +": "+sellerMail);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	private void paintAgain() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QuerySales"));
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateSale"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle")+ ": "+sellerMail);
		
		// ESTAS SON LAS DOS LÍNEAS NUEVAS:
		jButtonRegister.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Register"));
		jButtonLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Login"));
		jButtonVerAceptadas.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.VerAceptadas"));
	}
	
}