package com.app.controladores;

import com.app.entidades.Usuario;
import com.app.servicios.Servicio;
import com.app.servicios.UsuarioServicio;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsuarioController extends HttpServlet {
    private ObjectMapper mapper;
    private Servicio servicio;

    public UsuarioController() {
        mapper = new ObjectMapper();
        servicio = new UsuarioServicio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        switch (request.getPathInfo()) {
            case "/buscar":
                try {
                    // traemos las cuentas almacenadas
                    ArrayList<Usuario> usuarios = (ArrayList<Usuario>) servicio.listar(null);

                    // Creamos el objecto usuario
                    String json = mapper.writeValueAsString(usuarios);

                    // configuramos la respuesta a devolver
                    response.setStatus(HttpServletResponse.SC_OK);
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
                Map<String, Object> diccionarioUsuario = mapper.readValue(request.getInputStream(), HashMap.class);
                String resultado = (String)servicio.crear(diccionarioUsuario);

                // Creamos el objecto usuario
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


