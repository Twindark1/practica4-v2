package Logica;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class Articulo implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private String titulo;
    @Column(name="cuerpo", columnDefinition="TEXT")
    private String cuerpo;
    @OneToOne
    private Usuario autor;
    private Date fecha;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Comentario> setComentarios;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Etiqueta> setEtiquetas;
    @Transient
    private String pagina;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ArticuloUsuariol")
    private Set<Usuario> uLikes;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ArticuloUsuariod")
    private  Set<Usuario> uDislikes;

    public Articulo() {

    }

    public Articulo(String titulo, String cuerpo, Usuario autor, Date fecha, Set<Comentario> setComentarios, Set<Etiqueta> setEtiquetas, Set<Usuario> uLikes, Set<Usuario> uDislikes) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = fecha;
        this.setComentarios = setComentarios;
        this.setEtiquetas = setEtiquetas;
        this.uLikes = uLikes;
        this.uDislikes = uDislikes;
    }

    public Articulo(long id, String titulo, String cuerpo, Usuario autor, Date fecha, Set<Comentario> setComentarios, Set<Etiqueta> setEtiquetas, String pagina, Set<Usuario> uLikes, Set<Usuario> uDislikes) {
        this.id = id;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = fecha;
        this.setComentarios = setComentarios;
        this.setEtiquetas = setEtiquetas;
        this.pagina = pagina;
        this.uLikes = uLikes;
        this.uDislikes = uDislikes;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Set<Comentario> getSetComentarios() {
        return setComentarios;
    }

    public void setSetComentarios(Set<Comentario> setComentarios) {
        this.setComentarios = setComentarios;
    }

    public Set<Etiqueta> getSetEtiquetas() {
        return setEtiquetas;
    }

    public void setSetEtiquetas(Set<Etiqueta> setEtiquetas) {
        this.setEtiquetas = setEtiquetas;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
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
        return "Articulo{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", cuerpo='" + cuerpo + '\'' +
                ", autor=" + autor +
                ", fecha=" + fecha +
                ", setComentarios=" + setComentarios +
                ", setEtiquetas=" + setEtiquetas +
                ", pagina='" + pagina + '\'' +
                ", uLikes=" + uLikes +
                ", uDislikes=" + uDislikes +
                '}';
    }
}
