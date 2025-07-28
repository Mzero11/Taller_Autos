package trabajofinal.app.base.domain.controller.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import trabajofinal.app.base.domain.controller.dao.dao_models.DaoMarca;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoVehiculo;
import trabajofinal.app.base.domain.controller.datastruct.list.LinkedList;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoPersona;
import trabajofinal.app.base.models.Persona;
import trabajofinal.app.base.models.Vehiculo;
import trabajofinal.app.base.models.Marca;
import trabajofinal.app.base.models.Vehiculo;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@AnonymousAllowed
public class VehiculoService {
    private DaoVehiculo db;

    public VehiculoService() {
        db = new DaoVehiculo();
    }

    public void createVehiculo(Integer persona_id, Integer marca_id, @NotEmpty String placa,
                                @NotEmpty String chasis, Integer kilometraje) throws Exception {
        if (persona_id > 0 && marca_id > 0 && placa.trim().length() > 0 &&
            chasis.trim().length() > 0 && kilometraje > 0) {
            db.getObj().setPersona_id(persona_id);
            db.getObj().setMarca_id(marca_id);
            db.getObj().setPlaca(placa);
            db.getObj().setChasis(chasis);
            db.getObj().setKilometraje(kilometraje);
            if (!db.save())
                throw new Exception("No se pudo guardar los datos del Vehiculo");
        }
    }

    public void updateVehiculo(Integer id, Integer persona_id, Integer marca_id,
                                @NotEmpty String placa, @NotEmpty String chasis, Integer kilometraje) throws Exception {
        if (persona_id > 0 && marca_id > 0 && placa.trim().length() > 0 &&
            chasis.trim().length() > 0 && kilometraje > 0) {
            
            int pos = db.getPositionById(id); // ✅ Busca posición por ID real
            db.setObj(db.listAll().get(pos));
            db.getObj().setPersona_id(persona_id);
            db.getObj().setMarca_id(marca_id);
            db.getObj().setPlaca(placa);
            db.getObj().setChasis(chasis);
            db.getObj().setKilometraje(kilometraje);

            if (!db.update(pos))
                throw new Exception("No se pudo modificar los datos del Vehiculo");
        }
    }

    public List<HashMap> listaMarcaCombo() {
        List<HashMap> lista = new ArrayList<>();
        DaoMarca dm = new DaoMarca();
        if (!dm.listAll().isEmpty()) {
            Marca[] arreglo = dm.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("value", arreglo[i].getId().toString()); // ✅ corregido
                aux.put("label", arreglo[i].getModelo());
                lista.add(aux);
            }
        }
        return lista;
    }

    public List<HashMap> listaPersonaCombo() {
        List<HashMap> lista = new ArrayList<>();
        DaoPersona dp = new DaoPersona();
        if (!dp.listAll().isEmpty()) {
            Persona[] arreglo = dp.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("value", arreglo[i].getId().toString()); // ✅ corregido
                aux.put("label", arreglo[i].getUsuario());
                lista.add(aux);
            }
        }
        return lista;
    }

    public List<HashMap> listVehiculo() throws Exception {
        List<HashMap> lista = new ArrayList<>();
        if (!db.listAll().isEmpty()) {
            Vehiculo[] arreglo = db.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("id", arreglo[i].getId().toString()); // ✅ corregido

                DaoMarca dm = new DaoMarca();
                DaoPersona dp = new DaoPersona();

                Marca marca = dm.listAll().get(dm.getPositionById(arreglo[i].getMarca_id()));
                Persona persona = dp.listAll().get(dp.getPositionById(arreglo[i].getPersona_id()));

                aux.put("marca", marca.getModelo());
                aux.put("id_marca", marca.getId().toString());

                aux.put("persona", persona.getUsuario());
                aux.put("id_persona", persona.getId().toString());

                aux.put("placa", arreglo[i].getPlaca());
                aux.put("chasis", arreglo[i].getChasis());
                aux.put("kilometraje", arreglo[i].getKilometraje().toString());

                lista.add(aux);
            }
        }
        return lista;
    }

    public void deleteVehiculo(Integer id) throws Exception {
    int pos = db.getPositionById(id); // Encuentra la posición por ID
    if (pos == -1) {
        throw new Exception("Marca no encontrada con ID: " + id);
    }
if (!db.eliminar(pos)) {
    throw new Exception("No se pudo eliminar la Marca con ID: " + id);
}
}

       public List<HashMap> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = db.search(attribute, text, type);
        if(!lista.isEmpty())
            return Arrays.asList(lista.toArray());
        else
            return new ArrayList<>();
    }



public List<HashMap> order(String atributo, Integer type) throws Exception {
    LinkedList<Vehiculo> ordenadas;
    if ("nombre".equalsIgnoreCase(atributo)) {
        ordenadas = db.orderQ(type);
    } else {
        ordenadas = db.listAll();
    }

    List<HashMap> resultado = new ArrayList<>();
    for (Vehiculo c : ordenadas.toArray()) {
        resultado.add(db.toDict(c));   
    }
    return resultado;
}


}
