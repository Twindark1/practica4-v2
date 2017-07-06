package routes;

import Logica.Articulo;
import Logica.Comentario;
import Logica.Etiqueta;
import Logica.Usuario;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import Servicios.*;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.Spark;
import java.io.StringWriter;
import java.util.*;

import static spark.Spark.*;

public class Routes {

    private static String modificar = "false";
    private static String filtrar = "";
    private static List<Articulo> arts = new ArrayList<>();

    private static void startProyect() {

        staticFiles.location("/public");
        final Configuration configuration = new Configuration(new Version(2, 3, 0));
        configuration.setClassForTemplateLoading(Routes.class, "/");

        Spark.get("/home", (request, response) -> {
            Usuario user = request.session(true).attribute("usuario");
            Template resultTemplate = configuration.getTemplate("templates/home.ftl");
            StringWriter writer = new StringWriter();
            Map<String, Object> attributes = new HashMap<>();

            if(filtrar.equals("")) {
                arts = ArticuloServices.getInstancia().buscarArticulos();
            } else {
                List<Etiqueta> tags = EtiquetaServices.getInstancia().findAll();
                for (Etiqueta t: tags) {
                    if(t.getEtiqueta().equals(filtrar)) {
                        arts = artRelacionadoConEtiqueta(t);
                    }
                }
            }
            ArrayList<Articulo> Tarts = new ArrayList<>();
            List<String> paginas = new ArrayList<>();
            int cont = 0;
            int pag = 0;
            for (Articulo a: arts) {
                if(cont % 5 == 0) {
                    pag++;
                    paginas.add(String.valueOf(pag));
                }
                cont++;
                Tarts.add(new Articulo(a.getId(), a.getTitulo(), get70firstChar(a.getCuerpo()), a.getAutor(), a.getFecha(), a.getSetComentarios(), a.getSetEtiquetas(), String.valueOf(pag), a.getuLikes(), a.getuDislikes()));
            }
            attributes.put("paginas", paginas);
            attributes.put("User",user);
            attributes.put("etiquetas", EtiquetaServices.getInstancia().findAll());
            attributes.put("listaArticulos", Tarts);
            attributes.put("filtro", filtrar);
            resultTemplate.process(attributes, writer);
            return writer;
        }); 

        Spark.post("/home", (request, response) -> {

            filtrar = request.queryParams("filtro");
            response.redirect("/home#page1");
            return "";
        }); 

        Spark.get("/post/:artID", (request, response) -> {
            Usuario user = request.session(true).attribute("usuario");

            Template resultTemplate = configuration.getTemplate("templates/post.ftl");
            StringWriter writer = new StringWriter();
            Map<String, Object> attributes = new HashMap<>();

            String etiquetas = "";

            if (modificar == "true") {
                arts = ArticuloServices.getInstancia().findAll();
                for (Articulo a: arts) {
                    if(a.getId() == Long.parseLong(request.params("artID"))) {
                        boolean primer = true;
                        for (Etiqueta e: a.getSetEtiquetas()) {
                            if(primer) {
                                etiquetas += e.getEtiqueta();
                                primer = false;
                            }else{
                                etiquetas += "," + e.getEtiqueta();
                            }
                        }
                    }
                }
            }

            arts = ArticuloServices.getInstancia().findAll();

            attributes.put("User",user);
            attributes.put("listaArticulos", arts);
            attributes.put("artID", request.params("artID"));
            attributes.put("modificar", modificar);
            attributes.put("etiquetas", etiquetas);
            resultTemplate.process(attributes, writer);

            return writer;
        }); 

        Spark.get("/login", (request, response) -> {

            Template resultTemplate = configuration.getTemplate("templates/login.ftl");
            StringWriter writer = new StringWriter();
            Map<String, Object> attributes = new HashMap<>();

            resultTemplate.process(attributes, writer);
            return writer;
        }); 

        Spark.post("/login", (request, response) -> {

            Usuario user = UsuarioServices.getInstancia().find(request.queryParams("username"));

            if(user != null && user.getPassword().equals(request.queryParams("password"))){
                request.session(true);
                request.session().attribute("usuario", user);
                response.redirect("/home#page1");
            }else {
                response.redirect("/login"); //Mostrar un error aqui
            }
            return "";
        }); 

        Spark.get("/logout", (resquest, response) -> {

            Session ses = resquest.session(true);
            ses.invalidate();
            response.redirect("/home#page1");
            return "";
        }); 

        Spark.get("/adminRegister", (request, response) -> {

            Template resultTemplate = configuration.getTemplate("templates/registerAdmin.ftl");
            StringWriter writer = new StringWriter();
            Map<String, Object> attributes = new HashMap<>();

            resultTemplate.process(attributes, writer);
            return writer;
        }); 

        Spark.post("/adminRegister", (request, response) -> {

            try {
                Usuario admin = new Usuario(request.queryParams("username"), request.queryParams("name"), request.queryParams("password"),true,true);

                //Creado usuario en la BD
                UsuarioServices.getInstancia().crear(admin);
                //Creando una session
                request.session(true);
                request.session().attribute("usuario", admin);
                response.redirect("/home#page1");
            }catch (Exception e){
                System.out.println(e);
                response.redirect("/home#page1");
            }
            return "";
        });  

        Spark.get("/userRegister", (request, response) -> {

            Template resultTemplate = configuration.getTemplate("templates/registerUser.ftl");
            StringWriter writer = new StringWriter();
            Map<String, Object> attributes = new HashMap<>();

            resultTemplate.process(attributes, writer);
            return writer;
        }); 

        Spark.post("/userRegister", (request, response) -> {

            try {
                Usuario user = new Usuario(request.queryParams("username"), request.queryParams("name"), request.queryParams("password"),false,false);
                if (UsuarioServices.getInstancia().find(user.getUsername()) != null){
                    response.redirect("/userRegister");
                }
                else {
                    //Creado usuario en la BD
                    UsuarioServices.getInstancia().crear(user);
                    //Creando una session
                    request.session(true);
                    request.session().attribute("usuario", user);
                    response.redirect("/home#page1");
                }
            }catch (Exception e){
                System.out.println(e);
            }
            return "";
        }); 

        Spark.get("/modificarArticulo/:artID", (request, response) -> {

            modificar = "true";
            response.redirect("/post/" + request.params("artID"));

            return "";
        }); 

        Spark.post("/modificarArticulo/:artID", (request, response) -> {

            Set<Etiqueta> ets = new HashSet<Etiqueta>();
            modificar = "false";

            String[] etiquetas = request.queryParams("etiquetas").split(",");
            agregarEtiquetasNuevas(etiquetas, ets);
            Articulo ar = ArticuloServices.getInstancia().find(Long.parseLong(request.params("artID")));
            if(etiquetaModificado(ets, ar.getSetEtiquetas())) {
                ar.setSetEtiquetas(ets);
                ArticuloServices.getInstancia().editar(ar);
            }
            if(!ar.getTitulo().equals(request.queryParams("titulo"))) {
                ar.setTitulo(request.queryParams("titulo"));
                ArticuloServices.getInstancia().editar(ar);
            }
            if(!ar.getCuerpo().equals(request.queryParams("cuerpo"))) {
                ar.setCuerpo(request.queryParams("cuerpo"));
                ArticuloServices.getInstancia().editar(ar);
            }
            response.redirect("/home#page1");

            return "";
        }); 

        Spark.get("/eliminarArticulo/:artID", (request, response) -> {

            ArticuloServices.getInstancia().eliminar(Long.parseLong(request.params("artID")));
            response.redirect("/home#page1");

            return "";
        }); 

        Spark.get("/agregarArticulo", (request, response) -> {

            Template resultTemplate = configuration.getTemplate("templates/agregarArt.ftl");
            StringWriter writer = new StringWriter();
            Map<String, Object> attributes = new HashMap<>();
            Usuario us = request.session(true).attribute("usuario");

            attributes.put("usuario", us);
            resultTemplate.process(attributes, writer);
            return writer;
        }); 

        Spark.post("/agregarArticulo", (request, response) -> {

            String[] etiquetas = request.queryParams("etiquetas").split(",");
            for (int x = 0; x < etiquetas.length; x++){
                etiquetas[x] = etiquetas[x].replace(" ","");
            }
            Set<Etiqueta> ets = new HashSet<Etiqueta>();
            long now = System.currentTimeMillis();

            Usuario us = request.session(true).attribute("usuario");
            agregarEtiquetasNuevas(etiquetas, ets);
            Articulo ar = new Articulo(request.queryParams("titulo"),  request.queryParams("cuerpo"), us, new Date(now), new HashSet<Comentario>(), ets, new HashSet<Usuario>(), new HashSet<Usuario>());
            ArticuloServices.getInstancia().crear(ar);
            response.redirect("/home#page1");
            return "";
        }); 

        Spark.post("/agregarComentario/:artID", (request, response) -> {

            Usuario user = request.session().attribute("usuario");
            Comentario com = new Comentario(request.queryParams("comentario"), user, new HashSet<Usuario>(), new HashSet<Usuario>());
            ComentarioServices.getInstancia().crear(com);

            Articulo ar = ArticuloServices.getInstancia().find(Long.parseLong(request.params("artID")));
            ar.getSetComentarios().add(com);
            ArticuloServices.getInstancia().editar(ar);

            response.redirect("/post/" + request.params("artID"));
            return "";
        }); 

        Spark.get("/borrarComentario/:artID/:ID", (request, response) -> {

            Set<Comentario> tCom = new HashSet<Comentario>();
            Articulo a = ArticuloServices.getInstancia().find(Long.parseLong(request.params("artID")));
            Comentario com = ComentarioServices.getInstancia().find(Long.parseLong(request.params("ID")));
            for (Comentario c: a.getSetComentarios()) {
                if(c.getId() != com.getId()) {
                    tCom.add(c);
                }
            }
            a.setSetComentarios(tCom);
            ArticuloServices.getInstancia().editar(a);
            ComentarioServices.getInstancia().eliminar(Long.parseLong(request.params("ID")));
            response.redirect("/post/"+request.params("artID"));

            return "";
        }); 

        Spark.get("/adminUsuarios", (request, response) -> {

            Usuario user = request.session().attribute("usuario");

            Template resultTemplate = configuration.getTemplate("templates/adminUsuarios.ftl");
            StringWriter writer = new StringWriter();
            Map<String, Object> attributes = new HashMap<>();

            List<Usuario> urs = UsuarioServices.getInstancia().findAll();
            attributes.put("User", user);
            attributes.put("listaUsuarios", urs);
            resultTemplate.process(attributes, writer);
            return writer;
        }); 

        Spark.get("/asignarAdmin/:username/:administrador", (request, response) -> {

            Usuario us = UsuarioServices.getInstancia().find(request.params("username"));
            if(request.params("administrador").equals("true")) {
                us.setAdministrator(false);
                UsuarioServices.getInstancia().editar(us);
            } else {
                us.setAdministrator(true);
                us.setAuthor(true);
                UsuarioServices.getInstancia().editar(us);
            }
            response.redirect("/adminUsuarios");

            return "";
        }); 

        Spark.get("/asignarAutor/:username/:autor", (request, response) -> {

            Usuario us = UsuarioServices.getInstancia().find(request.params("username"));
            if(request.params("autor").equals("true")) {
                us.setAdministrator(false);
                us.setAuthor(false);
                UsuarioServices.getInstancia().editar(us);
            } else {
                us.setAuthor(true);
                UsuarioServices.getInstancia().editar(us);
            }
            response.redirect("/adminUsuarios");

            return "";
        }); 

        Spark.get("/meGustaArticulo/:artID", (request, response) -> {

            Usuario us = request.session(true).attribute("usuario");
            Articulo ar = ArticuloServices.getInstancia().find(Long.parseLong(request.params("artID")));

            Set<Usuario> tUsers = new HashSet<Usuario>();
            for (Usuario u: ar.getuDislikes()) {
                if(!u.getUsername().equals(us.getUsername())) {
                    tUsers.add(u);
                }
            }
            ar.setuDislikes(tUsers);
            ar.getuLikes().add(us);
            ArticuloServices.getInstancia().editar(ar);
            response.redirect("/post/" + request.params("artID"));

            return "";
        }); 

        Spark.get("/noMeGustaArticulo/:artID", (request, response) -> {

            Usuario us = request.session(true).attribute("usuario");
            Articulo ar = ArticuloServices.getInstancia().find(Long.parseLong(request.params("artID")));

            Set<Usuario> tUsers = new HashSet<Usuario>();
            for (Usuario u: ar.getuLikes()) {
                if(!u.getUsername().equals(us.getUsername())) {
                    tUsers.add(u);
                }
            }
            ar.setuLikes(tUsers);
            ar.getuDislikes().add(us);
            ArticuloServices.getInstancia().editar(ar);
            response.redirect("/post/" + request.params("artID"));

            return "";
        }); 

        Spark.get("/meGustaComentario/:artID/:ID", (request, response) -> {

            Usuario us = request.session(true).attribute("usuario");
            Comentario co = ComentarioServices.getInstancia().find(Long.parseLong(request.params("ID")));
            Articulo ar = ArticuloServices.getInstancia().find(Long.parseLong(request.params("artID")));

            Set<Usuario> tUsers = new HashSet<Usuario>();
            for (Usuario u: co.getuDislikes()) {
                if(!u.getUsername().equals(us.getUsername())) {
                    tUsers.add(u);
                }
            }
            co.setuDislikes(tUsers);
            co.getuLikes().add(us);
            ComentarioServices.getInstancia().editar(co);
            ArticuloServices.getInstancia().editar(ar);
            response.redirect("/post/" + request.params("artID"));

            return "";
        }); 

        Spark.get("/noMeGustaComentario/:artID/:ID", (request, response) -> {

            Usuario us = request.session(true).attribute("usuario");
            Comentario co = ComentarioServices.getInstancia().find(Long.parseLong(request.params("ID")));
            Articulo ar = ArticuloServices.getInstancia().find(Long.parseLong(request.params("artID")));

            Set<Usuario> tUsers = new HashSet<Usuario>();
            for (Usuario u: co.getuLikes()) {
                if(!u.getUsername().equals(us.getUsername())) {
                    tUsers.add(u);
                }
            }
            co.setuLikes(tUsers);
            co.getuDislikes().add(us);
            ComentarioServices.getInstancia().editar(co);
            ArticuloServices.getInstancia().editar(ar);
            response.redirect("/post/" + request.params("artID"));

            return "";
        }); 

        before("/*",(request, response) -> {

            try {
                String[] s = request.splat();
                if(!s[0].equals("adminRegister")) {
                    if(UsuarioServices.getInstancia().findAll().size() == 0){
                        response.redirect("/adminRegister");
                    }
                }
            } catch (Exception e) {
                response.redirect("/home#page1");
            }
        }); 

        before("/adminRegister",(request, response) -> {

            List<Usuario> users = UsuarioServices.getInstancia().findAll();
            if(users.size() > 0){
                response.redirect("/login");
            }
        }); 

        before("/agregarComentario/*",(request, response) -> {

            autorizado(request, response);
        }); 

        before("/agregarArticulo",(request, response) -> {

            autorizado(request, response);
        }); 

        before("/borrarComentario/*",(request, response) -> {

            autorizado(request, response);
        });  

        before("/eliminarArticulo/*",(request, response) -> {

            autorizado(request, response);
        });  

        before("/modificarArticulo/*",(request, response) -> {

            autorizado(request, response);
        });  

        before("/adminUsuarios",(request, response) -> {
            Usuario user = request.session().attribute("usuario");
            if(user == null){
                response.redirect("/login");
            }
            else{
                if(!user.isAdministrator()){
                    response.redirect("/login");
                }
            }
        });  

        before("/asignarAdmin/:*/:*", (request, response) -> {
            response.redirect("/adminUsuarios");
        });  

        before("/asignarUser/:*/:*", (request, response) -> {
            response.redirect("/adminUsuarios");
        });  

        before("/meGustaArticulo/:*", (request, response) -> {
            autorizado(request, response);
        });  

        before("/noMeGustaArticulo/:*", (request, response) -> {
            autorizado(request, response);
        });  

        before("/meGustaComentario/:*/:*", (request, response) -> {
            autorizado(request, response);
        });  

        before("/noMeGustaComentario/:*/:*", (request, response) -> {
            autorizado(request, response);
        });  

        after("/post/:*", (request, response) -> {

            modificar = "false";
        });  
    }

