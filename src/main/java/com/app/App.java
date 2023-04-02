package com.app;


import com.app.controladores.CuentaController;
import com.app.controladores.TransaccionController;
import com.app.controladores.UsuarioController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class App {
    public static void main(String[] args) {

        Server server = new Server(8080);
        server.setHandler(new DefaultHandler());

        ServletContextHandler context = new ServletContextHandler();

        context.setContextPath("/");
        context.addServlet(UsuarioController.class, "/usuario/*");
        context.addServlet(CuentaController.class, "/cuenta/*");
        context.addServlet(TransaccionController.class, "/transaccion/*");

        server.setHandler(context);

        try{
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
