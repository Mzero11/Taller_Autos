package trabajofinal.app.base.domain.controller.dao.dao_models;

import java.util.HashMap;

import trabajofinal.app.base.domain.controller.Utiles;
import trabajofinal.app.base.domain.controller.dao.AdapterDao;
import trabajofinal.app.base.domain.controller.datastruct.list.LinkedList;
import trabajofinal.app.base.models.Servicio;

public class DaoServicio extends AdapterDao<Servicio> {
    private Servicio obj;

    public DaoServicio() {
        super(Servicio.class);
        // TODO Auto-generated constructor stub
    }

    public Servicio getObj() {
        if (obj == null)
            this.obj = new Servicio();
        return this.obj;
    }

    public void setObj(Servicio obj) {
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
            Servicio[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {

                lista.add(toDict(arreglo[i]));
            }
        }
        return lista;
    }

public HashMap<String, String> toDict(Servicio c) throws Exception {

    HashMap<String, String> map = new HashMap<>();
    map.put("id",      c.getId().toString());
    map.put("nombre",  c.getNombre());
    map.put("descripcion",  c.getDescripcion());
    map.put("costo Total",    c.getCosto_total().toString());
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
    public LinkedList<Servicio> orderLastName(Integer type) {
        LinkedList<Servicio> lista = new LinkedList<>();
        if (!listAll().isEmpty()) {
            Integer cont = 0;
            Servicio arr[] = listAll().toArray();
            int n = arr.length;
            if (type == Utiles.ASCEDENTE) {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j].getNombre().toLowerCase()
                                .compareTo(arr[min_idx].getNombre().toLowerCase()) < 0) {
                            min_idx = j;
                            cont++;
                        }
                    }
                    Servicio temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j].getNombre().toLowerCase()
                                .compareTo(arr[min_idx].getNombre().toLowerCase()) > 0) {
                            min_idx = j;
                            cont++;
                        }
                    }
                    Servicio temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            }

            lista.toList(arr);
        }
        return lista;
    }

    private int partition(Servicio arr[], int begin, int end, Integer type) {
        Servicio pivot = arr[end];
        int i = (begin - 1);
        if (type == Utiles.ASCEDENTE) {
            for (int j = begin; j < end; j++) {
                if (arr[j].getNombre().toLowerCase().compareTo(pivot.getNombre().toLowerCase()) < 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    Servicio swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        } else {
            for (int j = begin; j < end; j++) {
                if (arr[j].getNombre().toLowerCase().compareTo(pivot.getNombre().toLowerCase()) > 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    Servicio swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        }
        Servicio swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    private void quickSort(Servicio arr[], int begin, int end, Integer type) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, type);

            quickSort(arr, begin, partitionIndex - 1, type);
            quickSort(arr, partitionIndex + 1, end, type);
        }
    }

    public LinkedList<Servicio> orderQ(Integer type) {
        LinkedList<Servicio> lista = new LinkedList<>();
        if (!listAll().isEmpty()) {

            Servicio arr[] = listAll().toArray();
            quickSort(arr, 0, arr.length - 1, type);
            lista.toList(arr);
        }
        return lista;
    }


}