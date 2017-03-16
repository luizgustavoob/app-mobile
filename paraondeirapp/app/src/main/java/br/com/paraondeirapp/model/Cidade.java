package br.com.paraondeirapp.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import br.com.paraondeirapp.constantes.IConstantesDatabase;

@DatabaseTable(tableName = IConstantesDatabase.TABELA_CIDADE)
public class Cidade implements Serializable {

    @DatabaseField(columnName = IConstantesDatabase.CIDADE_ID, id = true, canBeNull = false,
            dataType = DataType.INTEGER)
    private int idCidade;

    @DatabaseField(columnName = IConstantesDatabase.CIDADE_NOME, canBeNull = false, dataType = DataType.STRING)
    private String nome;

    @DatabaseField(columnName = IConstantesDatabase.CIDADE_IDESTADO, canBeNull = false, foreign = true,
            foreignAutoRefresh = true, foreignColumnName = IConstantesDatabase.ESTADO_ID)
    private Estado estado;

    public Cidade (){
        super();
    }

    public int getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(int idCidade) {
        this.idCidade = idCidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return this.nome + " - " + this.estado.toString();}
}
