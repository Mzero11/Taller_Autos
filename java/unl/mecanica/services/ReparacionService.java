public class ReparacionServiceImpl implements ReparacionService {
    private DaoReparacion dao = new DaoReparacion();

    @Override
    public void registrarReparacion(Reparacion reparacion) {
        dao.guardar(reparacion);
    }

    @Override
    public void actualizarReparacion(Reparacion reparacion) {
        dao.actualizar(reparacion);
    }

    @Override
    public void eliminarReparacion(Reparacion reparacion) {
        dao.eliminar(reparacion);
    }

    @Override
    public Reparacion buscarPorId(int id) {
        return dao.buscar(id);
    }
}