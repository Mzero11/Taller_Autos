package trabajofinal.app.base.domain.controller.dao.dao_models;

import trabajofinal.app.base.domain.controller.dao.AdapterDao;
import trabajofinal.app.base.models.Marca; // Asegúrate de que la ruta del paquete sea correcta

public class DaoMarca extends AdapterDao<Marca> {
    private static int nextId = 1; // Contador estático para IDs
    private Marca obj;

    public DaoMarca() {
        super(Marca.class);
    }

    public Marca getObj() {
        if (obj == null) {
            this.obj = new Marca();
        }
        return this.obj;
    }

    public void setObj(Marca obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            // Asigna un ID usando el contador estático y luego lo incrementa
            if (obj.getMarca_id() == null || obj.getMarca_id() == 0) { // Solo asigna si el ID no ha sido establecido
                obj.setMarca_id(nextId++);
            }
            this.persist(obj); // Método de AdapterDao para guardar el objeto
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar Marca: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Boolean update(Integer pos) {
        try {
            // Este update requiere la posición, lo cual es inusual para DAOs
            // basados en ID. Si tu AdapterDao permite update por ID, sería mejor.
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar Marca en posición " + pos + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Marca getById(Integer id) {
        try {
            Object[] marcas = this.listAll().toArray();
            for (Object item : marcas) {
                if (item instanceof Marca) {
                    Marca marca = (Marca) item;
                    if (marca.getMarca_id() != null && marca.getMarca_id().equals(id)) {
                        return marca;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar Marca por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Boolean update(Marca marca) {
        try {
            // Asumiendo que super.update(obj, id) es la forma correcta de usar tu AdapterDao
            super.update(marca, marca.getMarca_id());
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar Marca: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Marca getByModelo(String modelo) {
        try {
            Object[] marcas = this.listAll().toArray();
            for (Object item : marcas) {
                if (item instanceof Marca) {
                    Marca marca = (Marca) item;
                    if (marca.getModelo() != null && marca.getModelo().equalsIgnoreCase(modelo)) {
                        return marca;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar Marca por modelo: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        DaoMarca daoMarca = new DaoMarca();

        System.out.println("--- Creando y guardando Marcas ---");
        // Crear y guardar la primera marca
        daoMarca.getObj().setModelo("Toyota");
        daoMarca.getObj().setAnio(1937);
        if (daoMarca.save()) {
            System.out.println("Marca 'Toyota' guardada correctamente: " + daoMarca.getObj());
        } else {
            System.out.println("Error al guardar Marca 'Toyota'.");
        }
        daoMarca.setObj(null); // Limpiar el objeto actual del DAO

        // Crear y guardar la segunda marca
        daoMarca.getObj().setModelo("Ford");
        daoMarca.getObj().setAnio(1903);
        if (daoMarca.save()) {
            System.out.println("Marca 'Ford' guardada correctamente: " + daoMarca.getObj());
        } else {
            System.out.println("Error al guardar Marca 'Ford'.");
        }
        daoMarca.setObj(null);

        System.out.println("\n--- Marcas existentes ---");
        // Listar todas las marcas
        for (Object m : daoMarca.listAll().toArray()) {
             if (m instanceof Marca) {
                System.out.println(m);
            }
        }

        // Actualizar una Marca (ejemplo asumiendo ID 1 es Toyota)
        System.out.println("\n--- Actualizando Marca (ID 1) ---");
        Marca marcaAActualizar = daoMarca.getById(1);
        if (marcaAActualizar != null) {
            marcaAActualizar.setAnio(1938); // Actualizar el año de Toyota
            if (daoMarca.update(marcaAActualizar)) {
                System.out.println("Marca (ID 1) actualizada a: " + daoMarca.getById(1));
            } else {
                System.out.println("Error al actualizar Marca (ID 1).");
            }
        } else {
            System.out.println("Marca con ID 1 no encontrada para actualizar.");
        }
    }
}