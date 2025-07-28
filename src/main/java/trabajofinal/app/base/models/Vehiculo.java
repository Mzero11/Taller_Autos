package trabajofinal.app.base.models;



public class Vehiculo {
    private Integer id;
    private Integer persona_id;
    private Integer marca_id; 
    private String placa;
    private String chasis;
    private Integer kilometraje;

    public Integer getPersona_id() {
        return persona_id;
    }
    public void setPersona_id(Integer persona_id) {
        this.persona_id = persona_id;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getMarca_id() {
        return marca_id;
    }
    public void setMarca_id(Integer marca_id) {
        this.marca_id = marca_id;
    }
    public String getPlaca() {
        return placa;
    }
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    public String getChasis() {
        return chasis;
    }
    public void setChasis(String chasis) {
        this.chasis = chasis;
    }
    public Integer getKilometraje() {
        return kilometraje;
    }
    public void setKilometraje(Integer kilometraje) {
        this.kilometraje = kilometraje;
    }

}