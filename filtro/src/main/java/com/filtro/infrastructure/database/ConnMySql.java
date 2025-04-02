package com.filtro.infrastructure.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.filtro.config.AppSingleton;

public class ConnMySql implements ConnectionDb {
    @Override
    public Connection getConexion() throws SQLException {
        AppSingleton config = AppSingleton.INSTANCIA;
        String url = config.get("db.url");
        String usuario = config.get("db.user");
        String password = config.get("db.password");

        return DriverManager.getConnection(url, usuario, password);
    }

}
