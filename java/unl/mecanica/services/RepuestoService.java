public interface RepuestoService {
    void registrarRepuesto(Repuesto repuesto);
    void actualizarRepuesto(Repuesto repuesto);
    void eliminarRepuesto(Repuesto repuesto);
    Repuesto buscarPorId(int id);
}
public class RepuestoServiceImpl implements RepuestoService {
    private DaoRepuesto dao = new DaoRepuesto();

    @Override
    public void registrarRepuesto(Repuesto repuesto) {
        dao.guardar(repuesto);
    }

    @Override
    public void actualizarRepuesto(Repuesto repuesto) {
        dao.actualizar(repuesto);
    }

    @Override
    public void eliminarRepuesto(Repuesto repuesto) {
        dao.eliminar(repuesto);
    }

    @Override
    public Repuesto buscarPorId(int id) {
        return dao.buscar(id);
    }
}