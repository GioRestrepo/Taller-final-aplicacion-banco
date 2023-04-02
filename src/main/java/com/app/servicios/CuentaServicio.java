package com.app.servicios;

import com.app.entidades.Cuenta;
import com.app.entidades.CuentaCorriente;
import com.app.entidades.CuentaDeAhorros;
import com.app.entidades.Usuario;
import com.app.repositorios.CuentaBaseDeDatos;
import com.app.repositorios.Repositorio;
import com.app.repositorios.UsuarioBaseDeDatos;

import java.util.List;
import java.util.Map;

public class CuentaServicio implements Servicio{
    private Repositorio repositorioCuenta;
    private Repositorio repositorioUsuario;

    public CuentaServicio() {
        repositorioCuenta = new CuentaBaseDeDatos();
        repositorioUsuario = new UsuarioBaseDeDatos();
    }

    public Object crear(Map objeto) {
        String numeroCuenta = (String) objeto.get("numeroCuenta");
        int idUsuario = (int) objeto.get("idUsuario");
        String tipoCuenta = (String) objeto.get("TipoCuenta");
        double saldo = (double) objeto.get("saldo");

        if(!tipoCuenta.equalsIgnoreCase("CA") && !tipoCuenta.equalsIgnoreCase("CC")){
            throw new RuntimeException("Los unicos tipos de cuentas adminitdos son CA y CC");
        }

        Usuario usuario = (Usuario)repositorioUsuario.Buscar(idUsuario);

        if(usuario == null) {
            throw new RuntimeException("El usuario indicado no existe");
        }

        Cuenta cuenta;
        if(tipoCuenta.equalsIgnoreCase("CC")){
            cuenta = new CuentaCorriente(tipoCuenta, numeroCuenta, idUsuario, (float) saldo);
        } else {
            cuenta = new CuentaDeAhorros(tipoCuenta, numeroCuenta, idUsuario, (float)saldo);
        }

        return repositorioCuenta.crear(cuenta);
    }

    @Override
    public String eliminar(Object id) {
        return repositorioCuenta.eliminar(id);
    }

    @Override
    public List<?> listar(Object id) {
        return repositorioCuenta.listar(id);
    }
}
