package unl.mecanica.dao.dao_models;

public class DaoRepuesto extends AdapterDao<Repuesto> {
    private Repuesto obj;

    public DaoRepuesto() {
        super(Repuesto.class);
    }

    public Repuesto getObj() {
        if (obj == null)
            this.obj = new Repuesto("", 0);
        return this.obj;
    }

    public void setObj(Repuesto obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
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
