package trabajofinal.app.base.domain.controller.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import trabajofinal.app.base.domain.controller.dao.dao_models.DaoMarca;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoServicio;
import trabajofinal.app.base.domain.controller.datastruct.list.LinkedList;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoPersona;
import trabajofinal.app.base.models.Persona;
import trabajofinal.app.base.models.Marca;
import trabajofinal.app.base.models.Servicio;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@AnonymousAllowed
public class ServicioService {
    private DaoServicio ds;

    public ServicioService() {
        ds = new DaoServicio();
    }

    public void createServicio(@NotEmpty String nombre, @NotEmpty String descripcion, Integer costo_total) throws Exception {
        if (nombre.trim().length() > 0 && descripcion.trim().length() > 0 && costo_total>0) {
            ds.getObj().setNombre(nombre);
            ds.getObj().setDescripcion(descripcion);
            ds.getObj().setCosto_total(costo_total); // Asignar costo_total como null inicialmente
            if (!ds.save())
                throw new Exception("No se pudo guardar los datos del Servicio");
        }
    }

    public void updateServicio(Integer id, @NotEmpty String nombre, @NotEmpty String descripcion, Integer costo_total) throws Exception {
        if (nombre.trim().length() > 0 && descripcion.trim().length() > 0 && costo_total > 0) {
            
            int pos = ds.getPositionById(id); // ✅ Busca posición por ID real
            ds.setObj(ds.listAll().get(pos));
            ds.getObj().setNombre(nombre);;
            ds.getObj().setDescripcion(descripcion);
            ds.getObj().setCosto_total(costo_total); // Asignar costo_total como null inicialmente
            if (!ds.update(pos))
                throw new Exception("No se pudo modificar los datos del Servicio");
        }
    }

    public List<HashMap> listServicio() throws Exception {
        List<HashMap> lista = new ArrayList<>();
        if (!ds.listAll().isEmpty()) {
            Servicio[] arreglo = ds.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("id", arreglo[i].getId().toString()); // ✅ corregido

                aux.put("nombre", arreglo[i].getNombre());
                aux.put("descripcion", arreglo[i].getDescripcion());
                aux.put("costo_total", arreglo[i].getCosto_total().toString());

                lista.add(aux);
            }
        }
        return lista;
    }

    public void deleteServicio(Integer id) throws Exception {
    int pos = ds.getPositionById(id); // Encuentra la posición por ID
    if (pos == -1) {
        throw new Exception("Servicio no encontrado con ID: " + id);
    }
if (!ds.eliminar(pos)) {
    throw new Exception("No se pudo eliminar el Servicio con ID: " + id);
}
}

       public List<HashMap> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = ds.search(attribute, text, type);
        if(!lista.isEmpty())
            return Arrays.asList(lista.toArray());
        else
            return new ArrayList<>();
    }



public List<HashMap> order(String atributo, Integer type) throws Exception {
    LinkedList<Servicio> ordenadas;
    if ("nombre".equalsIgnoreCase(atributo)) {
        ordenadas = ds.orderQ(type);
    } else {
        ordenadas = ds.listAll();
    }

    List<HashMap> resultado = new ArrayList<>();
    for (Servicio c : ordenadas.toArray()) {
        resultado.add(ds.toDict(c));   
    }
    return resultado;
}

}
