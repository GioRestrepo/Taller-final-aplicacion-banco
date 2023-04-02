package com.app.entidades;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Transaccion {

    private int id;
    private String fecha;
    private String hora;
    private String tipoTransaccion;
    private float monto;
    private int idCuenta; //foreing key
    private String tipoCuentaDestino;

    public Transaccion(int id, String tipoTransaccion, float monto, int idCuenta, String tipoCuentaDestino) {
        this.id = id;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.idCuenta = idCuenta;
        this.tipoCuentaDestino = tipoCuentaDestino;
        // Los calculamos por debajo y no se los pedimos al cliente
        this.fecha = LocalDate.now().toString();
        this.hora = LocalTime.now().toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getTipoCuentaDestino() {
        return tipoCuentaDestino;
    }

    public void setTipoCuentaDestino(String tipoCuentaDestino) {
        this.tipoCuentaDestino = tipoCuentaDestino;
    }
}
