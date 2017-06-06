package br.com.gabrielfigueira.apppizzaria.model.DAO;

import java.util.ArrayList;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;

/**
 * Created by Fabricio on 04/06/2017.
 */

public class ClienteDAO {

    public List<Cliente> pesquisarPorNome(String nome){
        List<Cliente> lista = new ArrayList<>();
        Cliente c = new Cliente();
        c.setId(1);
        c.setNome("Fabricio");
        lista.add(c);
        c = new Cliente();
        c.setId(2);
        c.setNome("Gabriel");
        lista.add(c);
        return lista;
    }
}
