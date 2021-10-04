package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
/**
 * Clase Automata encargada de generar el automata Mealy o Moore con todos sus atributos.
 * @author Bryan
 * @author Danna
 *
 */
public class Automata {

    public final static String MEALY = "Mealy";
    public final static String MOORE = "Moore";

    private String tipoAutomata;
    private ArrayList<Estado> estados;
    private char[] lenguajeEntrada;
    private char[] lenguajeSalida;
    private HashMap<Estado, Integer> indices;
    private ArrayList<ArrayList<Estado>> particiones;
    private ArrayList<String> mensajeParticiones;
    private ArrayList<Estado> estadosConexos;
    private boolean conexo;


   
    /**
     * Se construye el automata con las variables necesarias como lo es el tipo, lenguaje de entrada, lenguaje de salida y los estados
     * @param tipoAutomata hace referencia al tipo de automata a construir.
     * @param lenguajeEntrada hace referencia al lenguaje de entrada para el automata seleccionado.
     * @param lenguajeSalida hace referencia al lenguaje de salida para el automata seleccionado.
     * @param estados hace referencia a los estados del automata
     */
    public Automata(String tipoAutomata, char[] lenguajeEntrada, char[] lenguajeSalida, ArrayList<Estado> estados) {
        this.tipoAutomata = tipoAutomata;
        this.lenguajeEntrada = lenguajeEntrada;
        this.lenguajeSalida = lenguajeSalida;
        this.estados = estados;
        indices = new HashMap<>();
        conexo = false;
        particiones = new ArrayList<>();
        mensajeParticiones = new ArrayList<>();
        inicializarIndices();
    }

    /**
     *  Se reinicia el atributo visitado de todos los estados en el ArrayList<Estado> pasado por parametro
     * @param arrayList
     */
    public void reiniciarVisitado(ArrayList<Estado> arrayList){
        for (Estado s : arrayList) {
            s.setVisitado(false);
        }
    }


    /**
     *  Inicializamos el HashMap con los indices de cada estado, este HashMap se utiliza principalmente para hacer el recorrido dfs en el grafo.
     */
    //Inicializamos el HashMap con los indices de cada estado
    private void inicializarIndices(){
        for (int i = 0; i < estados.size(); i++) {
            indices.put(estados.get(i), i);
        }
    }


    /**
     * Por medio del recorrido dfs se marcan como visitado los estados a los que se pueda acceder, los que no queden marcados como visitado 
     * son estados inaccesibles que seran eliminados.
     */
    public void estadosConexos(){
        //Recorremos el automata para encontrar los estados conexos
        //y los estados inaccesibles
        if(tipoAutomata.equals("Mealy")){
            depthFirstSearchMealy();
        } else{
            depthFirstSearchMoore();
        }

        estadosConexos = new ArrayList<Estado>();
        for(Estado e : estados){
            if(e.getVisitado() == true){
                estadosConexos.add(e);
            }
        }
        if(estadosConexos.size() == estados.size()){
            conexo = true;
        }
    }

    /**
     * Recorrido depthFirstSearch para las maquinas de Moore
     */
    private void depthFirstSearchMoore(){
        reiniciarVisitado(estados);

        Stack<Estado> stack = new Stack<>();
        boolean[] visitados = new boolean[estados.size()];
        Estado start = estados.get(0);
        stack.push(start);

        while (!stack.isEmpty()){
            Estado actual = stack.pop();
            int indice = indices.get(actual);
            if(!visitados[indice]){
                visitados[indice] = true;
                estados.get(indice).setVisitado(true);
            }

            for (int i = 0; i < actual.getEstadosSiguientes().size()-1; i++) {
                Estado e = actual.getEstadosSiguientes().get(i);
                if (!visitados[indices.get(e)]) {
                    stack.push(e);
                }
            }
        }
    }

    /**
     *Recorrido depthFirstSearch para las maquinas de Mealy
     */
    private void depthFirstSearchMealy(){
        reiniciarVisitado(estados);

        Stack<Estado> stack = new Stack<>();
        boolean[] visitados = new boolean[estados.size()];
        Estado start = estados.get(0);
        stack.push(start);

        while (!stack.isEmpty()){
            Estado actual = stack.pop();
            int indice = indices.get(actual);
            if(!visitados[indice]){
                visitados[indice] = true;
                estados.get(indice).setVisitado(true);
            }

            for (int i = 0; i < actual.getEstadosSiguientes().size(); i++) {
                Estado e = actual.getEstadosSiguientes().get(i);
                if (!visitados[indices.get(e)]) {
                    stack.push(e);
                }
            }
        }
    }

    /**
     *Reinicia la variable visitado los estados que se encuentran dentro de las particiones
     */
    private void reiniciarEstadosParticiones(){
        for (ArrayList<Estado> arrayList : particiones) {
            for (Estado s : arrayList){
                s.setVisitado(false);
            }
        }
    }

