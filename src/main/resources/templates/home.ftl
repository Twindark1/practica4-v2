<!DOCTYPE html>
<html>

<head>
    <title>Blog - Practica 4</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="css/Blog.css">

    <script
            src="https://code.jquery.com/jquery-3.2.1.js"
            integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE="
            crossorigin="anonymous"></script

    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <style type="text/css">
        .cssPagination .pages > .page:target ~ .page:last-child,
        .cssPagination .pages > .page {display: none;}
        .cssPagination .pages > :last-child,
        .cssPagination .pages > .page:target {display: block;}
    </style>
</head>

<body>

<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/home#page1">PUCMM</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="active"><a href="/home#page1">Home</a></li>

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

    <div class="container">
        <div class="row">
            <div class="col-md-4 col-lg-4 text-left">

            <h1>Blog - Practica 4</h1>
            </div>
            <div class="col-md-8 col-lg-8 text-right">
                <div style="margin-top: 20px">
            <form action="/home" method="post" class="navbar-form" role="search">
                <div class="input-group">
                    <input type="text" class="form-control"  placeholder="Tag" name="filtro" value="${filtro}" id="srch-term">
                    <div class="input-group-btn">
                        <button class="btn btn-primary" ><i class="glyphicon glyphicon-search"></i></button>
                    </div>
                </div>
            </form>
                </div>
            </div>

            <#if User??>
                <#if User.administrator || User.author>
                    <a href="/agregarArticulo"><button type="button" style="height: 50px" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-plus"></span> Crear Nuevo Post</button></a>
                </#if>

            </#if>

            <div class="cssPagination">
                <div class="pages">
                    <#if paginas??>
                        <#list paginas as pagina>
                        <div class="page" id="page${pagina}">
                            <#if listaArticulos??>
                                <#list listaArticulos as articulo>
                                    <#if articulo.pagina == pagina>
                                        <a href="/post/${articulo.id}">
                                            <div class="col-lg-12 col-md-12">
                                                <div class="col-md-12 col-lg-12 PostContainer">


                                                    <div class="col-lg-3 col-md-3 text-center" style="background-color: #d1d1d1">

                                                        <img src="img/posticon.png" width="150" style="margin: 12px;" class="text-center"/>
                                                    </div>
                                                    <div class="col-lg-9 col-md-9">
                                                        <div style="style="word-wrap: break-word;">
                                                        <h2 class="text-justify text-uppercase">${articulo.titulo} </h2>
                                                    </div>

                                                    <div class="text-justify">
                                                        <h4>${articulo.cuerpo}</h4>
                                                    </div>


                                                    <div style="word-wrap: break-word;" class="col-md-12">
                                                        <h3 class="text-left">Escrito por <span>${articulo.autor.nombre}</span> el <span>${articulo.fecha}</span></h3>


                                                        <h3 class="text-left">Etiquetas:
                                                            <#if articulo.setEtiquetas??>
                                                                <#list articulo.setEtiquetas as etiqueta>
                                                                    <span >&nbsp;  ${etiqueta.etiqueta}  &nbsp;</span>
                                                                </#list>
                                                            </#if>
                                                        </h3>
                                                    </div>

                                                    <div class="col-md-8"></div>
                                                    <div style="display: table; word-wrap: break-word;" class="col-md-4 text-right">
                                                        <div style="display: table-row">
                                                        <div style=" display: table-cell;">
                                                            <span class="glyphicon glyphicon-thumbs-up text-success" style="font-size: 24px">&nbsp;${articulo.uLikes?size}</span>
                                                        </div>
                                                        <div  style=" display: table-cell;">
                                                            <span class="glyphicon glyphicon-thumbs-down text-danger" style="font-size: 24px">&nbsp;${articulo.uDislikes?size}</span>
                                                        </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                    </div>
                                        </a>
                                    </#if>
                                </#list>
                            </#if>
                        </div>
                        </#list>
                    </#if>
                </div>
            </div>
        <div class="col-md-12 col-lg-12 text-center">
        <nav aria-label="Page navigation">
            <ul class="pagination pagination-lg">
                <#if paginas??>
                    <#list paginas as pagina>
                        <li class="page-item"><a class="page-link" href="#page${pagina}">${pagina}</a></li>
                    </#list>
                </#if>
            </ul>
        </nav>
        </div>
        </div>
    </div>
</body>
</html>