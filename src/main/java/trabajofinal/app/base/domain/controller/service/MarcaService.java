package trabajofinal.app.base.domain.controller.service;


import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.validation.constraints.NotEmpty;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoMarca;
import trabajofinal.app.base.models.Marca;
import io.micrometer.common.lang.NonNull; // Para campos que no pueden ser null


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors; // Para facilitar la conversión a List<Marca>

@BrowserCallable // Permite llamar a los métodos de este servicio desde el frontend de Hilla
@AnonymousAllowed // Permite el acceso a este servicio sin autenticación (ajusta según tus necesidades de seguridad)
public class MarcaService {
    private DaoMarca db;

    public MarcaService() {
        db = new DaoMarca(); // Instancia el DAO para Marca
    }

    /**
     * Crea una nueva marca con el modelo y año especificados.
     *
     * @param modelo El nombre del modelo de la marca (ej. "Serie F" o "Toyota").
     * @param anio El año asociado a la marca (ej. año de fundación).
     * @throws Exception Si no se pudo guardar los datos de la marca o si hay problemas de validación.
     */
    public void createMarca(@NotEmpty String modelo, @NonNull Integer anio) throws Exception {
        if (modelo.trim().length() == 0) {
            throw new Exception("El modelo de la marca no puede estar vacío.");
        }
        if (anio == null || anio <= 0) { // Validación adicional para el año
            throw new Exception("El año de la marca debe ser un valor positivo.");
        }

        db.getObj().setModelo(modelo);
        db.getObj().setAnio(anio);

        if (!db.save()) {
            throw new Exception("No se pudo guardar los datos de la marca");
        }
        db.setObj(null); // Limpiar el objeto del DAO después de guardar
    }

    /**
     * Actualiza una marca existente por su ID.
     *
     * @param id El ID de la marca a actualizar.
     * @param modelo El nuevo nombre del modelo de la marca.
     * @param anio El nuevo año asociado a la marca.
     * @throws Exception Si no se encontró la marca o no se pudo actualizar.
     */
    public void updateMarca(@NonNull Integer id, @NotEmpty String modelo, @NonNull Integer anio) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de marca inválido para actualizar.");
        }
        if (modelo.trim().length() == 0) {
            throw new Exception("El modelo de la marca no puede estar vacío.");
        }
        if (anio == null || anio <= 0) {
            throw new Exception("El año de la marca debe ser un valor positivo.");
        }

        Marca marca = db.getById(id);
        if (marca == null) {
            throw new Exception("No se encontró la marca con ID: " + id);
        }

        marca.setModelo(modelo);
        marca.setAnio(anio);

        if (!db.update(marca)) {
            throw new Exception("No se pudo modificar los datos de la marca");
        }
    }

    /**
     * Obtiene una marca por su ID.
     *
     * @param id El ID de la marca.
     * @return El objeto Marca, o null si no se encuentra.
     */
    public Marca getMarcaById(@NonNull Integer id) {
        return db.getById(id);
    }

    /**
     * Obtiene una lista de todas las marcas.
     *
     * @return Una lista de objetos Marca.
     */
    public List<Marca> listAllMarcas() {
        // Asegúrate de que listAll().toArray() sea compatible con tu implementación
        // y que los elementos sean realmente de tipo Marca.
        Object[] rawMarcas = db.listAll().toArray();
        return Arrays.stream(rawMarcas)
                     .filter(obj -> obj instanceof Marca) // Filtrar para seguridad
                     .map(obj -> (Marca) obj)
                     .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de marcas en formato HashMap, similar a tu listBanda.
     * Puede ser útil para ComboBoxes o tablas simples en el frontend si solo necesitas ID y nombre.
     *
     * @return Una lista de HashMaps con "id" y "modelo" de cada marca.
     */
    public List<HashMap<String, String>> listMarcasBasicInfo() {
        List<HashMap<String, String>> lista = new ArrayList<>();
        List<Marca> marcas = listAllMarcas(); // Usa el método listAllMarcas para obtener objetos Marca

        for (Marca marca : marcas) {
            HashMap<String, String> aux = new HashMap<>();
            if (marca.getMarca_id() != null) {
                 aux.put("id", marca.getMarca_id().toString());
            } else {
                 aux.put("id", "N/A"); // Manejar el caso de ID nulo, aunque no debería ocurrir con nextId
            }
            aux.put("modelo", marca.getModelo());
            lista.add(aux);
        }
        return lista;
    }

    // Puedes añadir un método para eliminar si es necesario
    // public void deleteMarca(Integer id) throws Exception { ... }
}