package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
        ArrayList<String> particiones = automata.getMensajeParticiones();
        pInicialLabel.setText(pInicialLabel.getText() + particiones.get(0));
        pDosLabel.setText(pDosLabel.getText() + particiones.get(1));
        pTresLabel.setText(pTresLabel.getText() + particiones.get(2));
        pFinalLabel.setText(pFinalLabel.getText() + particiones.get(particiones.size()-1));
    }

    @FXML
    void definirAutomata(ActionEvent event) {

        if(alfabetoEntradaTF.getText().equals("") || alfabetoSalidaTF.getText().equals("") || estadosTF.getText().equals("") || !mealyRB.isSelected() || !mooreRB.isSelected()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane optionPane = new JOptionPane("¡No ingreso alguno de los datos necesarios!", JOptionPane.WARNING_MESSAGE);
            JDialog dialog = optionPane.createDialog("Warning!");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
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
                matrizAutomata = new String[nombreEstados.length+1][alfabetoS.length+1];
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
        if(mooreRB.isSelected()){
            tablaAutomataGrid.getChildren().
            for(int i = 0; i < automata.getLenguajeEntrada().length; i++){
                for(int j = 0; j < automata.getLenguajeEntrada().length; j++){

                }
            }
        } else{

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
        columnas.setPrefWidth(80);
        tablaAutomataGrid.add(columnas, 0, 0);

        //El lenguaje de entrada serían las columnas de la matriz
        for(int i = 0; i < automata.getLenguajeEntrada().length; i ++){
            columnas = new TextField(automata.getLenguajeEntrada()[i]+"");
            columnas.setDisable(true);
            columnas.setPrefWidth(80);
            //La fila siempre es la 0, la columna es i+1 (porque la columna 0 ya existe)
            tablaAutomataGrid.add(columnas, i+1, 0);
        }
        for(int j = 0; j < estadosAutomataReducido.size(); j++){
            columnas = new TextField(estadosAutomataReducido.get(j).getNombre()+"");
            columnas.setDisable(true);
            columnas.setPrefWidth(80);
            //La columna siempre es la 0, la fila es j+1 (porque la fila 0 ya existe)
            tablaAutomataGrid.add(columnas, 0, j+1);
        }
        if(automata.getTipoAutomata().equals("Moore")){
            columnas = new TextField("Salida");
            columnas.setDisable(true);
            columnas.setPrefWidth(80);
            tablaAutomataGrid.add(columnas, automata.getLenguajeEntrada().length, 0);


            for(int i = 1; i < estadosAutomataReducido.size(); i++){
                for(int j = 1; j < automata.getLenguajeEntrada().length+1; j++){
                    String nombreSiguienteEstado = estadosAutomataReducido.get(i).getEstadosSiguientes().get(j).getNombre();
                    columnas = new TextField(nombreSiguienteEstado);
                    columnas.setDisable(true);
                    columnas.setPrefWidth(80);
                    tablaAutomataGrid.add(columnas, i, j);
                }
                columnas = new TextField(estadosAutomataReducido.get(i).getSalidas()[0]+"");
                columnas.setDisable(true);
                columnas.setPrefWidth(80);
                tablaAutomataGrid.add(columnas, automata.getLenguajeEntrada().length+1, i);
            }
        }else {
            for (int i = 1; i < estadosAutomataReducido.size(); i++) {
                for (int j = 1; j < automata.getLenguajeEntrada().length; j++) {

                    String nombreEstadoSiguiente = estadosAutomataReducido.get(i).getEstadosSiguientes().get(j).getNombre();
                    char salidaEstadoSiguiente = estadosAutomataReducido.get(i).getSalidas()[j];
                    TextField tf = new TextField(nombreEstadoSiguiente + ", " + salidaEstadoSiguiente);

                    tablaAutomataGrid.add(tf, j, i);
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
    }

    void crearTablaGrid(char[] nombreEstados, char[] alfabetoE){
        particionesPane.getChildren().remove(tablaAutomataGrid);
        ArrayList<Estado> automataReducido = automata.obtenerAutomataReducido();
        tablaAutomataGrid.getChildren().clear();
        tablaAutomataGrid.setAlignment(Pos.CENTER);
        TextField columna0 = new TextField("");
        columna0.setDisable(true);
        columna0.setPrefWidth(80);
        tablaAutomataGrid.add(columna0, 0, 0);

        //El lenguaje de entrada serían las columnas de la matriz
        for(int i = 0; i < alfabetoE.length; i ++){
            TextField columnas = new TextField(alfabetoE[i]+"");
            columnas.setDisable(true);
            columnas.setPrefWidth(80);
            //La fila siempre es la 0, la columna es i+1 (porque la columna 0 ya existe)
            tablaAutomataGrid.add(columnas, i+1, 0);
        }

        for(int j = 0; j < nombreEstados.length; j++){
            TextField columnas = new TextField(nombreEstados[j]+"");
            columnas.setDisable(true);
            columnas.setPrefWidth(80);
            //La columna siempre es la 0, la fila es j+1 (porque la fila 0 ya existe)
            tablaAutomataGrid.add(columnas, 0, j+1);
        }

        if(mooreRB.isSelected()){
            TextField columnas = new TextField("Salida");
            columnas.setDisable(true);
            columnas.setPrefWidth(80);
            tablaAutomataGrid.add(columnas, automata.getLenguajeEntrada().length, 0);


            for(int i = 1; i < nombreEstados.length; i++){
                for(int j = 1; j < alfabetoE.length+1; i++){
                    columnas = new TextField("");
                    columnas.setDisable(false);
                    columnas.setPrefWidth(80);
                    tablaAutomataGrid.add(columnas, i, j);
                }
            }
        } else{
            for(int i = 1; i < nombreEstados.length; i++){
                for(int j = 1; j < alfabetoE.length+1; i++){
                    TextField columnas = new TextField("");
                    columnas.setDisable(false);
                    columnas.setPrefWidth(80);
                    tablaAutomataGrid.add(columnas, i, j);
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
