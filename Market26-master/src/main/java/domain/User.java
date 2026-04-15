package domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlID
    @Id
    private String email;
    private String password;
    private String name;

    // 🎓 ITERACIÓN 2: Relación 1 a 1 con Monedero
    @OneToOne(cascade = CascadeType.ALL)
    private Monedero monedero;

    public User() {
        this.monedero = new Monedero();
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.monedero = new Monedero();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Monedero getMonedero() { return monedero; }
    public void setMonedero(Monedero monedero) { this.monedero = monedero; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return email != null && email.equals(other.email);
    }
}