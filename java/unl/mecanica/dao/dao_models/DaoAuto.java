package unl.mecanica.dao.dao_models;

public class DaoAuto extends AdapterDao<Auto> {
    private Auto obj;

    public DaoAuto() {
        super(Auto.class);
    }

    public Auto getObj() {
        if (obj == null)
            this.obj = new Auto("", "", 0, "");
        return this.obj;
    }

    public void setObj(Auto obj) {
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
