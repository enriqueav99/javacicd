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





    @GET
    @Path("/todos")
    public Response requestAllProductos(){

        return Response.ok().entity(Producto.listAll()).build();
    }



    @Operation(operationId = "requestProducto",
            summary = "Acceso a los datos de un socio identificado por su nombre",
            description = "Devuelve los datos del producto identificado a través de su nombre"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha localizado el producto y se devuelven sus datos",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No hay registrado un producto con el nombre especificado"
            )
    })
    @Path("/{nombre}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response requestProducto(
            @Parameter(required = true,
            description = "nombre del producto",
            in = ParameterIn.PATH,
            name = "nombre",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("nombre") String nombre) {

        return Response.ok().entity(mongoRepository.findByNombre(nombre).toString()).build();
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
    public Response addPRoducto(@RequestBody(description = "datos del producto",
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


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateByNombre(@RequestBody(description = "datos del producto",
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT,
                            implementation = UnidentifiedProduct.class
                    )
            )) UnidentifiedProduct product){
        mongoRepository.updateByNombre(product.nombre,product.stock);
        return Response.ok().entity("Producto actualizado").build();
    }

    @DELETE
    @Path("/{nombre}")
    public Response deleteByNombre(@PathParam("nombre") String nombre){
        mongoRepository.deleteByNombre(nombre);
        return Response.ok().entity("El objeto " + nombre + " ha sido eliminado").build();
    }
}