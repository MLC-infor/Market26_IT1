package businessLogic;

import java.io.File;
import java.util.Date;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import domain.Bid;
import domain.Buyer;
import domain.Sale;
import domain.Transaccion;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

@WebService
public interface BLFacade {

    @WebMethod public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file) throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException;
    
    @WebMethod public List<Sale> getSales(String desc);
    
    @WebMethod public List<Sale> getPublishedSales(String desc, Date pubDate);

    @WebMethod public void initializeBD();
    
    @WebMethod public boolean registrarUsuario(String nombre, String email, String password, boolean isSeller);
    
    @WebMethod public domain.User hacerLogin(String email, String password);
    
    @WebMethod public Bid crearBid(float precio, Buyer comprador, Sale oferta);
    
    @WebMethod public List<Sale> getOfertasAceptadas(String emailVendedor);

    @WebMethod
    public void aceptarOferta(domain.Bid bid);

    @WebMethod
    public void procesarCobro(domain.Bid bid);
    
    @WebMethod public boolean recargarSaldo(String emailUser, float cantidad);
    
    @WebMethod public List<Transaccion> getHistorial(String emailUsuario);
    
    @WebMethod
    public boolean retirarFondos(String emailUser, float cantidad);
    
    @WebMethod
    public boolean valorarVendedor(int puntuacion, String comentario, String emailComprador, String emailVendedor, Sale venta);

}