/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 3/11/2023
* Ultima alteracao.: 5/11/2023
* Nome.............: ControleDados.java
* Funcao...........: Classe responsavel pelo controle de elementos da interface e referencias entras classes
*************************************************************** */
package controller;

import java.io.IOException;
//Importacoes
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.MeioTransmissao;
import model.DispositivoTransmissor.Transmissor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControleDados implements Initializable {//Classe ControleDados
  //Atributos do FXML
  @FXML
  private Slider sliderErrorGenerator;
  @FXML
  private Label lblError;
  @FXML
  private Label lblSpeed;
  @FXML
  private MenuItem menuTCode;
  @FXML
  private MenuItem menuTCont;
  @FXML
  private MenuItem menuTEnqd;
  @FXML
  private ImageView bckground;
  @FXML
  private MenuButton menuConfig;
  @FXML
  private Button btnEnviar;
  @FXML
  private Button btnIniciar;
  @FXML
  private ChoiceBox<String> cbxTipoCodificacao;
  @FXML
  private ChoiceBox<String> cbxTipoControleErro;
  @FXML
  private ChoiceBox<String> cbxTipoEnquadramento;
  @FXML
  private ImageView imgIcon;
  @FXML
  private ImageView imgTextoGrande;
  @FXML
  private ImageView imgTextoPequeno;
  @FXML
  private Label lblTextoPequeno;
  @FXML
  private Label lblTextoGrande;
  @FXML
  private ImageView imgBackground;
  @FXML
  private ImageView imgBit01Alto;
  @FXML
  private ImageView imgBit01Baixo;
  @FXML
  private ImageView imgBit01altoBaixo;
  @FXML
  private ImageView imgBit01baixoAlto;
  @FXML
  private ImageView imgBit02Alto;
  @FXML
  private ImageView imgBit02Baixo;
  @FXML
  private ImageView imgBit02altoBaixo;
  @FXML
  private ImageView imgBit02baixoAlto;
  @FXML
  private ImageView imgBit03Alto;
  @FXML
  private ImageView imgBit03Baixo;
  @FXML
  private ImageView imgBit03altoBaixo;
  @FXML
  private ImageView imgBit03baixoAlto;
  @FXML
  private ImageView imgBit04Alto;
  @FXML
  private ImageView imgBit04Baixo;
  @FXML
  private ImageView imgBit04altoBaixo;
  @FXML
  private ImageView imgBit04baixoAlto;
  @FXML
  private ImageView imgBit05Alto;
  @FXML
  private ImageView imgBit05Baixo;
  @FXML
  private ImageView imgBit05altoBaixo;
  @FXML
  private ImageView imgBit05baixoAlto;
  @FXML
  private ImageView imgBit06Alto;
  @FXML
  private ImageView imgBit06Baixo;
  @FXML
  private ImageView imgBit06altoBaixo;
  @FXML
  private ImageView imgBit06baixoAlto;
  @FXML
  private ImageView imgBit07Alto;
  @FXML
  private ImageView imgBit07Baixo;
  @FXML
  private ImageView imgBit07altoBaixo;
  @FXML
  private ImageView imgBit07baixoAlto;
  @FXML
  private ImageView imgBit08Alto;
  @FXML
  private ImageView imgBit08Baixo;
  @FXML
  private ImageView imgBit08altoBaixo;
  @FXML
  private ImageView imgBit08baixoAlto;
  @FXML
  private ImageView imgMouse;
  @FXML
  private TextArea txtATransmissor;
  @FXML
  private TextField txtInsert;
  @FXML
  private ImageView transicao1;
  @FXML
  private ImageView transicao2;
  @FXML
  private ImageView transicao3;
  @FXML
  private ImageView transicao4;
  @FXML
  private ImageView transicao5;
  @FXML
  private ImageView transicao6;
  @FXML
  private ImageView transicao7;
  @FXML
  private Slider sliderSignalSpeed;

  //Seletores do menu de configuracao
  private String encodeSelector;
  private String enquadSelector;
  private String errorControlSelector;

  //Array de ImageView, para facilitar na manipulacao
  private ImageView[] imageViewsAlto;
  private ImageView[] imageViewsBaixo;
  private ImageView[] imageViewsAltoBaixo;
  private ImageView[] imageViewsBaixoAlto;
  private ImageView[] imageViewTransicoes;
  ContextMenu customContextMenu;

  //Atributo referente a velocidade
  private int signalSpeed = 1;
  private int errorChance = 2;

  //Instancia da classe de alerta
  Alert alerta = new  Alert(AlertType.ERROR);
  
  //Instancias das classe Transmissao e Meio Transmissao
  MeioTransmissao meioTransmissao;
  Transmissor transmissor;
  
  //Getters e Setters para acesso das informacoes fora do controle
  public int getSignalSpeed() {
    return signalSpeed;
  }
  public ImageView[] getImageViewTransicoes() {
    return imageViewTransicoes;
  }
  public ImageView[] getImageViewsAlto() {
    return imageViewsAlto;
  }
  public ImageView[] getImageViewsBaixo() {
    return imageViewsBaixo;
  }
  public ImageView[] getImageViewsAltoBaixo() {
    return imageViewsAltoBaixo;
  }
  public ImageView[] getImageViewsBaixoAlto() {
    return imageViewsBaixoAlto;
  }
  public ChoiceBox<String> getCbxTipoCodificacao() {
    return cbxTipoCodificacao;
  }
  public ChoiceBox<String> getCbxTipoEnquadramento() {
    return cbxTipoEnquadramento;
  }
  public ChoiceBox<String> getCbxTipoControleErro() {
    return cbxTipoControleErro;
  }
  public Button getBtnEnviar() {
    return btnEnviar;
  }
  public ImageView getImgMouse() {
    return imgMouse;
  }
  public int getErrorChance() {
    return errorChance;
  }
  public String getEncodeSelector() {
    return encodeSelector;
  }
  public String getEnquadSelector() {
    return enquadSelector;
  }
  public String getErrorControlSelector() {
    return errorControlSelector;
  }
  public String getTextoInserido() {
    return txtInsert.getText();
  }
  public TextArea getTxtATransmissor() {
    return txtATransmissor;
  }
  public MenuButton getMenuConfig() {
    return menuConfig;
  }
  public ImageView getBckground() {
    return bckground;
  }
  public Slider getSliderErrorGenerator() {
    return sliderErrorGenerator;
  }
  // Metodos

  //Dispara acoes ao clicar no botao de iniciar
  @FXML
  void onClickBtnIniciar(ActionEvent event) {
    btnIniciar.setVisible(true);
    btnIniciar.setVisible(false);
    menuConfig.setVisible(true);
    bckground.setVisible(false);
  }
  //Dispara acoes ao clicar no menu de configuracoes (definicao dos seletores de codificacao, enquadramento e controle de erros)
  @FXML
  void onClickMenuConfig(ActionEvent event) {
    cbxTipoCodificacao.setVisible(true);
    cbxTipoEnquadramento.setVisible(true);
    cbxTipoControleErro.setVisible(true);
  }

  //Dispara acoes ao clicar no botao para enviar o texto
  @FXML
  void onClickBtnEnviar(ActionEvent event) {
    if(!(this.getTextoInserido().isEmpty())){
      bckground.setVisible(true);
      imgIcon.setVisible(false);
      imgTextoGrande.setVisible(false);
      lblTextoPequeno.setVisible(false);
      lblTextoGrande.setVisible(false);
      imgTextoPequeno.setVisible(false);
      txtATransmissor.setVisible(true);
      imgIcon.setVisible(true);
      menuConfig.setVisible(false);
      transmissor.AplicacaoTransmissora(getTextoInserido());
      txtInsert.setText("");
      btnEnviar.setVisible(false);
      imgMouse.setVisible(false);
      sliderSignalSpeed.setVisible(true);
      sliderErrorGenerator.setVisible(true);
      lblSpeed.setVisible(true);
      lblError.setVisible(true);
    }else{
      alerta.setTitle("Erro na selecao de orientacao");
      alerta.setHeaderText("Opcao vazia ou inexistente");
      alerta.setContentText("Selecione uma opcao valida!");
      alerta.showAndWait();
    }
  }

  public void startErrorAlert() throws IOException{
    //carregando arquivos da tela e criando cenas
     FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/errorScreen.fxml"));
     Parent root = loader.load();
     //criando janela
     Stage stage = new Stage();
     // impossibilitando redimencionamento da tela
     stage.resizableProperty().setValue(false);
     //atribuindo o arquivo xml para uma cena e atribuindo a janela (stage)
     stage.setScene(new Scene(root));
     stage.show();
     //atribuindo icone a janela
     stage.getIcons().add(new Image(getClass().getResourceAsStream("../view/assets/icon.png")));
  }

  //Deixa as imagens dos sinais invisiveis
  public void disable(){
    for(int i = 0; i < 8; i++){
      imageViewsAlto[i].setVisible(false);
      imageViewsBaixo[i].setVisible(false);
      imageViewsAltoBaixo[i].setVisible(false);
      imageViewsBaixoAlto[i].setVisible(false);
      imageViewTransicoes[i].setVisible(false);
    }
  }
  //Verifica o tamanho da mensagem e seta a imagem do tamanho do texto correspondente
  public void setMessage(String message){
    imgIcon.setVisible(true);
    int tamanho = message.length()/4 + message.length()%4;
    if(tamanho > 5){
      imgTextoGrande.setVisible(true);
      lblTextoGrande.setVisible(true);
      lblTextoGrande.setText(message);
    }else{
      imgTextoPequeno.setVisible(true);
      lblTextoPequeno.setVisible(true);
      lblTextoPequeno.setText(message);
    }
  }

  public void changeVisibility() {
    txtInsert.setText("");
    lblTextoPequeno.setText("");
    lblTextoGrande.setText("");
    txtATransmissor.setText("");
    txtInsert.setVisible(true);
    imgMouse.setVisible(true);
    btnEnviar.setVisible(true);
    imgIcon.setVisible(false);
    imgTextoGrande.setVisible(false);
    lblTextoPequeno.setVisible(false);
    lblTextoGrande.setVisible(false);
    imgTextoPequeno.setVisible(false);
    sliderSignalSpeed.setVisible(false);
    sliderErrorGenerator.setVisible(false);
    lblSpeed.setVisible(false);
    lblError.setVisible(false);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //Intancia da classes e atribuicao da referencia
    meioTransmissao = new MeioTransmissao();
    //Passagem da referencia do controlador para o controle, que sera   ''passado para outras classes
    transmissor = new Transmissor(this);
    //Deixando alguns elementos temporariamente invisiveis
    bckground.setVisible(true);
    btnEnviar.setVisible(false);
    btnIniciar.setVisible(true);
    txtInsert.setVisible(false);
    txtATransmissor.setVisible(false);
    imgMouse.setVisible(false);
    imgIcon.setVisible(false);
    imgTextoGrande.setVisible(false);
    lblTextoPequeno.setVisible(false);
    lblTextoGrande.setVisible(false);
    imgTextoPequeno.setVisible(false);
    txtATransmissor.setEditable(false);
    menuConfig.setVisible(false);
    sliderSignalSpeed.setVisible(false);
    sliderErrorGenerator.setVisible(false);
      lblSpeed.setVisible(false);
      lblError.setVisible(false);
    txtInsert.setText("");
    
    //Atribuindo as imagens a indices de array para melhor manipulacao
    imageViewsAlto = new ImageView[] { imgBit01Alto, imgBit02Alto, imgBit03Alto, imgBit04Alto, imgBit05Alto,imgBit06Alto, imgBit07Alto, imgBit08Alto };
    imageViewsBaixo = new ImageView[] { imgBit01Baixo, imgBit02Baixo, imgBit03Baixo, imgBit04Baixo, imgBit05Baixo,imgBit06Baixo, imgBit07Baixo, imgBit08Baixo };
    imageViewsAltoBaixo = new ImageView[] { imgBit01altoBaixo, imgBit02altoBaixo, imgBit03altoBaixo, imgBit04altoBaixo,imgBit05altoBaixo, imgBit06altoBaixo, imgBit07altoBaixo, imgBit08altoBaixo };
    imageViewsBaixoAlto = new ImageView[] { imgBit01baixoAlto, imgBit02baixoAlto, imgBit03baixoAlto, imgBit04baixoAlto,imgBit05baixoAlto, imgBit06baixoAlto, imgBit07baixoAlto, imgBit08baixoAlto };
    imageViewTransicoes = new ImageView[]{transicao1,transicao2,transicao3,transicao4,transicao5,transicao6,transicao7,transicao1};
    
    //Deixando imagens invisiveis
    disable();

    //Configuracao do slider de velocidade
    sliderSignalSpeed.setMin(0);
    sliderSignalSpeed.setMax(2);
    sliderSignalSpeed.setValue(1);
    //definindo unidade de marcacao principal do slider
    sliderSignalSpeed.setMajorTickUnit(1);
    //definindo as unidades de marcacoes menores que compoem os espacos entre as marcacoes principais
    sliderSignalSpeed.setMinorTickCount(0);
    sliderSignalSpeed.setSnapToTicks(true);

    //Configuracao do slider de geracao de erro (probalbilidade)
    sliderErrorGenerator.setMin(1);
    sliderErrorGenerator.setMax(10);
    sliderErrorGenerator.setValue(2);
    sliderErrorGenerator.setMajorTickUnit(1);
    sliderErrorGenerator.setMinorTickCount(0);
    sliderErrorGenerator.setSnapToTicks(true);


    
    //Definicao de um listener para captar a mudanca de nivel dos sliders em tempo real
    sliderSignalSpeed.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue){
        //passando o novo valor para o atributo de velocidade do consumidor
        signalSpeed = newValue.intValue();
      }
    });
    sliderErrorGenerator.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue){
        //passando o novo valor para o atributo de velocidade do consumidor
        errorChance = newValue.intValue();
      }
    });

    
    
    //Definindo o conteudo do Choice Box
    cbxTipoCodificacao.getItems().addAll("Codificacao Binaria", "Codificacao Manchester",
        "Codificacao Manchester Diferencial");
    cbxTipoEnquadramento.getItems().addAll("Contagem de caracteres", "Insercao de bytes",
        "Insercao de bits","Violacao da Camada Fisica");
    cbxTipoControleErro.getItems().addAll("Bit de Paridade par", "Bit de Paridade Impar",
        "Polinomio CRC-32(IEEE 802)", "Codigo de Hamming");
    //Detectando selecao de item nos choicebox's
    cbxTipoCodificacao.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) {
          encodeSelector = newValue;
          System.out.println(encodeSelector);
          changeVisibility();
          disable();
        }
      }
    });
  cbxTipoEnquadramento.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) {
          enquadSelector = newValue;
          System.out.println(enquadSelector);
          changeVisibility();
          disable();
        }
      }
  });
  cbxTipoControleErro.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) {
          errorControlSelector = newValue;
          System.out.println(errorControlSelector);
          changeVisibility();
          disable();
        }
      }
    });

  }
}
