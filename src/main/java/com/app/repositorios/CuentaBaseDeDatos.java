package com.app.repositorios;

import com.app.entidades.Cuenta;
import com.app.entidades.CuentaCorriente;
import com.app.entidades.CuentaDeAhorros;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class CuentaBaseDeDatos implements Repositorio {
    private String conexionBD;

    public CuentaBaseDeDatos() {
        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
            conexionBD = "jdbc:sqlite:banco.db";
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e);
        }
    }

    @Override
    public Object crear(Object objeto) {
        try (Connection conexion = DriverManager.getConnection(conexionBD)) {
            Cuenta cuenta = (Cuenta) objeto;
            String sentenciaSql = "INSERT INTO cuentas (NUMERO_CUENTA, SALDO, TIPO_CUENTA, ID_USUARIO) " +
                    " VALUES('" + cuenta.getNumeroDeCuenta()
                    + "', " + cuenta.getSaldo()
                    + ", '" + cuenta.getTipoCuenta()
                    + "', " + cuenta.getIdUsuario() + ");";
            System.out.println(sentenciaSql);
            Statement sentencia = conexion.createStatement();
            sentencia.execute(sentenciaSql);
        } catch (SQLException e) {
            return "Error de base de datos: " + e;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

        return "Cuenta registrada";
     }

     @Override
     public String eliminar (Object id){
         try (Connection conexion = DriverManager.getConnection(conexionBD)) {
             int idCuenta = (int) id;

             // * Eliminamos las transacciones
             String sentenciaEliminarTransaccionsSql = "DELETE FROM TRANSACCIONES WHERE ID_CUENTA = ?";
             PreparedStatement sentenciaEliminarTransacciones = conexion.prepareStatement(sentenciaEliminarTransaccionsSql);

             sentenciaEliminarTransacciones.setInt(1, idCuenta);
             sentenciaEliminarTransacciones.executeUpdate();

             // * Eliminamos las cuentas
             String sentenciaEliminarCuentasSql = "DELETE FROM CUENTAS WHERE ID = ?";
             PreparedStatement sentenciaEliminarCuentas = conexion.prepareStatement(sentenciaEliminarCuentasSql);
             sentenciaEliminarCuentas.setInt(1, idCuenta);
             int cantidadDeTuplasAfectadas = sentenciaEliminarCuentas.executeUpdate();

             if(cantidadDeTuplasAfectadas > 0){
                 return "La cuenta ha sido eliminada";
             } else {
                throw  new RuntimeException("La cuenta indicada no existe");
             }

         } catch (SQLException e) {
             throw new RuntimeException("Error de base de datos: " + e);
         } catch (Exception e) {
             throw new RuntimeException("Error: " + e.getMessage());
         }
    }


    @Override
    public List<?> listar (Object idUsuarioAListar) {

        List<Cuenta> cuentas = new ArrayList<>();

        try (Connection conexion = DriverManager.getConnection(conexionBD)) {
            String sentenciaSql = "SELECT * FROM CUENTAS WHERE ID_USUARIO = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, (int)idUsuarioAListar);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null) {
                while (resultadoConsulta.next()) {
                    Cuenta cuenta;
                    int id = resultadoConsulta.getInt("ID");
                    String numeroCuenta = resultadoConsulta.getString("NUMERO_CUENTA");
                    float saldo = resultadoConsulta.getFloat("SALDO");
                    String tipoCuenta = resultadoConsulta.getString("TIPO_CUENTA");
                    int idUsuario = resultadoConsulta.getInt("ID_USUARIO");

                    if(tipoCuenta.equalsIgnoreCase("CC")){
                        cuenta = new CuentaCorriente(tipoCuenta, numeroCuenta, idUsuario, saldo);
                    } else {
                        cuenta = new CuentaDeAhorros(tipoCuenta, numeroCuenta, idUsuario, saldo);
                    }
                    cuenta.setId(id);

                    cuentas.add(cuenta);
                }

            }
            return cuentas;
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e);
        }
    }

    @Override
    public Object Buscar(int idCuentaABuscar) {
        try (Connection conexion = DriverManager.getConnection(conexionBD)) {

            String sentenciaSql =
                    "SELECT * FROM CUENTAS "
                            + "WHERE ID = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idCuentaABuscar);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null && resultadoConsulta.next()) {
                int id = resultadoConsulta.getInt("ID");
                String tipoCuenta = resultadoConsulta.getString("TIPO_CUENTA");

                Cuenta cuentaEncontrada;

                if(tipoCuenta.equalsIgnoreCase("CA")){
                    cuentaEncontrada = new CuentaDeAhorros(
                            tipoCuenta,
                            resultadoConsulta.getString("NUMERO_CUENTA"),
                            resultadoConsulta.getInt("ID_USUARIO"),
                            resultadoConsulta.getFloat("SALDO")
                    );
                } else {
                    cuentaEncontrada = new CuentaDeAhorros(
                            tipoCuenta,
                            resultadoConsulta.getString("NUMERO_CUENTA"),
                            resultadoConsulta.getInt("ID_USUARIO"),
                            resultadoConsulta.getFloat("SALDO")
                    );
                }
                cuentaEncontrada.setId(id);

                return cuentaEncontrada;
            }
        } catch (SQLException e) {
            return "Error de base de datos: " + e;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

        return null;
    }

    @Override
    public Object Actualizar(Object objeto) {
        Cuenta cuenta = (Cuenta)objeto;
        try (Connection conexion = DriverManager.getConnection(conexionBD)) {

            String sentenciaSql =
                    "UPDATE CUENTAS SET SALDO = ? WHERE ID = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setDouble(1, cuenta.getSaldo());
            sentencia.setInt(2, (int) cuenta.getId());

            return sentencia.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}

