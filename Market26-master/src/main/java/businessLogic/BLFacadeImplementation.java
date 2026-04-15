package businessLogic;

import java.io.File;
import java.util.Date;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import dataAccess.DataAccess;
import domain.Bid;
import domain.Buyer;
import domain.Sale;
import domain.Transaccion;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation implements BLFacade {
    
    DataAccess dbManager;

    public BLFacadeImplementation() {
        dbManager = new DataAccess();
    }

    @WebMethod
    public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file) throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
        dbManager.open();
        Sale product = dbManager.createSale(title, description, status, price, pubDate, sellerEmail, file);
        dbManager.close();
        return product;
    }

    @WebMethod
    public List<Sale> getSales(String desc) {
        dbManager.open();
        List<Sale> res = dbManager.getSales(desc);
        dbManager.close();
        return res;
    }

    @WebMethod
    public List<Sale> getPublishedSales(String desc, Date pubDate) {
        dbManager.open();
        List<Sale> res = dbManager.getPublishedSales(desc, pubDate);
        dbManager.close();
        return res;
    }

    @WebMethod
    public void initializeBD() {
        dbManager.open();
        dbManager.initializeDB();
        dbManager.close();
    }

    @WebMethod
    public boolean registrarUsuario(String nombre, String email, String password, boolean isSeller) {
        dbManager.open();
        boolean res = dbManager.registrarUsuario(nombre, email, password, isSeller);
        dbManager.close();
        return res;
    }

    @WebMethod
    public domain.User hacerLogin(String email, String password) {
        dbManager.open();
        domain.User res = dbManager.hacerLogin(email, password);
        dbManager.close();
        return res;
    }

    @WebMethod
    public Bid crearBid(float precio, Buyer comprador, Sale oferta) {
        dbManager.open();
        Bid b = dbManager.crearBid(precio, comprador, oferta);
        dbManager.close();
        return b;
    }

    @WebMethod
    public List<Sale> getOfertasAceptadas(String emailVendedor) {
        dbManager.open();
        List<Sale> res = dbManager.getOfertasAceptadas(emailVendedor);
        dbManager.close();
        return res;
    }

    @Override
    @WebMethod
    public void aceptarOferta(Bid bid) {
        dbManager.open();
        dbManager.aceptarOferta(bid);
        dbManager.close();
    }

    @Override
    @WebMethod
    public void procesarCobro(Bid bid) {
        dbManager.open();
        dbManager.procesarCobro(bid);
        dbManager.close();
    }

    @Override
    @WebMethod
    public boolean recargarSaldo(String emailUser, float cantidad) {
        dbManager.open();
        boolean res = dbManager.recargarSaldo(emailUser, cantidad);
        dbManager.close();
        return res;
    }

    @Override
    @WebMethod
    public List<Transaccion> getHistorial(String emailUser) {
        dbManager.open();
        List<Transaccion> res = dbManager.getHistorial(emailUser);
        dbManager.close();
        return res;
    }
 // Abre BLFacadeImplementation y comprueba que esto existe:
    public BLFacadeImplementation(DataAccess da) {
        System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
        this.dbManager = da;
    }
    
    @Override
    @WebMethod
    public boolean retirarFondos(String emailUser, float cantidad) {
        dbManager.open();
        boolean exito = dbManager.retirarFondos(emailUser, cantidad);
        dbManager.close();
        return exito;
    }
    
    @Override
    @WebMethod
    public boolean valorarVendedor(int puntuacion, String comentario, String emailComprador, String emailVendedor, Sale venta) {
        dbManager.open();
        boolean exito = dbManager.valorarVendedor(puntuacion, comentario, emailComprador, emailVendedor, venta);
        dbManager.close();
        return exito;
    }
}