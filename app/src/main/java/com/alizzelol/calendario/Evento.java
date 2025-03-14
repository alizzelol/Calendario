package com.alizzelol.calendario;

import java.io.Serializable;
import java.util.Date;

public class Evento implements Serializable {

    private String id;
    private String titulo;
    private String descripcion;
    private Date fecha;
    private String hora;
    private String tipo;

    public Evento() {
    }

    public Evento(String id, String titulo, String descripcion, Date fecha, String hora, String tipo) {
        this.id = id;
        this.titulo = titulo != null ? titulo : "";  // Manejo de nulos
        this.descripcion = descripcion != null ? descripcion : "";
        this.fecha = fecha;
        this.hora = hora != null ? hora : "";
        this.tipo = tipo != null ? tipo : "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", hora='" + hora + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}