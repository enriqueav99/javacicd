package es.upsa.tfg.repositories;

import es.upsa.tfg.models.Producto;
import es.upsa.tfg.models.UnidentifiedProduct;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MongoRepository implements PanacheMongoRepository<Producto> {
    public Producto findByNombre(String nombre){
        return Producto.find("{'nombre': ?1}", nombre).firstResult();
    }
    public List<Producto> getAll(){
        return Producto.listAll();
    }
    public void addProducto(UnidentifiedProduct unidentifiedProduct){
        Producto np = new Producto();
        np.nombre= unidentifiedProduct.getNombre();
        np.proveedor =unidentifiedProduct.getProveedor();
        np.stock= unidentifiedProduct.getStock();

        np.persist();
    }

    public void updateByNombre(String nombre, int stock, String proveedor){
        Producto producto = findByNombre(nombre);
        producto.proveedor=proveedor;
        producto.stock=stock;
        producto.persistOrUpdate();
    }

    public void deleteByNombre(String nombre){
        Producto.delete("{'nombre': ?1}", nombre);
    }


}
