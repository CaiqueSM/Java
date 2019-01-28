
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author caique
 */
abstract class Item {

    String nome;    
    abstract double ObterTamanho();
}

class Arquivo extends Item {
    
    private final double tamanho;
    private String extensao;

    Arquivo(String nome, String extensao, double tamanho) {

        this.nome = nome;
        this.extensao = extensao;
        this.tamanho = tamanho;
    }

    public String toString() {
        return "Nome: " + this.nome + "." + extensao + "\n" + "Tamanho: " + this.ObterTamanho() + "\n";
    }

    @Override
    public double ObterTamanho() {
        return this.tamanho;
    }
}

class Diretorio extends Item {

    private double tamanho;
    private ArrayList<Arquivo> arquivos;
    private ArrayList<Diretorio> subPastas;
    private int indicePasta, indiceArquivo;

    Diretorio(String nome) {
        this.nome = nome;
        this.arquivos = new Arquivo[64];
        this.subPastas = new Diretorio[16];
        indicePasta = 0;
        indiceArquivo = 0;
    }

    public void AdicionarPasta(Diretorio NovaPasta){
        subPastas[indicePasta] = NovaPasta;
        indicePasta++;
    }
    
    public void RemoverPasta(Diretorio Pasta){
        
    }
    
    public Diretorio ObterPasta(){
    
    return null;
    }
    
    public String toString() {
        String lista = this.nome;
        if (arquivos.length > -1) {
            lista += "\n" + "Lista de Arquivos: " + "\n";
            for (int i = 0; arquivos[i] != null; i++) {
                lista += arquivos[i].toString();
            }
        }
        if (subPastas.length > -1) {
            lista += "\n" + "Lista de Diretorios: " + "\n";
            for (int j = 0; subPastas[j] != null; j++) {
                lista += subPastas[j].nome + " " + "Tamanho: " + subPastas[j].ObterTamanho();
                lista += subPastas[j].toString();
            }
        }

        return lista;
    }

    @Override
    double ObterTamanho() {
        
        for (int i = 0; arquivos[i] != null; i++) {
            tamanho += arquivos[i].ObterTamanho();
        }

        for (int j = 0; subPastas[j] != null; j++) {
            tamanho += subPastas[j].ObterTamanho();
        }

        return tamanho;
    }
}

public class ad1_2018_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Diretorio raiz = new Diretorio("POO");
        raiz.subPastas[0] = new Diretorio("APs");
        raiz.subPastas[0].arquivos[0] = new Arquivo("Ap1-gabarito", "pdf", 8.0);
        raiz.subPastas[0].arquivos[1] = new Arquivo("Ap1", "odt", 8.0);
        raiz.subPastas[1] = new Diretorio("ADs");
        raiz.subPastas[1].arquivos[0] = new Arquivo("Ad1-gabarito", "pdf", 6.0);
        raiz.subPastas[1].arquivos[1] = new Arquivo("Ad1", "odt", 6.0);
        System.out.println(raiz.toString());
    }

}
