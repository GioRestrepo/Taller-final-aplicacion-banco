package com.app.servicios;

import java.util.List;
import java.util.Map;

public interface Servicio {
    public Object crear(Map objeto);
    public String eliminar(Object id);
    public List<?> listar(Object id);
}
