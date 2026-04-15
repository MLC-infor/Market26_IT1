package domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Monedero implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue
    private Integer id;
    private float saldo;

    public Monedero() {
        super();
        this.saldo = 0.0f; 
    }

    public Monedero(float saldoInicial) {
        this.saldo = saldoInicial;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    // Métodos de conveniencia para sumar y restar
    public void addSaldo(float cantidad) {
        this.saldo += cantidad;
    }

    public void removeSaldo(float cantidad) {
        this.saldo -= cantidad;
    }

	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}
}