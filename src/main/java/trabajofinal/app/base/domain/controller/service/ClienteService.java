package trabajofinal.app.base.domain.controller.service;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import trabajofinal.app.base.domain.controller.dao.dao_models.DaoCliente;
import trabajofinal.app.base.models.Cliente;

import com.github.javaparser.quality.NotNull;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@AnonymousAllowed
public class ClienteService {
    private DaoCliente dc;
    private BCryptPasswordEncoder passwordEncoder;

    public ClienteService(){
        dc = new DaoCliente();
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public void createcliente(@NotEmpty String nombre, @NotEmpty String telefono, @NotEmpty String correo) throws Exception {
        if(nombre.trim().length() > 0  && telefono.toString().length() > 0 && correo.toString().length() > 0) {
            dc.getObj().setnombre(nombre);
            dc.getObj().setTelefono(telefono);
            dc.getObj().setcorreo(correo);
            if(!dc.save())
                throw new  Exception("No se pudo guardar los datos de cliente");
        }
    }

    public void updatecliente(Integer id, @NotEmpty String nombre, @NotEmpty String telefono, @NotEmpty String correo) throws Exception {
        if(id != null && id > 0 && nombre.trim().length() > 0 && telefono.toString().length() > 0 && correo.toString().length() > 0) {
            dc.setObj(dc.listAll().get(id - 1));
            dc.getObj().setnombre(nombre);
            dc.getObj().setTelefono(telefono);
            dc.getObj().setcorreo(correo);
            if(!dc.update(id - 1))
                throw new  Exception("No se pudo modifcar los datos de cliente");
        }
    }

    public List<Cliente> listAllcliente(){
        return Arrays.asList(dc.listAll().toArray());
    }

    public List<HashMap> listcliente(){
        List<HashMap> lista = new ArrayList<>();
        if(!dc.listAll().isEmpty()) {
            Cliente [] arreglo = dc.listAll().toArray();
           
            for(int i = 0; i < arreglo.length; i++) {
                
                HashMap<String, String> aux = new HashMap<>();
                aux.put("id", arreglo[i].getId().toString(i));                
                aux.put("nombre", arreglo[i].getnombre());
                aux.put("telefono", arreglo[i].getTelefono());
                aux.put("correo", arreglo[i].getcorreo());
                lista.add(aux);
            }
        }
        return lista;
    }

    

    public void registerCliente(@NotEmpty String nombre, @NotEmpty String telefono, @NotEmpty String correo, @NotEmpty String password) throws Exception {
        if(nombre.trim().isEmpty() || telefono.trim().isEmpty() || correo.trim().isEmpty() || password.trim().isEmpty()) {
            throw new Exception("Todos los campos son requeridos.");
        }

        // Verificar que no exista ya un cliente con el mismo correo
        for (Cliente c : dc.listAll().toArray()) {
            if (c.getcorreo().equalsIgnoreCase(correo)) {
                throw new Exception("Ya existe un usuario con ese correo.");
            }
        }

        Cliente nuevo = new Cliente();
        nuevo.setId(dc.listAll().getSize() + 1);
        nuevo.setnombre(nombre);
        nuevo.setTelefono(telefono);
        nuevo.setcorreo(correo);
        nuevo.setPassword(passwordEncoder.encode(password));

        dc.setObj(nuevo);
        if (!dc.save()) {
            throw new Exception("No se pudo registrar el cliente.");
        }
    }

    public boolean login(@NotEmpty String correo, @NotEmpty String password) throws Exception {
        for (Cliente c : dc.listAll().toArray()) {
            if (c.getcorreo().equalsIgnoreCase(correo)) {
                if (passwordEncoder.matches(password, c.getPassword())) {
                    return true; // Login correcto
                } else {
                    return false; // Contraseña incorrecta
                }
            }
        }
        return false; // Usuario no encontrado
    }


}
