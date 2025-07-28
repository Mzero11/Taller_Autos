package trabajofinal.app.base.domain.controller.dao.dao_models;

import trabajofinal.app.base.domain.controller.dao.AdapterDao;
import trabajofinal.app.base.models.Rol;

public class DaoRol extends AdapterDao<Rol> {
    private Rol obj;

    public DaoRol() {
        super(Rol.class);
        // TODO Auto-generated constructor stub
    }

    public Rol getObj() {
        if (obj == null)
            this.obj = new Rol();
        return this.obj;
    }

    public void setObj(Rol obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength()+1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

}