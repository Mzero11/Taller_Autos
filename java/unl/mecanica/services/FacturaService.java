public interface FacturaService {
    void generarFactura(Factura factura);
    void actualizarFactura(Factura factura);
    void eliminarFactura(Factura factura);
    Factura buscarPorId(int id);
}
public class FacturaServiceImpl implements FacturaService {
    private DaoFactura dao = new DaoFactura();

    @Override
    public void generarFactura(Factura factura) {
        dao.guardar(factura);
    }

    @Override
    public void actualizarFactura(Factura factura) {
        dao.actualizar(factura);
    }

    @Override
    public void eliminarFactura(Factura factura) {
        dao.eliminar(factura);
    }

    @Override
    public Factura buscarPorId(int id) {
        return dao.buscar(id);
    }
}