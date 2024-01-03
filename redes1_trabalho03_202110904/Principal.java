/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 3/11/2023
* Ultima alteracao.: 5/11/2023
* Nome.............: Principal.java
* Funcao...........: Inicia a exibicao da GUI.
*************************************************************** */

//Importacoes
import controller.ControleDados;
import controller.ControleErrorScreen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Principal extends Application {
  //instancia da classe de controle
  ControleDados cD = new ControleDados();
  ControleErrorScreen cES = new ControleErrorScreen();
  private static Scene startTelaCamadaFisica;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    try {
      //definicao de icone e localizando no diretorio
      primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/assets/icon.png")));
      //setando o nome da aplicacao
      primaryStage.setTitle("Camada de Enlade de Dados");
      //impedindo a mudanca na resolucao das janelas, travando redimencionamento
      primaryStage.resizableProperty().setValue(false);

      //carregando os arquivos da tela e gerando novas cenas
      Parent telaCamadaFisica = FXMLLoader.load(getClass().getResource("/view/CamadaFisica.fxml"));
      startTelaCamadaFisica = new Scene(telaCamadaFisica);

      //atribuindo o aquivo xml da cena inicial e exibindo a cena na janela
      primaryStage.setScene(startTelaCamadaFisica);
      primaryStage.show();

    } catch (Exception e) {
      e.printStackTrace();// mensagem padrão do java em caso de erro
    }
  }
}