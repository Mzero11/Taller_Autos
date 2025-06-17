package trabajofinal.app.base.domain.controller.dao.dao_models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import trabajofinal.app.base.domain.controller.dao.AdapterDao;
import trabajofinal.app.base.domain.controller.datastruct.list.LinkedList;
import trabajofinal.app.base.models.Cliente; 

public class DaoCliente extends AdapterDao<Cliente> {
    private Cliente obj;


    public DaoCliente() {
        super(Cliente.class);
        // TODO Auto-generated constructor stub
    }

    public Cliente getObj() {
        if (obj == null)
            this.obj = new Cliente();
        return this.obj;
    }

    public void setObj(Cliente obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getSize()+1);
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