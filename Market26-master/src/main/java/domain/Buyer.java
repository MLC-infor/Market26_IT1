package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Buyer extends User {

    // Un Buyer realiza muchas Bids (Solicitudes)
    @OneToMany(mappedBy = "comprador", cascade = CascadeType.ALL)
    private List<Bid> bidsRealizadas;

    public Buyer() {
        super();
        this.bidsRealizadas = new ArrayList<>();
    }

    public Buyer(String email, String password, String name) {
        super(email, password, name);
        this.bidsRealizadas = new ArrayList<>();
    }
    
    // Getters y setters para la lista
}
