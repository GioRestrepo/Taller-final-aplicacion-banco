package com.app.servicios;

import com.app.entidades.Cuenta;
import com.app.entidades.CuentaCorriente;
import com.app.entidades.CuentaDeAhorros;
import com.app.entidades.Transaccion;
import com.app.repositorios.CuentaBaseDeDatos;
import com.app.repositorios.Repositorio;
import com.app.repositorios.TransaccionBaseDeDatos;
import com.app.servicios.validaciones.Validaciones;

import java.util.List;
import java.util.Map;

public class TransaccionServicio implements Servicio {
    private Repositorio repositorioTransacciones;
    private Repositorio repositorioCuentas;

    public TransaccionServicio() {
        repositorioTransacciones = new TransaccionBaseDeDatos();
        repositorioCuentas = new CuentaBaseDeDatos();
    }

    public Object crear(Map objeto) {

        String tipoTransaccion = (String) objeto.get("tipoTransaccion");
        float monto = Float.parseFloat(objeto.get("monto").toString());
        int idCuenta = (int) objeto.get("idCuenta");
        String tipoCuentaDestino = (String) objeto.get("tipoCuentaDestino");

        // Validamos los campos que obtenemos de la peticion
        Validaciones.validarCamposTransacciones(tipoTransaccion, tipoCuentaDestino, idCuenta, monto);

        // Obtenemos la cuenta relacionada
        Cuenta cuenta = (Cuenta)repositorioCuentas.Buscar(idCuenta);

        //Validamos que la cuenta obtenida existya
        Validaciones.validarObjecto(cuenta, "cuenta");

        // Creamos el objecto transaccion
        Transaccion transaccion = new Transaccion(0, tipoTransaccion, monto, idCuenta, tipoCuentaDestino);

        // Casteamos al tipo de cuenta
        if(cuenta.getTipoCuenta().equals("CC")){
            CuentaCorriente cuentaCorriente = new CuentaCorriente(cuenta.getTipoCuenta(), cuenta.getNumeroDeCuenta(), cuenta.getIdUsuario(), cuenta.getSaldo());
            // Aplicamos el tipo de transaccion
            cuenta.RealizarTransaccion(transaccion);
            cuenta = cuentaCorriente;
        } else {
            CuentaDeAhorros cuentaAhorro = new CuentaDeAhorros(cuenta.getTipoCuenta(), cuenta.getNumeroDeCuenta(), cuenta.getIdUsuario(), cuenta.getSaldo());
            // Aplicamos el tipo de transaccion
            cuenta.RealizarTransaccion(transaccion);
            cuenta = cuentaAhorro;
        }

        // Actualizamos el saldo de la cuenta
        int cuentaActualizada = (int) repositorioCuentas.Actualizar(cuenta);

        // Validamos que se haya actualizado el saldo correctamente
        Validaciones.validarCantidadActualizada(cuentaActualizada, "cuenta");

        // Creamos el registro de la transaccion
        return repositorioTransacciones.crear(transaccion);
    }

    @Override
    public String eliminar(Object id) {
        return repositorioTransacciones.eliminar(id);
    }

    @Override
    public List<?> listar(Object id) {
        return repositorioTransacciones.listar(id);
    }
}
