package domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Transaccion implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTransaccion;
    private float importe;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private String tipo; // "INGRESO", "GASTO", "RETIRADA"

    @ManyToOne
    private Monedero monedero;

    @ManyToOne
    private Sale ventaAsociada; // Puede ser null si es recarga/retirada

    public Transaccion() {}

    public Transaccion(float importe, Date fecha, String tipo, Monedero monedero, Sale ventaAsociada) {
        this.importe = importe;
        this.fecha = fecha;
        this.tipo = tipo;
        this.monedero = monedero;
        this.ventaAsociada = ventaAsociada;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getIdTransaccion() { return idTransaccion; }
    public float getImporte() { return importe; }
    public Date getFecha() { return fecha; }
    public String getTipo() { return tipo; }
    public Monedero getMonedero() { return monedero; }
    public Sale getVentaAsociada() { return ventaAsociada; }
}