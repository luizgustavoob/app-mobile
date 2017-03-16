package br.com.paraondeirapp.repository.dao;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.paraondeirapp.model.Cidade;
import br.com.paraondeirapp.constantes.IConstantesDatabase;

public class CidadeDAO extends GenericDAO<Cidade> {

    public CidadeDAO(Context ctx) throws SQLException {
        super(ctx, Cidade.class);
    }

    /**
     * Deleta as cidades que foram excluídas do servidor.
     * @param chaves
     * @return
     * @throws SQLException
     */
    public boolean deleteByListIdCidade(List<Integer> chaves) throws SQLException {
        try {
            DeleteBuilder<Cidade, Integer> builder = getDao().deleteBuilder();
            builder.where().in(IConstantesDatabase.CIDADE_ID, chaves);
            builder.delete();
            return true;
        } catch (Exception ex){
            throw new SQLException("Erro deletando os registros. Detalhe: " + ex.getMessage());
        }
    }
}
