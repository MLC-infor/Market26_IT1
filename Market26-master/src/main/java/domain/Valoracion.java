package domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Valoracion implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idValoracion;
    private int puntuacion; // 1 a 5
    private String comentario;
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @ManyToOne
    private User autor;
    @ManyToOne
    private User valorado;
    @ManyToOne
    private Sale venta;

    public Valoracion() {}

    public Valoracion(int puntuacion, String comentario, Date fecha, User autor, User valorado, Sale venta) {
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = fecha;
        this.autor = autor;
        this.valorado = valorado;
        this.venta = venta;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getIdValoracion() { return idValoracion; }
    public int getPuntuacion() { return puntuacion; }
    public String getComentario() { return comentario; }
    public Date getFecha() { return fecha; }
    public User getAutor() { return autor; }
    public User getValorado() { return valorado; }
    public Sale getVenta() { return venta; }
}