package br.com.gabrielfigueira.apppizzaria.util;

/**
 * Created by Fabricio on 08/06/2017.
 */

public enum ModoDominio {
    inserir(1),
    alterar(2);

    private final int valor;
    ModoDominio(int valorOpcao){
        valor = valorOpcao;
    }
    public int getValor(){
        return valor;
    }
}
