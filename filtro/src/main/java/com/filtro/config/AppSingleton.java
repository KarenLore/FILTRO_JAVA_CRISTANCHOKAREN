package com.filtro.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum AppSingleton {
    INSTANCIA;

    private final Properties propiedades = new Properties();

    AppSingleton() {
        cargarConfiguraciones("configmysql.properties");
    }

      
    private void cargarConfiguraciones(String rutaArchivo) {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(rutaArchivo)) {
        if (inputStream == null) {
            throw new IOException("Archivo no encontrado: " + rutaArchivo);
        }
        propiedades.load(inputStream);
    } catch (IOException e) {
        System.err.println("❌ Error cargando configuración: " + e.getMessage());
        throw new RuntimeException("No se pudo cargar la configuración", e);
    }
}
    public String get(String clave) {
        return propiedades.getProperty(clave, "No encontrado");
    }
}