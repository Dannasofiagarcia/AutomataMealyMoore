package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Stack;

public class Automata {

    public final static String MEALY = "Mealy";
    public final static String MOORE = "Moore";

    private String tipoAutomata;
    private ArrayList<Estado> estados;
    private char[] lenguajeEntrada;
    private char[] lenguajeSalida;
    private HashMap<Estado, Integer> indices;
    private boolean conexo;
    private ArrayList<ArrayList<Estado>> particiones;
    private ArrayList<String> mensajeParticiones;

    public Automata(String tipoAutomata, char[] lenguajeEntrada, char[] lenguajeSalida, ArrayList<Estado> estados) {
        this.tipoAutomata = tipoAutomata;
        this.lenguajeEntrada = lenguajeEntrada;
        this.lenguajeSalida = lenguajeSalida;
        this.estados = estados;
        indices = new HashMap<>();
        conexo = false;
    }

    public void reiniciarVisitado(ArrayList<Estado> arrayList){
        for (Estado s : arrayList) {
            s.setVisitado(false);
        }
    }

    //Inicializamos el HashMap con los indices de cada estado
    private void inicializarIndices(){
        for (int i = 0; i < estados.size(); i++) {
            indices.put(estados.get(i), i);
        }
    }

    private ArrayList<Estado> estadosConexos(){
        //Recorremos el automata para encontrar los estados conexos
        //y los estados inaccesibles
        depthFirstSearch();
        ArrayList<Estado> estadosConexos = new ArrayList<Estado>();
        for(Estado e : estados){
            if(e.getVisitado() == true){
                estadosConexos.add(e);
            }
        }
        if(estadosConexos.size() == estados.size()){
            conexo = true;
        }
        return estadosConexos;
    }

    private void depthFirstSearch(){
        reiniciarVisitado(estados);

        Stack<Estado> stack = new Stack<Estado>();
        boolean[] visitados = new boolean[estados.size()];
        Estado start = estados.get(0);
        stack.push(start);

        while (!stack.isEmpty()){
            Estado actual = stack.pop();
            int indice = indices.get(actual);
            visitados[indice] = true;
            estados.get(indice).setVisitado(true);

            for (Estado s : actual.getEstadosSiguientes()) {
                if (!visitados[indices.get(s)]) {
                    stack.push(s);
                }
            }
        }
    }

    private void reiniciarEstadosParticiones(){
        for (ArrayList<Estado> arrayList : particiones) {
            for (Estado s : arrayList){
                s.setVisitado(false);
            }
        }
    }

    private ArrayList<ArrayList<Estado>> primeraParticion(){
        ArrayList<Estado> estadosConexos = estadosConexos();

        reiniciarVisitado(estadosConexos);

        particiones = new ArrayList<>();
        ArrayList<Estado> primeraParticion;
        String salida1;
        String salida2;
        int contadorBloques = 0;

        for (int i = 0; i < estadosConexos.size()-1; i++) {

            if (estadosConexos.get(i).getVisitado() == false) {

                primeraParticion = new ArrayList<>();
                primeraParticion.add(estadosConexos.get(i));

                estadosConexos.get(i).setVisitado(true);
                //estadosConexos.get(i).actualizarParticionAnterior();
                estadosConexos.get(i).setParticion(contadorBloques);

                for (int j = i+1; j < estadosConexos.size(); j++) {
                    if (estadosConexos.get(j).getVisitado() == false) {

                        salida1 = String.valueOf(estadosConexos.get(i).getSalidas());
                        salida2 = String.valueOf(estadosConexos.get(j).getSalidas());

                        if (salida1.equals(salida2)) {
                            estadosConexos.get(j).setVisitado(true);
                            //estadosConexos.get(j).actualizarParticionAnterior();
                            estadosConexos.get(j).setParticion(contadorBloques);
                            primeraParticion.add(estadosConexos.get(j));
                        }

                    }
                }
                particiones.add(primeraParticion);
                contadorBloques++;
            }
        }
        obtenerMensajeParticiones();
        return particiones;
    }

