/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 1/12/2023
* Ultima alteracao.: 5/12/2023
* Nome.............: ControleDados.java
* Funcao...........: Classe responsavel pelo controle de elementos da interface e referencias entras classes
*************************************************************** */
package controller;

//Importacoes
import java.io.IOException;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.MeioTransmissao;
import model.DispositivoTransmissor.Transmissor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControleDados implements Initializable {//Classe ControleDados
  @FXML
  private Slider sliderErrorGenerator;
  @FXML
  private Label lblError;
  @FXML
  private Label lblSpeed;
  @FXML
  private Label lblShowAccessCtrlText;
  @FXML
  private Label lblShowCodeText;
  @FXML
  private Label lblShowEnquadText;
  @FXML
  private Label lblShowErrorCtrlText;
  @FXML
  private MenuItem menuTCode;
  @FXML
  private MenuItem menuTCont;
  @FXML
  private MenuItem menuTEnqd;
  @FXML
  private ImageView colisao1;
  @FXML
  private ImageView colisao2;
  @FXML
  private ImageView colisao3;
  @FXML
  private ImageView colisao4;
  @FXML
  private ImageView colisao5;
  @FXML
  private ImageView transmitindo1;
  @FXML
  private ImageView transmitindo2;
  @FXML
  private ImageView transmitindo3;
  @FXML
  private ImageView transmitindo4;
  @FXML
  private ImageView transmitindo5;
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
  private ChoiceBox<String> cbxTipoMAC;
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
  private TextArea txtTv;
  @FXML
  private TextArea txtATransmissor1;
  @FXML
  private TextArea txtATransmissor2;
  @FXML
  private TextArea txtATransmissor3;
  @FXML
  private TextArea txtATransmissor4;
  @FXML
  private TextArea txtATransmissor5;
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
  private String macSelector;
  
  //Array de ImageView, para facilitar na manipulacao
  private ImageView[] imageViewsAlto;
  private ImageView[] imageViewsBaixo;
  private ImageView[] imageViewsAltoBaixo;
  private ImageView[] imageViewsBaixoAlto;
  private ImageView[] imageViewTransicoes;
  ContextMenu customContextMenu;

  //Atributo referente a velocidade
  private int signalSpeed = 1;
  private int errorChance = 1;

  //Instancia da classe de alerta
  Alert alerta = new  Alert(AlertType.ERROR);
  
  //Instancias das classe Transmissao e Meio Transmissao
  MeioTransmissao meioTransmissao;
  
  private Transmissor transmissor1;
  private Transmissor transmissor2;
  private Transmissor transmissor3;
  private Transmissor transmissor4;
  private Transmissor transmissor5;
  
  private static boolean alredyRunned = false;
  
  
  public MeioTransmissao getMeioTransmissao() {
    return meioTransmissao;
  }
  public Transmissor getTransmissor1() {
    return transmissor1;
  }
  public Transmissor getTransmissor2() {
      return transmissor2;
  }
  public Transmissor getTransmissor3() {
      return transmissor3;
  }
  public Transmissor getTransmissor4() {
      return transmissor4;
  }
  public Transmissor getTransmissor5() {
      return transmissor5;
  }

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
  public ChoiceBox<String> getCbxTipoMAC() {
    return cbxTipoMAC;
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
  public String getMacSelector() {
    return macSelector;
  }
  public TextArea gettxtTv() {
    return txtTv;
  }
  public void setTxtTv(String texto) {
    if(txtTv!=null && texto != null){
      txtTv.appendText(texto);
    }
  }
  public TextArea getTxtATransmissor1() {
    return txtATransmissor1;
  }
  public TextArea getTxtATransmissor2() {
    return txtATransmissor2;
  }
  public TextArea getTxtATransmissor3() {
    return txtATransmissor3;
  }
  public TextArea getTxtATransmissor4() {
    return txtATransmissor4;
  }
  public TextArea getTxtATransmissor5() {
    return txtATransmissor5;
  }
  public MenuButton getMenuConfig() {
    return menuConfig;
  }
  public Slider getSliderErrorGenerator() {
    return sliderErrorGenerator;
  }
  public ImageView getColisao1() {
    return colisao1;
  }
  public ImageView getColisao2() {
    return colisao2;
  }
  public ImageView getColisao3() {
    return colisao3;
  }
  public ImageView getColisao4() {
    return colisao4;
  }
  public ImageView getColisao5() {
    return colisao5;
  }
  public ImageView getTransmitindo1() {
    return transmitindo1;
  }
  public ImageView getTransmitindo2() {
    return transmitindo2;
  }
  public ImageView getTransmitindo3() {
    return transmitindo3;
  }
  public ImageView getTransmitindo4() {
    return transmitindo4;
  }
  public ImageView getTransmitindo5() {
    return transmitindo5;
  }
  // Metodos

  //Dispara acoes ao clicar no botao de iniciar
  @FXML
  void onClickBtnIniciar(ActionEvent event) {
    btnIniciar.setVisible(true);
    btnIniciar.setVisible(false);
    menuConfig.setVisible(true);
    imgBackground.setImage(new Image(getClass().getResourceAsStream("/view/assets/background2.png")));
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
    txtTv.setVisible(true);
    txtATransmissor1.setVisible(true);
    txtATransmissor2.setVisible(true);
    txtATransmissor3.setVisible(true);
    txtATransmissor4.setVisible(true);
    txtATransmissor5.setVisible(true);
    menuConfig.setVisible(false);
    //transmissor.AplicacaoTransmissora(getTextoInserido());
    btnEnviar.setVisible(false);
    imgMouse.setVisible(false);
    sliderSignalSpeed.setVisible(true);
    sliderErrorGenerator.setVisible(true);
    lblSpeed.setVisible(true);
    lblError.setVisible(true);
    if(alredyRunned){
      restartThreads();
    }else{
      startThreads();
    }
    alredyRunned = true;
  }
  public void restartThreads(){
    changeVisibility();
    disable();

    //meioTransmissao.setMediaAccess(0);

    transmissor1.setArrived(false);
    transmissor2.setArrived(false);
    transmissor3.setArrived(false);
    transmissor4.setArrived(false);
    transmissor5.setArrived(false);

    transmissor1.setColidiu(false);    
    transmissor2.setColidiu(false);    
    transmissor3.setColidiu(false);    
    transmissor4.setColidiu(false);    
    transmissor5.setColidiu(false);    

    transmissor1.AplicacaoTransmissora(transmissor1.randomMessage());
    transmissor2.AplicacaoTransmissora(transmissor2.randomMessage());
    transmissor3.AplicacaoTransmissora(transmissor3.randomMessage());
    transmissor4.AplicacaoTransmissora(transmissor4.randomMessage());
    transmissor5.AplicacaoTransmissora(transmissor5.randomMessage());
  }
  public void startThreads(){
    transmissor1.start();
    transmissor2.start();
    transmissor3.start();
    transmissor4.start();
    transmissor5.start();
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
/*

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
 */

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

  public void changeVisibility() {
    txtTv.setText("");
    txtATransmissor1.setText("");
    txtATransmissor2.setText("");
    txtATransmissor3.setText("");
    txtATransmissor4.setText("");
    txtATransmissor5.setText("");
    imgMouse.setVisible(true);
    btnEnviar.setVisible(true);
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
    transmissor1 = new Transmissor(this,colisao1,transmitindo1);
    transmissor2 = new Transmissor(this,colisao2,transmitindo2);
    transmissor3 = new Transmissor(this,colisao3,transmitindo3);
    transmissor4 = new Transmissor(this,colisao4,transmitindo4);
    transmissor5 = new Transmissor(this,colisao5,transmitindo5);
    //Deixando alguns elementos temporariamente invisiveis
    btnEnviar.setVisible(false);
    btnIniciar.setVisible(true);
    txtTv.setVisible(false);
    txtATransmissor1.setVisible(false);
    txtATransmissor2.setVisible(false);
    txtATransmissor3.setVisible(false);
    txtATransmissor4.setVisible(false);
    txtATransmissor5.setVisible(false);
    imgMouse.setVisible(false);
    txtTv.setEditable(false);
    txtATransmissor1.setEditable(false);
    txtATransmissor2.setEditable(false);
    txtATransmissor3.setEditable(false);
    txtATransmissor4.setEditable(false);
    txtATransmissor5.setEditable(false);
    menuConfig.setVisible(false);
    sliderSignalSpeed.setVisible(false);
    sliderErrorGenerator.setVisible(false);
    lblSpeed.setVisible(false);
    lblError.setVisible(false);

    colisao1.setVisible(false);
    colisao2.setVisible(false);
    colisao3.setVisible(false);
    colisao4.setVisible(false);
    colisao5.setVisible(false);
    transmitindo1.setVisible(false);
    transmitindo2.setVisible(false);
    transmitindo3.setVisible(false);
    transmitindo4.setVisible(false);
    transmitindo5.setVisible(false);


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
    sliderErrorGenerator.setValue(1);
    sliderErrorGenerator.setMajorTickUnit(1);
    sliderErrorGenerator.setMinorTickCount(0);
    sliderErrorGenerator.setSnapToTicks(true);

    lblError.addEventFilter(MouseEvent.ANY, MouseEvent::consume);
    lblSpeed.addEventFilter(MouseEvent.ANY, MouseEvent::consume);
    lblShowEnquadText.addEventFilter(MouseEvent.ANY, MouseEvent::consume);
    lblShowCodeText.addEventFilter(MouseEvent.ANY, MouseEvent::consume);
    lblShowErrorCtrlText.addEventFilter(MouseEvent.ANY, MouseEvent::consume);
    lblShowAccessCtrlText.addEventFilter(MouseEvent.ANY, MouseEvent::consume);
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
    cbxTipoMAC.getItems().addAll("ALOHA Puro", "Slotted ALOHA",
        "CSMA nao persistente", "CSMA persistente","CSMA p persistente","CSMA/CD");
    //Detectando selecao de item nos choicebox's
    cbxTipoCodificacao.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) {
          encodeSelector = newValue;
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
          changeVisibility();
          disable();
        }
      }
    });
    cbxTipoMAC.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) {
          macSelector = newValue;
          changeVisibility();
          disable();
        }
      }
    });

  }
}
