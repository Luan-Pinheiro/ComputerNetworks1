/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 1/12/2023
* Ultima alteracao.: 5/12/2023
* Nome.............: Receptor.java
* Funcao...........: Classe responsavel por simular o dispositivo receptor, tal qual sua recepcao de dados
*************************************************************** */

package model.DispositivoReceptor;

import java.io.IOException;

//Importacoes
import controller.ControleDados;
import javafx.application.Platform;
import model.MeioTransmissao;
import model.DispositivoTransmissor.Transmissor;

public class Receptor {//Classe receptora
  private String typeEnquad;//Tipo de enquadramento
  private String typeErrorControl;//Tipo de controle de erros
  private int enquadSelector;//Seletor para tipo de enquadramento
  private int errorControlSelector;//Seletor para tipo de controle de erros
  private String typeEncode;//Tipo de codificacao
  private int seletor;//Seletor para tipo de codificacao
  private String typeAccess;
  private int macSelector;
  private static Transmissor transmissor;
  private MeioTransmissao mt;
  ControleDados cD; //Classe controladora
  StringBuilder signalBin; //Sinais da codificacao bit a bit binario
  StringBuilder signalManc; //Sinais da codificacao bit a bit Manchester
  StringBuilder signalDiffManc; //Sinais da codificacao bit a bit Manchester Diferencial
  
