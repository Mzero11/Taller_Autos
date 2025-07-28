package trabajofinal.app.base.domain.controller.dao.dao_models;

import java.util.HashMap;

import trabajofinal.app.base.domain.controller.Utiles;
import trabajofinal.app.base.domain.controller.dao.AdapterDao;
import trabajofinal.app.base.domain.controller.datastruct.list.LinkedList;
import trabajofinal.app.base.models.Vehiculo;
import trabajofinal.app.base.models.Vehiculo; 

public class DaoVehiculo extends AdapterDao<Vehiculo> {
    private Vehiculo obj;

    public DaoVehiculo() {
        super(Vehiculo.class);
        // TODO Auto-generated constructor stub
    }

    public Vehiculo getObj() {
        if (obj == null)
            this.obj = new Vehiculo();
        return this.obj;
    }

    public void setObj(Vehiculo obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength()+1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }


public Boolean eliminar(int index) {
    try {
        super.delete(index); // Llama al AdapterDao.delete corregido
        return true;
    } catch (Exception e) {
        e.printStackTrace(); // Para debug si algo falla
        return false;
    }
}



    public LinkedList<HashMap<String, String>> all() throws Exception {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();
        if (!this.listAll().isEmpty()) {
            Vehiculo[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {

                lista.add(toDict(arreglo[i]));
            }
        }
        return lista;
    }

public HashMap<String, String> toDict(Vehiculo c) throws Exception {

    DaoMarca daoMarca = new DaoMarca();
    DaoPersona  DaoPersona  = new DaoPersona();

    HashMap<String, String> map = new HashMap<>();
    map.put("id",      c.getId().toString());
    map.put("marca",  daoMarca.findById(c.getMarca_id()).getModelo());
    map.put("albun",   DaoPersona.findById(c.getPersona_id()).getUsuario());
    map.put("placa",     c.getPlaca());
    map.put("chasis",     c.getChasis());
    map.put("kilometraje",    c.getKilometraje().toString());
    return map;
}


public LinkedList<HashMap<String, String>> search(String attribute, String text, Integer type) throws Exception {
    LinkedList<HashMap<String, String>> lista = all();
    LinkedList<HashMap<String, String>> resp = new LinkedList<>();

    if (!lista.isEmpty()) {
        HashMap<String, String>[] arr = lista.toArray();
        System.out.println(attribute + " " + text + " ** *** * * ** * * * *");

        for (HashMap<String, String> m : arr) {
            String valor = m.get(attribute);  // <- puede ser null

            if (valor == null) {
                System.out.println("Advertencia: no se encontró el atributo '" + attribute + "' en el mapa");
                continue; // salta este elemento si no tiene el campo buscado
            }

            valor = valor.toLowerCase();
            String comparado = text.toLowerCase();

            switch (type) {
                case 1:
                    System.out.println(attribute + " " + text + " UNO");
                    if (valor.startsWith(comparado)) {
                        resp.add(m);
                    }
                    break;
                case 2:
                    System.out.println(attribute + " " + text + " DOS");
                    if (valor.endsWith(comparado)) {
                        resp.add(m);
                    }
                    break;
                default:
                    System.out.println(attribute + " " + text + " TRES");
                    if (valor.contains(comparado)) {
                        resp.add(m);
                    }
                    break;
            }
        }
    }

    return resp;
}

//order
    public LinkedList<Vehiculo> orderLastName(Integer type) {
        LinkedList<Vehiculo> lista = new LinkedList<>();
        if (!listAll().isEmpty()) {
            Integer cont = 0;
            long startTime = System.currentTimeMillis();
            Vehiculo arr[] = listAll().toArray();
            int n = arr.length;
            if (type == Utiles.ASCEDENTE) {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j].getPlaca().toLowerCase()
                                .compareTo(arr[min_idx].getPlaca().toLowerCase()) < 0) {
                            min_idx = j;
                            cont++;
                        }
                    }
                    Vehiculo temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j].getPlaca().toLowerCase()
                                .compareTo(arr[min_idx].getPlaca().toLowerCase()) > 0) {
                            min_idx = j;
                            cont++;
                        }
                    }
                    Vehiculo temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            }

            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado " + endTime + " he hizo " + cont);
            lista.toList(arr);
        }
        return lista;
    }

    private int partition(Vehiculo arr[], int begin, int end, Integer type) {
        Vehiculo pivot = arr[end];
        int i = (begin - 1);
        if (type == Utiles.ASCEDENTE) {
            for (int j = begin; j < end; j++) {
                if (arr[j].getPlaca().toLowerCase().compareTo(pivot.getPlaca().toLowerCase()) < 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    Vehiculo swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        } else {
            for (int j = begin; j < end; j++) {
                if (arr[j].getPlaca().toLowerCase().compareTo(pivot.getPlaca().toLowerCase()) > 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    Vehiculo swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        }
        Vehiculo swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    private void quickSort(Vehiculo arr[], int begin, int end, Integer type) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, type);

            quickSort(arr, begin, partitionIndex - 1, type);
            quickSort(arr, partitionIndex + 1, end, type);
        }
    }

    public LinkedList<Vehiculo> orderQ(Integer type) {
        LinkedList<Vehiculo> lista = new LinkedList<>();
        if (!listAll().isEmpty()) {

            Vehiculo arr[] = listAll().toArray();
            quickSort(arr, 0, arr.length - 1, type);
            lista.toList(arr);
        }
        return lista;
    }



}