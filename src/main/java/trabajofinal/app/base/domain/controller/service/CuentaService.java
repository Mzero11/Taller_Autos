package trabajofinal.app.base.domain.controller.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoCuenta;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoRol;

import org.checkerframework.checker.units.qual.A;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;


@BrowserCallable
@AnonymousAllowed
public class CuentaService {
    private DaoCuenta db;
    private SecurityContext context;

    public CuentaService() {
        db = new DaoCuenta();
        context = SecurityContextHolder.getContext();
    }

    public HashMap<String, String> createRoles(){
        HashMap<String, String> mapa = new HashMap<>();
        mapa.put("resp", "Ya creado");
        mapa.put("code", "201");
        DaoRol dr = new DaoRol();
        if (dr.listAll().isEmpty()) {
            dr.getObj().setNombre("admin");
            dr.save();
            dr.getObj().setNombre("user");
            dr.save();
            dr.setObj(null);
            mapa.put("resp", "Creado");
            mapa.put("code", "200");
        }
        return mapa;
    }

    public Authentication getAuthentication() {
        System.out.println("Pide autenticacion *********");
        System.out.println(context.getAuthentication());
        return context.getAuthentication();
    }

    public Boolean isLogin(){
        if (getAuthentication() != null) 
            return getAuthentication().isAuthenticated();
        return false;
    }

    public HashMap<String, Object> login(String email, String password) throws Exception {
        HashMap<String, Object> mapa = new HashMap<>();
        try {
            HashMap<String, Object> aux = db.login(email,password);
            if (aux != null) {
                context.setAuthentication(
                    new UsernamePasswordAuthenticationToken(aux.get("usuario").toString(), 
                        aux.get("id").toString(),
                        getAutorities(aux)));
                mapa.put("user", context.getAuthentication());
                mapa.put("message", "OK");
                mapa.put("estado", "true");
            }
        } catch (Exception e) {
            mapa.put("user", new HashMap<>());
            mapa.put("message", "Usuario o clave incorrecta, o cuenta no existe");
            mapa.put("estadao", "false");
            context.setAuthentication(null);
            System.out.println(e);
            // TODO: handle exception
        }
        return mapa;
    }

    private static List<GrantedAuthority> getAutorities(HashMap<String, Object> user) throws Exception {
        DaoRol dr = new DaoRol();
        dr.setObj(dr.get(Integer.parseInt(user.get("rol").toString())));
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_" + dr.getObj().getNombre()));
        return list;
    }


    
public HashMap<String, String> logout() {
    context.setAuthentication(null);
    HashMap<String, String> mapa = new HashMap<>();
    mapa.put("msg", "ok");
    return mapa;
}
}
