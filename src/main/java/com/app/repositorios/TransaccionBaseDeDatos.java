package com.app.repositorios;


import com.app.entidades.Transaccion;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class TransaccionBaseDeDatos implements Repositorio {
    private String conexionBD;

    public TransaccionBaseDeDatos(){
        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
            conexionBD = "jdbc:sqlite:banco.db";
        }catch (SQLException e){
            System.err.println("Error de base de datos: " + e);
        }
    }


    @Override
    public Object crear(Object objeto) {
        try (Connection conexion = DriverManager.getConnection(conexionBD)) {
            Transaccion transaccion = (Transaccion) objeto;
            String sentenciaSql = "INSERT INTO TRANSACCIONES (FECHA, HORA, TIPO_TRANSACCION, MONTO, ID_CUENTA, TIPO_CUENTA_DESTINO) " +
                    " VALUES('" + transaccion.getFecha() + "', '" + transaccion.getHora()
                    + "', '" + transaccion.getTipoTransaccion() + "', " + transaccion.getMonto()
                    + ","  + transaccion.getIdCuenta() + ",'" + transaccion.getTipoCuentaDestino() + "');";
            Statement sentencia = conexion.createStatement();
            sentencia.execute(sentenciaSql);
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return "Transacción realizada";
    }

    @Override
    public String eliminar(Object id) {
        try (Connection conexion = DriverManager.getConnection(conexionBD)) {

            // * Eliminamos las transacciones
            String sentenciaEliminarTransaccionsSql = "DELETE FROM TRANSACCIONES WHERE ID = ?";
            PreparedStatement sentenciaEliminarTransacciones = conexion.prepareStatement(sentenciaEliminarTransaccionsSql);

            sentenciaEliminarTransacciones.setInt(1, (int) id);
            int cantidadDeTuplasAfectadas = sentenciaEliminarTransacciones.executeUpdate();

            if(cantidadDeTuplasAfectadas > 0){
                return "La transacción ha sido eliminada";
            } else {
                throw new RuntimeException("Ha ocurrido un error al eliminar la transaccion");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public List<?> listar(Object idCuentaAListar) {
        List<Transaccion> transacciones = new ArrayList<>();

        try (Connection conexion = DriverManager.getConnection(conexionBD)) {
            String sentenciaSql = "SELECT * FROM TRANSACCIONES WHERE ID_CUENTA = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, (int)idCuentaAListar);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null) {
                while (resultadoConsulta.next()) {
                    Transaccion transaccion = null;
                    int id = resultadoConsulta.getInt("ID");
                    String fecha = resultadoConsulta.getString("FECHA");
                    String hora = resultadoConsulta.getString("HORA");
                    String tipoTransaccion = resultadoConsulta.getString("TIPO_TRANSACCION");
                    float monto = resultadoConsulta.getFloat("MONTO");
                    int idCuenta = resultadoConsulta.getInt("ID_CUENTA");
                    String tipoCuenta = resultadoConsulta.getString("TIPO_CUENTA_DESTINO");

                    transaccion = new Transaccion(id, tipoTransaccion, monto, idCuenta,tipoCuenta);
                    transacciones.add(transaccion);
                }
                return transacciones;
            }
        } catch (SQLException e) {
            throw  new RuntimeException("Error de base de datos: " + e);
        }
        return null;
    }

    @Override
    public Object Buscar(int id) {
        throw new RuntimeException("No implementado para este repositorio");
    }

    @Override
    public Object Actualizar(Object objecto) {
        throw new RuntimeException("No implementado para este repositorio");
    }
}
