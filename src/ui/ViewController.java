package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Automata;
import model.Estado;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


public class ViewController {

    @FXML
    private Pane particionesPane;

    @FXML
    private ToggleGroup automatasGroup;

    @FXML
    private Button definirBtn;

    @FXML
    private Button definirEstadosBtn;

    @FXML
    private TextField alfabetoEntradaTF;

    @FXML
    private TextField alfabetoSalidaTF;

    @FXML
    private TextField estadosTF;

    @FXML
    private RadioButton mealyRB;

    @FXML
    private RadioButton mooreRB;

    @FXML
    private Label pDosLabel;

    @FXML
    private Label pFinalLabel;

    @FXML
    private Label pInicialLabel;

    @FXML
    private Label pTresLabel;

    @FXML
    private Button reducirBtn;

    @FXML
    private Button reiniciarBtn;

    @FXML
    private GridPane tablaAutomataGrid;

    private String[][] matrizAutomata;
    private Automata automata;

    @FXML
    void initialize(){

    }

    void showPartitions(){
        pInicialLabel.setText("Pinicial = ");
        pDosLabel.setVisible(true);
        pTresLabel.setVisible(true);
        pFinalLabel.setVisible(true);
        ArrayList<String> particiones = automata.getMensajeParticiones();
        if(particiones.size() == 2){
            pInicialLabel.setText(pInicialLabel.getText() + particiones.get(0));
            pDosLabel.setText(pFinalLabel.getText() + particiones.get(1));
            pTresLabel.setVisible(false);
            pFinalLabel.setVisible(false);
        } else if (particiones.size() == 3){
            pInicialLabel.setText(pInicialLabel.getText() + particiones.get(0));
            pDosLabel.setText(pDosLabel.getText() + particiones.get(1));
            pTresLabel.setText(pFinalLabel.getText() + particiones.get(2));
            pFinalLabel.setVisible(false);
        } else if(particiones.size() == 4){
            pInicialLabel.setText(pInicialLabel.getText() + particiones.get(0));
            pDosLabel.setText(pDosLabel.getText() + particiones.get(1));
            pTresLabel.setText(pTresLabel.getText() + particiones.get(2));
            pFinalLabel.setText(pFinalLabel.getText() + particiones.get(3));
        } else {
            pInicialLabel.setText(pInicialLabel.getText() + particiones.get(0));
            pDosLabel.setText(pDosLabel.getText() + particiones.get(1));
            pTresLabel.setText(pTresLabel.getText() + particiones.get(particiones.size() - 2));
            pFinalLabel.setText(pFinalLabel.getText() + particiones.get(particiones.size() - 1));
        }
    }

