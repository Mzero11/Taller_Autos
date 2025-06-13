package trabajofinal.app.base.domain.controller.service;


import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.validation.constraints.NotEmpty;
import io.micrometer.common.lang.NonNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import trabajofinal.app.base.domain.controller.dao.dao_models.DaoVehiculo;
import trabajofinal.app.base.models.Marca;
import trabajofinal.app.base.models.Vehiculo;


@BrowserCallable
@AnonymousAllowed

public class VehiculoService {
    private DaoVehiculo db;
    private MarcaService marcaService; // Para acceder a las marcas

    public VehiculoService() {
        db = new DaoVehiculo();
        marcaService = new MarcaService(); // Instancia el servicio de Marca
    }

    /**
     * Crea un nuevo vehículo.
     * Se espera que el cliente_id y marca_id sean válidos.
     *
     * @param cliente_id El ID del cliente propietario.
     * @param marca_id El ID de la marca del vehículo.
     * @param placa El número de placa.
     * @param chasis El número de chasis.
     * @param kilometraje El kilometraje.
     * @throws Exception Si no se pudo guardar el vehículo o si hay problemas de validación/relación.
     */
    public void createVehiculo(
            @NonNull Integer cliente_id,
            @NonNull Integer marca_id,
            @NotEmpty String placa,
            @NotEmpty String chasis,
            @NonNull Integer kilometraje) throws Exception {

        if (cliente_id == null || cliente_id <= 0) {
            throw new Exception("El ID del cliente debe ser un valor positivo.");
        }
        if (marca_id == null || marca_id <= 0) {
            throw new Exception("El ID de la marca debe ser un valor positivo.");
        }
        if (placa.trim().length() == 0) {
            throw new Exception("La placa del vehículo no puede estar vacía.");
        }
        if (chasis.trim().length() == 0) {
            throw new Exception("El chasis del vehículo no puede estar vacío.");
        }
        if (kilometraje == null || kilometraje < 0) { // El kilometraje no puede ser negativo
            throw new Exception("El kilometraje debe ser un valor no negativo.");
        }

        // Cargar el objeto Marca completo para establecer la relación
        Marca marcaAsociada = marcaService.getMarcaById(marca_id);
        if (marcaAsociada == null) {
            throw new Exception("La marca con ID " + marca_id + " no existe.");
        }

        // Usa el objeto del DAO para configurar el nuevo vehículo
        db.getObj().setCliente_id(cliente_id);
        db.getObj().setMarca(marcaAsociada); // Establece el objeto Marca completo
        db.getObj().setPlaca(placa);
        db.getObj().setChasis(chasis);
        db.getObj().setKilometraje(kilometraje);

        if (!db.save()) {
            throw new Exception("No se pudo guardar los datos del vehículo");
        }
        db.setObj(null); // Limpiar el objeto del DAO después de guardar
    }

    /**
     * Actualiza un vehículo existente.
     *
     * @param id El ID del vehículo a actualizar.
     * @param cliente_id El nuevo ID del cliente propietario.
     * @param marca_id El nuevo ID de la marca del vehículo.
     * @param placa La nueva placa.
     * @param chasis El nuevo chasis.
     * @param kilometraje El nuevo kilometraje.
     * @throws Exception Si no se encontró el vehículo o no se pudo actualizar.
     */
    public void updateVehiculo(
            @NonNull Integer id,
            @NonNull Integer cliente_id,
            @NonNull Integer marca_id,
            @NotEmpty String placa,
            @NotEmpty String chasis,
            @NonNull Integer kilometraje) throws Exception {

        if (id == null || id <= 0) {
            throw new Exception("ID de vehículo inválido para actualizar.");
        }
        // Validaciones similares a createVehiculo
        if (cliente_id == null || cliente_id <= 0) {
            throw new Exception("El ID del cliente debe ser un valor positivo.");
        }
        if (marca_id == null || marca_id <= 0) {
            throw new Exception("El ID de la marca debe ser un valor positivo.");
        }
        if (placa.trim().length() == 0) {
            throw new Exception("La placa del vehículo no puede estar vacía.");
        }
        if (chasis.trim().length() == 0) {
            throw new Exception("El chasis del vehículo no puede estar vacío.");
        }
        if (kilometraje == null || kilometraje < 0) {
            throw new Exception("El kilometraje debe ser un valor no negativo.");
        }

        Vehiculo vehiculo = db.getById(id);
        if (vehiculo == null) {
            throw new Exception("No se encontró el vehículo con ID: " + id);
        }

        // Cargar el objeto Marca completo para actualizar la relación
        Marca marcaAsociada = marcaService.getMarcaById(marca_id);
        if (marcaAsociada == null) {
            throw new Exception("La marca con ID " + marca_id + " no existe.");
        }

        vehiculo.setCliente_id(cliente_id);
        vehiculo.setMarca(marcaAsociada); // Actualiza el objeto Marca
        vehiculo.setPlaca(placa);
        vehiculo.setChasis(chasis);
        vehiculo.setKilometraje(kilometraje);

        if (!db.update(vehiculo)) {
            throw new Exception("No se pudo modificar los datos del vehículo");
        }
    }

    /**
     * Obtiene un vehículo por su ID, asegurándose de cargar la marca asociada.
     * @param id El ID del vehículo.
     * @return El objeto Vehiculo completo, o null si no se encuentra.
     */
    public Vehiculo getVehiculoById(@NonNull Integer id) {
        return db.getById(id); // El DAO ya maneja la carga de la marca
    }

    /**
     * Obtiene una lista de todos los vehículos.
     * Asegura que cada vehículo tenga su objeto Marca asociado cargado.
     *
     * @return Una lista de objetos Vehiculo.
     */
    public List<Vehiculo> listAllVehiculos() {
        Object[] rawVehiculos = db.listAll().toArray();
        return Arrays.stream(rawVehiculos)
                     .filter(obj -> obj instanceof Vehiculo)
                     .map(obj -> {
                         Vehiculo vehiculo = (Vehiculo) obj;
                         // Si el objeto Marca no está cargado (solo tiene marca_id), cárgalo.
                         // Esto es útil si listAll() en el DAO no hace la carga de la relación.
                         if (vehiculo.getMarca() == null && vehiculo.getMarca_id() != null) {
                             Marca marca = marcaService.getMarcaById(vehiculo.getMarca_id());
                             vehiculo.setMarca(marca);
                         }
                         return vehiculo;
                     })
                     .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de vehículos en formato HashMap para información básica.
     *
     * @return Una lista de HashMaps con "id", "placa", "marcaModelo" (modelo de la marca).
     */
    public List<HashMap<String, String>> listVehiculosBasicInfo() {
        List<HashMap<String, String>> lista = new ArrayList<>();
        List<Vehiculo> vehiculos = listAllVehiculos(); // Usa el método listAllVehiculos

        for (Vehiculo vehiculo : vehiculos) {
            HashMap<String, String> aux = new HashMap<>();
            if (vehiculo.getId() != null) {
                aux.put("id", vehiculo.getId().toString());
            } else {
                aux.put("id", "N/A");
            }
            aux.put("placa", vehiculo.getPlaca());
            // Para la marca, obtenemos el modelo del objeto Marca asociado
            aux.put("marcaModelo", (vehiculo.getMarca() != null) ? vehiculo.getMarca().getModelo() : "Desconocida");
            lista.add(aux);
        }
        return lista;
    }

    // Puedes añadir un método para eliminar si es necesario
    // public void deleteVehiculo(Integer id) throws Exception { ... }
}