import java.util.Date;

public class DaoOrdenReparacion extends AdapterDao<OrdenReparacion> {
    private OrdenReparacion obj;

    public DaoOrdenReparacion() {
        super(OrdenReparacion.class);
    }

    public OrdenReparacion getObj() {
        if (obj == null)
            this.obj = new OrdenReparacion(0, new Date(), new Date(), "");
        return this.obj;
    }

    public void setObj(OrdenReparacion obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.id = listAll().getLength() + 1;
            this.persist(obj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
