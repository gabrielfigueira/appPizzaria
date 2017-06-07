package br.com.gabrielfigueira.apppizzaria.model.Entidades;

import java.util.Date;

/**
 * Created by figueira on 06/06/17.
 */

public class ComandaProduto {
    private int id;
    private double quantidade;
    private Date data_hora_entrega;

    private Comanda comanda;
    private Produto produto;
}
