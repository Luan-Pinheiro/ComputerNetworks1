/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 1/12/2023
* Ultima alteracao.: 5/12/2023
* Nome.............: Transmissor.java
* Funcao...........: Classe responsavel por simular o dispositivo traansmissor, tal qual seu envio de dados
*************************************************************** */
package model.DispositivoTransmissor;
import java.util.Random;

import controller.ControleDados;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class Transmissor extends Thread{
  private static ControleDados cD; //Controlador
  private String typeEncode;//Tipo de codificacao
  private StringBuilder signal; // Sinais de transmissao
  private String typeErrorControl;//Tipo de controle de erros
  private String typeAccess; //Tipo de acesso ao meio
  private String typeEnquad;//Tipo de enquadramento
  private int seletor;//Seletor para tipo de codificacao
  private int macSelector; //Seletor para tipo de acesso ao meio
  private int enquadSelector;//Seletor para tipo de enquadramento
  private int errorControlSelector;//Seletor para tipo de controle de erros
  private int id; // Id proprio de cada dispositivo
  private static int cont = 1; // contador estatico para definicao unica de cada id de dispositivo
  private boolean isArrived = false; // variavel de confirmacao
  private boolean colidiu = false; // flag de analise de colisao 
  private boolean csmaFlag = true; // auxiliar do ciclo de verificacao do cmsa 
  private boolean isFreeCsmaCd = true;
  StringBuilder signalBin; //Sinais da codificacao bit a bit binario
  StringBuilder signalManc; //Sinais da codificacao bit a bit Manchester
  StringBuilder signalDiffManc; //Sinais da codificacao bit a bit Manchester Diferencial
  ImageView colisao; // Imagem de colisao daquele dispositivo
  ImageView transmitindo; // Imagem de transmissao daquele dispositivo

  public Transmissor(ControleDados controleDados,ImageView col, ImageView trms) {//Referencia no construtor
    cD = controleDados;
    id = cont++;
    this.colisao = col;
    this.transmitindo = trms;
  }
  
  public boolean getColidiu() {
    return colidiu;
  }
  public void setColidiu(boolean colidiu) {
    this.colidiu = colidiu;
  }
  public boolean getIsArrived() {
    return isArrived;
  }
  public void setArrived(boolean value) {
    this.isArrived = value;
  }
  
  public StringBuilder getSignal() {
    return signal;
  }

  public void showTransmitindo(){
    Platform.runLater(()->{
      new Thread(() ->{
        try {
          transmitindo.setVisible(true);
          sleep(800);
          transmitindo.setVisible(false);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    });
  }

  public void showColisao(){
    Platform.runLater(()->{
      new Thread(() ->{
        try {
          colisao.setVisible(true);
          sleep(800);
          colisao.setVisible(false);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    });
  }

  public void hideTransmitindo(){
    Platform.runLater(()->{
      new Thread(() ->{
        try {
          sleep(600);
          colisao.setVisible(false);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    });
  }


  public void hideColisao(){
    Platform.runLater(()->{
      new Thread(() ->{
        try {
          sleep(600);
          colisao.setVisible(false);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    });
  }

  public static TextArea selecTextArea(int id){
    TextArea txtA = null;
    switch (id) {
      case 1:
        txtA = cD.getTxtATransmissor1();
        break;
      case 2:
        txtA = cD.getTxtATransmissor2();
        break;
      case 3:
        txtA = cD.getTxtATransmissor3();
        break;
      case 4:
        txtA = cD.getTxtATransmissor4();
        break;
      case 5:
        txtA = cD.getTxtATransmissor5();
        break;
      default:
        break;
    }
    return txtA;
  }

  public int getSeletorValue() {//Selecao do tipo de decodificacao
    typeEncode = cD.getEncodeSelector();
    if (typeEncode!=null) {
      switch (typeEncode) {
        case "Codificacao Binaria":
          seletor = 0;
          break;
        case "Codificacao Manchester":
          seletor = 1;
          break;
        case "Codificacao Manchester Diferencial":
          seletor = 2;
          break;
        default:
          //System.out.println("Nao selecionado, padrao definido: 'Codificacao Binaria'");
          seletor = 0;
          break;
      }
    }else{
      //System.out.println("Nao selecionado, padrao definido: 'Codificacao Binaria'");
      seletor = 0;
    }
    return seletor;
  }


  public int getSeletorEnquadramentoValue() {//Selecao do tipo de decodificacao
    typeEnquad = cD.getEnquadSelector();
    if(typeEnquad!=null){
      switch (typeEnquad) {
        case "Contagem de caracteres":
          enquadSelector = 0;
          break;
        case "Insercao de bytes":
          enquadSelector = 1;
          break;
        case "Insercao de bits":
          enquadSelector = 2;
          break;
        case "Violacao da Camada Fisica":
          enquadSelector = 3;
          break;
        default:
          //System.out.println("Nao selecionado, padrao definido: 'Contagem de caracteres'");
          enquadSelector = 0;
          break;
      }
    }else{
      //System.out.println("Nao selecionado, padrao definido: 'Contagem de caracteres'");
      enquadSelector = 0;
    }
    return enquadSelector;
  }
  
  public int getID() {
    return id;
  }

  public int getSeletorControleErroValue() {//Selecao do tipo de decodificacao
    typeErrorControl = cD.getErrorControlSelector();
    if(typeErrorControl!=null){
      switch (typeErrorControl) {
        case "Bit de Paridade par":
          errorControlSelector = 0;
          break;
        case "Bit de Paridade Impar":
          errorControlSelector = 1;
          break;
        case "Polinomio CRC-32(IEEE 802)":
          errorControlSelector = 2;
          break;
        case "Codigo de Hamming":
          errorControlSelector = 3;
          break;
        default:
          //System.out.println("Nao selecionado, padrao definido: 'Bit de Paridade par'");
          errorControlSelector = 0;
          break;
      }
    }else{
      //System.out.println("Nao selecionado, padrao definido: 'Bit de Paridade par'");
      errorControlSelector = 0;
    }
    return errorControlSelector;
  }

  public boolean isFreeCsmaCd() {
    return isFreeCsmaCd;
  }

  public void setFreeCsmaCd(boolean isFreeCsmaCd) {
    this.isFreeCsmaCd = isFreeCsmaCd;
  }

  public int getSeletorMAC() {
    typeAccess = cD.getMacSelector();
    if (typeAccess!=null){
      switch (typeAccess) {
        case "ALOHA Puro":
          macSelector = 0;
          break;
        case "Slotted ALOHA":
          macSelector = 1;
          break;
        case "CSMA nao persistente":
          macSelector = 2;
          break;
        case "CSMA persistente":
          macSelector = 3;
          break;
        case "CSMA p persistente":
          macSelector = 4;
          break;
        case "CSMA/CD":
          macSelector = 5;
          break;
        default:
          macSelector = 0;
          break;
      }
    }else{
      //System.out.println("Nao selecionado, padrao definido: 'Bit de Paridade par'");
      errorControlSelector = 0;
    }
    return macSelector;
  }

  public String randomMessage(){
    String message[] = {"UESB","Oi, gosto de Redes","Espelhando Tela","Tanembaum","Computer Science"};
    Random randomic = new Random();
    int randomicIndex= randomic.nextInt(5);
    return message[randomicIndex];
  }

  private static int[] getBinaryValue(String value, int id){
    char[] caractere = value.toCharArray();
    int convertedValue[] = new int[caractere.length];// Tamanho do array de int
    TextArea telaTransmissao = selecTextArea(id);
    //Array de caracteres, cada indice corresponde a um char da String inicial
    //Valor ascii do termo
    int asciiValue;
    //Auxiliar que armazena o ascii
    int asciiValueaux;
    //indice do array de inteiros, que armazenara 32 bits por completo em cada indice, com a conversao ficarao 4 letras por posicao no array de int
    int index = 0;
    //armazenara os valores de cada letra binario/ascii
    String charValues = "CARACTERE:\n";
    //titulo da parte que mostrara o binario completo                                                                     
    String binCompleto = "COMPLETO:\n"; 
    String allStringBinaryValue = "";// amazenara o binario completo
    int contador = 0; // incrementado de 8 em 8 para auxiliar na verificacao da aquebra de linha
    for (int i = 0; i < caractere.length; i++) {
      //Captura o valor ascii do caractere
      asciiValue = (int) (caractere[i]);
      //armazenando o valor de asciiValue, que sera modificado durante a iteracao
      asciiValueaux = asciiValue;
      //String que armazenara o valor binari do caractere
      String binaryValue = "";
      while (asciiValue > 0) {
        //Adiciona parte da conversao na string
        binaryValue = String.valueOf(asciiValue % 2) + binaryValue;
        //Passa para o proximo termo da divisao
        asciiValue /= 2;
      }
      //Caso o caractere nao preencha os 8bits do byte, eh preenchido com zero, para padronizacao
      while (binaryValue.length() < 8) {
        binaryValue = "0" + binaryValue;
      }
      //Incrementa contador em 8, simulando o tamanho da string
      contador += 8;
      //Adiciona o correspondente binario de cada termo na string de exibicao
      allStringBinaryValue += binaryValue;
      //String de termo a termo sendo concatenada para exibicao
      charValues += "Binario de'" + caractere[i] + "':\n" + binaryValue + "\n" + "ASCii de '" + caractere[i] + "': "
          + asciiValueaux + "\n";
      //exibicao de string termo a termo na tela
      final String auxCharValues = charValues;
      final String auxAllStringBinaryValue = allStringBinaryValue;
      Platform.runLater(() -> {
        telaTransmissao.setText(auxCharValues);
      });  

      for (int bitAtual = 0; bitAtual < 8; bitAtual++) {
        //Verifica qual eh o valor do bit naquela posicao e atribui a indexBitValue
        int indexBitValue = binaryValue.charAt(bitAtual) == '1' ? 1 : 0;
        // Realiza deslocamento a esquerda dos bits convertedValue e combina com o o bit e indexBitValue com o operador OR
        convertedValue[index] = (convertedValue[index] << 1) | indexBitValue;
      }
      index++;
      // Realiza a quebra de linha a cada 32 bits
      if (contador > 0 && contador % 16 == 0)
        allStringBinaryValue += "\n";
      // Caso seja ultima iteracao printa na tela do transmissor o binario completo referente ao texto inserido
      if (i == caractere.length - 1){
        Platform.runLater(() -> {
        telaTransmissao.setText(auxCharValues + binCompleto + auxAllStringBinaryValue);
        });      
      }
  
    }
      //retorna o valor
      return convertedValue;
    }
    
  public void AplicacaoTransmissora(String mensagem) {
    CamadaDeAplicacaoTransmissora(mensagem);//Proximo metodo
  }

  public void CamadaDeAplicacaoTransmissora(String mensagem) {
    //chamar o enlace
    int quadro[] = getBinaryValue(mensagem, this.id);
    CamadaEnlaceDadosTransmissora(quadro);
  }

  public void CamadaEnlaceDadosTransmissora(int quadro[]) {
    CamadaEnlaceDadosTransmissoraEnquadramento(quadro);
  }

  
  public void CamadaEnlaceDadosTransmissoraEnquadramento(int quadro[]) {
    int tipoDeEnquadramento = this.getSeletorEnquadramentoValue();
    int quadroEnquadrado[] = {};
    switch (tipoDeEnquadramento) {
      case 0:
      quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramentoContagemDeCaracteres(quadro);
      break;
      case 1:
      quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBytes(quadro);
      break;
      case 2:
      quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBits(quadro);
      break;
      case 3:
      quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramentoViolacaoDaCamadaFisica(quadro);
      break;
    }
    CamadaEnlaceDadosTransmissoraControleDeErro(quadroEnquadrado);
  }

  public int[] CamadaEnlaceDadosTransmissoraEnquadramentoContagemDeCaracteres(int quadro[]) {
    return quadro;
  }
  
  public int[] CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBytes(int quadro[]) {
    return quadro;
  }
  
  public int[] CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBits(int quadro[]) {
    return quadro;
  }
  
  public int[] CamadaEnlaceDadosTransmissoraEnquadramentoViolacaoDaCamadaFisica(int quadro[]) {
    return quadro;
  }
  
  public void CamadaEnlaceDadosTransmissoraControleDeErro (int quadro []) {
    int tipoDeControleDeErro = this.getSeletorControleErroValue(); // alterar de acordo com o teste
    int quadroControlado [] = {};
    switch (tipoDeControleDeErro) {
      case 0: // bit de paridade par
        quadroControlado = CamadaEnlaceDadosTransmissoraControleDeErroBitParidadePar(quadro);
        break;
      case 1: // bit de paridade impar
        quadroControlado = CamadaEnlaceDadosTransmissoraControleDeErroBitParidadeImpar(quadro);
        break;
      case 2: // CRC
        quadroControlado = CamadaEnlaceDadosTransmissoraControleDeErroCRC(quadro);
        break;
      case 3: // codigo de Hamming
        quadroControlado = CamadaEnlaceDadosTransmissoraControleDeErroCodigoDeHamming(quadro);
        break;
    }// fim do switch/case
    CamadaAcessoAoMeioTransmissora(quadroControlado);
  }

  public int[] CamadaEnlaceDadosTransmissoraControleDeErroBitParidadePar(int quadro[]) {
    int[] quadroControlado = quadro;
    for(int i = 0; i < quadro.length; i++){
      int counter = 0;
      int valor = quadro[i];
      for(int k = 0; k < 8; k++){
        //Obtendo bit atual, verificando e incrementando contador
        int bit = (valor >> k) & 1;
        counter = (bit==1) ? ++counter : counter;
      }
      //Insercao do bit de paridade
      quadroControlado[i] |= (counter%2 == 0) ? (0) : (1<<8);
    }
    return quadroControlado;
  }// fim do metodo CamadaEnlaceDadosTransmissoraControledeErroBitParidadePar

  public int[] CamadaEnlaceDadosTransmissoraControleDeErroBitParidadeImpar(int quadro[]) {
    int[] quadroControlado = quadro;
    for(int i = 0; i < quadro.length; i++){
      int counter = 0;
      int valor = quadro[i];
      for(int k = 0; k < 8; k++){
        int bit = (valor >> k) & 1;
        counter = (bit==1) ? ++counter : counter;
      }
      quadroControlado[i] |= (counter%2 != 0) ? 0 : (1<<8);
    }
    return quadroControlado;
  }// fim do metodo CamadaEnlaceDadosTransmissoraControledeErroBitParidadeImpar

  public int[] CamadaEnlaceDadosTransmissoraControleDeErroCRC(int quadro[]) {
    return quadro;
    // usar polinomio CRC-32(IEEE 802)
  }// fim do metodo CamadaEnlaceDadosTransmissoraControledeErroCRC

  public int[] CamadaEnlaceDadosTransmissoraControleDeErroCodigoDeHamming(int quadro[]) {
    return quadro;
  }// fim do metodo CamadaEnlaceDadosTransmissoraControleDeErroCodigoDehamming
  
  public void CamadaAcessoAoMeioTransmissora(int quadro[]) {
    int seletorMac = this.getSeletorMAC();
    switch (seletorMac) {
      case 0:
        CamadaAcessoAoMeioTransmissoraAlohaPuro(quadro);
      break;
      case 1:
        CamadaAcessoAoMeioTransmissoraSlottedAloha(quadro);
      break;
      case 2:
        CamadaAcessoAoMeioTransmissoraCsmaNaoPersistente(quadro);
      break;
      case 3:
        CamadaAcessoAoMeioTransmissoraCsmaPersistente(quadro);
      break;
      case 4:
        CamadaDeAcessoAoMeioTransmissoraCsmaPPersistente(quadro);
      break;
      case 5:
        CamadaDeAcessoAoMeioTransmissoraCsmaCD(quadro);
      break;
    }
  }// fim do metodo CamadaAcessoAoMeioTransmisora


  public int randomTimer(){
    Random randomic = new Random();
    int randomicNumber = randomic.nextInt(4);
    return (randomicNumber+1);
  }

  public int[] CamadaAcessoAoMeioTransmissoraAlohaPuro(int quadro []) {
    new Thread(() -> {
      showTransmitindo();//Imagem de transmissao
      System.out.println("Transmissor ["+this.getID()+"] fazendo primeiro envio");
      CamadaFisicaTransmissora(quadro);//Envia quadro
      try {
        sleep(1200); //Aguarda tempo de confirmacao
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      while (!isArrived) { //Caso nao receber a confirmacao, ou seja, houve colisao a faz retransmissao
        try {
          int timer = ((randomTimer()) * 1000);
          int timerInSeconds = (timer / 1000);
          System.out.println("\nTransmissor [" + this.getID() + "] aguardando temporizador de " + timerInSeconds + " segundos");
          sleep(timer); //Aguarda por tempo aleatorio
          showTransmitindo();//Imagem de transmissao
          System.out.println("\nTransmissor [" + this.getID() + "] reenviando");
          CamadaFisicaTransmissora(quadro);//Envia quadro
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        try {
          sleep(1200); //Aguarda confirmacao
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      isArrived = false; //Termino de transmissao
    }).start();
    return quadro;// envio do quadro
  }//fim do metodo CamadaAcessoAoMeioTransmisoraAlohaPuro

  public int[] CamadaAcessoAoMeioTransmissoraSlottedAloha (int quadro []) {
    new Thread(() -> {
      while((System.currentTimeMillis()%10000)%1000 > 0) { //Aguarda ate o tempo do slot chegar
        if((System.currentTimeMillis()%10000)%1000 == 0){// Em seu slot de tempo faz a transmissao
          showTransmitindo();//Imagem de transmissao
          System.out.println("Transmissor ["+this.getID()+"] fazendo primeiro envio");
          CamadaFisicaTransmissora(quadro);//Envia quadro
          try {
            sleep(1200);//Aguarda tempo de confirmacao
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          while (!isArrived) { //Caso nao receber a confirmacao, ou seja, houve colisao a faz retransmissao
            try {
              System.out.println("\nTransmissor [" + this.getID() + "] aguardando tempo do Slot de");
              sleep(((System.currentTimeMillis() % 10000) % 1000));//Aguarda seu slot de tempo
              showTransmitindo();//Imagem de transmissao
              System.out.println("\nTransmissor [" + this.getID() + "] reenviando");
              CamadaFisicaTransmissora(quadro);//Envia quadro
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          try {
              sleep(1200);//Aguarda confirmacao
          } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
        isArrived = false;//Termino de ciclo de transmissao
      }
    }).start();
    return quadro;// envio do quadro
  }//fim do metodo CamadaAcessoAoMeioTransmisoraSlottedAloha

  public int[] CamadaAcessoAoMeioTransmissoraCsmaNaoPersistente(int quadro []) {
    new Thread(() -> {
      while (csmaFlag){ //Ciclo do csma
        if(cD.getMeioTransmissao().isFree()){//Caso o meio estiver livre transmite
          cD.getMeioTransmissao().setFree(false); //Ocupa o meio
          showTransmitindo();//Imagem de transmissao
          System.out.println("Transmissor ["+this.getID()+"] fazendo primeiro envio");
          CamadaFisicaTransmissora(quadro);//Envia quadro
          try {
            sleep(1200);//Aguarda confirmacao
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          while (!isArrived) { //Caso nao receber a confirmacao, ou seja, houve colisao a faz retransmissao
            try {
              int timer = ((randomTimer()) * 1500);
              int timerInSeconds = (timer / 1500);
              System.out.println("\nTransmissor [" + this.getID() + "] aguardando temporizador de " + timerInSeconds + " segundos");
              sleep(timer); //Aguarda por tempo aleatorio
              if(cD.getMeioTransmissao().isFree()){//Se o meio estiver livre
                cD.getMeioTransmissao().setFree(false);//Ocupa o meio
                showTransmitindo();//Imagem de transmissao
                System.out.println("\nTransmissor [" + this.getID() + "] reenviando");
                CamadaFisicaTransmissora(quadro);//Envia quadro
                try {
                  sleep(1200);//Aguarda confirmacao
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          csmaFlag = false; //Encerra ciclo de espera/reenvio do csma
          isArrived = false; //Termino de ciclo de transmissao
        }else{ //Caso o meio estiver livre transmite, aguarda por tempo aleatorio
          int timer = ((randomTimer()) * 150);
          try {
            sleep(timer);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }).start();
    return quadro;
  }//fim do metodo CamadaAcessoAoMeioTransmisoraCsmaNaoPersistente

  public int[] CamadaAcessoAoMeioTransmissoraCsmaPersistente(int quadro []) {
    new Thread(() -> {
      while (csmaFlag) {//Ciclo do csma
        if(!(cD.getMeioTransmissao().isFree())){//Caso o meio estiver livre transmite
          try {
            sleep(150); //Aguarda tempo inicial de transmissao
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
           e.printStackTrace();
          }
        }else{
          cD.getMeioTransmissao().setFree(false); //Ocupa o meio
          showTransmitindo();//Imagem de transmissao
          System.out.println("Transmissor ["+this.getID()+"] fazendo primeiro envio");
          CamadaFisicaTransmissora(quadro);//Envia quadro
          try {
            sleep(1200);//Aguarda confirmacao
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          while (!isArrived) { //Caso nao receber a confirmacao, ou seja, houve colisao a faz retransmissao
            try {
              int timer = ((randomTimer()) * 1500);
              int timerInSeconds = (timer / 1500);
              System.out.println("\nTransmissor [" + this.getID() + "] aguardando temporizador de " + timerInSeconds + " segundos");
              sleep(timer); //Aguarda por tempo aleatorio
              while(!(cD.getMeioTransmissao().isFree())){ //Persiste ate em enviar ate o meio desocupar
                try {
                  sleep(50);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
              cD.getMeioTransmissao().setFree(false);//Ocupa o meio
              showTransmitindo();//Imagem de transmissao
              System.out.println("\nTransmissor [" + this.getID() + "] reenviando");
              CamadaFisicaTransmissora(quadro);//Envia quadro
              try {
                sleep(1200);//Aguarda confirmacao
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          csmaFlag = false;//Encerra ciclo de espera/reenvio do csma
          isArrived = false;//Termino de ciclo de transmissao
        }
      }
    }).start();
    return quadro;
  }//fim do metodo CamadaAcessoAoMeioTransmisoraCsmaPersistente

  
  public int[] CamadaDeAcessoAoMeioTransmissoraCsmaPPersistente (int quadro []) {
    new Thread(() -> {
      Random randomic = new Random();
      int p = randomic.nextInt(2);
      while (csmaFlag) {
        p = randomic.nextInt(2);//Geracao da probabilidade
        //Caso o meio estiver livre transmite e atender a probalilidade e slot de tempo
        if(cD.getMeioTransmissao().isFree() && ((System.currentTimeMillis()%10000)%1000 == 0) && (p == 1)){ 
          cD.getMeioTransmissao().setFree(false);
          showTransmitindo(); //Imagem de transmissao
          System.out.println("Transmissor ["+this.getID()+"] fazendo primeiro envio");
          CamadaFisicaTransmissora(quadro);//Envia quadro
          try {
            sleep(1200);//Aguarda confirmacao
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          while (!isArrived) { //Caso nao receber a confirmacao, ou seja, houve colisao a faz retransmissao
            int timer = ((randomTimer()) * 1500);
            int timerInSeconds = (timer / 1500);
            try {
              System.out.println("\nTransmissor [" + this.getID() + "] aguardando temporizador de " + timerInSeconds + " segundos");
              sleep(timer);
              p = randomic.nextInt(2);
              while(!(cD.getMeioTransmissao().isFree()) || ((System.currentTimeMillis()%10000)%1000 != 0) || !(p == 1)){
                p = randomic.nextInt(2);
              }
              cD.getMeioTransmissao().setFree(false);
              showTransmitindo(); //Imagem de transmissao
              System.out.println("\nTransmissor [" + this.getID() + "] reenviando");
              CamadaFisicaTransmissora(quadro);//Envia quadro
              try {
                sleep(1200);//Aguarda confirmacao
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          csmaFlag = false;//Encerra ciclo de espera/reenvio do csma
          isArrived = false;//Termino de ciclo de transmissao
        }else{
          int randomicNumber = randomic.nextInt(3);
          int timer = (randomicNumber+1);
          try {
            sleep(timer);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }).start();
    return quadro;
  }//fim do metodo CamadaAcessoAoMeioTransmisoraCsmaPPersistente
  
  public int[] CamadaDeAcessoAoMeioTransmissoraCsmaCD (int quadro []) {
    new Thread(() -> {
      while (csmaFlag) {
        if(cD.getMeioTransmissao().isFree()){
          if (isFreeCsmaCd) { //Verifica se houve colisao para
            cD.getMeioTransmissao().setFree(false);//Ocupa o meio
            showTransmitindo(); //Imagem de transmissao
            System.out.println("Transmissor ["+this.getID()+"] fazendo primeiro envio");
            CamadaFisicaTransmissora(quadro);//Envia quadro
            while (!isArrived) { //Caso nao receber a confirmacao, ou seja, houve colisao a faz retransmissao
              System.out.println("Retransmitindo ========================================");
              int timer = ((randomTimer()) * 1500);
              int timerInSeconds = (timer / 1500);
              try {
                System.out.println("\nTransmissor [" + this.getID() + "] aguardando temporizador de " + timerInSeconds + " segundos");
                sleep(timer); //Aguarda por tempo aleatorio
                while(!(cD.getMeioTransmissao().isFree())){ //Verifica se o meio fica ocupado
                  sleep(30);
                }
                cD.getMeioTransmissao().setFree(false);//Ocupa o meio
                showTransmitindo(); //Imagem de transmissao
                System.out.println("\nTransmissor [" + this.getID() + "] reenviando");
                CamadaFisicaTransmissora(quadro);//Envia quadro
                try {
                  sleep(1200); //Aguarda confirmacao
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }else{ //Verifica se houve colisao durante a passagem do meio, caso sim, faz a retransmissao
            System.out.println("Retransmitindo ========================================");
              int timer = ((randomTimer()) * 1500);
              int timerInSeconds = (timer / 1500);
              try {
                System.out.println("\nTransmissor [" + this.getID() + "] aguardando temporizador de " + timerInSeconds + " segundos");
                sleep(timer); //Aguarda por tempo aleatorio
                while(!(cD.getMeioTransmissao().isFree())){
                  sleep(30);
                }
                cD.getMeioTransmissao().setFree(false);//Ocupa o meio
                showTransmitindo(); //Imagem de transmissao
                System.out.println("\nTransmissor [" + this.getID() + "] reenviando");
                CamadaFisicaTransmissora(quadro);//Envia quadro
                try {
                  sleep(1200); //Aguarda confirmacao
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
          }
          csmaFlag = false;//Encerra ciclo de espera/reenvio do csma
          isArrived = false;//Termino de ciclo de transmissao
        }else{
          Random randomic = new Random();
          int randomicNumber = randomic.nextInt(3);
          int timer = (randomicNumber+1);
          try {
            sleep(timer*500); //Aguarda por tempo aleatorio
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }).start();
    return quadro;
  }//fim do metodo CamadaAcessoAoMeioTransmisoraCsmaCD 

  
  public void CamadaFisicaTransmissora(int quadro[]) {
    int tipoDeCodificacao = this.getSeletorValue(); // alterar de acordo o teste
    int fluxoBrutoDeBits[] = {};
    switch (tipoDeCodificacao) {
      case 0: //Chama a codificao binaria
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoBinaria(quadro);
        signal = signalBin;
        break;
      case 1: //Chama a codificacao manchester
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchester(quadro);
        signal = signalManc;
        break;
      case 2: //Chama a codificacao manchester diferencial
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
        signal = signalDiffManc;
        break;
      }// fim do switch/case
      if (macSelector == 5) {
        cD.getMeioTransmissao().MeioDeComunicacaoCD(fluxoBrutoDeBits, cD, signal, seletor, this);
      }else{
        cD.getMeioTransmissao().MeioDeComunicacao(fluxoBrutoDeBits, cD, signal,seletor,this);
      }
  }// fim do metodo CamadaFisicaTransmissora
  
  public int[] CamadaFisicaTransmissoraCodificacaoBinaria(int[] quadro) {
    signalBin = new StringBuilder(); //Estrutura do sinal
    int[] binary = new int[quadro.length]; //Cria array com mesmo tamanho
    int currentBit; //Bit atual
    int shift = 0;//Varivael que ira auxiliar no deslocamento e insercao de bits
    for (int i = 0; i < quadro.length; i++) {//Percorre o array original
       int valor = quadro[i];//Aramazena oconteudo do index
       for(int k = 0; k < 16; k++){//Percorre o indice do array codificado
        //Realiza o deslocamento a direita em k posicoes e captura o bit, por meio do operador AND
        currentBit = (valor >> k) & 1;
        signalBin = (currentBit==1) ? signalBin.append(1) : signalBin.append(0); //Passando o bit para o sinal
        /*Quando o deslocamento for 32 o indice da codificacao esta completamente preenchido 
        entao passa para o proximo indice e retorna o auxiliar de deslocamento para 0*/
        if (shift == 16) {
          shift = 0;
        }
        binary[i] |= currentBit << shift;//Passagem bit a bit para o array de codificacao
        shift++;
      }
    }
    signalBin = signalBin.reverse();//Reverte a string para ficar na ordem certa
    return binary;
  }

  public int[] CamadaFisicaTransmissoraCodificacaoManchester(int[] quadro) {
    signalManc = new StringBuilder(); //Estrutura do sinal
    int[] manchester = new int[quadro.length * 2];//Cria array com o dobro do tamanho, ja que armazenara o bit e sua codificacao corespondente
    int index = 0; //indice para manchesterEncode
    int shift = 0; //Varivael que ira auxiliar no deslocamento e insercao de bits
    //Percorre cada indice de array do quadro
    for (int i = 0; i < quadro.length; i++) {
      int valor = quadro[i]; // armazena o conteudo do quadro no indice quadroIndex
      
      //Cada iteracao, codifica 1 index do quadro preenchendo 2 index do manchester, pois codificado cada indice so pode armazenar 2 letras
      for (int k = 0; k < 32; k++) {
        //Realiza o deslocamento a direita em k posicoes e captura o bit, por meio do operador AND
        int currentBit = (valor >> k) & 1;
        /*Quando o deslocamento for 32 o indice da codificacao esta completamente preenchido 
        entao passa para o proximo indice e retorna o auxiliar de deslocamento para 0*/
        if (shift == 32) {
          index++;
          shift = 0;
        }
        //Realiza a insercao bit a bit da codificacao, em que se o bit atual for 1, eh insire 1 e 0, caso for 0, insere 0 e 1
        manchester[index] |= (currentBit == 1) ? encode(manchester[index], shift, 1, 0): encode(manchester[index], shift, 0, 1);
        signalManc = (currentBit==1) ? signalManc.append(10) : signalManc.append(01); //Passando o bit para o sinal
        shift += 2;//Incrementa o deslocamento em 2, ja que 2 posicoes ja foram inseridas com a codificacao
      }
    }
    signalManc = signalManc.reverse();//Reverte a string para ficar na ordem certa
    //retorna array codificado
    return manchester;
  }
  
  public int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(int[] quadro) {
    signalDiffManc = new StringBuilder(); // Estrutura do sinal
    int[] differentialManchester = new int[quadro.length * 2];// Cria array com o dobro do tamanho, ja que armazenara o
                                                              // bit e sua codificacao corespondente
    int index = 0; // indice para manchesterEncode
    int shift = 0; // Varivael que ira auxiliar no deslocamento e insercao de bits
    int encodedBit = 0;// Armazena o valor do bit anterior, para auxiliar na codificacao Manchester
                       // Diferencial, inicia Alto Baixo

    // Percorre cada indice de array do quadro
    for (int i = 0; i < quadro.length; i++) {
      int currentBit;// Variavel para armazenar o bit atual
      int valor = quadro[i]; // armazena o conteudo do quadro no indice quadroIndex
      // Cada iteracao, codifica 1 index do quadro preenchendo 2 index do manchester,
      // pois codificado cada indice so pode armazenar 2 letras
      for (int k = 0; k < 32; k++) {
        /*
         * Quando o deslocamento for 32 o indice da codificacao esta completamente
         * preenchido
         * entao passa para o proximo indice e retorna o auxiliar de deslocamento para 0
         */
        if (shift == 32) {
          index++;
          shift = 0;
        }
        currentBit = (valor >> k) & 1; // Realiza o deslocamento a direita em k posicoes e captura o bit, por meio do
                                       // operador AND
        if (currentBit == 1 || currentBit == -1) {// Em bit = 1 ocorre a alteracao no bit de codificacao
          if (currentBit == encodedBit) {// Caso for igual
            differentialManchester[index] = encode(differentialManchester[index], shift, 0, 1);// Codifica 01
            signalDiffManc.append(01);// Adiciona a codificacao ao sinal
            encodedBit = 0;// Atualiza o bit de codificacao
          } else {// Caso nao
            differentialManchester[index] = encode(differentialManchester[index], shift, 1, 0);// Codifica 10
            signalDiffManc.append(10);// Adiciona a codificacao ao sinal
            encodedBit = 1;// Atualiza o bit de codificacao
          }
        } else {// Caso o bit for 0 q o de codificacao for 1 codifica 10, caso nao codifica 01
          differentialManchester[index] |= (encodedBit == 1) ? encode(differentialManchester[index], shift, 1, 0)
              : encode(differentialManchester[index], shift, 0, 1);
          signalDiffManc = (currentBit == 1) ? signalDiffManc.append(10) : signalDiffManc.append(01); // Adiciona a
                                                                                                      // codificacao ao
                                                                                                      // sinal
        }
        // Realiza a insercao bit a bit da codificacao, se caso o bit atual for igual a
        // 0 ocorre transicao
        shift += 2;// Incrementa o deslocamento em 2, ja que 2 posicoes ja foram inseridas com a
                   // codificacao
      }
    }
    signalDiffManc = signalDiffManc.reverse();// Reverte a string para ficar na ordem certa
    // retorna array codificado
    return differentialManchester;
  }

  public int encode(int index, int shift, int bit, int encode){
    //Realiza o deslocamento a esquerda em deslocation posisoes, insere o bit e adiciona ao indice atraves do operador | 
    index |= (bit << shift); 
    //Realiza o deslocamento a esquerda em deslocation + 1 posisoes, insere a codificacao e adiciona ao indice atraves do operador |
    index |= (encode << shift+1); 
    return index;
  }
  
  public void run(){
    this.AplicacaoTransmissora(randomMessage());
  }
}
