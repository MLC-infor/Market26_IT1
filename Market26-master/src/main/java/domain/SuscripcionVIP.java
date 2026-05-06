package domain; // Cambia esto si tu paquete se llama distinto

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class SuscripcionVIP implements Serializable {
    
    @Id
    @GeneratedValue
    private Integer idSuscripcion;
    
    private Date fechaInicio;
    private Date fechaCaducidad;
    private boolean activa;

    @OneToOne
    private User usuario;

    // Constructor vacío requerido por JPA
    public SuscripcionVIP() {
    }

    public SuscripcionVIP(User usuario, Date fechaCaducidad) {
        this.usuario = usuario;
        this.fechaInicio = new Date();
        this.fechaCaducidad = fechaCaducidad;
        this.activa = true;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getIdSuscripcion() { return idSuscripcion; }
    public void setIdSuscripcion(Integer idSuscripcion) { this.idSuscripcion = idSuscripcion; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(Date fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }
}