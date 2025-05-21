public interface OrdenReparacionService {
    void crearOrden(OrdenReparacion orden);
    void actualizarOrden(OrdenReparacion orden);
    void eliminarOrden(OrdenReparacion orden);
    OrdenReparacion buscarPorId(int id);
}
public class OrdenReparacionServiceImpl implements OrdenReparacionService {
    private DaoOrdenReparacion dao = new DaoOrdenReparacion();

    @Override
    public void crearOrden(OrdenReparacion orden) {
        dao.guardar(orden);
    }

    @Override
    public void actualizarOrden(OrdenReparacion orden) {
        dao.actualizar(orden);
    }

    @Override
    public void eliminarOrden(OrdenReparacion orden) {
        dao.eliminar(orden);
    }

    @Override
    public OrdenReparacion buscarPorId(int id) {
        return dao.buscar(id);
    }
}