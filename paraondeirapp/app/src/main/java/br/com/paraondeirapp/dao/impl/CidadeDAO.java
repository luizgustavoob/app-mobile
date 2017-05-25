package br.com.paraondeirapp.dao.impl;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.paraondeirapp.model.Cidade;
import br.com.paraondeirapp.constantes.IConstantesDatabase;

public class CidadeDAO extends GenericDAO<Cidade> {

    public CidadeDAO(Context context) throws SQLException {
        super(context, Cidade.class);
    }

    /**
     * Deleta as cidades que foram excluídas do servidor.
     * @param idsCidades
     * @return
     * @throws SQLException
     */
    public boolean deleteByListIdCidade(List<Integer> idsCidades) throws SQLException {
        try {
            DeleteBuilder<Cidade, Integer> builder = getDao().deleteBuilder();
            builder.where().in(IConstantesDatabase.CIDADE_ID, idsCidades);
            builder.delete();
            return true;
        } catch (Exception ex){
            throw new SQLException("Erro deletando os registros. Detalhe: " + ex.getMessage());
        }
    }
}
