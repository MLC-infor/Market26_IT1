package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
public class Sale implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSale;

    private String titulo;
    private String descripcion;
    private float precioOriginal;
    
    // El estado puede ser "Abierta" o "Cerrada"
    private String estado; 
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    private int foto; 

    // Relación con el vendedor (El "1" del diagrama)
    @ManyToOne
    private Seller vendedor;

    // Relación con las solicitudes/bids (El "0..*" del diagrama)
    @OneToMany(mappedBy = "oferta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Bid> bidsRecibidas = new ArrayList<>();

    // Constructor vacío obligatorio para JPA
    public Sale() {
    }

    // Constructor con parámetros
    public Sale(String titulo, String descripcion, float precioOriginal, String estado, Date fecha, int foto, Seller vendedor) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precioOriginal = precioOriginal;
        this.estado = estado;
        this.fecha = fecha;
        this.foto = foto;
        this.vendedor = vendedor;
    }

    // --- GETTERS Y SETTERS ---

    public Integer getIdSale() { return idSale; }
    public void setIdSale(Integer idSale) { this.idSale = idSale; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public float getPrecioOriginal() { return precioOriginal; }
    public void setPrecioOriginal(float precioOriginal) { this.precioOriginal = precioOriginal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public int getFoto() { return foto; }
    public void setFoto(int foto) { this.foto = foto; }

    public Seller getVendedor() { return vendedor; }
    public void setVendedor(Seller vendedor) { this.vendedor = vendedor; }

    public List<Bid> getBidsRecibidas() { return bidsRecibidas; }
    public void setBidsRecibidas(List<Bid> bidsRecibidas) { this.bidsRecibidas = bidsRecibidas; }
    
    // Método de ayuda para añadir una solicitud (Bid) a esta oferta
    public void addBid(Bid bid) {
        this.bidsRecibidas.add(bid);
        bid.setOferta(this);
    }
}