package com.app.entidades;

import java.util.ArrayList;

public abstract class Cuenta {

    protected int id;
    protected float saldo;
    protected String numeroDeCuenta;
    protected String tipoCuenta;
    protected int numeroDeRetiros = 0;
    protected int numeroDeConsignaciones = 0;
    protected int idUsuario;


    public Cuenta(String tipoCuenta, String numeroDeCuenta, int idUsuario, float saldo) {
        this.tipoCuenta = tipoCuenta;
        this.numeroDeCuenta = numeroDeCuenta;
        this.idUsuario = idUsuario;
        this.saldo = saldo;
    }

    public abstract void consignarDinero(float dinero);

    public  abstract void transferirDinero(String cuentaDestino, float montoTransferencia);

    public void retirarDinero (float dinero){
        if (dinero <= this.saldo) {
            this.saldo -= dinero;
            this.numeroDeRetiros++;
        }else {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public String getNumeroDeCuenta() {
        return numeroDeCuenta;
    }

    public void setNumeroDeCuenta(String numeroDeCuenta) {
        this.numeroDeCuenta = numeroDeCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public int getNumeroDeRetiros() {
        return numeroDeRetiros;
    }

    public void setNumeroDeRetiros(int numeroDeRetiros) {
        this.numeroDeRetiros = numeroDeRetiros;
    }

    public int getNumeroDeConsignaciones() {
        return numeroDeConsignaciones;
    }

    public void setNumeroDeConsignaciones(int numeroDeConsignaciones) {
        this.numeroDeConsignaciones = numeroDeConsignaciones;
    }

    public void RealizarTransaccion(Transaccion transaccion){
        switch (transaccion.getTipoTransaccion().toLowerCase()) {
            case "depositar":
                this.consignarDinero(transaccion.getMonto());
                break;
            case "retirar":
                this.retirarDinero(transaccion.getMonto());
                break;
            case "transferir":
                this.transferirDinero(transaccion.getTipoCuentaDestino(), transaccion.getMonto());
                break;
            default:
                throw new RuntimeException("Transaccion desconocida");
        }
    }
}
