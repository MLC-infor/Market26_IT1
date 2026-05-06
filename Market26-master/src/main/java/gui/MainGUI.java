package gui;

import javax.swing.*;
import businessLogic.BLFacade;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainGUI extends JFrame {
	
	private String sellerMail;
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonQueryQueries = null;
	private JButton jButtonRegister = null; 
	private JButton jButtonLogin = null;
	private JButton jButtonVerAceptadas = null;
	private JButton jButtonBilletera = null; 
	private JButton jButtonVIP = null; 
	private JButton jButtonLogout = null; 
	
	private JLabel lblSaldo = null; 
	private JLabel lblMiValoracion = null; // NUEVO: Etiqueta para la valoración

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
	
	public MainGUI(String mail) {
		super();
		this.sellerMail = mail;
		
		boolean isLoggedIn = (sellerMail != null && !sellerMail.trim().isEmpty());
		
		this.setSize(500, isLoggedIn ? 500 : 300); 
		this.setLocationRelativeTo(null); 
		
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 14));
		jLabelSelectOption.setForeground(new Color(50, 50, 50)); 
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		
		rdbtnNewRadioButton = new JRadioButton("English");
		rdbtnNewRadioButton.addActionListener(e -> { Locale.setDefault(new Locale("en")); paintAgain(); });
		rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
		rdbtnNewRadioButton_1.addActionListener(e -> { Locale.setDefault(new Locale("eus")); paintAgain(); });
		rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
		rdbtnNewRadioButton_2.addActionListener(e -> { Locale.setDefault(new Locale("es")); paintAgain(); });
		
		buttonGroup.add(rdbtnNewRadioButton_1);
		buttonGroup.add(rdbtnNewRadioButton_2);
		buttonGroup.add(rdbtnNewRadioButton);
		
		panel = new JPanel();
		panel.add(rdbtnNewRadioButton_1);
		panel.add(rdbtnNewRadioButton_2);
		panel.add(rdbtnNewRadioButton);
		
		jButtonCreateQuery = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateSale"));
		jButtonCreateQuery.addActionListener(e -> new CreateSaleGUI(sellerMail).setVisible(true));
		
		jButtonQueryQueries = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QuerySales"));
		jButtonQueryQueries.addActionListener(e -> {
		    JFrame a = new QuerySalesGUI(sellerMail);
		    a.addWindowListener(new WindowAdapter() { public void windowClosed(WindowEvent ev) { actualizarSaldo(); } });
		    a.setVisible(true);
		});
		
		jButtonRegister = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Register"));
		jButtonRegister.setBackground(new Color(230, 240, 255)); 
		jButtonRegister.addActionListener(e -> new RegisterGUI().setVisible(true));
		
		jButtonLogin = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Login"));
		jButtonLogin.setBackground(new Color(200, 255, 200)); 
		jButtonLogin.addActionListener(e -> {
			new LoginGUI().setVisible(true);
			this.dispose(); 
		});
		
		jButtonVerAceptadas = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.VerAceptadas"));
        jButtonVerAceptadas.addActionListener(e -> new VerOfertasAceptadasGUI(sellerMail).setVisible(true));

        jButtonBilletera = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Billetera"));
        jButtonBilletera.addActionListener(e -> {
            JFrame a = new BilleteraGUI(sellerMail);
            a.addWindowListener(new WindowAdapter() { public void windowClosed(WindowEvent ev) { actualizarSaldo(); } });
            a.setVisible(true);
        });

        jButtonVIP = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.VIPButton"));
        jButtonVIP.setBackground(new Color(255, 215, 0)); 
        jButtonVIP.addActionListener(e -> {
            JFrame a = new SuscripcionVIPGUI(sellerMail);
            a.addWindowListener(new WindowAdapter() { public void windowClosed(WindowEvent ev) { actualizarSaldo(); } });
            a.setVisible(true);
        });
        
        jButtonLogout = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.LogoutButton"));
        jButtonLogout.setBackground(new Color(255, 100, 100)); 
        jButtonLogout.setForeground(Color.WHITE); 
        jButtonLogout.setFont(new Font("Tahoma", Font.BOLD, 12));
        jButtonLogout.addActionListener(e -> {
            JFrame a = new MainGUI(null); 
            a.setVisible(true);
            this.dispose();
        });

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);

        jContentPane = new JPanel();
        jContentPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        jContentPane.setBackground(Color.WHITE);
        
        if (!isLoggedIn) {
        	jContentPane.setLayout(new GridLayout(4, 1, 10, 15)); 
        	jContentPane.add(jLabelSelectOption);
        	jContentPane.add(jButtonLogin);
        	jContentPane.add(jButtonRegister);
        	jContentPane.add(panel);
        	setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + " - Bienvenido Invitado");
        } else {
        	jContentPane.setLayout(new GridLayout(8, 1, 10, 10)); 
        	jContentPane.add(jLabelSelectOption);
        	jContentPane.add(jButtonQueryQueries);
        	jContentPane.add(jButtonCreateQuery);
        	jContentPane.add(jButtonVerAceptadas); 
        	jContentPane.add(jButtonBilletera); 
        	jContentPane.add(jButtonVIP); 
        	jContentPane.add(jButtonLogout);
        	jContentPane.add(panel);
        	setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + ": " + sellerMail);
            
            // --- HEADER CON EL SALDO Y VALORACIÓN ---
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
            headerPanel.setBackground(new Color(245, 245, 245));
            headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200))); 
            
            // 1. Etiqueta de Valoración
            lblMiValoracion = new JLabel();
            lblMiValoracion.setFont(new Font("SansSerif", Font.BOLD, 14));
            lblMiValoracion.setForeground(new Color(218, 165, 32)); // Dorado
            actualizarValoracion();
            headerPanel.add(lblMiValoracion);
            
            // 2. Etiqueta de Saldo
            lblSaldo = new JLabel();
            lblSaldo.setFont(new Font("SansSerif", Font.BOLD, 14));
            lblSaldo.setForeground(new Color(0, 100, 0)); 
            actualizarSaldo(); 
            headerPanel.add(lblSaldo);
            
            wrapperPanel.add(headerPanel, BorderLayout.NORTH);
        }
		
        wrapperPanel.add(jContentPane, BorderLayout.CENTER);
		setContentPane(wrapperPanel);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	public void actualizarSaldo() {
	    if (sellerMail != null && !sellerMail.trim().isEmpty() && lblSaldo != null) {
	        float saldoActual = MainGUI.getBusinessLogic().getSaldoUsuario(sellerMail);
	        lblSaldo.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Balance") + " " + String.format("%.2f", saldoActual) + " €");
	    }
	}
	
	public void actualizarValoracion() {
        if (sellerMail != null && !sellerMail.trim().isEmpty() && lblMiValoracion != null) {
            float miValoracion = MainGUI.getBusinessLogic().getValoracionMedia(sellerMail);
            String txtBase = ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MyRating");
            
            if (miValoracion > 0) {
                lblMiValoracion.setText(txtBase + " " + String.format("%.1f", miValoracion) + " / 5.0");
            } else {
                lblMiValoracion.setText(txtBase + " " + ResourceBundle.getBundle("Etiquetas").getString("MainGUI.NoVotes"));
            }
        }
    }
	
	private void paintAgain() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QuerySales"));
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateSale"));
		jButtonRegister.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Register"));
		jButtonLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Login"));
		jButtonVerAceptadas.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.VerAceptadas"));
		jButtonBilletera.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Billetera"));
		
		if(jButtonVIP != null) jButtonVIP.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.VIPButton"));
		if(jButtonLogout != null) jButtonLogout.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.LogoutButton"));
		
		actualizarSaldo();
		actualizarValoracion(); // Actualizamos la traducción de la valoración
		
		if(sellerMail != null && !sellerMail.trim().isEmpty()) {
			this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle")+ ": "+sellerMail);
		} else {
			this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + " - Bienvenido Invitado");
		}
	}
}