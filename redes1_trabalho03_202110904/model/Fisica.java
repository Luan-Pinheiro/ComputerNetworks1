/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 3/11/2023
* Ultima alteracao.: 5/11/2023
* Nome.............: Fisica.java
* Funcao...........: Classe responsavel por emitir os dados e sinais, realizar a codificacao e decodificao, desempenha o papel da camada fisica
*************************************************************** */

package model;

//Importacoes
import controller.ControleDados;

public class Fisica {//Classe transmissor
  private String typeEncode;//Tipo de codificacao
  private int seletor;//Seletor para tipo de codificacao
  ControleDados cD;
  MeioTransmissao mt = new MeioTransmissao();
  StringBuilder signalBin; //Sinais da codificacao bit a bit binario
  StringBuilder signalManc; //Sinais da codificacao bit a bit Manchester
  StringBuilder signalDiffManc; //Sinais da codificacao bit a bit Manchester Diferencial
  //private final int[] HEADER = {0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0};

  public Fisica(ControleDados cD) {//Referencia no construtor
    this.cD = cD;
  }

//--------------------------------------------- CODIFICAR --------------------------------------------- //
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
          System.out.println("Nao selecionado, padrao definido: 'Codificacao Binaria'");
          seletor = 0;
          break;
      }
    }else{
      System.out.println("Nao selecionado, padrao definido: 'Codificacao Binaria'");
      seletor = 0;
    }
    return seletor;
  }

  
  public void CamadaFisicaTransmissora(int quadro[]) {
    int tipoDeCodificacao = this.getSeletorValue(); // alterar de acordo o teste
    int fluxoBrutoDeBits[] = {};
    switch (tipoDeCodificacao) {
      case 0: //Chama a codificao binaria
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoBinaria(quadro);
        mt.MeioDeComunicacao(fluxoBrutoDeBits, cD, signalBin,seletor);
        break;
      case 1: //Chama a codificacao manchester
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchester(quadro);
        mt.MeioDeComunicacao(fluxoBrutoDeBits, cD, signalManc,seletor);
        break;
      case 2: //Chama a codificacao manchester diferencial
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
        mt.MeioDeComunicacao(fluxoBrutoDeBits, cD, signalDiffManc,seletor);
        break;
    }// fim do switch/case
  }// fim do metodo CamadaFisicaTransmissora
  
  public int[] CamadaFisicaTransmissoraCodificacaoBinaria(int[] quadro) {
    signalBin = new StringBuilder(); //Estrutura do sinal
    int[] binary = new int[quadro.length]; //Cria array com mesmo tamanho
    int currentBit; //Bit atual
    int shift = 0;//Varivael que ira auxiliar no deslocamento e insercao de bits
    for (int i = 0; i < quadro.length; i++) {//Percorre o array original
       int valor = quadro[i];//Aramazena oconteudo do index
       for(int k = 0; k < 32; k++){//Percorre o indice do array codificado
        //Realiza o deslocamento a direita em k posicoes e captura o bit, por meio do operador AND
        currentBit = (valor >> k) & 1;
        signalBin = (currentBit==1) ? signalBin.append(1) : signalBin.append(0); //Passando o bit para o sinal
        /*Quando o deslocamento for 32 o indice da codificacao esta completamente preenchido 
        entao passa para o proximo indice e retorna o auxiliar de deslocamento para 0*/
        if (shift == 32) {
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

  //--------------------------------------------- DECODIFICAR --------------------------------------------- //
  public void CamadaFisicaReceptora(int quadro[], ControleDados cD) {
    this.cD = cD;
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
    Enlace camadaEnlace= new Enlace(cD);
    camadaEnlace.CamadaEnlaceDadosReceptora(fluxoBrutoDeBits);
  }// fim do metodo CamadaFisicaTransmissora

  //Decodificacao
  public int[] CamadaFisicaReceptoraDecodificacaoBinaria(int[] quadro) {
    int[] decoded = new int[quadro.length]; //Cria array com mesmo tamanho
    int currentBit; //Bit atual
    int shift = 0;//Varivael que ira auxiliar no deslocamento e insercao de bits
    for (int i = 0; i < quadro.length; i++) {
       int valor = quadro[i];
       for(int k = 0; k < 32; k++){
        //Realiza o deslocamento a direita em k posicoes e captura o bit, por meio do operador AND
        currentBit = (valor >> k) & 1; 
        if (shift == 32) {
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

}