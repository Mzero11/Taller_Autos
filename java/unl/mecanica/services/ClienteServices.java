public interface ClienteService {
    Cliente buscarPorCedula(String cedula);
    void registrarCliente(Cliente cliente);
    void actualizarCliente(Cliente cliente);
    public class ClienteServiceImpl implements ClienteService {
        private DaoCliente dao = new DaoCliente();
    
        @Override
        public Cliente buscarPorCedula(String cedula) {
            return dao.buscarPorCedula(cedula);
        }
    
        @Override
        public void registrarCliente(Cliente cliente) {
            dao.guardar(cliente);
        }
    
        @Override
        public void actualizarCliente(Cliente cliente) {
            dao.actualizar(cliente);
        }
    
        @Override
        public void eliminarCliente(Cliente cliente) {
            dao.eliminar(cliente);
        }
    }
}