  public Receptor(MeioTransmissao mt,Transmissor trs) {//Referencia no construtor
    this.mt = mt;
    this.cD = mt.getCd();
    transmissor = trs;
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

  void CamadaAcessoAoMeioReceptora (int quadro []) {
    int seletorMac = this.getSeletorMAC();
    switch (seletorMac) {
      case 0:
        CamadaAcessoAoMeioReceptoraAlohaPuro(quadro);
      break;
      case 1:
        CamadaAcessoAoMeioReceptoraSlottedAloha(quadro);
      break;
      case 2:
        CamadaAcessoAoMeioReceptoraCsmaNaoPersistente(quadro);
      break;
      case 3:
        CamadaAcessoAoMeioReceptoraCsmaPersistente(quadro);
      break;
      case 4:
        CamadaAcessoAoMeioReceptoraCsmaPPersistente(quadro);
      break;
      case 5:
        CamadaAcessoAoMeioReceptoraCsmaCD(quadro);
      break;
    }
  }//fim do metodo CamadaAcessoAoMeioDadosReceptora

  public void CamadaAcessoAoMeioReceptoraAlohaPuro (int quadro []) {
    
    if(transmissor.getColidiu()){//Verificacao de colisao no meio
      transmissor.showColisao(); // Imagem de colisao
      transmissor.setColidiu(false); // Reinicia variavel de verificacao de colisao
      System.out.println("\nHouve Colisao no Transmissor[" + transmissor.getID()+"]");
    }else{
      transmissor.setArrived(true); //Envia confimarcao de chegada correta na transmissao
      CamadaEnlaceDadosReceptora(quadro);//Passa para proxima camada
      System.out.println("\nConfimarcao de chegada do quadro do Transmissor[" + transmissor.getID()+"]");
      System.out.println("\n******************************************************************");
    }
  }//fim do metodo CamadaAcessoAoMeioReceptoraAlohaPuro

  public void CamadaAcessoAoMeioReceptoraSlottedAloha (int quadro []) {
    if(transmissor.getColidiu()){//Verificacao de colisao no meio
      transmissor.showColisao();// Imagem de colisao
      transmissor.setColidiu(false);// Reinicia variavel de verificacao de colisao
      System.out.println("\nHouve Colisao no Transmissor[" + transmissor.getID()+"]");
      System.out.println("\n******************************************************************");
    }else{
      transmissor.setArrived(true);//Envia confimarcao de chegada correta na transmissao
      CamadaEnlaceDadosReceptora(quadro);//Passa para proxima camada
      System.out.println("\nConfimarcao de chegada do quadro do Transmissor[" + transmissor.getID()+"]");
      System.out.println("\n******************************************************************");
    }
  }//fim do metodo CamadaAcessoAoMeioReceptoraSlottedAloha 

  public int[] CamadaAcessoAoMeioReceptoraCsmaNaoPersistente (int quadro []) {
    if(transmissor.getColidiu()){//Verificacao de colisao no meio
      transmissor.showColisao();// Imagem de colisao
      transmissor.setColidiu(false);// Reinicia variavel de verificacao de colisao
      System.out.println("\nHouve Colisao no Transmissor[" + transmissor.getID()+"]");
    }else{
      transmissor.setArrived(true);//Envia confimarcao de chegada correta na transmissao
      CamadaEnlaceDadosReceptora(quadro);//Passa para proxima camada
      System.out.println("\nConfimarcao de chegada do quadro do Transmissor[" + transmissor.getID()+"]");
      System.out.println("\n******************************************************************");
    }
    cD.getMeioTransmissao().setFree(true); //Liberacao do meio
    return quadro;
  }//fim do metodo CamadaAcessoAoMeioReceptoraCsmaNaoPersistente

  public int[] CamadaAcessoAoMeioReceptoraCsmaPersistente (int quadro []) {
    if(transmissor.getColidiu()){//Verificacao de colisao no meio
      transmissor.showColisao();// Imagem de colisao
      transmissor.setColidiu(false);// Reinicia variavel de verificacao de colisao
      System.out.println("\nHouve Colisao no Transmissor[" + transmissor.getID()+"]");
    }else{
      transmissor.setArrived(true);//Envia confimarcao de chegada correta na transmissao
      CamadaEnlaceDadosReceptora(quadro);//Passa para proxima camada
      System.out.println("\nConfimarcao de chegada do quadro do Transmissor[" + transmissor.getID()+"]");
      System.out.println("\n******************************************************************");
    }
    cD.getMeioTransmissao().setFree(true); //Liberacao do meio
    return quadro;
  }//fim do metodo CamadaAcessoAoMeioReceptoraCsmaPersistente

  public int[] CamadaAcessoAoMeioReceptoraCsmaPPersistente (int quadro []) {
    if(transmissor.getColidiu()){//Verificacao de colisao no meio
      transmissor.showColisao();// Imagem de colisao
      transmissor.setColidiu(false);// Reinicia variavel de verificacao de colisao
      System.out.println("\nHouve Colisao no Transmissor[" + transmissor.getID()+"]");
    }else{
      transmissor.setArrived(true);//Envia confimarcao de chegada correta na transmissao
      CamadaEnlaceDadosReceptora(quadro);//Passa para proxima camada
      System.out.println("\nConfimarcao de chegada do quadro do Transmissor[" + transmissor.getID()+"]");
      System.out.println("\n******************************************************************");
    }
    cD.getMeioTransmissao().setFree(true); //Liberacao do meio
    return quadro;
  }//fim do metodo CamadaAcessoAoMeioReceptoraCsmaPPersistente
    
  public int[] CamadaAcessoAoMeioReceptoraCsmaCD (int quadro []) {
    if(transmissor.getColidiu()){//Verificacao de colisao no meio
      transmissor.showColisao();// Imagem de colisao
      transmissor.setColidiu(false);// Reinicia variavel de verificacao de colisao
      System.out.println("\nHouve Colisao no Transmissor[" + transmissor.getID()+"]");
    }else{
      transmissor.setArrived(true);//Envia confimarcao de chegada correta na transmissao
      transmissor.setFreeCsmaCd(true);////Envia confimarcao de chegada correta para o verificador do Csma Cd
      CamadaEnlaceDadosReceptora(quadro);//Passa para proxima camada
      System.out.println("\nConfimarcao de chegada do quadro do Transmissor[" + transmissor.getID()+"]");
      System.out.println("\n******************************************************************");
    }
    cD.getMeioTransmissao().setFree(true); //Liberacao do meio
   return quadro;
  }//fim do metodo CamadaAcessoAoMeioReceptoraCsmaCD 

  public void CamadaFisicaReceptora(int quadro[], ControleDados cD) {
    this.cD = cD;
    transmissor.hideTransmitindo();
    transmissor.hideColisao();
    int tipoDeDecodificacao = this.getSeletorValue();
    int fluxoBrutoDeBits[] = {};
    switch (tipoDeDecodificacao) {
      case 0: // codificao binaria
        fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoBinaria(quadro);
        break;
      case 1: // codificacao manchester
        fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchester(quadro);
        break;
      case 2: // codificacao manchester diferencial
        fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(quadro);
        break;
    }// fim do switch/case
    // chama proxima camada
    CamadaAcessoAoMeioReceptora(fluxoBrutoDeBits);
  }// fim do metodo CamadaFisicaTransmissora

  //Decodificacao
  public int[] CamadaFisicaReceptoraDecodificacaoBinaria(int[] quadro) {
    int[] decoded = new int[quadro.length]; //Cria array com mesmo tamanho
    int currentBit; //Bit atual
    int shift = 0;//Varivael que ira auxiliar no deslocamento e insercao de bits
    for (int i = 0; i < quadro.length; i++) {
       int valor = quadro[i];
       for(int k = 0; k < 16; k++){
        //Realiza o deslocamento a direita em k posicoes e captura o bit, por meio do operador AND
        currentBit = (valor >> k) & 1; 
        if (shift == 16) {
          shift = 0;
        }
        decoded[i] |= currentBit << shift;
        shift++;
      }
    }
    return decoded;
  }

  public int[] CamadaFisicaReceptoraDecodificacaoManchester(int[] quadroManchester) { 
    int[] decoded = new int[quadroManchester.length/2];
    int encodedIndex = 0; // indice para quadroDecodificado
    int shift = 0; //Varivael que ira auxiliar no deslocamento e insercao de bits
    int indexContent; // conteudo do indice do array codificado

    for (int index = 0; index < decoded.length; index++) {//Percorre o array decodificado
      for (int k = 0; k < 64; k+=2) {//Percorre dois indices do array codificado, com o iterador de 2 em 2, para soh considerar o bit principal
        //Caso o k for maior que 32, siginifica quem um indice do array codificado foi percorrido, entao amazena o conteudo do proximo indice
        indexContent = (k > 31) ? quadroManchester[encodedIndex+1] :  quadroManchester[encodedIndex];
        //Obtem o bit apos deslocar a direita k posicoes e operar com AND 1
        int bit = (indexContent >> k) & 1;
        //Volta o deslocamento para 0, para auxiliar no bit a bit do proximo indice  
        if (shift == 32) {shift = 0;}
        //Realiza a decodificacao, em que se o bit atual for 1, eh insire 1, caso for 0, insere 0 
        decoded[index] |= (bit == 1) ? decode(decoded[index], shift, 1) : decode(decoded[index], shift, 0);
        //Incrementa o deslocamento
        shift++;
      }
      //Incrementa o indice do array codificado de 2 em 2, ja que no for mais interno, trabalha-se 2 index deste array
      encodedIndex+=2;
    }
    //retorna array decodificado
    return decoded;
  }

  public int[] CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(int[] quadroDiferencial) {
    int[] decoded = new int[quadroDiferencial.length / 2];
    int indexContent; // conteudo do indice do array codificado
    int encodedIndex = 0; // indice para quadroDecodificado
    int shift = 0; // VariÃ¡vel que auxiliara no deslocamento e leitura de bits
    String bitAux = "10";//Auxiliar inicial
    boolean flag = true; //Auxiliar para controlar a troca
    for (int index = 0; index < decoded.length; index++) {//Percorre o array decodificado
      for (int k = 0; k < 64 ;k+=2) {//Percorre dois indices do array codificado, com o iterador de 2 em 2, para soh considerar o bit principal
        //Caso o k for maior que 32, siginifica quem um indice do array codificado foi percorrido, entao amazena o conteudo do proximo indice
        indexContent = (k > 31) ?  quadroDiferencial[encodedIndex+1] : quadroDiferencial[encodedIndex];
        if (shift == 32) {shift = 0;}

        int currentBit = ((indexContent >> (k)) & 1);
        int bit2 = ((indexContent >> (k+1)) & 1);
        StringBuilder stringVerif = new StringBuilder();
        stringVerif.append(currentBit);
        stringVerif.append(bit2);

        if ((stringVerif.toString().equals(bitAux))){
          if(flag){
						decoded[index] |= decode(decoded[index], shift, 1);// insere o bit 1
						bitAux = stringVerif.toString();	
            flag = false;
					}
        }
        else{
					if(!flag){
						decoded[index] |= decode(decoded[index], shift, 1); //insere o bit 1
						bitAux = stringVerif.toString();//Atualiza a string auxiliar
					}
        }
        //Atualiza o bit anterior extraindo o bit menos signifcativo do array de codificacao diferencial
        shift++;
      }
      //Incrementa o indice do array codificado de 2 em 2, ja que no for mais interno, trabalha-se 2 index deste array
      encodedIndex+=2;
    }
    //retorna o array decodificado
    return decoded;
  }


  public int decode(int index, int deslocation, int bit){
    //Realiza o deslocamento a esquerda em deslocation posisoes, insere o bit e adiciona ao indice atraves do operador | 
    return index |= (bit << deslocation);
  }

  //--------------------------------------------- DESENQUADRAR ---------------------------------------------//
  public void CamadaEnlaceDadosReceptora(int quadro[]) {
    CamadaEnlaceDadosReceptoraEnquadramento(quadro);
  }// fim do metodo CamadaEnlaceDadosReceptora

  public void CamadaEnlaceDadosReceptoraEnquadramento(int quadro[]) {
    int tipoDeEnquadramento = this.getSeletorEnquadramentoValue(); // alterar de acordo com o teste
    int quadroDesenquadrado[] = {};
    switch (tipoDeEnquadramento) {
      case 0:
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres(quadro);
        break;
      case 1:
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes(quadro);
        break;
      case 2:
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBits(quadro);
        break;
      case 3:
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraEnquadramentoViolacaoDaCamadaFisica(quadro);
        break;
    }
    CamadaEnlaceDadosReceptoraControleDeErro(quadroDesenquadrado);
  }

  public int[] CamadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres(int quadro[]) {
    return quadro;
    // implementacao do algoritmo para DESENQUADRAR
  }// fim do metodo CamadaEnlaceDadosReceptoraContagemDeCaracteres

  public int[] CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes(int quadro[]) {
    return quadro;
    // implementacao do algoritmo para DESENQUADRAR
  }// fim do metodo CamadaEnlaceDadosReceptoraInsercaoDeBytes

  public int[] CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBits(int quadro[]) {
    return quadro;
    // implementacao do algoritmo para DESENQUADRAR
  }// fim do metodo CamadaEnlaceDadosReceptoraInsercaoDeBits

  public int[] CamadaEnlaceDadosReceptoraEnquadramentoViolacaoDaCamadaFisica(int quadro[]) {
    return quadro;
    // implementacao do algoritmo para DESENQUADRAR
  }// fim do metodo CamadaEnlaceDadosReceptoraViolacaoDaCamadaFisica

  public void CamadaEnlaceDadosReceptoraControleDeErro(int quadro[]) {
    int tipoDeControleDeErro = getSeletorControleErroValue(); // alterar de acordo com o teste
    int quadroDesenquadrado [] = {};
    switch (tipoDeControleDeErro) {
      case 0: // bit de paridade par
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadePar(quadro);
        break;
      case 1: // bit de paridade impar
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadeImpar(quadro);
        break;
      case 2: // CRC
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraControleDeErroCRC(quadro);
        break;
      case 3: // codigo de hamming
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraControleDeErroCodigoDeHamming(quadro);
        break;
    }// fim do switch/case
    // chama proxima camada
    CamadaDeAplicacaoReceptora(quadroDesenquadrado);
  }// fim do metodo CamadaEnlaceDadosReceptoraControleDeErro

  public int[] CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadePar (int quadro []) {
    int[] quadroControlado = {};
    quadroControlado = new int[quadro.length];
    boolean flag = false;
    for(int i = 0; i < quadro.length; i++){
      int counter = 0;
      int valor = quadro[i];
      for(int k = 0; k < 9; k++){
        //Obtendo bit atual, verificando e incrementando contador
        int bit = (valor >> k) & 1;
        counter = (bit==1) ? ++counter : counter;
      }
      for(int x = 0; x < 8; x++){
        //Insercao dos bit no quadro
        int bit = (valor >> x) & 1;
        quadroControlado[i] |= (bit<<x);
      }
      if(counter%2!=0){
        //flag de erro (contador eh impar)
        flag = true;
      }
    }
    
    if(flag){
      Platform.runLater(()->{
        try {
          //dispara tela de erro
          cD.startErrorAlert();
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    }
    
    return quadroControlado;
   }//fim do metodo CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadePar
   
  public int[] CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadeImpar (int quadro []) {
    int[] quadroControlado = new int[quadro.length];
    boolean flag = false;
    for(int i = 0; i < quadro.length; i++){
      int counter = 0;
      int valor = quadro[i];
      for(int k = 0; k < 9; k++){
        //Obtendo bit atual, verificando e incrementando contador
        int bit = (valor >> k) & 1;
        counter = (bit==1) ? ++counter : counter;
      }
      for(int x = 0; x < 8; x++){
        //Insercao dos bit no quadro
        int bit = (valor >> x) & 1;
        quadroControlado[i] |= (bit<<x);
      }
      if(counter%2==0){
        //flag de erro (contador eh par)
        flag = true;
      }
    }
    
    if(flag){
      Platform.runLater(()->{
        try {
          //dispara tela de erro
          cD.startErrorAlert();
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    }
    
    return quadroControlado;
   }//fim do metodo CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadeImpar
  
  public int[] CamadaEnlaceDadosReceptoraControleDeErroCRC (int quadro []) {
    return quadro;
    //implementacao do algoritmo para VERIFICAR SE HOUVE ERRO
    //usar polinomio CRC-32(IEEE 802)
   }//fim do metodo CamadaEnlaceDadosReceptoraControleDeErroCRC
  
  public int[] CamadaEnlaceDadosReceptoraControleDeErroCodigoDeHamming (int quadro []) {
    return quadro;
    //implementacao do algoritmo para VERIFICAR SE HOUVE ERRO
  }//fim do metodo CamadaEnlaceDadosReceptoraControleDeErroCodigoDeHamming

  private static String get32bit(int quadroIndex) {
    String in32bits = "";
    int bit = 1;
    for (int i = 0; i < 32; i++) {
      // Operando bit a bit e passando valor para a string
      in32bits = ((bit & quadroIndex) != 0 ? "1" : "0") + in32bits;
      // Deslocando uma casa a esquerda, para que seja possivel compara o proximo bit
      // equivale dizer que esta mutiplicando por 2
      bit <<= 1;
    }
    return in32bits;
  }

  public static String[] get8bits(String indice) {
    String[] resultado = new String[4];//Divide o tamanho de 32 bits do indice em 4 substrings de 8bits

    for (int i = 0; i < 4; i++) {
      int inicioSubString = i * 8; //variavel de inicio da substring
      int fimSubString = inicioSubString + 8;//variavel de fim da substring

      //Verifica se o fimSubString nao ultrapassa o tamanho da string
      if (fimSubString <= indice.length()) {
        resultado[i] = indice.substring(inicioSubString, fimSubString);
      } else {
      //Caso ultrapasse o limite eh adicionado o resto
        resultado[i] = indice.substring(inicioSubString);
      }
    }
    return resultado;
  }

  public void CamadaDeAplicacaoReceptora(int[] quadro) {
    //String que vai armazenar os valores de 8 em 8 bits, o que permitira a conversao
    String[] resultado = new String[4];
    //Int que permitira o casting de char, para o inteiro do ascii correspondente ao termo
    int valorAscii = 0;
    //Char que permitira o casting de inteiro, em decimal, para o termo correspondete
    char caractere;
    //Armazenara o valor convertido de binario para o texto de saida
    String mensagem = "";

    for (int i = 0; i < quadro.length; i++) {
      //Auxiliar
      String conteudoIndex = "";
      //Recebe o condeudo do indice do array de int que contem 32 bits
      conteudoIndex += get32bit(quadro[i]);
      //Divide a string e quatro partes, para ser possivel a conversao de termo a termo
      resultado = get8bits(conteudoIndex);
      for (int k = 0; k < 4; k++) {
        //Valor ascii dos correspondente em binario
        valorAscii = Integer.parseInt(resultado[k], 2);
        //Caractere corresponde ao valor ascii
        caractere = (char) valorAscii;
        //Adciona o termo a mensagem final
        mensagem += caractere;
      }
    }
    //Chama o proximo metodo
    AplicacaoReceptora(mensagem);
  }

  public void AplicacaoReceptora(String mensagem) {
    Platform.runLater(() -> {
      cD.setTxtTv(mensagem+"\n");
  });
  }

}
