package trabajofinal.app.base.domain.controller.dao;


import trabajofinal.app.base.domain.controller.datastruct.list.LinkedList;
import trabajofinal.app.base.domain.controller.datastruct.list.Node;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;

import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;



public class AdapterDao<T> implements InterfaceDao<T> {
    private Class<T> clazz;
    private Gson g;
    protected static String base_path = "data" + File.separatorChar;

    public AdapterDao(Class<T> clazz) {
        this.clazz = clazz;
        this.g = new GsonBuilder().setPrettyPrinting().create();
    }

    private String readFile() throws Exception {
        File file = new File(base_path + clazz.getSimpleName() + ".json");
        if (!file.exists()) {
            saveFile("[]");
        }
        StringBuilder sb = new StringBuilder();
        try (Scanner in = new Scanner(new FileReader(file))) {
            while (in.hasNextLine()) {
                sb.append(in.nextLine()).append("\n");
            }
        } catch (Exception e) {
            throw new Exception("Error al leer el archivo: " + file.getAbsolutePath(), e);
        }
        return sb.toString();
    }

    private void saveFile(String data) throws Exception {
        File file = new File(base_path + clazz.getSimpleName() + ".json");
        if (!file.exists()) {
            file.createNewFile();
        }
        // if (!file.exists()) {
        FileWriter fw = new FileWriter(file);
        fw.write(data);
        fw.close();

        // }
        try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
            writer.write(data);
        } catch (Exception e) {
            throw new Exception("Error al guardar el archivo: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public LinkedList<T> listAll() {
        LinkedList<T> lista = new LinkedList<>();
        try {
            String data = readFile();
            T[] m = (T[]) g.fromJson(data, java.lang.reflect.Array.newInstance(clazz, 0).getClass());
            lista.toList(m);
        } catch (Exception e) {
            // TODO
        }

        return lista;
    }

    @Override
    public void persist(T obj) throws Exception {
        LinkedList<T> lista = listAll();
        lista.add(obj);
        saveFile(g.toJson(lista.toArray()));
    }

    @Override
    public void update(T obj, Integer pos) throws Exception {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'update'");
        LinkedList<T> lista = listAll();
        lista.update(obj, pos);
        saveFile(g.toJson(lista.toArray()));
    }

    @Override
    public void update_id(T obj, Integer id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update_id'");
    }

    @Override
    public void update_by_id(T obj, Integer id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update_by_id'");
    }

    /*
     * public Boolean delete(Integer id) throws Exception {
     * LinkedList<T> lista = listAll();
     * T obj = lista.get(id);
     * if (obj != null) {
     * lista.remove(obj);
     * saveFile(g.toJson(lista.toArray()));
     * return true;
     * }
     * return false;
     * }
     */

    @Override
    public T get(Integer id) throws Exception {
        if (!listAll().isEmpty())
            return BinarySearchRecursive(listAll().toArray(), 0, listAll().getSize() - 1, id);
        else
            return null;
    }

    public T BinarySearchRecursive(T[] arr, int a, int b, Integer id) throws Exception {
        if (a > b) {
            return null;
        }

        int n = a + (b - a) / 2;

        Integer currentId = (Integer) getMethod("Id", arr[n]);

        if (currentId.equals(id)) {
            return arr[n];
        } else if (currentId > id) {
            return BinarySearchRecursive(arr, a, n - 1, id);
        } else {
            return BinarySearchRecursive(arr, n + 1, b, id);
        }
    }

    private Object getMethod(String attribute, T obj) throws Exception {
        return obj.getClass().getMethod("get" + attribute).invoke(obj);
    }
}