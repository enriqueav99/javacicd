package es.upsa.tfg.models;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

@MongoEntity(collection = "Producto", database = "tfg")
public class Producto extends PanacheMongoEntity {

    public  String nombre;
    public int stock;
    public String proveedor;


    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", stock=" + stock +
                ", proveedor='" + proveedor + '\'' +
                '}';
    }
}
