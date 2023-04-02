package com.app.servicios;

import com.app.entidades.Usuario;
import com.app.repositorios.Repositorio;
import com.app.repositorios.UsuarioBaseDeDatos;

import java.util.List;
import java.util.Map;

public class UsuarioServicio implements Servicio {
    private Repositorio repositorio;

    public UsuarioServicio() {
        repositorio = new UsuarioBaseDeDatos();
    }

    @Override
    public Object crear(Map objeto) {

        String nombre = (String) objeto.get("nombre");
        String apellido = (String) objeto.get("apellido");
        String cedula = (String) objeto.get("cedula");

        Usuario usuario = new Usuario(0, nombre, apellido, cedula);
        return repositorio.crear(usuario);
    }

    @Override
    public String eliminar(Object id) {
        return repositorio.eliminar(id);
    }

    @Override
    public List<?> listar(Object id) {
        return repositorio.listar(id);
    }
}
