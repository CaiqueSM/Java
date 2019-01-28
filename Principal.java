/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;

/**
 * NumberArgsWrongException gera uma exceção caso a localização ou o nome do arquivo 
 * não seja passado como argumento para o metodo main.
 *
 * @author caique da silva machado.
 */
class NumberArgsWrongException extends IOException {

    public NumberArgsWrongException(String msg){
        super(msg);
    }
}

/**
 * Principal é a classe mais importate do programa e nela está abrigado o metodo
 * main e a função isolado que obtem os vertices isolados de um grafo e retorna
 * um vetor do tipo Set os contendo.
 *
 * @author caique da silva machado.
 */
public class Principal {

    /**
     * main é o metodo principal do programa e nele está implementado o
     * algoritmo para retornar a quantidade de componentes conexas de um grafo e
     * estas componentes.
     *
     * @param args argumentos que podem ser passados para o metodo via linha de
     * comando e neste programa o argumento esperado é uma string contendo o
     * endereço do arquivo a ser aberto.
     * @throws java.io.IOException Retorna uma string informando que a sintaxe
     * do nome do arquivo,do nome do diretório ou do rótulo do volume está
     * incorreta.
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        /*
         * A variavel "caminho" recebe a localização do arquivo armazenado em
         * disco que será aberto e a variavel linha será usada para armazenar o
         * conteudo atual de uma linha lida do arquivo.
         */
        String caminho, linha;
        BufferedReader arquivo = null;
        /*
         * As coleções "vertices" e "arestas" recebem respectivamente os vertices
         * e as arestas do grafo fornecido pelo arquivo de entrada e o vetor de
         * coleções "subgrafo" armazena as componetes conexas do grafo.
         */
        Set vertices = new HashSet(), arestas = new HashSet(), componentes = new HashSet();
        Set[] subgrafo = null;
        /*
         * A constante "tamanho" é usada como faixa de intervalo para o vetor de
         * coleções "subgrafo" e as variaveis "indice" e "ant" são indices para 
         *este mesmo vetor.
         */
        final int tamanho;
        //O vetor encontrados contem os vertices identificados no grafo.
        String[] encontrados;
        //Algumas variaveis para controle e contadores de laços de repetição.
        int indice = 0, ant, vazia = 0, j = 0;
        //verifica-se algum argumento foi passado para o programa.
        caminho = checarCaminho(args);
        /*
         * Será realizado no bloco abaixo a tentativa de ler e manipular os
         * dados do arquivo de entrada.
         */
        try {
            //leitura do arquivo.
            arquivo = new BufferedReader(new FileReader(caminho));
            //inicializa algumas variaveis.
            linha = arquivo.readLine();
            tamanho = (int) Math.pow((linha.length()), 2);
            subgrafo = new HashSet[tamanho];
            //obtem os vertices do grafo.
            encontrados = buscarVertices(linha);
            for (int i = 0; encontrados[i] != null; i++) {
                vertices.add(encontrados[i]);
            }
            /*
             * verifica se os vertices(de grau maior ou igual a 1(um)) de uma
             * aresta pertencem ao grafo e caso pertençam adiciona-os ao vetor
             * "subgrafo".
             */
            while ((linha = arquivo.readLine()) != null) {
                encontrados = buscarVertices(linha);
                //Nota: O codigo abaixo faz distinção entre letras maiusculas e
                //minusculas, assim por exemplo se "F" for um vertice da coleção
                //de vertices e "f" "G" for uma arresta, esta e outras ocorrencias
                //de "f" seram consideradas como não pertencentes ao grafo
                //e se não houver ocorrencias com "F" este sará considerado como
                //vertice isolado.
                if ((vertices.contains(encontrados[0]))
                        && (vertices.contains(encontrados[1]))) {
                    if (indice == 0) {
                        arestas.add(encontrados[0]);
                        arestas.add(encontrados[1]);
                        subgrafo[indice] = new HashSet(arestas);
                        ++indice;
                    } else {
                        ant = indice;
                        for (int i = 0; i < ant; i++) {
                            arestas.clear();
                            if (subgrafo[i] != null) {
                                if ((subgrafo[i].contains(encontrados[0]))
                                        || (subgrafo[i].contains(encontrados[1]))) {
                                    arestas.add(encontrados[0]);
                                    arestas.add(encontrados[1]);
                                    subgrafo[i].addAll(arestas);
                                    ++indice;
                                }
                            } else {
                                vazia = i;
                                break;
                            }
                        }
                        /*
                         * Se o questinamento abaixo for verdadeiro tem-se que o
                         * grafo é desconexo e será necessario armazenar estás
                         * componentes.
                         */
                        if (indice == ant) {
                            arestas.add(encontrados[0]);
                            arestas.add(encontrados[1]);
                            subgrafo[vazia] = new HashSet(arestas);
                            ++indice;
                        }
                    }
                }
            }
            /*
             * como o algoritmo é ingenuo é necessario verificar se nenhum sub-
             * grafico foi considerado como desconexo por engano.
             */
            for (int k = 0; subgrafo[k] != null; k++) {
                for (int h = 1; subgrafo[h] != null; h++) {
                    componentes.addAll(subgrafo[h]);
                    while (!componentes.isEmpty()) {
                        if ((subgrafo[k].contains(componentes.iterator().next()))
                                && subgrafo[k] != subgrafo[h]) {
                            subgrafo[k].addAll(subgrafo[h]);
                            subgrafo[h] = null;
                            break;
                        } else {
                            componentes.remove(componentes.iterator().next());
                        }
                    }
                }
            }
            //checa se não há vertices isolados.
            if (Esta_isolado(subgrafo, vertices)) {
                subgrafo = isolado(subgrafo, vertices);
            }
        } catch (IOException e1) {
            //Imprime uma string informando o erro.
            System.out.println(e1);
        } finally {
            //fecha o arquivo apos terminar a leitura.
            if (arquivo != null) {
                arquivo.close();
                /**
                 * imprime na saída padrão a quantidade de componentes conexas e
                 * estas componentes.
                 */
                System.out.println("quantidade:" + " " + contador(subgrafo));
                while (subgrafo[j] != null) {
                    System.out.println(subgrafo[j]);
                    j++;
                }
            }
        }
    }

    /**
     * A função isolado verifica-se existem vertices isoloados no grafo, ou seja
     * com grau menor que 1(um).
     *
     * @param a é um conjunto contendo os vertices das componentes conexas do
     * grafo.
     * @param b é o conjunto de todos os vertices do grafo (V(G)).
     * @return Se não houver vertices isolados retorna o conjunto original
     * passado como parametro e caso haja retorna o conjunto passado como
     * parametro mais os vertices isolados.
     */
    public static Set[] isolado(Set[] a, Set b) {
        Set uniao = new HashSet();
        int ultpos = 0;
        //une em um mesmo conjunto todos os vertices das componentes encotradas.
        for (int i = 0; a[i] != null; i++) {
            uniao.addAll(a[i]);
            ultpos = i;
        }
        //verifica se todos os vertices de "b" estão em "união" e caso não esteja
        //considera-se este vertice como isolado e o adicina ao vetor "a" como
        //sendo um subgrafo dexconexo.
        while (!b.isEmpty()) {
            if (!uniao.contains(b.iterator().next())) {
                if (a[ultpos] != null) {
                    ++ultpos;
                }
                a[ultpos] = new HashSet();
                a[ultpos].add(b.iterator().next());                
            }
            b.remove(b.iterator().next());
        }
        //retorna o vetor "a" modificado ou não.
        return a;
    }

    /**
     * A função Esta_isolado informa se há vertices isolados no grafo.
     *
     * @param s é um conjunto contendo os vertices das componentes conexas do
     * grafo.
     * @param v é um conjunto de todos os vertices do grafo(V(G)).
     * @return retorna verdadeiro ao encontrar o primeiro vertice isolado do
     * grafo e retorna falso caso não seja encontrado vertices isolados.
     */
    public static boolean Esta_isolado(Set[] s, Set v) {
        Set conjunto = new HashSet();
        //une em um mesmo conjunto todos os vertices das componentes encotradas.
        for (int i = 0; s[i] != null; i++) {
            conjunto.addAll(s[i]);
        }
        //verifica se todos os vertices de "v" estão em "conjunto" e caso não esteja
        //considera-se este vertice como isolado e retorna o valor verdadeiro.
        while (!v.isEmpty()) {
            if (!conjunto.contains(v.iterator().next())) {
                return true;
            } else {
                v.remove(v.iterator().next());
            }
        }
        //retorna o valor falso caso todos os vertices de "v" estejam em "conjunto".
        return false;
    }

    /**
     * A função buscarVertices considera que a string passada como parametro
     * possui os vertices de um grafo e assume que qualquer sequência de
     * caracteres separados por espaço é um vertice do grafo.
     *
     * @param l é uma string não vazia contendo os vertices do grafo.
     * @return retorna um vetor do tipo string contendo em cada posição um
     * vertice do grafo.
     */
    public static String[] buscarVertices(String l) {
        String[] resultado = new String[l.length() - 1];
        int j = 0;
        //Verifica se a string não está vazia.
        if (!l.isEmpty()) {
            //procura os vertices.
            for (int i = 0; i < l.length(); i++) {
                if (l.charAt(i) != ' ') {
                    if (resultado[j] != null) {
                        resultado[j] += l.charAt(i);
                    } else {
                        resultado[j] = "";
                        resultado[j] += l.charAt(i);
                    }
                } else {
                    ++j;
                }
            }
        }
        //retorna um vetor contendo em cada posição os vertices do grafo.
        return resultado;
    }

    /**
     * verifica se o numero correto de argumentos foi passado para o programa.
     *
     * @param s Recebe um vetor do tipo string contendo o caminho do arquivo a
     * ser aberto.
     * @return Retorna a string passada como parametro se esta não é vazia.
     * @throws NumberArgsWrongException Se a string passada como parametro for vazia
     * assume-se que nenhum argumento foi passado.
     */
    public static String checarCaminho(String[] s) throws NumberArgsWrongException {
        if (s.length != 1) {
            throw new NumberArgsWrongException("Este programa precisa de 1(um) argumento do tipo"
                + " string contendo o local do arquivo de texto(*.txt)"
                + " a ser manipulado");
        }
        return s[0];
    }

    /**
     * A função contador é usada para informar o numero atual de componentes
     * conexas encontradas.
     *
     * @param c É um vetor do tipo coleção contendo as componentes conexas do
     * grafo.
     * @return Retorna um numero inteiro informando a quantidade de componentes
     * conexas do grafo.
     */
    public static int contador(Set[] c) {
        int cont = 0;
        //A variavel cont é incrementada se a posição atual do vetor é não vazia.
        for (int i = 0; c[i] != null; i++) {
            cont++;
        }
        return cont;
    }
}
