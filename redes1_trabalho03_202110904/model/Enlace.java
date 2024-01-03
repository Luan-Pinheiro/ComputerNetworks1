/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 3/11/2023
* Ultima alteracao.: 5/11/2023
* Nome.............: Enlace.java
* Funcao...........: Classe responsavel pelo enquadramento, desenquadramento e controle de erro, desempenha o papel da camada de enlace de dados
*************************************************************** */
package model;

import java.io.IOException;

import controller.ControleDados;
import javafx.scene.control.TextArea;

public class Enlace {
  
  private String typeEnquad;//Tipo de enquadramento
  private String typeErrorControl;//Tipo de controle de erros
  private int enquadSelector;//Seletor para tipo de enquadramento
  private int errorControlSelector;//Seletor para tipo de controle de erros
  ControleDados cD;
  Fisica camadaFisica;
  public Enlace(ControleDados cD){
    this.cD = cD;
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
          System.out.println("Nao selecionado, padrao definido: 'Contagem de caracteres'");
          enquadSelector = 0;
          break;
      }
    }else{
      System.out.println("Nao selecionado, padrao definido: 'Contagem de caracteres'");
      enquadSelector = 0;
    }
    return enquadSelector;
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
          System.out.println("Nao selecionado, padrao definido: 'Bit de Paridade par'");
          errorControlSelector = 0;
          break;
      }
    }else{
      System.out.println("Nao selecionado, padrao definido: 'Bit de Paridade par'");
      errorControlSelector = 0;
    }
    return errorControlSelector;
  }
  //--------------------------------------------- ENQUADRAR --------------------------------------------- //
  private static int[] getBinaryValue(String value, TextArea telaTransmissao){
    //Array de caracteres, cada indice corresponde a um char da String inicial
    char[] caractere = value.toCharArray();
    //Valor ascii do termo
    int asciiValue;
    //Auxiliar que armazena o ascii
    int asciiValueaux;
    int convertedValue[] = new int[caractere.length];// Tamanho do array de int
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
  
  public void CamadaDeAplicacaoTransmissora(String mensagem) {
    //chamar o enlace
    int quadro[] = getBinaryValue(mensagem, cD.getTxtATransmissor());
    CamadaEnlaceDadosTransmissora(quadro);
  }

  public void CamadaEnlaceDadosTransmissora(int quadro[]) {
    CamadaEnlaceDadosTransmissoraControleDeErro(quadro);
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
    CamadaEnlaceDadosTransmissoraEnquadramento(quadroControlado);
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
    camadaFisica = new Fisica(cD);
    camadaFisica.CamadaFisicaTransmissora(quadroEnquadrado);//Proximo metodo
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
    System.out.println("\n\n");
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
      if(counter%2!=0){
        //flag de erro (contador eh impar)
        flag = true;
      }
    }
    if(flag){
      try {
        //dispara tela de erro
        cD.startErrorAlert();
      } catch (IOException e) {
        e.printStackTrace();
      }
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
      try {
        //dispara tela de erro
        cD.startErrorAlert();
      } catch (IOException e) {
        e.printStackTrace();
      }
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
    cD.setMessage(mensagem);//Mostra o texto na tela apos todo o processo de conversao e decodificacao
  }
}
