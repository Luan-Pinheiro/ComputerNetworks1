/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 3/11/2023
* Ultima alteracao.: 5/11/2023
* Nome.............: Transmissor.java
* Funcao...........: Classe responsavel por simular o dispositivo traansmissor, tal qual seu envio de dados
*************************************************************** */
package model.DispositivoTransmissor;

import controller.ControleDados;
import model.Enlace;

public class Transmissor {

  ControleDados cD;

  public Transmissor(ControleDados cD) {//Referencia no construtor
    this.cD = cD;
  }

  public void AplicacaoTransmissora(String mensagem) {
    Enlace camadaEnlace = new Enlace(cD);
    camadaEnlace.CamadaDeAplicacaoTransmissora(mensagem);//Proximo metodo
  }

}