    @FXML
    void definirAutomata(ActionEvent event) {
        System.out.println(alfabetoEntradaTF.getText());
        System.out.println(alfabetoSalidaTF.getText());
        System.out.println(estadosTF.getText());
        System.out.println(mooreRB.isSelected());
        System.out.println(mealyRB.isSelected());
        if(alfabetoEntradaTF.getText().equals("") || alfabetoSalidaTF.getText().equals("") || estadosTF.getText().equals("") || (mealyRB.isSelected()==false && mooreRB.isSelected()==false)) {
            Toolkit.getDefaultToolkit().beep();
            Alert optionPane = new Alert(Alert.AlertType.WARNING, "¡No ingreso alguno de los datos necesarios!");
            optionPane.show();
        }
        else {
            mooreRB.setDisable(true);
            mealyRB.setDisable(true);
            estadosTF.setDisable(true);
            alfabetoSalidaTF.setDisable(true);
            alfabetoEntradaTF.setDisable(true);
            reiniciarBtn.setDisable(false);
            definirBtn.setDisable(true);
            reducirBtn.setDisable(false);
            reducirBtn.setVisible(false);
            pInicialLabel.setVisible(false);
            pFinalLabel.setVisible(false);
            pDosLabel.setVisible(false);
            pTresLabel.setVisible(false);
            definirEstadosBtn.setDisable(false);
            String alfabetoEntrada = alfabetoEntradaTF.getText();
            alfabetoEntrada.replace(" ", "");
            String[] temp = alfabetoEntrada.split(",");
            char[] alfabetoE = convertirStringEnChar(temp);

            String alfabetoSalida = alfabetoSalidaTF.getText();
            alfabetoSalida.replace(" ", "");
            temp = alfabetoSalida.split(",");
            char[] alfabetoS = convertirStringEnChar(temp);

            String estados = estadosTF.getText();
            estados.replace(" ", "");
            temp = estados.split(",");
            char[] nombreEstados = convertirStringEnChar(temp);

            if(mealyRB.isSelected()){
                //Filas, columnas. Cuando es mealy agregamos una fila porque los estados se piden así
                //- /n estado1 /n estado2 / estadon
                //agregamos una column nada más porque Mealy no tiene columna de salida
                matrizAutomata = new String[nombreEstados.length+1][alfabetoE.length+1];
            } else if(mooreRB.isSelected()){
                //Filas, columnas. Cuando es moore agregamos una fila porque los estados se piden así
                //- /n estado1 /n estado2 / estadon
                //agregamos dos columnas porque las columnas estan así
                //- | entrada1, entradan, salida
                matrizAutomata = new String[nombreEstados.length+1][alfabetoS.length+2];
            }
            crearTablaGrid(nombreEstados, alfabetoE);
        }
    }

    @FXML
    void definirEstados(ActionEvent event) {
      for(Node nodo : tablaAutomataGrid.getChildren()){
          TextField textField = (TextField) nodo;
          if(nodo != null) {
              matrizAutomata[GridPane.getRowIndex(nodo)][GridPane.getColumnIndex(nodo)] = textField.getText();
          }
      }
        String alfabetoEntrada = alfabetoEntradaTF.getText();
        alfabetoEntrada.replace(" ", "");
        String[] temp = alfabetoEntrada.split(",");
        char[] alfabetoE = convertirStringEnChar(temp);

        String alfabetoSalida = alfabetoSalidaTF.getText();
        alfabetoSalida.replace(" ", "");
        temp = alfabetoSalida.split(",");
        char[] alfabetoS = convertirStringEnChar(temp);


        ArrayList<Estado> estadosAutomata = new ArrayList<>();
        HashMap<String, Estado> mapa = new HashMap<>();
        String tipoAutomata;
        //****
        //MEALY
        //*****
        if(mealyRB.isSelected() == true) {
            tipoAutomata = automata.MEALY;
            //Filas
            for (int i = 1; i < matrizAutomata.length; i++) {
                //Columnas
                String[] salidasEstado = new String[alfabetoE.length];
                for (int j = 1; j < matrizAutomata[i].length; j++) {
                    String salida = matrizAutomata[i][j];
                    salida.replace(" ", "");
                    String[] salidaTemp = salida.split(",");
                    //En la 0 esta el estado siguiente, en 1 esta la salida (b,1)
                    salidasEstado[j-1] = salidaTemp[1];
                }

                Estado estadoAutomata = new Estado(matrizAutomata[i][0],  convertirStringEnChar(salidasEstado));
                mapa.put(matrizAutomata[i][0], estadoAutomata);
                estadosAutomata.add(estadoAutomata);
            }
            for(int i = 1; i < matrizAutomata.length; i++) {
                for (int j = 1; j < matrizAutomata[i].length; j++) {
                    String datosEstadosSiguiente = matrizAutomata[i][j];
                    datosEstadosSiguiente.replace(" ", "");
                    String[] estadoSiguienteTemporal = datosEstadosSiguiente.split(",");
                    Estado estadoSiguiente = mapa.get(estadoSiguienteTemporal[0]);
                    estadosAutomata.get(i-1).getEstadosSiguientes().add(estadoSiguiente);
                }
            }
            String prueba1 = "";
            automata = new Automata(tipoAutomata, alfabetoE, alfabetoS, estadosAutomata);

        } else if(mooreRB.isSelected() == true){
            tipoAutomata = automata.MOORE;
            //Filas

            for (int i = 1; i < matrizAutomata.length; i++) {
                //Columnas
                String sal = matrizAutomata[i][alfabetoE.length+1];
                sal.replace(" ", "");
                char[] salida = new char[1];
                salida[0] = sal.charAt(0);

                String nombre = matrizAutomata[i][0];

                String nombreEstado = matrizAutomata[i][0];
                Estado e = new Estado(nombreEstado, salida);
                mapa.put(nombreEstado, e);
                estadosAutomata.add(e);
            }


            for (int i = 1; i < matrizAutomata.length; i++) {
                //Columnas
                for (int j = 1; j < matrizAutomata[i].length; j++) {
                    String nombreSiguiente = matrizAutomata[i][j];
                    Estado es = mapa.get(nombreSiguiente);
                    estadosAutomata.get(i-1).getEstadosSiguientes().add(es);

                }
            }
            automata = new Automata(tipoAutomata, alfabetoE, alfabetoS, estadosAutomata);

        }

        definirEstadosBtn.setVisible(false);
        reducirBtn.setVisible(true);
        reducirBtn.setDisable(false);
        pInicialLabel.setVisible(true);
        automata.estadosConexos();
        if(automata.isConexo() == false) {
            pInicialLabel.setText("El automata no es conexo, se eliminarán los estados inaccesibles");
        } else{
            pInicialLabel.setText("El automata es conexo, todos los estados son accesibles");
        }

    desactivarTextFields();

    }

