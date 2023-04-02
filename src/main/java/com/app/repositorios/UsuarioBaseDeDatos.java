package com.app.repositorios;

import com.app.entidades.Cuenta;
import com.app.entidades.CuentaDeAhorros;
import com.app.entidades.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioBaseDeDatos implements Repositorio {
    private String conexionBD;

    public UsuarioBaseDeDatos(){
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
            Usuario usuario = (Usuario) objeto;
            String sentenciaSql = "INSERT INTO USUARIOS (NOMBRE, APELLIDO, CEDULA) " +
                    " VALUES('" + usuario.getNombre() + "', '" + usuario.getApellido()
                    + "', '" + usuario.getCedula() + "');";
            Statement sentencia = conexion.createStatement();
            sentencia.execute(sentenciaSql);
        } catch (SQLException e) {
            String message  = e.getMessage();
            if(message.contains("SQLITE_CONSTRAINT_UNIQUE")){
                throw new RuntimeException("Error: " + "La identificacion ingresada ya existe");
            }
            throw new RuntimeException("Error de base de datos: " + e);
        } catch (Exception e) {
            return new RuntimeException("Error: " + e.getMessage());
        }
        return "Usuario creado";
    }

    @Override
    public String eliminar(Object id) {
        try (Connection conexion = DriverManager.getConnection(conexionBD)) {
            int idUsuario = (int) id;

            // * Obtención de Cuentas
            String sentenciaCuentasSql = "SELECT ID FROM CUENTAS WHERE ID_USUARIO = ?";
            PreparedStatement sentenciaCuentas = conexion.prepareStatement(sentenciaCuentasSql);
            sentenciaCuentas.setInt(1, idUsuario);
            ResultSet resultadoConsultaCuentas = sentenciaCuentas.executeQuery();

            // * Eliminamos las transacciones
            int tempId= 0;
            String sentenciaEliminarTransaccionsSql = "DELETE FROM TRANSACCIONES WHERE ID_CUENTA = ?";
            PreparedStatement sentenciaEliminarTransacciones;

            if(resultadoConsultaCuentas != null){
                while(resultadoConsultaCuentas.next()){
                    tempId = resultadoConsultaCuentas.getInt("ID");
                    if(tempId != 0){
                        sentenciaEliminarTransacciones = conexion.prepareStatement(sentenciaEliminarTransaccionsSql);
                        sentenciaEliminarTransacciones.setInt(1, tempId);
                        sentenciaEliminarTransacciones.executeUpdate();
                    }
                }
            }

            // * Eliminamos las cuentas
            String sentenciaEliminarCuentasSql =
                    "DELETE FROM CUENTAS WHERE ID_USUARIO = ?";
            PreparedStatement sentenciaEliminarCuentas = conexion.prepareStatement(sentenciaEliminarCuentasSql);
            sentenciaEliminarCuentas.setInt(1, idUsuario);
            sentenciaEliminarCuentas.executeUpdate();

            // * Eliminación de usuario
            String sentenciaSql = "Delete FROM USUARIOS WHERE ID = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idUsuario);
            
            int tuplasAfectadas = sentencia.executeUpdate();

            if(tuplasAfectadas > 0){
                return "El usuario ha sido eliminado";
            } else {
                throw new RuntimeException("El usuario indicado no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Hubo un error con la conexión: " + e);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public List<?> listar(Object idUnUsed) {
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexion = DriverManager.getConnection(conexionBD)) {
            String sentenciaSql = "SELECT * FROM USUARIOS";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null) {
                while (resultadoConsulta.next()) {
                    Usuario usuario = null;
                    int id = resultadoConsulta.getInt("ID");
                    String nombre = resultadoConsulta.getString("NOMBRE");
                    String apellido = resultadoConsulta.getString("APELLIDO");
                    String cedula = resultadoConsulta.getString("CEDULA");

                    usuario = new Usuario(id, nombre, apellido, cedula);
                    usuarios.add(usuario);
                }
                return usuarios;
            }
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e);
        }
        return null;
    }

    @Override
    public Object Buscar(int idUsuarioAEncontrar) {
        try (Connection conexion = DriverManager.getConnection(conexionBD)) {

            String sentenciaSql =
                    "SELECT * FROM USUARIOS "
                            + "WHERE ID = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idUsuarioAEncontrar);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null && resultadoConsulta.next()) {
                Usuario usuario = new Usuario(
                            idUsuarioAEncontrar,
                            resultadoConsulta.getString("NOMBRE"),
                            resultadoConsulta.getString("APELLIDO"),
                            resultadoConsulta.getString("CEDULA")
                        );

                usuario.setId(idUsuarioAEncontrar);

                return usuario;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Object Actualizar(Object objecto) {
        throw new RuntimeException("No implementado para este repositorio");
    }
}
