package br.com.paraondeirapp.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

import br.com.paraondeirapp.enumeration.YesNo;
import br.com.paraondeirapp.constantes.IConstantesDatabase;

@DatabaseTable(tableName = IConstantesDatabase.TABELA_AVALIACAO)
public class Avaliacao implements Serializable {

    @DatabaseField(columnName = IConstantesDatabase.AVALIACAO_ID, id = true, canBeNull = false,
            dataType = DataType.INTEGER)
    private int idAvaliacao;

    @DatabaseField(columnName = IConstantesDatabase.AVALIACAO_ESTABELECIMENTO, canBeNull = false,
            foreign = true, foreignAutoRefresh = true,
            foreignColumnName = IConstantesDatabase.ESTABELECIMENTO_ID)
    private Estabelecimento estabelecimento;

    @DatabaseField(columnName = IConstantesDatabase.AVALIACAO_LIKE, canBeNull = false,
            dataType = DataType.ENUM_STRING)
    private YesNo gostou;

    @DatabaseField(columnName = IConstantesDatabase.AVALIACAO_USER, canBeNull = false,
            dataType = DataType.STRING)
    private String usuario;

    @DatabaseField(columnName = IConstantesDatabase.AVALIACAO_DTAVALIACAO,
            dataType = DataType.STRING)
    private String data;

    public Avaliacao(){
        super();
    }

    public int getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(int idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public YesNo getGostou() {
        return gostou;
    }

    public void setGostou(YesNo gostou) {
        this.gostou = gostou;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getData(){
        return this.data;
    }

    public void setData(String data){
        this.data = data;
    }
}
