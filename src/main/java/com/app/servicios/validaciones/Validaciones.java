package com.app.servicios.validaciones;

public class Validaciones {
    public static void validarCamposTransacciones(String tipoTransaccion,String tipoCuentaDestino, int idCuenta, float monto){
        if(tipoTransaccion == null ||
                tipoTransaccion.equalsIgnoreCase("") ||
                tipoCuentaDestino == null ||
                tipoCuentaDestino.equalsIgnoreCase("") ||
                monto < 0 || idCuenta < 0)
        {
            throw new RuntimeException("Los valores ingresados son incorrectos");
        }

        if(!tipoTransaccion.equalsIgnoreCase("retirar") &&
                !tipoTransaccion.equalsIgnoreCase("depositar") &&
                !tipoTransaccion.equalsIgnoreCase("transferir"))
        {
            throw new RuntimeException("La transaccion indicada no es valida, la unicas transacciones disponibles son " +
                    "retirar, depositar y transferir");
        }

        if(!tipoCuentaDestino.equalsIgnoreCase("CA") && !tipoCuentaDestino.equalsIgnoreCase("CC")){
            throw new RuntimeException("El tipo de cuenta destino no es valido, los unicos tipos validos son CC (Para cuetna de ahorro)" +
                    " y CA (Para cuenta corriente)");
        }
    }

    public static void validarObjecto(Object objecto, String nombreObjecto){
        if(objecto == null){
            throw new RuntimeException("El/La " + nombreObjecto + " indicad@ no existe");
        }
    }

    public static void validarCantidadActualizada(int cantidad, String nombreObjecto){
        if(cantidad <= 0) {
            throw new RuntimeException("Ha ocurrido un error al actualizar el/la " + nombreObjecto);
        }
    }
}
