package trabajofinal.app.base.domain.controller.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import trabajofinal.app.base.domain.controller.dao.dao_models.DaoMarca;
import trabajofinal.app.base.models.Marca;

import com.github.javaparser.quality.NotNull;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotEmpty;


@BrowserCallable
@AnonymousAllowed
public class MarcaService {
    private DaoMarca db;
    public MarcaService(){
        db = new DaoMarca();
    }

    public void createMarca(@NotEmpty String modelo, @NonNull Date fecha) throws Exception {
        if(modelo.trim().length() > 0 && fecha.toString().length() > 0) {
            db.getObj().setModelo(modelo);
            db.getObj().setFecha(fecha);
            if(!db.save())
                throw new  Exception("No se pudo guardar los datos de la Marca");
        }
    }

public void updateMarca(Integer id, @NotEmpty String modelo, @NonNull Date fecha) throws Exception {
    if (modelo.trim().length() > 0 && fecha.toString().length() > 0) {
        int pos = db.getPositionById(id); // ✅ Encuentra la posición real del objeto con ese id

        db.setObj(db.listAll().get(pos));
        db.getObj().setModelo(modelo);
        db.getObj().setFecha(fecha);

        if (!db.update(pos)) {
            throw new Exception("No se pudo modificar los datos de la Marca");
        }
    }
}
    public List<HashMap> listMarca(){
        List<HashMap> lista = new ArrayList<>();
        if(!db.listAll().isEmpty()) {
            Marca [] arreglo = db.listAll().toArray();           
            for(int i = 0; i < arreglo.length; i++) {
                
                HashMap<String, String> aux = new HashMap<>();
                aux.put("id", arreglo[i].getId().toString());                
                aux.put("modelo", arreglo[i].getModelo());
                aux.put("fecha", arreglo[i].getFecha().toString());
                lista.add(aux);
            }
        }
        return lista;
    }

    public void deleteMarca(Integer id) throws Exception {
    int pos = db.getPositionById(id); // Encuentra la posición por ID
    if (pos == -1) {
        throw new Exception("Marca no encontrada con ID: " + id);
    }
if (!db.eliminar(pos)) {
    throw new Exception("No se pudo eliminar la Marca con ID: " + id);
}
}
}
