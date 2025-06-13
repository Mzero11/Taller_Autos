package trabajofinal.app.base.models;



public class Vehiculo {
    private Integer id;
    private Integer cliente_id;
    private Integer marca_id; // Para la clave foránea en la base de datos
    private String placa;
    private String chasis;
    private Integer kilometraje;
    private Marca marca; // Objeto Marca para la relación de objetos

    // Constructor por defecto
    public Vehiculo() {
    }

    // Constructor con campos básicos (útil para construir desde datos de DB donde solo viene el ID de la marca)
    public Vehiculo(Integer id, Integer cliente_id, Integer marca_id, String placa, String chasis, Integer kilometraje) {
        this.id = id;
        this.cliente_id = cliente_id;
        this.marca_id = marca_id;
        this.placa = placa;
        this.chasis = chasis;
        this.kilometraje = kilometraje;
    }

    // Constructor con todos los campos, incluyendo el objeto Marca
    public Vehiculo(Integer id, Integer cliente_id, Marca marca, String placa, String chasis, Integer kilometraje) {
        this.id = id;
        this.cliente_id = cliente_id;
        this.marca = marca;
        this.marca_id = (marca != null) ? marca.getMarca_id() : null; // Sincroniza marca_id con el objeto Marca
        this.placa = placa;
        this.chasis = chasis;
        this.kilometraje = kilometraje;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Integer getCliente_id() {
        return cliente_id;
    }

    public Integer getMarca_id() {
        return marca_id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getChasis() {
        return chasis;
    }

    public Integer getKilometraje() {
        return kilometraje;
    }

    public Marca getMarca() {
        return marca;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setCliente_id(Integer cliente_id) {
        this.cliente_id = cliente_id;
    }

    public void setMarca_id(Integer marca_id) {
        this.marca_id = marca_id;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public void setKilometraje(Integer kilometraje) {
        this.kilometraje = kilometraje;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
        this.marca_id = (marca != null) ? marca.getMarca_id() : null; // Sincroniza marca_id al establecer el objeto Marca
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
               "id=" + id +
               ", cliente_id=" + cliente_id +
               ", marca_id=" + marca_id +
               ", placa='" + placa + '\'' +
               ", chasis='" + chasis + '\'' +
               ", kilometraje=" + kilometraje +
               ", marca=" + (marca != null ? marca.getModelo() : "N/A") + // Muestra el modelo de la marca si existe
               '}';
    }
}