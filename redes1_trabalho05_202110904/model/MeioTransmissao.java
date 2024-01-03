/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 1/12/2023
* Ultima alteracao.: 5/12/2023
* Nome.............: MeioTransmissao.java
* Funcao...........: Classe responsavel pelo transporte de dados entre a classe transmissora e a receptora
*************************************************************** */

package model;

import java.util.Random;

//Importacoes
import controller.ControleDados;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import model.DispositivoReceptor.Receptor;
import model.DispositivoTransmissor.Transmissor;

public class MeioTransmissao extends Thread {//Classe Meio de transmissao
  private StringBuilder signal;//Sinal local
  ControleDados cD;//Classe controladora
  private Receptor receptor;//receptor local
  private int typeEncode;
  private char previousBit;//Auxiliar na animacao, para capturar bit anterior
  private int speed;//Velocidade 
  private static int mediaAccess = 0; //Variavel de verificacao de acesso ao meio
  private boolean isFree = true; //Auxiliar de verificacao de colisao


  //Arrays de imagens local
  ImageView[] baixo;
  ImageView[] alto;
  ImageView[] altoBaixo;
  ImageView[] baixoAlto;
  ImageView[] transicao;

  //Metodo que, baseado no atributo capturado em tempo real do controle, atribui a velocidade da passagem do sinal na tela
  public int setSpeedSignal() {
    speed = cD.getSignalSpeed();
    switch(speed) {
      case 0: 
        speed = 90;
        break;
      case 1: 
        speed = 60;
        break;
      case 2: 
        speed = 30;
        break;
      default:
        System.out.println("Não foi possivel atribuir");
        speed = 60;
        break;
    }
    return speed;
  }
  //Getters e Setters
  public ControleDados getCd() {
    return cD;
  }
  public static void setMediaAccess(int mediaAcc,ControleDados cD) {
    mediaAccess = mediaAcc;
  }
  public boolean isFree() {
    return this.isFree;
  }
  public  void setFree(boolean bool) {
    this.isFree = bool;
  }
  public int getTypeEncode() {
    return typeEncode;
  }

  //Realiza a passagem de dados do transmissor para o receptor
  public void MeioDeComunicacao(int fluxoBrutoDeBits[], ControleDados cD, StringBuilder signal, int typeEncode,Transmissor trs) {
    this.cD = cD;
    this.signal = trs.getSignal();
    this.typeEncode = typeEncode;
    
    receptor = new Receptor(this, trs);  
    baixo = cD.getImageViewsBaixo();//Array de ImageView baixo
    alto = cD.getImageViewsAlto();//Array de ImageView alto
    altoBaixo = cD.getImageViewsAltoBaixo();//Array de ImageView altoBaixo
    baixoAlto = cD.getImageViewsBaixoAlto();//Array de ImageView baixoAlto
    transicao = cD.getImageViewTransicoes();//Array de ImageView transicao  

    int iterator = 0;
    int fluxoBrutoDeBitsPontoA[] = {};
    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    int fluxoBrutoDeBitsPontoB[] = new int[fluxoBrutoDeBitsPontoA.length];
    
    //Incrementa acesso ao meio
    mediaAccess++;

    if(mediaAccess>1){//Verifica colisao
      trs.setColidiu(true);
      trs.showColisao();
    }
    //atribuicao do valor inteiro obtido no slider
    int errorChance = cD.getErrorChance();
    Random randomic = new Random();
    //Definicao da probabilidade de acordo com o slider
    int randomicNumber = randomic.nextInt(errorChance);

    while (iterator < fluxoBrutoDeBitsPontoA.length) {
      fluxoBrutoDeBitsPontoB[iterator] = fluxoBrutoDeBitsPontoA[iterator];
      if (randomicNumber == 0)
      fluxoBrutoDeBitsPontoB[iterator] = fluxoBrutoDeBitsPontoA[iterator]; // BITS Sendo transferidos
      else{
        // invertendo um bit aleatorio do quadro (erro)
        int fluxoBrutoDeBitsPontoAInterferencia = fluxoBrutoDeBitsPontoA[iterator]^=1<<(randomic.nextInt(9));
        fluxoBrutoDeBitsPontoB[iterator] = fluxoBrutoDeBitsPontoAInterferencia;
      }
      iterator++;
    } // fim do while
    
    Platform.runLater(()->{
      buildSignal(typeEncode);//Animacao
    });

    //Decrementa acesso ao meio
    mediaAccess--;
    receptor.CamadaFisicaReceptora(fluxoBrutoDeBitsPontoB, cD);
  }//fim do metodo MeioDeTransmissao

