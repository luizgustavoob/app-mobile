package br.com.paraondeirapp.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import br.com.paraondeirapp.constantes.IConstantesDatabase;

@DatabaseTable(tableName = IConstantesDatabase.TABELA_ESTADO)
public class Estado implements Serializable {

    @DatabaseField(columnName = IConstantesDatabase.ESTADO_ID, id = true, canBeNull = false, dataType = DataType.INTEGER)
    public int idUf;

    @DatabaseField(columnName = IConstantesDatabase.ESTADO_NOME, canBeNull = false, dataType = DataType.STRING)
    public String nome;

    @DatabaseField(columnName = IConstantesDatabase.ESTADO_SIGLA, canBeNull = false, dataType = DataType.STRING)
    public String sigla;

    public Estado(){
        super();
    }

    public int getIdUf() {
        return idUf;
    }

    public void setIdUf(int idUf) {
        this.idUf = idUf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public String toString() {
        return this.sigla;
    }
}
