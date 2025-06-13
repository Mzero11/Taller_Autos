package trabajofinal.app.base.models;

public class Marca {
    private Integer marca_id;
    private String modelo;    // Ej: "Serie F" para Ford, o un nombre distintivo de la marca
    private Integer anio;     // Ej: año de fundación de la marca

    // Constructor por defecto
    public Marca() {
    }

    // Constructor con todos los campos
    public Marca(Integer marca_id, String modelo, Integer anio) {
        this.marca_id = marca_id;
        this.modelo = modelo;
        this.anio = anio;
    }

    // Getters
    public Integer getMarca_id() {
        return marca_id;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    // Setters
    public void setMarca_id(Integer marca_id) {
        this.marca_id = marca_id;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    @Override
    public String toString() {
        return "Marca{" +
               "marca_id=" + marca_id +
               ", modelo='" + modelo + '\'' +
               ", anio=" + anio +
               '}';
    }
}