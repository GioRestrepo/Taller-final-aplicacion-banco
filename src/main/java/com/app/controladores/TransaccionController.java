package com.app.controladores;

import com.app.entidades.Transaccion;
import com.app.entidades.Usuario;
import com.app.servicios.Servicio;
import com.app.servicios.TransaccionServicio;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransaccionController extends HttpServlet {
    private ObjectMapper mapper;
    private Servicio servicio;

    public TransaccionController() {
        mapper = new ObjectMapper();
        servicio = new TransaccionServicio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        switch (request.getPathInfo()) {
            case "/buscar":
                try {
                    // obtenemos el id para filtrar desde los parametros
                    int id = Integer.parseInt(request.getParameter("id"));

                    // traemos las cuentas almacenadas
                    ArrayList<Transaccion> transacciones = (ArrayList<Transaccion>) servicio.listar(id);

                    // Creamos el objecto transaccion
                    String json = mapper.writeValueAsString(transacciones);

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
                Map<String, Object> diccionarioTransaccion = mapper.readValue(request.getInputStream(), HashMap.class);
                String resultado = (String)servicio.crear(diccionarioTransaccion);

                // Creamos el objecto transaccion
                Map<String, String> respuesta = new HashMap<>();
                respuesta.put("mensaje", resultado);
                String json = mapper.writeValueAsString(respuesta);

                // configuramos la respuesta a devolver
                response.setStatus(HttpServletResponse.SC_CREATED);
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
