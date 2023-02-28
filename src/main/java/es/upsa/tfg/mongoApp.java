package es.upsa.tfg;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@OpenAPIDefinition(
        info = @Info(title = "OpenAPI para productos",
                version = "1.0.0",
                contact = @Contact(url = "https://drupal.upsa.es/sites/default/files/guiaDocente2022_INFORMATICA_4_1_SistemasDeInformacion.pdf",
                        name = "TFG",
                        email = "eandresvi.inf@upsa.es"
                ),
                description = "API de productos",
                license = @License(name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        tags = {@Tag(name = "productos",
                description = "Gestión de productos"
        )
        }
)
@ApplicationPath("/")
public class mongoApp extends Application{
}
