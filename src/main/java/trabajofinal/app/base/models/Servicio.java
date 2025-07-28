package trabajofinal.app.base.models;

public class Servicio {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer costo_total;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCosto_total() {
        return this.costo_total;
    }

    public void setCosto_total(Integer costo_total) {
        this.costo_total = costo_total;
    }
}
