/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 3/11/2023
* Ultima alteracao.: 5/11/2023
* Nome.............: ControleErrorScreen.java
* Funcao...........: Classe responsavel pelo controle da tela de erro
*************************************************************** */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ControleErrorScreen {

  @FXML
  private Button btnOkF;
  @FXML
  private ImageView imgErrorDetected;

  //ao clicar no botao a funcao eh disparada
  @FXML
  void onClickBtnOkF(ActionEvent event) {
    Stage stage = (Stage) btnOkF.getScene().getWindow(); //Criando Janela
    stage.close(); //Fechando o Stage
  }
}
