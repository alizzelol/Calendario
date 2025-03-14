package com.alizzelol.calendario;

public class Inscripcion {
    private String padreId;
    private String eventoId;

    // Constructor vacío (necesario para Firestore)
    public Inscripcion() {}

    public Inscripcion(String padreId, String eventoId) {
        this.padreId = padreId;
        this.eventoId = eventoId;
    }

    public String getPadreId() {
        return padreId;
    }

    public void setPadreId(String padreId) {
        this.padreId = padreId;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    @Override
    public String toString() {
        return "Inscripcion{" +
                "padreId='" + padreId + '\'' +
                ", eventoId='" + eventoId + '\'' +
                '}';
    }
}



