/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 23/08/2023
* Ultima alteracao.: 13/09/2023
* Nome.............: Transmissor.java
* Funcao...........: Classe responsavel por emitir os dados e sinais, realizar a selecao da Camada fisica transmissora
                     bem como sua codificacao
*************************************************************** */

package model;

//Importacoes
import controller.ControleDados;
import javafx.scene.control.TextArea;

public class Transmissor {//Classe transmissor

  private String typeEncode;//Tipo de codificacao
  private int seletor;//Seletor para tipo de codificacao
  ControleDados cD;
  MeioTransmissao mt = new MeioTransmissao();
  StringBuilder signalBin; //Sinais da codificacao bit a bit binario
  StringBuilder signalManc; //Sinais da codificacao bit a bit Manchester
  StringBuilder signalDiffManc; //Sinais da codificacao bit a bit Manchester Diferencial

  public Transmissor(ControleDados cD) {//Referencia no construtor
    this.cD = cD;
  }
  
  public int getSeletorValue() {//Selecao do tipo de decodificacao
    typeEncode = cD.getSeletor();
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
        System.out.println("Não foi possivel atribuir");
        break;
    }
    return seletor;
  }

  private static int[] getBinaryValue(String value, TextArea telaTransmissao) {
    //Array de caracteres, cada indice corresponde a um char da String inicial
    char[] caractere = value.toCharArray();
    //Valor ascii do termo
    int asciiValue;
    //Auxiliar que armazena o ascii
    int asciiValueaux;
    //Valor final inicialmente inicializado com zero
    int inteiro = caractere.length / 4; //parte inteira da divisao
    int quebrado = caractere.length % 4; //resto da divisao
    int tamanho = inteiro + (quebrado > 0 ? 1 : 0);// definindo o tamanho exato, nivelando por cima
    int convertedValue[] = new int[tamanho];// Tamanho do array de int
    //indice do array de inteiros, que armazenara 32 bits por completo em cada indice, com a conversao ficarao 4 letras por posicao no array de int
    int index = 0;
    //armazenara os valores de cada letra binario/ascii
    String charValues = "----------------   VALORES  ----------------\n";
    //titulo da parte que mostrara o binario completo                                                                     
    String binCompleto = "----------- BINARIO COMPLETO -----------\n"; 
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
      charValues += "Binario de '" + caractere[i] + "': " + binaryValue + "\n" + "ASCII de '" + caractere[i] + "': "
          + asciiValueaux + "\n\n";
      //exibicao de string termo a termo na tela
      telaTransmissao.setText(charValues);

      for (int bitAtual = 0; bitAtual < 8; bitAtual++) {
        //Verifica qual eh o valor do bit naquela posicao e atribui a indexBitValue
        int indexBitValue = binaryValue.charAt(bitAtual) == '1' ? 1 : 0;
        // Realiza deslocamento a esquerda dos bits convertedValue e combina com o o bit e indexBitValue com o operador OR
        convertedValue[index] = (convertedValue[index] << 1) | indexBitValue;
      }
      // Verificacao de incrementacao do array de int
      if (i % 4 == 3)
        index++;
      // Realiza a quebra de linha a cada 32 bits
      if (contador > 0 && contador % 32 == 0)
        allStringBinaryValue += "\n";
      // Caso seja ultima iteracao printa na tela do transmissor o binario completo referente ao texto inserido
      if (i == caractere.length - 1)
        telaTransmissao.setText(charValues + binCompleto + allStringBinaryValue);
    }
    //retorna o valor
    return convertedValue;
  }

  public void AplicacaoTransmissora(String mensagem) {
    CamadaDeAplicacaoTransmissora(mensagem);//Proximo metodo
  }

  public void CamadaDeAplicacaoTransmissora(String mensagem) {
    int quadro[] = getBinaryValue(mensagem, cD.getTxtATransmissor());
    CamadaFisicaTransmissora(quadro);//Proximo metodo
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
  signalDiffManc = new StringBuilder(); //Estrutura do sinal
  int[] differentialManchester = new int[quadro.length * 2];//Cria array com o dobro do tamanho, ja que armazenara o bit e sua codificacao corespondente
  int index = 0; //indice para manchesterEncode
  int shift = 0; //Varivael que ira auxiliar no deslocamento e insercao de bits
  int encodedBit= 0;//Armazena o valor do bit anterior, para auxiliar na codificacao Manchester Diferencial, inicia Alto Baixo
  
  //Percorre cada indice de array do quadro
  for (int i = 0; i < quadro.length; i++) {
    int currentBit;//Variavel para armazenar o bit atual
    int valor = quadro[i]; // armazena o conteudo do quadro no indice quadroIndex
    //Cada iteracao, codifica 1 index do quadro preenchendo 2 index do manchester, pois codificado cada indice so pode armazenar 2 letras
    for (int k = 0; k < 32; k++) {
      /*Quando o deslocamento for 32 o indice da codificacao esta completamente preenchido 
      entao passa para o proximo indice e retorna o auxiliar de deslocamento para 0*/
      if (shift == 32) {
        index++;
        shift = 0;
      }
      currentBit = (valor >> k) & 1; //Realiza o deslocamento a direita em k posicoes e captura o bit, por meio do operador AND
      if (currentBit == 1 || currentBit == -1){//Em bit = 1 ocorre a alteracao no bit de codificacao
        if(currentBit == encodedBit){//Caso for igual
          differentialManchester[index] = encode(differentialManchester[index], shift, 0, 1);//Codifica 01
          signalDiffManc.append(01);//Adiciona a codificacao ao sinal
          encodedBit = 0;//Atualiza o bit de codificacao
        }else{//Caso nao
          differentialManchester[index] = encode(differentialManchester[index], shift, 1, 0);//Codifica 10
          signalDiffManc.append(10);//Adiciona a codificacao ao sinal
          encodedBit = 1;//Atualiza o bit de codificacao
        }
      }
      else{//Caso o bit for 0 q o de codificacao for 1 codifica 10, caso nao codifica 01
        differentialManchester[index] |= (encodedBit == 1) ? encode(differentialManchester[index], shift, 1, 0) : encode(differentialManchester[index], shift, 0, 1);
        signalDiffManc = (currentBit==1) ? signalDiffManc.append(10) : signalDiffManc.append(01); //Adiciona a codificacao ao sinal
      }
      //Realiza a insercao bit a bit da codificacao, se caso o bit atual for igual a 0 ocorre transicao
      shift += 2;//Incrementa o deslocamento em 2, ja que 2 posicoes ja foram inseridas com a codificacao
    }
  }
  signalDiffManc = signalDiffManc.reverse();//Reverte a string para ficar na ordem certa
  //retorna array codificado
  return differentialManchester;
}


  public int encode(int index, int shift, int bit, int encode){
    //Realiza o deslocamento a esquerda em deslocation posisoes, insere o bit e adiciona ao indice atraves do operador | 
    index |= (bit << shift); 
    //Realiza o deslocamento a esquerda em deslocation + 1 posisoes, insere a codificacao e adiciona ao indice atraves do operador |
    index |= (encode << shift+1); 
    return index;
  }

}