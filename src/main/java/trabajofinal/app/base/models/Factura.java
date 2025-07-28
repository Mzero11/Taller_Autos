package trabajofinal.app.base.models;



import java.util.Date;

public class Factura {
    private Integer id;
    private Integer persona_id;
    private Integer vehiculo_id;
    private Date fecha;
    private Integer total;
    private Boolean pagado;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPersona_id() { return persona_id; }
    public void setPersona_id(Integer persona_id) { this.persona_id = persona_id; }

    public Integer getVehiculo_id() { return vehiculo_id; }
    public void setVehiculo_id(Integer vehiculo_id) { this.vehiculo_id = vehiculo_id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }

    public Boolean getPagado() { return pagado; }
    public void setPagado(Boolean pagado) { this.pagado = pagado; }
}
