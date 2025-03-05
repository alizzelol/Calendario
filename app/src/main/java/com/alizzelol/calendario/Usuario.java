package com.alizzelol.calendario;

public class Usuario {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String rol;

    public Usuario(String nombre, String apellido, String email, String telefono, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getRol() { return rol; }
}
