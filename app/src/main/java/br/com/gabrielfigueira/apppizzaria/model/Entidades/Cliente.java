package br.com.gabrielfigueira.apppizzaria.model.Entidades;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import br.com.gabrielfigueira.apppizzaria.util.DataHelper;

/**
 * Created by Gabriel on 04/06/2017.
 */

public class Cliente {

    private int id;
    private int id_centralizado;
    private String nome;
    private String cpf;
    private String logradouro;
    private int numero;
    private String bairro;
    private String cep;
    private String cidade;
    private String telefone;
    private String email;
    private Date data_cadastro;
    private Date data_sincronizacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



    @Override
    public String toString() {
        return Integer.toString(id).trim() + " - " + nome.trim();
    }

    public int getId_centralizado() {
        return id_centralizado;
    }

    public void setId_centralizado(int id_centralizado) {
        this.id_centralizado = id_centralizado;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(Date data_cadastro) {
        this.data_cadastro = data_cadastro;
    }

    public Date getData_sincronizacao() {
        return data_sincronizacao;
    }

    public void setData_sincronizacao(Date data_sincronizacao) {
        this.data_sincronizacao = data_sincronizacao;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public JSONObject toJson() throws Exception {
        JSONObject obj = new JSONObject();
        if (id_centralizado != 0)
            obj.put("id", id_centralizado);
        obj.put("nome", nome);
        obj.put("cpf", cpf);
        obj.put("logradouro",logradouro);
        obj.put("numero", numero);
        obj.put("bairro", bairro);
        obj.put("cep", cep);
        obj.put("cidade", cidade);
        obj.put("telefone", telefone);
        obj.put("email", email);
        obj.put("data_sincronizacao", DataHelper.dateToStr(data_sincronizacao));
        return obj;
    }
}