  public void MeioDeComunicacaoCD(int fluxoBrutoDeBits[], ControleDados cD, StringBuilder signal, int typeEncode,Transmissor trs) {
    this.cD = cD;
    this.signal = trs.getSignal();
    this.typeEncode = typeEncode;
    
    receptor = new Receptor(this, trs);  
    baixo = cD.getImageViewsBaixo();//Array de ImageView baixo
    alto = cD.getImageViewsAlto();//Array de ImageView alto
    altoBaixo = cD.getImageViewsAltoBaixo();//Array de ImageView altoBaixo
    baixoAlto = cD.getImageViewsBaixoAlto();//Array de ImageView baixoAlto
    transicao = cD.getImageViewTransicoes();//Array de ImageView transicao  

    int iterator = 0;
    int fluxoBrutoDeBitsPontoA[] = {};
    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    int fluxoBrutoDeBitsPontoB[] = new int[fluxoBrutoDeBitsPontoA.length];
    
    //Incrementa acesso ao meio
    mediaAccess++;

    if(mediaAccess>1){ //Verifica colisao
      trs.setColidiu(true);
      trs.showColisao();
      trs.setFreeCsmaCd(false);
    }
    
    if(!(trs.getColidiu() && (trs.getSeletorMAC() == 5))){
      //atribuicao do valor inteiro obtido no slider
      int errorChance = cD.getErrorChance();
      Random randomic = new Random();
      //Definicao da probabilidade de acordo com o slider
      int randomicNumber = randomic.nextInt(errorChance);
  
      while (iterator < fluxoBrutoDeBitsPontoA.length) {
        fluxoBrutoDeBitsPontoB[iterator] = fluxoBrutoDeBitsPontoA[iterator];
        if (randomicNumber == 0)
        fluxoBrutoDeBitsPontoB[iterator] = fluxoBrutoDeBitsPontoA[iterator]; // BITS Sendo transferidos
        else{
          // invertendo um bit aleatorio do quadro (erro)
          int fluxoBrutoDeBitsPontoAInterferencia = fluxoBrutoDeBitsPontoA[iterator]^=1<<(randomic.nextInt(9));
          fluxoBrutoDeBitsPontoB[iterator] = fluxoBrutoDeBitsPontoAInterferencia;
        }
        iterator++;
      } // fim do while
      
      Platform.runLater(()->{
        buildSignal(typeEncode);//Animacao
      });
  
      //Decrementa acesso ao meio
      mediaAccess--;
      receptor.CamadaFisicaReceptora(fluxoBrutoDeBitsPontoB, cD);
    }
  }//fim do metodo MeioDeTransmissaoCD


  // ---------------------------- ANIMACAO DO SINAL ----------------------------

  //Constoi a animacao do sinal para cada tipo de codificacao
  public void buildSignal(int typeEncode) {
    new Thread(() -> {
      switch (typeEncode) {
        case 0:
          update(alto, baixo,transicao,typeEncode);
          break;
        case 1:
          update(altoBaixo, baixoAlto,transicao,typeEncode);
          break;
        case 2:
          update(altoBaixo, baixoAlto,transicao,typeEncode);
          break;
      }
    }).start();
  }

  public void update(ImageView[] alto, ImageView[] baixo, ImageView[] transicao, int tipo) {
    for (int i = 0; i < signal.length(); i++) {
      if(i == 0){
        previousBit = signal.charAt(i);
      }
      try {
        takeToRight(alto, baixo,transicao);//Empurra as imagens para a direita
        getNewSignal(signal.charAt(i), alto, baixo, transicao,tipo);//Coloca uma nova imagem do proximosinal na posicao 0
        sleep(setSpeedSignal());//seta velocidade da animacao
      } catch (InterruptedException e) {
        e.printStackTrace();//mensagem padrao em caso de excecao
      }
    }
  }

  //Atribui a imagem na posicao 0 referente ao bit no momento
  public void getNewSignal(char bit, ImageView[] first, ImageView[] second, ImageView[] transicao, int tipo){
    Platform.runLater(() -> {
      //Deixando a imagens invisiveis
      first[0].setVisible(false);
      second[0].setVisible(false);
      transicao[0].setVisible(false);
      //Cria a transicao se os bits atual/anteiror forem diferentes para cada tipo de codificacao
      switch(tipo){
        case 0:
          if (bit != previousBit) {
            transicao[0].setVisible(true);
          }
          break;
        case 1:
          if (bit == previousBit) {
            transicao[0].setVisible(true);
          }
          break;
        case 2:
          if (bit == previousBit) {
            transicao[0].setVisible(true);
          }
          break;
      }
      previousBit = bit;
      //Define qual imagem ficara visivel 
      if (bit == '1')
        first[0].setVisible(true);
      else
        second[0].setVisible(true);

    });
  }
  //Empurra os indices de 1 a 7 uma posicao pra frente na tela
  public void takeToRight(ImageView[] first, ImageView[] second, ImageView[] transicao) {
    Platform.runLater(() -> {
      for (int i = 7; i >= 1; i--) {
        //Define visibilidade de cada indice, iniciando do maior indice do array de imagens
        //empurra os indices para o proximo espaco na tela
        boolean previousSecondIsVisible = second[i - 1].isVisible();
        boolean previousFirstIsVisible = first[i - 1].isVisible();
        second[i].setVisible(previousSecondIsVisible);
        first[i].setVisible(previousFirstIsVisible);
        //Define a transicao, que so sera exibida caso necessario, como definido no getNewSignal
        if(i > 1){
          transicao[i-1].setVisible(transicao[i - 2].isVisible());
        }else{
          transicao[i].setVisible(transicao[i - 1].isVisible());
        }
      }
    });

  }
}
