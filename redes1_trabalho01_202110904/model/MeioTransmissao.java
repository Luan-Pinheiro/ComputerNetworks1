/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 23/08/2023
* Ultima alteracao.: 12/09/2023
* Nome.............: MeioTransmissao.java
* Funcao...........: Classe responsavel pelo transporte de dados entre a classe transmissora e a receptora
*************************************************************** */

package model;

//Importacoes
import controller.ControleDados;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

public class MeioTransmissao extends Thread {//Classe Meio de transmissao
  StringBuilder signal;//Sinal local
  ControleDados cD;//Classe controladora

  //Arrays de imagens local
  ImageView[] baixo;
  ImageView[] alto;
  ImageView[] altoBaixo;
  ImageView[] baixoAlto;
  ImageView[] transicao;

  Receptor receptor = new Receptor(this);//receptor local

  private char previousBit;//Auxiliar na animacao, para capturar bit anterior
  private int speed;//Velocidade 


  //Metodo que, baseado no atributo capturado em tempo real do controle, atribui a velocidade da passagem do sinal na tela
  public int setSpeedSignal() {
    speed = cD.getSignalSpeed();
    switch(speed) {
        case 0: 
            speed = 300;
            break;
        case 1: 
            speed = 170;
            break;
        case 2: 
            speed = 80;
            break;
        default:
            System.out.println("Não foi possivel atribuir");
            break;
    }
    return speed;
  }

  //Realiza a passagem de dados do transmissor para o receptor
  public void MeioDeComunicacao(int fluxoBrutoDeBits[], ControleDados cD, StringBuilder signal, int typeEncode) {
    this.cD = cD;
    this.signal = signal;//
    baixo = cD.getImageViewsBaixo();//Array de ImageView baixo
    alto = cD.getImageViewsAlto();//Array de ImageView alto
    altoBaixo = cD.getImageViewsAltoBaixo();//Array de ImageView altoBaixo
    baixoAlto = cD.getImageViewsBaixoAlto();//Array de ImageView baixoAlto
    transicao = cD.getImageViewTransicoes();//Array de ImageView transicao

    int fluxoBrutoDeBitsPontoA[] = {};
    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    int iterator = 0;
    int fluxoBrutoDeBitsPontoB[] = new int[fluxoBrutoDeBitsPontoA.length];

    while (iterator < fluxoBrutoDeBitsPontoA.length) {
      fluxoBrutoDeBitsPontoB[iterator] = fluxoBrutoDeBitsPontoA[iterator]; // BITS Sendo transferidos
      iterator++;
    } // fim do while

    buildSignal(signal,typeEncode);//Animacao
   
    // chama proxima camada
    receptor.CamadaFisicaReceptora(fluxoBrutoDeBitsPontoB, cD);
    
  }// fim do metodo MeioDeTransmissao

  //Constoi a animacao do sinal para cada tipo de codificacao
  public void buildSignal(StringBuilder signal, int typeEncode) {
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

  public void update(ImageView[] alto, ImageView[] baixo,ImageView[] transicao,int tipo) {
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
    cD.getCbxTipoCodificacao().setVisible(true);//Manipulacao na interface
    cD.getBtnEnviar().setVisible(true);//Manipulacao na interface
    cD.getImgMouse().setVisible(true);//Manipulacao na interface
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
