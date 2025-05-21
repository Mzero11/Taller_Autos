package unl.mecanica.dao.dao_models;

import java.util.Date;

public class DaoReparacion extends AdapterDao<Reparacion> {
    private Reparacion obj;

    public DaoReparacion() {
        super(Reparacion.class);
    }

    public Reparacion getObj() {
        if (obj == null)
            this.obj = new Reparacion(0, "", new Date());
        return this.obj;
    }

    public void setObj(Reparacion obj) {
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
