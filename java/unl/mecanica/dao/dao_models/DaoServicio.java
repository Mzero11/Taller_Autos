package unl.mecanica.dao.dao_models;

public class DaoServicio extends AdapterDao<Servicio> {
    private Servicio obj;

    public DaoServicio() {
        super(Servicio.class);
    }

    public Servicio getObj() {
        if (obj == null)
            this.obj = new Servicio("", 0);
        return this.obj;
    }

    public void setObj(Servicio obj) {
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
