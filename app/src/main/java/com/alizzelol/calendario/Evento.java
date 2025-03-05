package com.alizzelol.calendario;

import java.util.Date;

public class Evento {
    private String id;
    private String titulo;
    private String descripcion;
    private Date fecha;
    private String tipo;

    public Evento(String id, String titulo, String descripcion, Date fecha, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public Date getFecha() { return fecha; }
    public String getTipo() { return tipo; }
}