package Logica;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Etiqueta implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private String etiqueta;

    public Etiqueta() {

    }

    public Etiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return "Etiqueta{" +
                "id=" + id +
                ", etiqueta='" + etiqueta + '\'' +
                '}';
    }
}
