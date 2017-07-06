package Servicios;

import Logica.Articulo;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ArticuloServices extends GestionDb<Articulo> {
    private static ArticuloServices instancia;

    private ArticuloServices() {super(Articulo.class);}

    public static ArticuloServices getInstancia() {
        if(instancia==null){
            instancia = new ArticuloServices();
        }
        return instancia;
    }

    public List<Articulo> buscarArticulos() {
        EntityManager em = ArticuloServices.getInstancia().getEntityManager();
        Query query = em.createQuery("select a from Articulo a order by id desc");
        List<Articulo> lista = query.getResultList();
        return lista;
    }
}
