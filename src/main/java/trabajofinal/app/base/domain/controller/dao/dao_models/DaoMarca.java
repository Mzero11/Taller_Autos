package trabajofinal.app.base.domain.controller.dao.dao_models;

import trabajofinal.app.base.domain.controller.dao.AdapterDao;
import trabajofinal.app.base.models.Marca; 

public class DaoMarca extends AdapterDao<Marca> {
    private Marca obj;

    public DaoMarca() {
        super(Marca.class);
        // TODO Auto-generated constructor stub
    }

    public Marca getObj() {
        if (obj == null)
            this.obj = new Marca();
        return this.obj;
    }

    public void setObj(Marca obj) {
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

        public Marca findById(Integer id) throws Exception {
        for (Marca g : this.listAll().toArray()) {
            if (g.getId().equals(id)) {
                return g;
            }
        }
        return null;
    }

public Boolean eliminar(int index) {
    try {
        super.delete(index); // Llama al AdapterDao.delete corregido
        return true;
    } catch (Exception e) {
        e.printStackTrace(); // Para debug si algo falla
        return false;
    }
}
}