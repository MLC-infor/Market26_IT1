package dataAccess;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Seller;
import domain.User;
import domain.Buyer;
import domain.Bid;
import domain.Sale;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;
    private static final int baseSize = 160;

	private static final String basePath="src/main/resources/images/";

	ConfigXML c=ConfigXML.getInstance();

     public DataAccess()  {
		if (c.isDatabaseInitialized()) {
			String fileName=c.getDbFilename();

			File fileToDelete= new File(fileName);
			if(fileToDelete.delete()){
				File fileToDeleteTemp= new File(fileName+"$");
				fileToDeleteTemp.delete();
				System.out.println("File deleted");
			 } else {
				 System.out.println("Operation failed");
				}
		}
		open();
		if  (c.isDatabaseInitialized()) 
			initializeDB();
		System.out.println("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());

		close();

	}
     
    public DataAccess(EntityManager db) {
    	this.db=db;
    }

	
	/**
	 * This method initializes the database with some products, sellers, buyers and bids.
	 */	
	public void initializeDB(){
		
		db.getTransaction().begin();

		try { 
	       
		    // 1. Crear vendedores (Añadimos "1234" como contraseña genérica)
			Seller seller1=new Seller("seller1@gmail.com", "1234", "Aitor Fernandez");
			Seller seller2=new Seller("seller22@gmail.com", "1234", "Ane Gaztañaga");
			Seller seller3=new Seller("seller3@gmail.com", "1234", "Test Seller");

			// 2. Crear un comprador de prueba para poder hacer regateos
			Buyer buyer1 = new Buyer("comprador1@gmail.com", "1234", "Iker Comprador");
			
			// 3. Crear productos
			Date today = UtilDate.trim(new Date());
		
			// Usamos nuestro nuevo método: titulo, descripcion, precioOriginal, estado, fecha, foto
			Sale sale1 = seller1.addSale("futbol baloia", "oso polita, gutxi erabilita", 10f, "Abierta", today, 0);
			seller1.addSale("salomon mendiko botak", "44 zenbakia, 3 ateraldi", 20f, "Abierta", today, 0);
			seller1.addSale("samsung 42 telebista", "berria, erabili gabe", 175f, "Abierta", today, 0);

			seller2.addSale("imac 27", "7 urte, dena ondo dabil", 200f, "Abierta", today, 0);
			seller2.addSale("iphone 17", "oso gutxi erabilita", 400f, "Abierta", today, 0);
			seller2.addSale("orbea mendiko bizikleta", "29, 10 urte", 225f, "Abierta", today, 0);

			// 4. Crear una Solicitud (Bid) de prueba para mostrar en tu video
			// El comprador ofrece 8€ por el balon que cuesta 10€
			Bid bid1 = new Bid(8f, today, "Pendiente", buyer1, sale1);
			sale1.addBid(bid1);
			
			// 5. Guardar todo en la base de datos
			db.persist(seller1);
			db.persist(seller2);
			db.persist(seller3);
			db.persist(buyer1);
			// No hace falta persistir Sale ni Bid porque tienen CascadeType.ALL desde Seller y Buyer

			db.getTransaction().commit();
			System.out.println("Db initialized con Usuarios, Ofertas y Solicitudes!");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	public Sale createSale(String title, String description, int status, float price,  Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
		System.out.println(">> DataAccess: createProduct=> title= "+title+" seller="+sellerEmail);
		try {
			if(pubDate.before(UtilDate.trim(new Date()))) {
				throw new MustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorSaleMustBeLaterThanToday"));
			}
			if (file==null)
				throw new FileNotUploadedException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorFileNotUploadedException"));

			db.getTransaction().begin();
			
			Seller seller = db.find(Seller.class, sellerEmail);
			if (seller.doesSaleExist(title)) {
				db.getTransaction().commit();
				throw new SaleAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.SaleAlreadyExist"));
			}

			// Adaptado a nuestro nuevo modelo: asignamos foto=0 y estado="Abierta"
			Sale sale = seller.addSale(title, description, price, "Abierta", pubDate, 0);

			db.persist(seller); 
			db.getTransaction().commit();
			System.out.println("sale stored "+sale+ " "+seller);

			return sale;
		} catch (NullPointerException e) {
			e.printStackTrace();
			db.getTransaction().commit();
			return null;
		}
	}
	
	
	public List<Sale> getSales(String desc) {
		System.out.println(">> DataAccess: getProducts=> from= "+desc);

		List<Sale> res = new ArrayList<Sale>();	
		// Cambiado s.title por s.titulo
		TypedQuery<Sale> query = db.createQuery("SELECT s FROM Sale s WHERE s.titulo LIKE ?1",Sale.class);   
		query.setParameter(1, "%"+desc+"%");
		
		List<Sale> sales = query.getResultList();
	 	for (Sale sale:sales){
		   res.add(sale);
		}
	 	return res;
	}
	
	
	public List<Sale> getPublishedSales(String desc, Date pubDate) {
	    try {
	        // Traemos absolutamente todo de la tabla Sale, sin filtros de fecha
	        TypedQuery<Sale> query = db.createQuery("SELECT s FROM Sale s", Sale.class);   
	        return query.getResultList();
	    } catch (Exception e) {
	        return new ArrayList<Sale>();
	    }
	}

    public void open(){
		String fileName=c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);
			  db = emf.createEntityManager();
    	   }
		System.out.println("DataAccess opened => isDatabaseLocal: "+c.isDatabaseLocal());
	}

	public BufferedImage getFile(String fileName) {
		File file=new File(basePath+fileName);
		BufferedImage targetImg=null;
		try {
             targetImg = rescale(ImageIO.read(file));
        } catch (IOException ex) {
        }
		return targetImg;

	}
	
	public BufferedImage rescale(BufferedImage originalImage)
    {
		System.out.println("rescale "+originalImage);
        BufferedImage resizedImage = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
        g.dispose();
        return resizedImage;
    }
	
	/**
	 * Método para el Caso de Uso: Registrar Usuario
	 * Registra un nuevo Buyer o Seller en la base de datos.
	 * * @return true si se registra, false si el email ya existe.
	 */
	public boolean registrarUsuario(String nombre, String email, String password, boolean isSeller) {
		System.out.println(">> DataAccess: registrarUsuario=> email=" + email + " isSeller=" + isSeller);
		try {
			db.getTransaction().begin();

			// 1. Comprobamos si el usuario ya existe
			User existe = db.find(User.class, email);
			if (existe != null) {
				db.getTransaction().commit();
				return false; // El email ya está en uso
			}

			// 2. Creamos el usuario según el tipo
			if (isSeller) {
				Seller nuevoVendedor = new Seller(email, password, nombre);
				db.persist(nuevoVendedor);
			} else {
				Buyer nuevoComprador = new Buyer(email, password, nombre);
				db.persist(nuevoComprador);
			}

			db.getTransaction().commit();
			System.out.println("Usuario registrado correctamente: " + email);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().commit();
			return false;
		}
	}
	
	/**
	 * Método para el Caso de Uso: Hacer Login
	 * Comprueba si las credenciales son correctas.
	 * @return El objeto User si tiene éxito, o null si falla.
	 */
	public domain.User hacerLogin(String email, String password) {
		System.out.println(">> DataAccess: hacerLogin=> email=" + email);
		try {
			db.getTransaction().begin();
			
			// Buscamos al usuario por su email (que es la clave primaria)
			domain.User usuario = db.find(domain.User.class, email);
			
			// Si existe y la contraseña coincide, hacemos login
			if (usuario != null && usuario.getPassword().equals(password)) {
				db.getTransaction().commit();
				System.out.println("Login correcto para: " + email);
				return usuario;
			}
			
			db.getTransaction().commit();
			return null; // Credenciales incorrectas
			
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().commit();
			return null;
		}
	}
	
	// Caso de Uso: Aceptar Oferta / Ofrecer menor precio
    public Bid crearBid(float precio, Buyer comprador, Sale oferta) {
        try {
            db.getTransaction().begin();
            Buyer b = db.find(Buyer.class, comprador.getEmail());
            Sale s = db.find(Sale.class, oferta.getIdSale());

            String estadoBid = (precio >= s.getPrecioOriginal()) ? "Aceptada" : "Pendiente";
            Bid bid = new Bid(precio, new Date(), estadoBid, b, s);
            
            if (precio >= s.getPrecioOriginal()) s.setEstado("Cerrada");

            s.addBid(bid);
            db.persist(bid);
            db.getTransaction().commit();
            return bid;
        } catch (Exception e) {
            db.getTransaction().rollback();
            return null;
        }
    }

    // Caso de Uso: Ver Ofertas Aceptadas
    public List<Sale> getOfertasAceptadas(String emailVendedor) {
        TypedQuery<Sale> query = db.createQuery("SELECT s FROM Sale s WHERE s.vendedor.email=?1 AND s.estado='Cerrada'", Sale.class);
        query.setParameter(1, emailVendedor);
        return query.getResultList();
    }
	
	public void close(){
		db.close();
		System.out.println("DataAcess closed");
	}
	
}