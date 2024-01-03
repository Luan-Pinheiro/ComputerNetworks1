/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 23/08/2023
* Ultima alteracao.: 13/09/2023
* Nome.............: Receptor.java
* Funcao...........: Classe responsavel por receber os dados e sinais, realizar a selecao da Camada fisica receptora
                     bem como sua decodificacao
*************************************************************** */

package model;

//Importacoes
import controller.ControleDados;

public class Receptor {//Classe receptora

  ControleDados cD; //Classe controladora
  private int seletor;//Seletor para tipo de codificacao
  private String typeDecode;//Tipo de codificacao

  public Receptor(MeioTransmissao mt) {//Referencia no construtor
  }

  public int getSeletorValue(ControleDados cD) {//Selecao do tipo de decodificacao
    typeDecode = cD.getSeletor();
    switch (typeDecode) {
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

  public void CamadaFisicaReceptora(int quadro[], ControleDados cD) {
    this.cD = cD;
    int tipoDeDecodificacao = this.getSeletorValue(cD);
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
    CamadaDeAplicacaoReceptora(fluxoBrutoDeBits);
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
    cD.setMessage(mensagem);//Mostra o texto na tela apos todo o processo de conversao e decodificacao
  }
}
