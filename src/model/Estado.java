package model;

import java.util.ArrayList;

/**
 * Clase estado que es utilizada por la clase Automata, pues un automata puede tener uno o muchos estados.
 * @author Bryan
 * @author Danna
 */
public class Estado {

    private String nombre;
    private int particion;
    private char[] salidas;
    private ArrayList<Estado> estadosSiguientes;
    private boolean visitado;
    
/**	
 * Metodo contructor para la generar un estado de un automata.
 * @param nombre nombre del estado.
 * @param salidas valores de salida del estado.
 */
    public Estado(String nombre, char[] salidas) {
        this.nombre = nombre;
        this.salidas = salidas;
        particion = -1;
        visitado = false;
        estadosSiguientes = new ArrayList<>();
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


    public char[] getSalidas() {
        return salidas;
    }

    public void setSalidas(char[] salidas) {
        this.salidas = salidas;
    }

    public ArrayList<Estado> getEstadosSiguientes() {
        return estadosSiguientes;
    }

    public void setEstadosSiguientes(ArrayList<Estado> estadosSiguientes) {
        this.estadosSiguientes = estadosSiguientes;
    }
}
