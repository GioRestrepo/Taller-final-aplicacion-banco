package com.app.entidades;

import java.util.ArrayList;

public class CuentaDeAhorros extends Cuenta {

    public CuentaDeAhorros(String tipoCuenta, String numeroDeCuenta, int idUsuario, float saldo) {
        super(tipoCuenta, numeroDeCuenta, idUsuario, saldo);
    }

    @Override
    public void consignarDinero(float dinero) {
        if (dinero > 0) {
            if (dinero > 0 && numeroDeConsignaciones < 2) {
                this.saldo += (dinero + (dinero * 0.05));
                this.numeroDeConsignaciones++;
            } else {
                this.saldo += dinero;
                this.numeroDeConsignaciones++;
            }
        }
    }

    @Override
    public void retirarDinero(float dinero){

        if (dinero <= this.saldo) {
            if (numeroDeRetiros > 3) {
                float dineroRetirado = this.saldo -= (dinero + (dinero * 0.01));
                this.numeroDeRetiros++;
            } else {
                this.saldo -= dinero;
                this.numeroDeRetiros++;
            }
        } else {
            System.out.println("Saldo insuficiente");
        }

    }

    @Override
    public void transferirDinero(String cuentaDestino, float montoTransferencia){

        float dinero = (float) (montoTransferencia + (montoTransferencia * 0.015));

        if (this.getTipoCuenta() != cuentaDestino){

            if (montoTransferencia <= this.saldo) {
                this.saldo -= dinero;
            }
        }else{
            if (montoTransferencia <= this.saldo) {
                this.saldo -= montoTransferencia;
            }
        }
    }
}