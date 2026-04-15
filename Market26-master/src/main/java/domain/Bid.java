package domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBid;

    private float precioPropuesto;
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    private String estado; // Ejemplo: "Pendiente", "Aceptada", "Rechazada"

    // Relación con el comprador (El "1" del diagrama en el lado del Buyer)
    @ManyToOne
    private Buyer comprador;

    // Relación con la oferta/sale (El "1" del diagrama en el lado de Sale)
    @ManyToOne
    private Sale oferta;

    // Constructor vacío obligatorio para la base de datos (JPA)
    public Bid() {
    }

    // Constructor con los parámetros
    public Bid(float precioPropuesto, Date fecha, String estado, Buyer comprador, Sale oferta) {
        this.precioPropuesto = precioPropuesto;
        this.fecha = fecha;
        this.estado = estado;
        this.comprador = comprador;
        this.oferta = oferta;
    }

    // --- GETTERS Y SETTERS ---

    public Integer getIdBid() {
        return idBid;
    }

    public void setIdBid(Integer idBid) {
        this.idBid = idBid;
    }

    public float getPrecioPropuesto() {
        return precioPropuesto;
    }

    public void setPrecioPropuesto(float precioPropuesto) {
        this.precioPropuesto = precioPropuesto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Buyer getComprador() {
        return comprador;
    }

    public void setComprador(Buyer comprador) {
        this.comprador = comprador;
    }

    public Sale getOferta() {
        return oferta;
    }

    public void setOferta(Sale oferta) {
        this.oferta = oferta;
    }
    @Override
    public String toString() {
        return this.comprador.getEmail() + " (" + this.precioPropuesto + "€)";
    }
}