    private static void agregarEtiquetasNuevas(String[] etiquetas, Set<Etiqueta> ets) {

        List<Etiqueta> tags = EtiquetaServices.getInstancia().findAll();
        for (int i = 0; i < etiquetas.length; i++) {
            Etiqueta et = new Etiqueta(etiquetas[i]);
            boolean esta = false;
            for (Etiqueta e: tags) {
                if(e.getEtiqueta().contains(etiquetas[i])){
                    esta = true;
                }
            }
            if(!esta) {
                EtiquetaServices.getInstancia().crear(et);
                tags = EtiquetaServices.getInstancia().findAll();
            }
        }

        for (int i = 0; i < etiquetas.length; i++) {
            for(Etiqueta e :EtiquetaServices.getInstancia().findAll()) {
                if(e.getEtiqueta().equals(etiquetas[i])) {
                    ets.add(e);
                }
            }
        }

    }

    private static boolean etiquetaModificado(Set<Etiqueta> ets, Set<Etiqueta> setEtiquetas) {

        if ((ets.size() == 0 && setEtiquetas.size() > 0) ||  (ets.size() > 0 && setEtiquetas.size() == 0)){
            return true;
        }
        for (Etiqueta s: setEtiquetas) {
            for (Etiqueta s1: ets) {
                if(!s.getEtiqueta().contains(s1.getEtiqueta())) {
                    return true;
                }
            }
        }

        return false;
    }

    private static String get70firstChar(String text){

        int contador = 0;
        String finalText = "";

        for(int x = 0; x < text.length(); x++){
            finalText += text.charAt(x);
            contador++;
            if (contador == 70){
                break;
            }
     }
     return finalText + "...";
    }

    private static void autorizado(Request request, Response response) {

        Session ses = request.session(true);
        Usuario user = ses.attribute("usuario");

        if(user == null){
            halt(401, "No Autorizado");
        }
    }

    private static List<Articulo> artRelacionadoConEtiqueta(Etiqueta tag) {

        List<Articulo> artRelacionados = new ArrayList<>();
        List<Articulo> articulos = ArticuloServices.getInstancia().findAll();

        for (Articulo a: articulos) {
            for (Etiqueta e: a.getSetEtiquetas()) {
                if(e.getId() == tag.getId()) {
                    artRelacionados.add(a);
                }
            }
        }

        return artRelacionados;
    }
}
