package com.app.entidades;

import java.util.ArrayList;

public class CuentaCorriente extends Cuenta {
    private int numeroDetransferencias = 0;

    public CuentaCorriente(String tipoCuenta, String numeroDeCuenta, int idUsuario, float saldo) {
        super(tipoCuenta, numeroDeCuenta, idUsuario, saldo);
    }

    @Override
    public void consignarDinero(float dinero) {
        if (dinero > 0) {
            this.saldo += dinero;
            this.numeroDeConsignaciones++;
        }
    }

    @Override
    public void retirarDinero(float dinero){
        if (dinero <= this.saldo && numeroDeRetiros <6) {
            this.saldo -= dinero;
            this.numeroDeRetiros++;
        } else{
            System.out.println("EL máximo de retiros es Cinco");
        }
    }

    @Override
    public void transferirDinero(String cuentaDestino, float montoTransferencia){
        float dinero = (float) (montoTransferencia + (montoTransferencia * 0.02));

        if (this.getTipoCuenta() != cuentaDestino){
            if (montoTransferencia <= this.saldo && this.numeroDetransferencias <2) {
                this.saldo -= dinero;
                this.numeroDetransferencias++;
            }
        }else {
            if (montoTransferencia <= this.saldo) {
                this.saldo -= montoTransferencia;
            }
        }
    }
}