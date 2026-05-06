package gui;

import java.awt.Color;
import java.net.URL;
import java.util.Locale;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;

public class ApplicationLauncher { 
	
	public static void main(String[] args) {

		ConfigXML c=ConfigXML.getInstance();		
		Locale.setDefault(new Locale(c.getLocale()));

		try {
			
			BLFacade appFacadeInterface;
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			
			if (c.isBusinessLogicLocal()) {
				DataAccess da= new DataAccess();
				appFacadeInterface=new BLFacadeImplementation(da);
			}
			else { //If remote
				 String serviceName= "http://"+c.getBusinessLogicNode() +":"+ c.getBusinessLogicPort()+"/ws/"+c.getBusinessLogicName()+"?wsdl";	 
				 URL url = new URL(serviceName);

		        //1st argument refers to wsdl document above
				//2nd argument is service name, refer to wsdl document above
		        QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
		 
		        Service service = Service.create(url, qname);

		        appFacadeInterface = service.getPort(BLFacade.class);
			} 
			
			// 1. Cargamos la lógica ANTES de abrir las ventanas
			MainGUI.setBussinessLogic(appFacadeInterface);
			
			// ==========================================================
			// 2. EL TRUCO PARA EL VÍDEO: ABRIR DOS VENTANAS A LA VEZ
			// ==========================================================
			
			// Ventana 1 (Se abrirá en la parte izquierda de tu pantalla)
			MainGUI ventana1 = new MainGUI(null);
			ventana1.setLocation(100, 200); 
			ventana1.setVisible(true);

			// Ventana 2 (Se abrirá en la parte derecha de tu pantalla)
			MainGUI ventana2 = new MainGUI(null);
			ventana2.setLocation(800, 200); 
			ventana2.setVisible(true);
			
			// ==========================================================
			
		}catch (Exception e) {
			System.out.println("Error in ApplicationLauncher: "+e.toString());
		}
	}
}
