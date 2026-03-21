package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Seller extends User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST, mappedBy="vendedor")
	private List<Sale> sales = new ArrayList<Sale>();

	public Seller() {
		super();
	}

	public Seller(String email, String password, String name) {
		super(email, password, name);
	}
	
	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	public String toString(){
		return super.getEmail() + ";" + super.getName() + sales;
	}
	
	/**
	 * Método adaptado a los atributos del UML
	 */
	public Sale addSale(String titulo, String descripcion, float precioOriginal, String estado, Date fecha, int foto)  {
		Sale sale = new Sale(titulo, descripcion, precioOriginal, estado, fecha, foto, this);
        sales.add(sale);
        return sale;
	}
	
	/**
	 * This method checks if the sale already exists
	 */
	public boolean doesSaleExist(String titulo)  {	
		for (Sale s: sales)
			if (s.getTitulo().compareTo(titulo) == 0)
			 return true;
		return false;
	}
}