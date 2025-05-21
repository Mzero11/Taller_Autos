public interface ServicioService {
    void registrarServicio(Servicio servicio);
    void actualizarServicio(Servicio servicio);
    void eliminarServicio(Servicio servicio);
    Servicio buscarPorId(int id);
}
public class ServicioServiceImpl implements ServicioService {
    private DaoServicio dao = new DaoServicio();

    @Override
    public void registrarServicio(Servicio servicio) {
        dao.guardar(servicio);
    }

    @Override
    public void actualizarServicio(Servicio servicio) {
        dao.actualizar(servicio);
    }

    @Override
    public void eliminarServicio(Servicio servicio) {
        dao.eliminar(servicio);
    }

    @Override
    public Servicio buscarPorId(int id) {
        return dao.buscar(id);
    }
}