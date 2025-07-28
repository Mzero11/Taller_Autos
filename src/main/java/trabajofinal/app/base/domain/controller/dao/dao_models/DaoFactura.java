package trabajofinal.app.base.domain.controller.dao.dao_models;

import trabajofinal.app.base.domain.controller.dao.AdapterDao;
import trabajofinal.app.base.models.Factura;

public class DaoFactura extends AdapterDao<Factura> {
    private Factura obj;

    public DaoFactura() {
        super(Factura.class);
    }

    public Factura getObj() {
        if (obj == null)
            obj = new Factura();
        return obj;
    }

    public void setObj(Factura obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
}
