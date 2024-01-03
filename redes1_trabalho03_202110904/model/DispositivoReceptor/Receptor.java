/* ***************************************************************
* Autor............: Luan Pinheiro Azevedo
* Matricula........: 202110904
* Inicio...........: 3/11/2023
* Ultima alteracao.: 5/11/2023
* Nome.............: Receptor.java
* Funcao...........: Classe responsavel por simular o dispositivo receptor, tal qual sua recepcap de dados
*************************************************************** */

package model.DispositivoReceptor;

//Importacoes
import controller.ControleDados;
import model.Fisica;
import model.MeioTransmissao;

public class Receptor {//Classe receptora
  Fisica camadaFisica;
  ControleDados cD; //Classe controladora
  
  public Receptor(MeioTransmissao mt) {//Referencia no construtor
    this.cD = mt.getCd();
  }

  public void CamadaFisicaReceptora(int quadro[], ControleDados cD){
    camadaFisica = new Fisica(cD);
    camadaFisica.CamadaFisicaReceptora(quadro, cD);
  }
}
