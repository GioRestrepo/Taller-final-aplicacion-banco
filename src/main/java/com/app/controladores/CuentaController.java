package com.app.controladores;

import com.app.entidades.Cuenta;
import com.app.servicios.CuentaServicio;
import com.app.servicios.Servicio;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CuentaController extends HttpServlet {
    private ObjectMapper mapper;
    private Servicio servicio;

    public CuentaController() {
        mapper = new ObjectMapper();
        servicio = new CuentaServicio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        switch (request.getPathInfo()) {
            case "/buscar":
                try {
                    //Obtenemos el query param para filtrar las cuentas
                    int id = Integer.parseInt(request.getParameter("id"));

                    // traemos las cuentas almacenadas
                    ArrayList<Cuenta> cuentas = (ArrayList<Cuenta>) servicio.listar(id);

                    // Creamos el objecto cuenta
                    String json = mapper.writeValueAsString(cuentas);

                    // configuramos la respuesta a devolver
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.setContentType("application/json");
                    response.getWriter().println(json);
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Map<String, String> error = new HashMap<>();
                    error.put("error", e.getMessage());
                    String json = mapper.writeValueAsString(error);
                    response.setContentType("application/json");
                    response.getWriter().println(json);
                }
                break;
            default:
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Map<String, String> error = new HashMap<>();
                error.put("error", "No se encontro el recurso");
                String json = mapper.writeValueAsString(error);
                response.setContentType("application/json");
                response.getWriter().println(json);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contenido = request.getContentType();
        if (contenido.equals("application/json")) {
            try{
                // Creamos un diccionario con los datos del objecto y lo enviamos al servicio de creacion
                Map<String, Object> diccionarioCuenta = mapper.readValue(request.getInputStream(), HashMap.class);
                String resultado = (String)servicio.crear(diccionarioCuenta);

                // Creamos el objecto cuenta
                Map<String, String> respuesta = new HashMap<>();
                respuesta.put("mensaje", resultado);
                String json = mapper.writeValueAsString(respuesta);

                // configuramos la respuesta a devolver
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(json);
            } catch (Exception e){
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                String json = mapper.writeValueAsString(error);
                response.setContentType("application/json");
                response.getWriter().println(json);
            }
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El contenido debe ser JSON");
            String json = mapper.writeValueAsString(error);
            response.setContentType("application/json");
            response.sendError(HttpServletResponse.SC_CONFLICT, json);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String resultado = servicio.eliminar(id);

            // Creamos el objecto respuesta
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", resultado);
            String json = mapper.writeValueAsString(respuesta);

            // configuramos la respuesta a devolver
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            response.getWriter().println(json);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            String json = mapper.writeValueAsString(error);
            response.setContentType("application/json");
            response.getWriter().println(json);
        }
    }
}
