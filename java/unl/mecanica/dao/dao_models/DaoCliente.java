package unl.mecanica.dao.dao_models;

public class DaoCliente extends AdapterDao<Cliente> {
    private Cliente obj;

    public DaoCliente() {
        super(Cliente.class);
    }

    public Cliente getObj() {
        if (obj == null)
            this.obj = new Cliente(0, "", "", null);
        return this.obj;
    }

    public void setObj(Cliente obj) {
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

