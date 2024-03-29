package es.upsa.tfg.resources;

import es.upsa.tfg.models.Producto;
import es.upsa.tfg.models.UnidentifiedProduct;
import es.upsa.tfg.repositories.MongoRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/mongo")
@Tag(name="productos")
@Produces(MediaType.APPLICATION_JSON)
public class Resource {

    @Inject
    MongoRepository mongoRepository;

    @Operation(operationId = "saludo",
            summary = "Devuelve una cadena de texto",
            description = "Endpoint para comprobar que funciona el CI/CD"
    )

    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Respuesta correcta"
            ),
            @APIResponse(responseCode = "500",
                    description = "Error al recuperar productos"
            )
    })
    @GET
    @Path("/saludo")
    public Response saludo(){
        return Response.ok().entity("Hola buenas tardes.").build();
    }

    @Operation(operationId = "requestAllProductos",
            summary = "Acceso a los datos de todos los productos",
            description = "Devuelve los datos de los productos"
    )

    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se han localizado los productos y se devuelven sus datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY,
                                    implementation = Producto.class
                            )
                    )
            ),
            @APIResponse(responseCode = "500",
                    description = "Error al recuperar productos"
            )
    })

    @GET
    @Path("/todos")
    public Response requestAllProductos(){

        return Response.ok().entity(Producto.listAll()).build();
    }



    @Operation(operationId = "requestProducto",
            summary = "Acceso a los datos de un producto identificado por su id",
            description = "Devuelve los datos del producto identificado a través de su id"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha localizado el producto y se devuelven sus datos",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No hay registrado un producto con el id especificado"
            )
    })
    @Path("/{id}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response requestProducto(
            @Parameter(required = true,
            description = "id del producto",
            in = ParameterIn.PATH,
            name = "nombre",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id") String id) {

        return Response.ok().entity(mongoRepository.findById(id).toString()).build();
    }




    @Operation(operationId = "count",
            summary = "Acceso a los datos de un producto identificado por su nombre",
            description = "Devuelve los datos del producto identificado a través de su nombre"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha localizado el producto y se devuelven sus datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Producto.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No hay registrado un producto con el nombre especificado"
            )
    })
    @Path("/contar")
    @GET
    public Response count(){

        return Response.ok().entity(mongoRepository.count()).build();
    }


    @Operation(operationId = "addProducto",
            description = "Crea un nuevo producto",
            summary = "Registra un nuevo producto en el sistema"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha creado el producto correctamente. Se devuelve sus datos entre los que se incluye su código",
                    headers = @Header(name = HttpHeaders.LOCATION,
                            description = "URI con la que acceder al producto que se ha creado",
                            schema = @Schema(type = SchemaType.STRING,
                                    format = "uri"
                            )
                    ),
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Producto.class
                            )
                    )
            ),

            @APIResponse(responseCode = "500",
                    description = "Se ha producido un error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)
            )
    })
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProducto(@RequestBody(description = "datos del producto",
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT,
                            implementation = UnidentifiedProduct.class
                    )
            )
    )
                                UnidentifiedProduct uproducto){
        mongoRepository.addProducto(uproducto);
        return Response.ok().entity("Insertado").build();
    }


    @Operation(operationId = "updateProducto",
            summary = "Actualiza un producto",
            description = "Cambia los datos de un producto"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha actualizado el producto correctamente.",
                    headers = @Header(name = HttpHeaders.LOCATION,
                            description = "URI con la que acceder al producto que se ha creado",
                            schema = @Schema(type = SchemaType.STRING,
                                    format = "uri"
                            )
                    ),
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Producto.class
                            )
                    )
            ),

            @APIResponse(responseCode = "500",
                    description = "Se ha producido un error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)
            )
    })
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateByNombre(@RequestBody(description = "datos del producto",
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT,
                            implementation = UnidentifiedProduct.class
                    )
            )) UnidentifiedProduct product){
        mongoRepository.updateByNombre(product.nombre,product.stock, product.proveedor);
        return Response.ok().entity("Producto actualizado").build();
    }

    @Operation(operationId = "deleteProducto",
            summary = "Borra un producto",
            description = "Elimina un producto por su id"
    )
    @DELETE
    @Path("/borrar/{id}")
    public Response deleteById(@PathParam("id") String id){
        mongoRepository.borrarPorId(id);
        return Response.ok().entity("El objeto " + id + " ha sido eliminado").build();
    }

    @Operation(operationId = "deleteProductoNombre",
            summary = "Borra un producto",
            description = "Elimina un producto por su nombre"
    )
    @DELETE
    @Path("/{nombre}")
    public Response deleteByNombre(@PathParam("nombre") String nombre){
        mongoRepository.deleteByNombre(nombre);
        return Response.ok().entity("El objeto " + nombre + " ha sido eliminado").build();
    }
}