    void desactivarTextFields(){
        for(Node node : tablaAutomataGrid.getChildren()){
            node.setDisable(true);
        }
    }

    @FXML
    void reducirAutomata(ActionEvent event) {
        ArrayList<Estado> estadosAutomataReducido = automata.obtenerAutomataReducido();
        particionesPane.getChildren().remove(tablaAutomataGrid);
        tablaAutomataGrid.getChildren().clear();
        tablaAutomataGrid.setAlignment(Pos.CENTER);
        TextField columnas = new TextField("");
        columnas.setDisable(true);
        tablaAutomataGrid.add(columnas, 0, 0);

        //El lenguaje de entrada serían las columnas de la matriz
        for(int i = 0; i < automata.getLenguajeEntrada().length; i ++){
            columnas = new TextField(automata.getLenguajeEntrada()[i]+"");
            columnas.setDisable(true);
            //La fila siempre es la 0, la columna es i+1 (porque la columna 0 ya existe)
            tablaAutomataGrid.add(columnas, i+1, 0);
        }
        for(int j = 0; j < estadosAutomataReducido.size(); j++){
            columnas = new TextField(estadosAutomataReducido.get(j).getNombre()+"");
            columnas.setDisable(true);
            //La columna siempre es la 0, la fila es j+1 (porque la fila 0 ya existe)
            tablaAutomataGrid.add(columnas, 0, j+1);
        }
        if(automata.getTipoAutomata().equals("Moore")){
            columnas = new TextField("Salida");
            columnas.setDisable(true);
            tablaAutomataGrid.add(columnas, automata.getLenguajeEntrada().length+1, 0);


            for(int i = 1; i < estadosAutomataReducido.size()+1; i++){
                for(int j = 1; j < automata.getLenguajeEntrada().length+1; j++){
                    String nombreSiguienteEstado = estadosAutomataReducido.get(i-1).getEstadosSiguientes().get(j-1).getNombre();
                    columnas = new TextField(nombreSiguienteEstado);
                    columnas.setDisable(true);
                    tablaAutomataGrid.add(columnas, j, i);
                }
                columnas = new TextField(estadosAutomataReducido.get(i-1).getSalidas()[0]+"");
                columnas.setDisable(true);
                tablaAutomataGrid.add(columnas, automata.getLenguajeEntrada().length+1, i);
            }
        }else {
            for (int i = 1; i < estadosAutomataReducido.size()+1; i++) {
                for (int j = 1; j < automata.getLenguajeEntrada().length+1; j++) {

                    String nombreEstadoSiguiente = estadosAutomataReducido.get(i-1).getEstadosSiguientes().get(j-1).getNombre();
                    char salidaEstadoSiguiente = estadosAutomataReducido.get(i-1).getSalidas()[j-1];
                    columnas = new TextField(nombreEstadoSiguiente + ", " + salidaEstadoSiguiente);
                    columnas.setDisable(true);
                    tablaAutomataGrid.add(columnas, j, i);
                }
            }
        }
        particionesPane.getChildren().add(tablaAutomataGrid);

        showPartitions();
    }