    private ArrayList<ArrayList<Estado>> particionesRestantes() {
        ArrayList<Estado> estadosConexos = estadosConexos();
        ArrayList<ArrayList<Estado>> temp;
        ArrayList<Estado> particionNueva;

        int contadorParticiones = particiones.size()-1;
        boolean detenerParticionamiento = false;

        while (detenerParticionamiento != true) {

            reiniciarEstadosParticiones();

            //Recorremos las particiones
            temp = particiones;
            for (int i = 0; i < particiones.size(); i++) {
                ArrayList<Estado> estadosParticion = particiones.get(i);
                particionNueva = new ArrayList<Estado>();
                contadorParticiones++;

                for (int j = 0; j < estadosParticion.size() - 1; j++) {
                    Estado actual = estadosParticion.get(j);

                    for (int k = j+1; k < estadosParticion.size() && actual.getVisitado() == false; k++) {
                        Estado siguiente = estadosParticion.get(j);
                        if(mismaParticion(actual, siguiente) == true){
                            actual.setVisitado(true);
                        } else{
                            particiones.get(i).remove(siguiente);
                            particionNueva.add(siguiente);
                            siguiente.setParticion(contadorParticiones);
                            siguiente.setVisitado(true);
                        }
                    }
                }
                if(particionNueva.size() != 0){
                    particiones.add(particionNueva);
                }
            }
            obtenerMensajeParticiones();
            //Si particiones al terminar el for es igual que al inicio, terminamos de hacer el particionamiento
            if(particiones.equals(temp)){
                detenerParticionamiento = true;
            }
        }
        return particiones;
    }

    private boolean mismaParticion(Estado estado1, Estado estado2){
        boolean detener = false;
        boolean pertenecen = true;
        for(int i = 0; i < estado1.getEstadosSiguientes().size() && detener == false; i++){
            if(estado1.getEstadosSiguientes().get(i).getParticion() != estado2.getEstadosSiguientes().get(i).getParticion()){
                detener = true;
                pertenecen = false;
            }
        }
        return pertenecen;
    }

    public void obtenerMensajeParticiones(){
        String mensaje = "";
        for(int i = 0; i < particiones.size(); i++){
            for(int j = 0; j < particiones.get(i).size(); j++){
                if(j == 0) {
                    mensaje += "{" + particiones.get(i).get(j).getNombre();
                } else if(j != 0 && j != particiones.get(i).size()-1){
                    mensaje += ", " + particiones.get(i).get(j).getNombre();
                } else{
                    mensaje += particiones.get(i).get(j).getNombre() + "}";
                }
            }
        }
    }

    public ArrayList<Estado> obtenerAutomataReducido () {
        ArrayList<Estado> nuevosEstados = new ArrayList<>();
        int indiceNombre = 0;
        for (int i = 0; i < particiones.size(); i++) {
            String nombre = "Q" + indiceNombre;
            indiceNombre++;
            Estado e = new Estado(nombre, particiones.get(i).get(0).getSalidas());
            nuevosEstados.add(e);
        }
        for (int i = 0; i < nuevosEstados.size(); i++) {
            for (int j = 0; j < lenguajeEntrada.length; j++) {
                int n = particiones.get(i).get(0).getEstadosSiguientes().get(j).getParticion();
                nuevosEstados.get(i).getEstadosSiguientes().add(nuevosEstados.get(n));
            }
        }

        return nuevosEstados;
    }


    public String getTipoAutomata() {
        return tipoAutomata;
    }

    public void setTipoAutomata(String tipoAutomata) {
        this.tipoAutomata = tipoAutomata;
    }

    public ArrayList<Estado> getEstados() {
        return estados;
    }

    public void setEstados(ArrayList<Estado> estados) {
        this.estados = estados;
    }

    public char[] getLenguajeEntrada() {
        return lenguajeEntrada;
    }

    public void setLenguajeEntrada(char[] lenguajeEntrada) {
        this.lenguajeEntrada = lenguajeEntrada;
    }

    public char[] getLenguajeSalida() {
        return lenguajeSalida;
    }

    public void setLenguajeSalida(char[] lenguajeSalida) {
        this.lenguajeSalida = lenguajeSalida;
    }

    public HashMap<Estado, Integer> getIndices() {
        return indices;
    }

    public void setIndices(HashMap<Estado, Integer> indices) {
        this.indices = indices;
    }

    public boolean isConexo() {
        return conexo;
    }

    public void setConexo(boolean conexo) {
        this.conexo = conexo;
    }

    public ArrayList<ArrayList<Estado>> getParticiones() {
        return particiones;
    }

    public void setParticiones(ArrayList<ArrayList<Estado>> particiones) {
        this.particiones = particiones;
    }

    public ArrayList<String> getMensajeParticiones() {
        return mensajeParticiones;
    }

    public void setMensajeParticiones(ArrayList<String> mensajeParticiones) {
        this.mensajeParticiones = mensajeParticiones;
    }
}
