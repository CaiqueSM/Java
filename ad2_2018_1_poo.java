
import java.io.*;
import java.util.*;

class Elemento {

    private String informacao;
    private int proximo;

    Elemento(String informacao, int proximo) {
        this.informacao = informacao;
        this.proximo = proximo;
    }

    public String ObterInformacao() {
        return informacao;
    }

    public void AtualizarInformacao(String informacao) {
        this.informacao = informacao;
    }

    public int ObterProximo() {
        return proximo;
    }

    public void AtualizarProximo(int proximo) {
        this.proximo = proximo;
    }
}

class Josephus {

    private ArrayList<Elemento> listaCircular = new ArrayList<>();
    //Armazema as informações do arquivo de entrada.
    private String[] arquivoDeRecuperacao;
    //Todos os valores de k. 
    private List razao = new ArrayList<>();
    private LineNumberReader ultimaLinha = null;
    private BufferedReader entrada = null;
    private BufferedWriter saida = null;
    private String linhaDoArquivo, nomeArquivo;
    //Quantos k existem.
    private int numeroDeRepeticoes;
    private int indice = 0;

    Josephus(String caminho) throws IOException {
        nomeArquivo = ObterNomeDoArquivo(caminho);
        try {
            entrada = new BufferedReader(new FileReader(caminho));
            //obtem o número de linhas do arquivo.
            ultimaLinha = new LineNumberReader(new FileReader(caminho));
            ultimaLinha.skip(new File(caminho).length());
            int numeroDeLinhas = ultimaLinha.getLineNumber();
            arquivoDeRecuperacao = new String[numeroDeLinhas];
            //Recupera o nome dos participantes e os adiciona em uma lista.
            while (!(linhaDoArquivo = entrada.readLine()).toUpperCase().equals("FIM")) {
                listaCircular.add(new Elemento(linhaDoArquivo, indice + 1));
                arquivoDeRecuperacao[indice] = linhaDoArquivo;
                indice++;
            }
            //Faz o ultimo elemento apontar para o primeiro tornando a lista circular.
            listaCircular.get(indice - 1).AtualizarProximo(0);
            //Recupera o valor de N.
            numeroDeRepeticoes = Integer.parseInt(entrada.readLine());
            //Recupera os valores de K.
            while ((linhaDoArquivo = entrada.readLine()) != null) {
                razao.add(Integer.parseInt(linhaDoArquivo));
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (entrada != null) {
                entrada.close();
            }
        }
    }
    
    
    private String ObterNomeDoArquivo(String caminho) {
        String nome;
        int posicao = caminho.length() - 1;
        while ((caminho.charAt(posicao) != '\\') && (posicao > 0)) {            
            posicao--;
        }       
        nome = caminho.substring(posicao+1, caminho.length());
        return nome;
    }

    public void Executar() throws IOException {
        try {            
            saida = new BufferedWriter(new FileWriter("saida-"+nomeArquivo));
            for (int i = 0; i < numeroDeRepeticoes; i++) {
                String sobreviventes = "";
                indice = 0;
                //Executa até haver um sobrevivente.
                for (int w = listaCircular.size() - 1; w > 0; w--) {
                    //Escolhe o elemento que será eliminado.
                    for (int j = 1; j < (int) razao.get(i); j++) {
                        indice = listaCircular.get(indice).ObterProximo();
                    }
                    if (listaCircular.get(indice).ObterInformacao().equals("")) {
                        while (listaCircular.get(indice).ObterInformacao().equals("")) {
                            indice = listaCircular.get(indice).ObterProximo();
                        }
                        listaCircular.get(indice).AtualizarInformacao("");
                    } else {
                        listaCircular.get(indice).AtualizarInformacao("");
                    }
                    indice++;
                    if (indice < listaCircular.size()) {
                        if (listaCircular.get(indice).ObterInformacao().equals("")) {
                            while (listaCircular.get(indice).ObterInformacao().equals("")) {
                                indice = listaCircular.get(indice).ObterProximo();
                            }
                        }
                    } else {
                        indice = 0;
                    }
                }
                //constroi a string com os sobreviventes.
                for (int k = 0; k < listaCircular.size(); k++) {
                    if (!listaCircular.get(k).ObterInformacao().equals("")) {
                        sobreviventes += listaCircular.get(k).ObterInformacao() + " ";
                    }
                }
                saida.write(razao.get(i) + " " + sobreviventes);
                saida.newLine();
                //Recupera a informação da lista para que se possa testa-la com diferentes
                //valores de k.
                for (int z = 0; z < listaCircular.size(); z++) {
                    listaCircular.get(z).AtualizarInformacao(arquivoDeRecuperacao[z]);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            saida.close();            
        }
    }
}

public class ad2_2018_1_poo {

    //O local do arquivo é passado como argumento para a função principal.
    public static void main(String[] args) throws IOException {        
        try {
            Josephus teste = new Josephus(args[0]);
            teste.Executar();
        } catch (IOException e) {
            System.out.print(e);
        }
    }

}
