package model;

import java.util.ArrayList;

public class Estado {

    private String nombre;
    private boolean visitado;
    private int particion;
    private int particionAnterior;
    //El siguiente arreglo de char contiene el comportamiento de el estado dependiendo las entradas
    //por ejemplo, si entra un 0 guardamos a que estado va y su salida (A,1)
    private char[] comportamiento;
    private ArrayList<Estado> estadosSiguientes;

    public Estado(String nombre, char[] comportamiento) {
        this.nombre = nombre;
        this.comportamiento = comportamiento;
        visitado = false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getVisitado() {
        return visitado;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }

    public int getParticion() {
        return particion;
    }

    public void setParticion(int particion) {
        this.particion = particion;
    }

    public int getParticionAnterior() {
        return particionAnterior;
    }

    public void setParticionAnterior(int particionAnterior) {
        this.particionAnterior = particionAnterior;
    }

    public char[] getComportamiento() {
        return comportamiento;
    }

    public void setComportamiento(char[] comportamiento) {
        this.comportamiento = comportamiento;
    }

    public ArrayList<Estado> getEstadosSiguientes() {
        return estadosSiguientes;
    }

    public void setEstadosSiguientes(ArrayList<Estado> estadosSiguientes) {
        this.estadosSiguientes = estadosSiguientes;
    }

    //Este método actualiza el bloque anterior como el bloque actual dado que el bloque actual será cambiado por uno nuevo ya que hay una nueva partición
    public void actualizarParticionAnterior(){
        particionAnterior = particion;
    }
}
