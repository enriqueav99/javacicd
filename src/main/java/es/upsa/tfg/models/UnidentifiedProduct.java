package es.upsa.tfg.models;

import lombok.*;

@Builder
@Data
public class UnidentifiedProduct {

    public  String nombre;
    public int stock;
    public String proveedor;
}
