package trabajofinal.app.base.domain.controller.dao.dao_models;

import trabajofinal.app.base.domain.controller.dao.AdapterDao;
import trabajofinal.app.base.models.Vehiculo; // Asegúrate de que la ruta del paquete sea correcta
import trabajofinal.app.base.models.Marca; // Asegúrate de que la ruta del paquete sea correcta

public class DaoVehiculo extends AdapterDao<Vehiculo> {
    private static int nextId = 1; // Contador estático para IDs
    private Vehiculo obj;
    private DaoMarca daoMarca;

    public DaoVehiculo() {
        super(Vehiculo.class);
        this.daoMarca = new DaoMarca();
    }

    public Vehiculo getObj() {
        if (obj == null) {
            this.obj = new Vehiculo();
        }
        return this.obj;
    }

    public void setObj(Vehiculo obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            // Asigna un ID usando el contador estático y luego lo incrementa
            if (obj.getId() == null || obj.getId() == 0) { // Solo asigna si el ID no ha sido establecido
                obj.setId(nextId++);
            }
            this.persist(obj); // Método de AdapterDao para guardar el objeto
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar Vehiculo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar Vehiculo en posición " + pos + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Vehiculo getById(Integer id) {
        try {
            Object[] vehiculos = this.listAll().toArray();
            for (Object item : vehiculos) {
                if (item instanceof Vehiculo) {
                    Vehiculo vehiculo = (Vehiculo) item;
                    if (vehiculo.getId() != null && vehiculo.getId().equals(id)) {
                        // Al recuperar, también recuperamos y asignamos el objeto Marca completo.
                        if (vehiculo.getMarca_id() != null) {
                            Marca marcaAsociada = daoMarca.getById(vehiculo.getMarca_id());
                            vehiculo.setMarca(marcaAsociada);
                        }
                        return vehiculo;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar Vehiculo por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Boolean update(Vehiculo vehiculo) {
        try {
            super.update(vehiculo, vehiculo.getId());
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar Vehiculo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        DaoMarca daoMarca = new DaoMarca();
        DaoVehiculo daoVehiculo = new DaoVehiculo();

        // --- Preparación: Asegurarse de que haya Marcas guardadas ---
        System.out.println("--- Preparando Marcas para Vehículos ---");
        Marca toyota = daoMarca.getByModelo("Toyota");
        if (toyota == null) {
            daoMarca.getObj().setModelo("Toyota");
            daoMarca.getObj().setAnio(1937);
            if (daoMarca.save()) {
                toyota = daoMarca.getObj();
                System.out.println("Marca 'Toyota' creada para pruebas: " + toyota);
            } else {
                System.out.println("No se pudo crear la Marca 'Toyota'. Saliendo.");
                return;
            }
        } else {
             System.out.println("Marca 'Toyota' ya existe: " + toyota);
        }
        daoMarca.setObj(null);

        Marca ford = daoMarca.getByModelo("Ford");
        if (ford == null) {
            daoMarca.getObj().setModelo("Ford");
            daoMarca.getObj().setAnio(1903);
            if (daoMarca.save()) {
                ford = daoMarca.getObj();
                System.out.println("Marca 'Ford' creada para pruebas: " + ford);
            } else {
                System.out.println("No se pudo crear la Marca 'Ford'. Saliendo.");
                return;
            }
        } else {
             System.out.println("Marca 'Ford' ya existe: " + ford);
        }
        daoMarca.setObj(null);

        // --- 1. Crear y guardar Vehículos ---
        System.out.println("\n--- Creando y guardando Vehículos ---");
        // Vehiculo 1: Toyota
        daoVehiculo.getObj().setCliente_id(101);
        daoVehiculo.getObj().setMarca(toyota);
        daoVehiculo.getObj().setPlaca("PBA-1234");
        daoVehiculo.getObj().setChasis("CHASIS-TOYOTA-001");
        daoVehiculo.getObj().setKilometraje(50000);
        if (daoVehiculo.save()) {
            System.out.println("Vehículo Toyota guardado: " + daoVehiculo.getObj());
        } else {
            System.out.println("Error al guardar Vehículo Toyota.");
        }
        daoVehiculo.setObj(null);

        // Vehiculo 2: Ford
        daoVehiculo.getObj().setCliente_id(102);
        daoVehiculo.getObj().setMarca(ford);
        daoVehiculo.getObj().setPlaca("LOJ-5678");
        daoVehiculo.getObj().setChasis("CHASIS-FORD-002");
        daoVehiculo.getObj().setKilometraje(75000);
        if (daoVehiculo.save()) {
            System.out.println("Vehículo Ford guardado: " + daoVehiculo.getObj());
        } else {
            System.out.println("Error al guardar Vehículo Ford.");
        }
        daoVehiculo.setObj(null);

        System.out.println("\n--- Vehículos existentes ---");
        for (Object v : daoVehiculo.listAll().toArray()) {
            if (v instanceof Vehiculo) {
                Vehiculo vehiculo = (Vehiculo) v;
                // Si el objeto Marca no está cargado (solo tiene marca_id), cárgalo.
                if (vehiculo.getMarca() == null && vehiculo.getMarca_id() != null) {
                    vehiculo.setMarca(daoMarca.getById(vehiculo.getMarca_id()));
                }
                System.out.println(vehiculo);
            }
        }

        // --- 2. Actualizar un Vehículo (ejemplo asumiendo ID 1 es el primer vehículo) ---
        System.out.println("\n--- Actualizando Vehículo (ID 1) ---");
        Vehiculo vehiculoAActualizar = daoVehiculo.getById(1);
        if (vehiculoAActualizar != null) {
            vehiculoAActualizar.setKilometraje(55000);
            if (daoVehiculo.update(vehiculoAActualizar)) {
                System.out.println("Vehículo (ID 1) actualizado a: " + daoVehiculo.getById(1));
            } else {
                System.out.println("Error al actualizar Vehículo (ID 1).");
            }
        } else {
            System.out.println("Vehículo con ID 1 no encontrado para actualizar.");
        }
    }
}