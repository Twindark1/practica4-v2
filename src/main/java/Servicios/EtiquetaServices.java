package Servicios;

import Logica.Etiqueta;

public class EtiquetaServices extends GestionDb<Etiqueta> {

    private static EtiquetaServices instancia;

    private EtiquetaServices() {super(Etiqueta.class);}

    public static EtiquetaServices getInstancia() {
        if(instancia==null){
            instancia = new EtiquetaServices();
        }
        return instancia;
    }
}
