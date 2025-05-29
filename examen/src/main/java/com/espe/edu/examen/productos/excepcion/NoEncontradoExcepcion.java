package com.espe.edu.examen.productos.excepcion;

public class NoEncontradoExcepcion extends RuntimeException {

    private final Integer errorCode;
    private final String entidad;

    public NoEncontradoExcepcion(String entidad, String message) {
        super(message);
        this.errorCode = 1;
        this.entidad = entidad;
    }

    public NoEncontradoExcepcion(String message) {
        super(message);
        this.errorCode = 1;
        this.entidad = "Producto";
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", Entidad: " + this.entidad + ", Mensaje: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getEntidad() {
        return entidad;
    }
}
