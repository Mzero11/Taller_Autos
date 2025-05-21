public interface MecanicoService {
    Mecanico buscarPorCedula(String cedula);
    void registrarMecanico(Mecanico mecanico);
    void actualizarMecanico(Mecanico mecanico);
    void eliminarMecanico(Mecanico mecanico);
}
public class MecanicoServiceImpl implements MecanicoService {
    private DaoMecanico dao = new DaoMecanico();

    @Override
    public Mecanico buscarPorCedula(String cedula) {
        return dao.buscarPorCedula(cedula);
    }

    @Override
    public void registrarMecanico(Mecanico mecanico) {
        dao.guardar(mecanico);
    }

    @Override
    public void actualizarMecanico(Mecanico mecanico) {
        dao.actualizar(mecanico);
    }

    @Override
    public void eliminarMecanico(Mecanico mecanico) {
        dao.eliminar(mecanico);
    }
}