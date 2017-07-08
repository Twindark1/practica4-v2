<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">

<head>
    <title>Blog - Practica 4</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <link rel="stylesheet" href="css/Blog.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>


</head>
<body>


<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/home#page1">PUCMM</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="/home#page1">Home</a></li>

        <#if User??>
            <#if User.administrator>
                <li><a href="/adminUsuarios">Admin Panel</a></li>

            </#if>
        </#if>
        </ul>
        <ul class="nav navbar-nav navbar-right">
        <#if User??>
                <li><a><span class="glyphicon glyphicon-user"></span> ${User.nombre} </a></li>
                <li><a href="/logout"><span class="glyphicon glyphicon-log-in"></span> Desconectarse</a></li>
        <#else>
            <li><a href="/userRegister"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
            <li><a href="/login"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
        </#if>


        </ul>
    </div>
</nav>

<!-- Page Content -->
<div class="container">

    <div class="row">

        <!-- Blog Post Content Column -->
        <div class="col-lg-12 col-md-12">

            <!-- Blog Post -->

            <!-- Title -->
            <#list listaArticulos as articulo>
                <#if articulo.id?string["0"] == artID>
                    <#if modificar == "false">


                        <h1 style="font-size: 50px">${articulo.titulo}</h1>
                        <!-- Author -->
                        <p class="lead">
                            by  ${articulo.autor.nombre}
                            <#if User??>
                                <#if User.administrator || User.nombre == articulo.autor.nombre>


                                    <a href="/eliminarArticulo/${articulo.id}" class="btn btn-danger" role="button">Eliminar <span class="glyphicon glyphicon-trash"></span></a>
                                    <#if modificar == "false">
                                        <a href="/modificarArticulo/${articulo.id}" class="btn btn-warning" role="button">Modificar <span class="glyphicon glyphicon-pencil"></span></a>
                                    </#if>
                                </#if>
                            </#if>
                        </p>

                        <div class="col-md-12" style="margin: 16px;">
                            <div class="col-md-4 text-left">
                                <p><span class="glyphicon glyphicon-time"></span> Posteado el ${articulo.fecha}</p>
                            </div>

                            <div class="col-md-4 text-right"></div>
                            <div class="col-md-4 text-right" >
                                <span style="font-size: 20px" class="text-success">+${articulo.uLikes?size}&nbsp;</span>
                                <span style="font-size: 20px" class="text-danger"> -${articulo.uDislikes?size}&nbsp;</span>
                            </div>
                        </div>
                        <hr>



                        <!-- Post Content -->
                        <div class="text-justify" style="word-wrap: break-word; white-space: pre-wrap; font-size: 18px;">${articulo.cuerpo}</div>
                        <#if articulo.setEtiquetas??>
                            <br><br>
                            <#list articulo.setEtiquetas as etiqueta>
                                <span style="background-color: #cfe0fd; padding: 8px; margin: 8px; border-radius: 20%;  font-size: 18px" class=" text-right text-primary"> ${etiqueta.etiqueta} </span>
                            </#list>
                        </#if>

                        <#if User??>
                            <div class="col-md-12 text-right">
                                <a href="/meGustaArticulo/${articulo.id}" ><button class="btn btn-success"><span class="glyphicon glyphicon-thumbs-up"> Like</span></button></a>
                                <a href="/noMeGustaArticulo/${articulo.id}"<button class="btn btn-danger"><span class="glyphicon glyphicon-thumbs-down"> Dislike</span></button></a>
                            </div>

                        </#if>
                        <hr>
                        <h2>Comentarios</h2>
                        <hr>
                    <div class="col-md-12">
                        <#if articulo.setComentarios?size gt 0>
                            <#list articulo.setComentarios as comentario>
                            <div style="padding: 10px; margin-bottom: 24px; background-color: #f9f9f9; box-shadow: #d1d1d1 5px 5px 5px 5px;" class="col-md-12 col-lg-12">


                                <div class="col-lg-3 col-md-3 text-center">
                                    <img src="/img/comment.png" width="50" style="margin: 6px;" class="text-center"/>

                                    <h4><strong>${comentario.author.nombre}</strong></h4>


                                </div>
                                <#if User??>
                                    <#if User.administrator || User.nombre == articulo.autor.nombre>

                                    <div class="col-lg-9 col-md-9 text-center">

                                        <div class="col-lg-11 col-md-11 text-center" style="word-wrap: break-word; white-space: pre-wrap; font-size: 16px">
                                        ${comentario.comentario}
                                        </div>

                                        <div class="col-lg-1 col-md-1">
                                            <a  class="btn btn-danger"  href="/borrarComentario/${articulo.id}/${comentario.id}">X</a>
                                        </div>


                                    <#else>
                                        <#if User.nombre == comentario.author.nombre>
                                        <div class="col-lg-9 col-md-9 text-center">

                                            <div class="col-lg-11 col-md-11 text-center" style="word-wrap: break-word; white-space: pre-wrap; font-size: 16px">
                                            ${comentario.comentario}
                                            </div>
                                            <div class="col-lg-1 col-md-1">
                                                <a  class="btn btn-danger"  href="/borrarComentario/${articulo.id}/${comentario.id}">X</a>
                                            </div>
                                        <#else>
                                        <div class="col-lg-9 col-md-9 text-center">
                                            <div class="col-lg-11 col-md-11 text-center" style="word-wrap: break-word; white-space: pre-wrap; font-size: 16px">
                                            ${comentario.comentario}
                                            </div>
                                            <div class="col-lg-1 col-md-1">
                                            </div>

                                        </#if>

                                    </#if>

                                <#else>
                                <div class="col-lg-9 col-md-9 text-center">
                                    <div class="col-lg-11 col-md-11 text-center" style="word-wrap: break-word; white-space: pre-wrap; font-size: 16px">
                                    ${comentario.comentario}
                                    </div>
                                    <div class="col-lg-1 col-md-1">
                                    </div>
                                </#if>


                                <div class="col-md-12" style="margin-top: 16px">
                                    <#if User??>
                                        <div class="col-md-12 text-right">
                                            <a href="/meGustaComentario/${articulo.id}/${comentario.id}"  ><button class="btn btn-success"><span class="glyphicon glyphicon-thumbs-up"> Like</span></button></a>
                                            <a href="/noMeGustaComentario/${articulo.id}/${comentario.id}""<button class="btn btn-danger"><span class="glyphicon glyphicon-thumbs-down"> Dislike</span></button></a>
                                        </div>
                                    </#if>
                                    <div class="col-md-12 text-right" style="margin-top: 8px;">
                                        <span style="font-size: 20px" class="text-success">+${comentario.uLikes?size}&nbsp;</span>
                                        <span style="font-size: 20px" class="text-danger"> -${comentario.uDislikes?size}&nbsp;</span>
                                    </div>
                                </div>
                            </div>
                            </div>
                            </#list>
                        </#if>
                    </div>


                        <#if User??>
                            <!-- Comments Form -->
                            <div>
                                <h4 style="margin-top: 16px">Deja un Comentario:</h4>
                                <form role="form" method="post" action="/agregarComentario/${articulo.id?string["0"]}">
                                    <div class="form-group">
                                        <textarea class="form-control" rows="3" name="comentario" required></textarea>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Guardar</button>
                                </form>
                            </div>
                        </#if>

                    </div>
                    <#else>

                        <h1 class="text-primary">Modificar Articulo</h1>
                        <hr>
                        <form action="/modificarArticulo/${articulo.id?string["0"]}" method="post">

                            <div class="form-group">
                                <label >Titulo</label>
                                <input type="text" class="form-control full-input" name="titulo" value="${articulo.titulo}" placeholder="Agrega el titulo de tu Post">
                            </div>

                            <div class="form-group">
                                <label >Contenido</label>
                                <textarea class="span6 form-control full-input" rows="18"  name="cuerpo" placeholder="Agrega el cuerpo de tu Post aqui..." required>${articulo.cuerpo}</textarea>
                            </div>

                            <div class="form-group">
                                <label>Etiquetas</label>
                                <input type="text" class="form-control full-input" name="etiquetas" value="${etiquetas}" placeholder="tag1,tag2,tag3">
                            </div>
                            <div class="align-content-center text-center">
                                <input style="margin-top: 8px; width:200px;" type="submit" class="btn btn-primary" value="Aceptar">
                            </div>

                        </form>
                    </#if>



                </#if>
            </#list>
        </div>
</div>
</div>

<br><br><br>

    <script src="http://code.jquery.com/jquery-latest.js"></script>


</body>
</html>