    /**
     *Por medio de este metodo obtenemos la primera particion, donde se obtiene por medio de la verificacion de las salidas de los estados
     *los estados que tengan las mismas salidas para el alfabeto de entrada se ubicaran en una misma particion
     */
    private void primeraParticion(){
        reiniciarVisitado(estadosConexos);

        String salida1;
        String salida2;
        int contadorBloques = 0;
        particiones = new ArrayList<>();
        ArrayList<Estado> primeraParticion;


        for (int i = 0; i < estadosConexos.size(); i++) {

            if (estadosConexos.get(i).getVisitado() == false) {

                primeraParticion = new ArrayList<>();
                primeraParticion.add(estadosConexos.get(i));
                estadosConexos.get(i).setParticion(contadorBloques);
                estadosConexos.get(i).setVisitado(true);

                for (int j = i+1; j < estadosConexos.size(); j++) {
                    if (estadosConexos.get(j).getVisitado() == false) {

                        if(tipoAutomata.equals("Mealy")) {
                            salida1 = String.valueOf(estadosConexos.get(i).getSalidas()[0] + estadosConexos.get(i).getSalidas()[1] + "");
                            salida2 = String.valueOf(estadosConexos.get(j).getSalidas()[0] + estadosConexos.get(i).getSalidas()[1] + "");
                        }else{
                            salida1 = String.valueOf(estadosConexos.get(i).getSalidas()[0] + "");
                            salida2 = String.valueOf(estadosConexos.get(j).getSalidas()[0] + "");
                        }

                        if (salida1.equals(salida2)) {
                            estadosConexos.get(j).setParticion(contadorBloques);
                            estadosConexos.get(j).setVisitado(true);
                            primeraParticion.add(estadosConexos.get(j));
                        }

                    }
                }
                particiones.add(primeraParticion);
                contadorBloques++;
            }
        }
        obtenerMensajeParticiones();
    }

    /**
     *se encarga de realizar las n particiones que se puedan realizar al algoritmo verificando que los estadios
     *siguientes pertenezcan a la misma particion teniendo en cuenta la particion 1
     */
    private void particionesRestantes() {
        ArrayList<ArrayList<Estado>> temp;
        ArrayList<Estado> particionNueva;

        int contadorParticiones = particiones.size()-1;
        boolean detenerParticionamiento = false;

        while (detenerParticionamiento != true) {

            reiniciarEstadosParticiones();

            //Recorremos las particiones
            int sizeParticiones = particiones.size();
            for (int i = 0; i < particiones.size(); i++) {
                ArrayList<Estado> estadosParticion = particiones.get(i);
                particionNueva = new ArrayList<Estado>();

                for (int j = 0; j < estadosParticion.size() - 1; j++) {
                    Estado actual = estadosParticion.get(j);

                    for (int k = j+1; k < estadosParticion.size() && actual.getVisitado() == false; k++) {
                        Estado siguiente = estadosParticion.get(k);
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
                    contadorParticiones++;
                }
            }
            obtenerMensajeParticiones();
            //Si particiones al terminar el for es igual que al inicio, terminamos de hacer el particionamiento
            if(particiones.size() == sizeParticiones){
                detenerParticionamiento = true;
            }
        }
    }

    /**
     *  Verifica si los estados siguientes de dos estados pertenecen a la misma particion
     * @param estado1
     * @param estado2
     * @return un boolean para determinar si esta o no en la misma particion
     */
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

    /**
     *Obtenemos las particiones en forma de mensaje para que pueda ser posteriormente mostrado en la interfaz al usuario.
     */
    public void obtenerMensajeParticiones(){
        String mensaje = "";
        for(int i = 0; i < particiones.size(); i++){
            for(int j = 0; j < particiones.get(i).size(); j++){
                if(j == 0) {
                    if(particiones.get(i).size() == 1) {
                        if(i != particiones.size()-1) {
                            mensaje += "{" + particiones.get(i).get(j).getNombre() + "}, ";
                        }else{
                            mensaje += "{" + particiones.get(i).get(j).getNombre() + "}";
                        }
                    } else{
                        mensaje += "{" + particiones.get(i).get(j).getNombre();
                    }
                } else if(j != 0 && j != particiones.get(i).size()-1){
                    mensaje += ", " + particiones.get(i).get(j).getNombre();
                } else{
                    mensaje += ", " + particiones.get(i).get(j).getNombre() + "}";
                }
            }
        }
        mensajeParticiones.add(mensaje);
    }

    /**
     *Teniendo todas las particiones, se obtiene el automata reducido. 
     */
    public ArrayList<Estado> obtenerAutomataReducido() {
        primeraParticion();
        ArrayList<Estado> nuevosEstados = new ArrayList<>();
        particionesRestantes();
        int indiceNombre = 0;
        for (int i = 0; i < particiones.size(); i++) {
            String nombre = "q" + indiceNombre;
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
