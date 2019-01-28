/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//import java.util.*;
//import java.io.*;

interface Dicionario {

    void inserir(Object c, Object v) throws DicionarioExc;

    Object consulta(Object c);

    void remover(Object c) throws DicionarioExc;

}

class DicionarioIMPL implements Dicionario {

    private class Pares {

        Object chave, valor;

        Pares(Object chave, Object valor) {
            this.chave = chave;
            this.valor = valor;
        }

        @Override
        public String toString() {
            return getClass().getName() + "\tNome:" + chave + "\tTel:" + valor;
        }
    };
    private final Pares[] elementos;
    private int ultimo;

    public DicionarioIMPL(int TAM) {
        elementos = new Pares[TAM];
        ultimo = 0;
    }

    @Override
    public void inserir(Object c, Object v) throws DicionarioExc {
        if (ultimo == elementos.length) {
            throw new DicionarioExc("O dicionario está lotado");
        }
        elementos[ultimo] = new Pares(c, v);
        ++ultimo;
    }

    @Override
    public Object consulta(Object c) {
        for (Pares elemento : elementos) {
            if (elemento != null && c.equals(elemento.chave)) {
                return elemento;
            }
        }
        return null;
    }

    @Override
    public void remover(Object c) throws DicionarioExc {
        for (int i = 0; i < ultimo; i++) {
            if (c.equals(elementos[i].chave)) {
                elementos[i] = null;
                --ultimo;
                return;
            }
        }
        throw new DicionarioExc("Não foi encontrado a chave " + c);
    }
}

class DicionarioExc extends Exception {

    public DicionarioExc(String msg) {
        super(msg);
    }
}

/**
 *
 * @author caique
 */
public class aula10 {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Dicionario dic1 = new DicionarioIMPL(4);
        try {
            dic1.inserir("caique", "24 99935-5488");
            dic1.inserir("bruno", "24 99852-8488");
            dic1.inserir("fulano", "88 98888-8888");
            dic1.remover("fulano");
            dic1.inserir(2, 4);
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println(dic1.consulta("caique"));
        System.out.println(dic1.consulta("bruno"));
        System.out.println(dic1.consulta("fulano"));
         System.out.println(dic1.consulta(1));
    }

}
