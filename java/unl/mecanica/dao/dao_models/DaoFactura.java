package unl.mecanica.dao.dao_models;

import java.util.Date;

public class DaoFactura extends AdapterDao<Factura> {
    private Factura obj;

    public DaoFactura() {
        super(Factura.class);
    }

    public Factura getObj() {
        if (obj == null)
            this.obj = new Factura(0, new Date(), null);
        return this.obj;
    }

    public void setObj(Factura obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.idFactura = listAll().getLength() + 1;
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

