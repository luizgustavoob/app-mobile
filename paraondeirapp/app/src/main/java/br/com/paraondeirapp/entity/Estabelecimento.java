package br.com.paraondeirapp.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import br.com.paraondeirapp.interfaces.IConstantesDatabase;

@DatabaseTable(tableName = IConstantesDatabase.TABELA_ESTABELECIMENTO)
public class Estabelecimento implements Serializable {

    @DatabaseField(columnName = IConstantesDatabase.ESTABELECIMENTO_ID, id = true, canBeNull = false, dataType = DataType.INTEGER)
    private int idEstabelecimento;

    @DatabaseField(columnName = IConstantesDatabase.ESTABELECIMENTO_NOME, canBeNull = false, dataType = DataType.STRING)
    private String nome;

    @DatabaseField(columnName = IConstantesDatabase.ESTABELECIMENTO_IDEND, canBeNull = false, foreign = true,
            foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 4, foreignColumnName = IConstantesDatabase.ENDERECO_ID)
    private Endereco endereco;

    @DatabaseField(columnName = IConstantesDatabase.ESTABELECIMENTO_TELEFONE, dataType = DataType.STRING)
    private String telefone;

    @DatabaseField(columnName = IConstantesDatabase.ESTABELECIMENTO_IMAGEM, dataType = DataType.BYTE_ARRAY)
    private byte[] imagem;

    public Estabelecimento(){
        super();
    }

    public int getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(int idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
