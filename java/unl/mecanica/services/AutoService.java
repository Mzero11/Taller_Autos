public class AutoServiceImpl implements AutoService {
    private DaoAuto dao = new DaoAuto();

    @Override
    public Auto buscarPorPlaca(String placa) {
        return dao.buscarPorPlaca(placa);
    }

    @Override
    public void registrarAuto(Auto auto) {
        dao.guardar(auto);
    }

    @Override
    public void actualizarAuto(Auto auto) {
        dao.actualizar(auto);
    }

    @Override
    public void eliminarAuto(Auto auto) {
        dao.eliminar(auto);
    }
}