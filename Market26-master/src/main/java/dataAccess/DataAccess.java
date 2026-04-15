package dataAccess;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.persistence.*;
import configuration.ConfigXML;
import configuration.UtilDate;
import domain.*;
import exceptions.*;

public class DataAccess {
    private EntityManager db;
    private EntityManagerFactory emf;
    private static final int baseSize = 160;
    private static final String basePath = "src/main/resources/images/";
    ConfigXML c = ConfigXML.getInstance();

    public DataAccess() {
        if (c.isDatabaseInitialized()) {
            File file = new File(c.getDbFilename());
            file.delete();
            new File(c.getDbFilename() + "$").delete();
        }
        open();
        if (c.isDatabaseInitialized()) initializeDB();
        close();
    }

    public void open() {
        String fileName = c.getDbFilename();
        if (c.isDatabaseLocal()) {
            emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
        } else {
            Map<String, String> props = new HashMap<>();
            props.put("javax.persistence.jdbc.user", c.getUser());
            props.put("javax.persistence.jdbc.password", c.getPassword());
            emf = Persistence.createEntityManagerFactory("objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, props);
        }
        db = emf.createEntityManager();
    }

    public void close() { db.close(); }

    public void initializeDB() {
        db.getTransaction().begin();
        try {
        	Seller seller1=new Seller("seller1@gmail.com", "1234", "Aitor Fernandez");
			Seller seller2=new Seller("seller22@gmail.com", "1234", "Ane Gaztañaga");
			Seller seller3=new Seller("seller3@gmail.com", "1234", "Test Seller");

			Buyer buyer1 = new Buyer("comprador1@gmail.com", "1234", "Iker Comprador");
			Date today = UtilDate.trim(new Date());
		
			Sale sale1 = seller1.addSale("futbol baloia", "oso polita, gutxi erabilita", 10f, "Abierta", today, 0);
			seller1.addSale("salomon mendiko botak", "44 zenbakia, 3 ateraldi", 20f, "Abierta", today, 0);
			seller1.addSale("samsung 42 telebista", "berria, erabili gabe", 175f, "Abierta", today, 0);

			seller2.addSale("imac 27", "7 urte, dena ondo dabil", 200f, "Abierta", today, 0);
			seller2.addSale("iphone 17", "oso gutxi erabilita", 400f, "Abierta", today, 0);
			seller2.addSale("orbea mendiko bizikleta", "29, 10 urte", 225f, "Abierta", today, 0);

			Bid bid1 = new Bid(8f, today, "Pendiente", buyer1, sale1);
			sale1.addBid(bid1);
			
			// 🎓 ANTIDOPING: persist() coge los objetos de Java y los mete en ObjectDB.
			db.persist(seller1);
			db.persist(seller2);
			db.persist(seller3);
			db.persist(buyer1);;
            db.getTransaction().commit();
        } catch (Exception e) { e.printStackTrace(); db.getTransaction().rollback(); }
    }

    public domain.User hacerLogin(String email, String password) {
        db.getTransaction().begin();
        domain.User u = db.find(domain.User.class, email);
        db.getTransaction().commit();
        if (u != null && u.getPassword().equals(password)) return u;
        return null;
    }

    public boolean registrarUsuario(String nombre, String email, String password, boolean isSeller) {
        db.getTransaction().begin();
        if (db.find(User.class, email) != null) { db.getTransaction().commit(); return false; }
        User u = isSeller ? new Seller(email, password, nombre) : new Buyer(email, password, nombre);
        db.persist(u);
        db.getTransaction().commit();
        return true;
    }

    public Sale createSale(String title, String desc, int stat, float price, Date date, String email, File f) {
        db.getTransaction().begin();
        try {
            Seller s = db.find(Seller.class, email);
            
            // Creamos la oferta y nos aseguramos de que el estado sea "Abierta"
            Sale sale = s.addSale(title, desc, price, "Abierta", date, 0);
            
            // 🎓 ¡LA CLAVE ESTÁ AQUÍ! Forzamos a la base de datos a guardar la oferta
            db.persist(sale); 
            db.persist(s);
            
            db.getTransaction().commit();
            System.out.println("Oferta guardada en BD: " + title); // Chivato en la consola
            return sale;
        } catch (Exception e) {
            db.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
    }

    public List<Sale> getPublishedSales(String desc, Date date) {
        TypedQuery<Sale> q = db.createQuery("SELECT s FROM Sale s WHERE s.estado='Abierta'", Sale.class);
        return q.getResultList();
    }

    public Bid crearBid(float precio, Buyer comprador, Sale oferta) {
        db.getTransaction().begin();
        try {
            Buyer b = db.find(Buyer.class, comprador.getEmail());
            Sale s = db.find(Sale.class, oferta.getIdSale());

            // 🎓 DOBLE VERIFICACIÓN (Parte 1): ¿Tiene dinero para hacer la oferta?
            if (b.getMonedero().getSaldo() < precio) {
                db.getTransaction().rollback(); // Cancelamos la operación
                return null; // Devolvemos null para avisar a la pantalla de que no se puede
            }

            Bid bid = new Bid(precio, new Date(), "Pendiente", b, s);
            s.addBid(bid);
            db.persist(bid);
            db.getTransaction().commit();
            return bid;
        } catch (Exception e) {
            db.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
    }

 // -------------------------------------------------------------------
    // CASO DE USO 1: Seleccionar Oferta Ganadora
    // -------------------------------------------------------------------
    public void aceptarOferta(Bid bid) {
        db.getTransaction().begin();
        try {
            Bid b = db.find(Bid.class, bid.getIdBid());
            Sale s = b.getOferta();

            // Cambiamos el estado de todas las ofertas
            for (Bid o : s.getBidsRecibidas()) {
                if (o.getIdBid().equals(b.getIdBid())) {
                    o.setEstado("Aceptada");
                } else {
                    o.setEstado("Rechazada");
                }
            }
            // Cerramos la venta
            s.setEstado("Cerrada");
            
            db.getTransaction().commit();
        } catch (Exception e) { 
            db.getTransaction().rollback(); 
            throw e; 
        }
    }

    // -------------------------------------------------------------------
    // CASO DE USO 2: Procesar Cobro
    // -------------------------------------------------------------------
    public void procesarCobro(Bid bid) {
        db.getTransaction().begin();
        try {
            Bid b = db.find(Bid.class, bid.getIdBid());
            Sale s = b.getOferta();
            Buyer buyer = b.getComprador();
            Seller seller = s.getVendedor();

            float precio = b.getPrecioPropuesto();
            
            // 🎓 DOBLE VERIFICACIÓN (Parte 2): ¿Sigue teniendo el dinero?
            if (buyer.getMonedero().getSaldo() < precio) {
                // Si no lo tiene, lanzamos un error y se cancela la transacción entera
                throw new Exception("El comprador ya no tiene saldo suficiente.");
            }
            
            // Movemos el dinero
            buyer.getMonedero().removeSaldo(precio);
            seller.getMonedero().addSaldo(precio);

            // Generamos el historial con los nombres exactos
            Transaccion tB = new Transaccion(precio, new Date(), "COMPRA", buyer.getMonedero(), s);
            Transaccion tS = new Transaccion(precio, new Date(), "VENTA", seller.getMonedero(), s);
            db.persist(tB);
            db.persist(tS);

            db.getTransaction().commit();
        } catch (Exception e) { 
            db.getTransaction().rollback(); 
            throw new RuntimeException(e.getMessage()); // Pasamos el error hacia la GUI
        }
    }

    public boolean recargarSaldo(String email, float cant) {
        db.getTransaction().begin();
        User u = db.find(User.class, email);
        u.getMonedero().addSaldo(cant);
        db.persist(new Transaccion(cant, new Date(), "RECARGA", u.getMonedero(), null));
        db.getTransaction().commit();
        return true;
    }

    public List<Transaccion> getHistorial(String email) {
        db.getTransaction().begin();
        try {
            User u = db.find(User.class, email);
            if (u == null || u.getMonedero() == null) {
                db.getTransaction().commit();
                return new ArrayList<>();
            }
            // 🎓 Búsqueda por el objeto exacto en lugar del ID (mucho más seguro en ObjectDB)
            TypedQuery<Transaccion> q = db.createQuery("SELECT t FROM Transaccion t WHERE t.monedero = :m", Transaccion.class);
            q.setParameter("m", u.getMonedero());
            List<Transaccion> res = q.getResultList();
            
            db.getTransaction().commit();
            return res;
        } catch (Exception e) {
            db.getTransaction().rollback();
            e.printStackTrace(); // Esto nos imprimirá el error en rojo en Eclipse si algo falla
            return new ArrayList<>(); 
        }
    }
    
    public List<Sale> getOfertasAceptadas(String email) {
        db.getTransaction().begin();
        try {
            // 1. Buscamos ventas que YO he vendido (Soy el Seller)
            TypedQuery<Sale> qVendedor = db.createQuery(
                "SELECT s FROM Sale s WHERE s.vendedor.email = :e AND s.estado = 'Cerrada'", Sale.class);
            qVendedor.setParameter("e", email);
            List<Sale> misVentas = qVendedor.getResultList();

            // 2. Buscamos ventas que YO he comprado (Soy el Buyer con puja Aceptada)
            // Buscamos en las pujas (Bids) que estén aceptadas para este email
            TypedQuery<Sale> qComprador = db.createQuery(
                "SELECT b.oferta FROM Bid b WHERE b.comprador.email = :e AND b.estado = 'Aceptada'", Sale.class);
            qComprador.setParameter("e", email);
            List<Sale> misCompras = qComprador.getResultList();

            // Juntamos ambas listas
            List<Sale> todas = new ArrayList<>();
            todas.addAll(misVentas);
            todas.addAll(misCompras);

            db.getTransaction().commit();
            return todas;
        } catch (Exception e) {
            db.getTransaction().rollback();
            return new ArrayList<>();
        }
    }
    
 // 🎓 Método necesario para buscar productos por texto (usado en QuerySalesGUI)
    public List<Sale> getSales(String desc) {
        System.out.println(">> DataAccess: getSales=> from= " + desc);
        List<Sale> res = new ArrayList<Sale>();
        // JPQL: Buscamos en la entidad Sale donde el título se parezca al texto introducido
        TypedQuery<Sale> query = db.createQuery("SELECT s FROM Sale s WHERE s.titulo LIKE ?1", Sale.class);
        query.setParameter(1, "%" + desc + "%");

        List<Sale> sales = query.getResultList();
        for (Sale sale : sales) {
            res.add(sale);
        }
        return res;
    }
    
    public boolean retirarFondos(String emailUser, float cantidad) {
        db.getTransaction().begin();
        try {
            User u = db.find(User.class, emailUser);
            // Comprobamos que el usuario existe y que TIENE DINERO SUFICIENTE
            if (u != null && u.getMonedero().getSaldo() >= cantidad && cantidad > 0) {
                u.getMonedero().removeSaldo(cantidad); // Le quitamos el dinero
                
                // Guardamos el movimiento en el historial
                Transaccion t = new Transaccion(cantidad, new Date(), "RETIRADA", u.getMonedero(), null);
                db.persist(t);
                
                db.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.getTransaction().rollback();
        return false;
    }
    
    public boolean valorarVendedor(int puntuacion, String comentario, String emailComprador, String emailVendedor, Sale venta) {
        db.getTransaction().begin();
        try {
            // Buscamos a los protagonistas en la BD
            User autor = db.find(User.class, emailComprador);
            User valorado = db.find(User.class, emailVendedor);
            Sale v = db.find(Sale.class, venta.getIdSale());

            if (autor != null && valorado != null && v != null) {
                // Usamos tu clase Valoracion
                Valoracion val = new Valoracion(puntuacion, comentario, new Date(), autor, valorado, v);
                db.persist(val);
                db.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.getTransaction().rollback();
        return false;
    }
    
}