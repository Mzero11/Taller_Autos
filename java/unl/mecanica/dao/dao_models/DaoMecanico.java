package unl.mecanica.dao.dao_models;

public class DaoMecanico extends AdapterDao<Mecanico> {
    private Mecanico obj;

    public DaoMecanico() {
        super(Mecanico.class);
    }

    public Mecanico getObj() {
        if (obj == null)
            this.obj = new Mecanico(0, "", "", "", null);
        return this.obj;
    }

    public void setObj(Mecanico obj) {
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

