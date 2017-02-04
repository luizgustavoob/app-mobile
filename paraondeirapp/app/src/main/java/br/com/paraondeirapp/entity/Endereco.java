package br.com.paraondeirapp.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import br.com.paraondeirapp.interfaces.IConstantesDatabase;

@DatabaseTable(tableName = IConstantesDatabase.TABELA_ENDERECO)
public class Endereco implements Serializable {

    @DatabaseField(columnName = IConstantesDatabase.ENDERECO_ID, id = true, canBeNull = false, dataType = DataType.INTEGER)
    private int idend;

    @DatabaseField(columnName = IConstantesDatabase.ENDERECO_LOGRADOURO, canBeNull = false, dataType = DataType.STRING)
    private String logradouro;

    @DatabaseField(columnName = IConstantesDatabase.ENDERECO_BAIRRO, canBeNull = false, dataType = DataType.STRING)
    private String bairro;

    @DatabaseField(columnName = IConstantesDatabase.ENDERECO_NUMERO, canBeNull = false, dataType = DataType.STRING)
    private String numero;

    @DatabaseField(columnName = IConstantesDatabase.ENDERECO_CEP, canBeNull = false, dataType = DataType.STRING)
    private String cep;

    @DatabaseField(columnName = IConstantesDatabase.ENDERECO_IDCIDADE, canBeNull = false, foreign = true,
            foreignAutoRefresh = true, foreignColumnName = IConstantesDatabase.CIDADE_ID)
    private Cidade cidade;

    public Endereco (){
        super();
    }

    public int getIdend() {
        return idend;
    }

    public void setIdend(int idend) {
        this.idend = idend;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return this.logradouro + ", " + this.numero + ". " +
                this.bairro;
    }
}
