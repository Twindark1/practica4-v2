package Logica;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Comentario implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    @Column(name="comentario", columnDefinition="TEXT")
    private String comentario;
    @ManyToOne
    private Usuario author;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ComentarioUsuariol")
    private Set<Usuario> uLikes;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ComentarioUsuariod")
    private Set<Usuario> uDislikes;

    public Comentario() {

    }

    public Comentario(String comentario, Usuario author, Set<Usuario> uLikes, Set<Usuario> uDislikes) {

        this.comentario = comentario;
        this.author = author;
        this.uLikes = uLikes;
        this.uDislikes = uDislikes;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getAuthor() {
        return author;
    }

    public void setAuthor(Usuario author) {
        this.author = author;
    }

    public Set<Usuario> getuLikes() {
        return uLikes;
    }

    public void setuLikes(Set<Usuario> uLikes) {
        this.uLikes = uLikes;
    }

    public Set<Usuario> getuDislikes() {
        return uDislikes;
    }

    public void setuDislikes(Set<Usuario> uDislikes) {
        this.uDislikes = uDislikes;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "id=" + id +
                ", comentario='" + comentario + '\'' +
                ", author=" + author +
                ", uLikes=" + uLikes +
                ", uDislikes=" + uDislikes +
                '}';
    }
}