    @FXML
    void reiniciar(ActionEvent event) {
        mooreRB.setDisable(false);
        mealyRB.setDisable(false);
        estadosTF.setDisable(false);
        alfabetoSalidaTF.setDisable(false);
        alfabetoEntradaTF.setDisable(false);
        reiniciarBtn.setDisable(true);
        definirBtn.setDisable(false);
        reducirBtn.setDisable(true);
        mooreRB.setSelected(false);
        mealyRB.setSelected(false);
        estadosTF.setText("");
        alfabetoSalidaTF.setText("");
        alfabetoEntradaTF.setText("");
        particionesPane.getChildren().remove(tablaAutomataGrid);
        pInicialLabel.setText("Pinicial = ");
        pDosLabel.setText("P2 = ");
        pTresLabel.setText("P3 = ");
        pFinalLabel.setText("Pfinal");
        automata = null;
        tablaAutomataGrid.getChildren().clear();
        definirEstadosBtn.setVisible(true);
    }

    void crearTablaGrid(char[] nombreEstados, char[] alfabetoE){
        particionesPane.getChildren().remove(tablaAutomataGrid);
        //tablaAutomataGrid.getChildren().clear();
        TextField columna0 = new TextField("");
        columna0.setDisable(true);
        tablaAutomataGrid.add(columna0, 0, 0);

        //El lenguaje de entrada serían las columnas de la matriz
        for(int i = 1; i < alfabetoE.length+1; i ++){
            TextField columnas = new TextField(alfabetoE[i-1] + "");
            columnas.setDisable(true);
            //La fila siempre es la 0, la columna es i+1 (porque la columna 0 ya existe)
            tablaAutomataGrid.add(columnas, i, 0);
        }

        for(int j = 1; j < nombreEstados.length+1; j++){
            TextField columnas = new TextField(nombreEstados[j-1]+"");
            columnas.setDisable(true);
            //La columna siempre es la 0, la fila es j+1 (porque la fila 0 ya existe)
            tablaAutomataGrid.add(columnas, 0, j);
        }

        if(mooreRB.isSelected()){
            TextField columnas = new TextField("Salida");
            columnas.setDisable(true);
            tablaAutomataGrid.add(columnas, alfabetoE.length+1, 0);


            for(int i = 1; i < nombreEstados.length+1; i++){
                for(int j = 1; j < alfabetoE.length+2; j++){
                    columnas = new TextField("");
                    columnas.setDisable(false);
                    tablaAutomataGrid.add(columnas, j, i);
                }
            }
        } else{
            for(int i = 1; i < nombreEstados.length+1; i++){
                for(int j = 1; j < alfabetoE.length+1; j++){
                    TextField columnas = new TextField("");
                    columnas.setDisable(false);
                    tablaAutomataGrid.add(columnas, j, i);
                }
            }
        }
        particionesPane.getChildren().add(tablaAutomataGrid);
    }

    char[] convertirStringEnChar(String[] cadena) {
        char[] resultado = new char[cadena.length];
        for (int i = 0; i < cadena.length; i++) {
            resultado[i] = cadena[i].charAt(0);
        }
        return resultado;
    }
}
