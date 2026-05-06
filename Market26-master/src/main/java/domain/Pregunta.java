package domain; // Cambia esto si tu paquete se llama distinto

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Pregunta implements Serializable {
    
    @Id
    @GeneratedValue
    private Integer idPregunta;
    
    private String textoDuda;
    private String textoRespuesta;
    private Date fecha;

    @ManyToOne
    private Sale venta;

    @ManyToOne
    private Buyer comprador;

    // Constructor vacío requerido por JPA
    public Pregunta() {
    }

    public Pregunta(String textoDuda, Sale venta, Buyer comprador) {
        this.textoDuda = textoDuda;
        this.textoRespuesta = ""; // Empieza vacía hasta que el vendedor responda
        this.fecha = new Date();
        this.venta = venta;
        this.comprador = comprador;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getIdPregunta() { return idPregunta; }
    public void setIdPregunta(Integer idPregunta) { this.idPregunta = idPregunta; }

    public String getTextoDuda() { return textoDuda; }
    public void setTextoDuda(String textoDuda) { this.textoDuda = textoDuda; }

    public String getTextoRespuesta() { return textoRespuesta; }
    public void setTextoRespuesta(String textoRespuesta) { this.textoRespuesta = textoRespuesta; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Sale getVenta() { return venta; }
    public void setVenta(Sale venta) { this.venta = venta; }

    public Buyer getComprador() { return comprador; }
    public void setComprador(Buyer comprador) { this.comprador = comprador; }
}