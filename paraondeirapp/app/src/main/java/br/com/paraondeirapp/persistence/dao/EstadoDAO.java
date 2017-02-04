package br.com.paraondeirapp.persistence.dao;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.paraondeirapp.entity.Estado;
import br.com.paraondeirapp.interfaces.IConstantesDatabase;

public class EstadoDAO extends GenericDAO<Estado> {

    public EstadoDAO(Context ctx) throws SQLException {
        super(ctx, Estado.class);
    }

    /**
     * Deleta os estados que foram excluídos do servidor.
     * @param chaves
     * @return
     * @throws SQLException
     */
    public boolean deleteByListIdEstado(List<Integer> chaves) throws SQLException {
        try {
            DeleteBuilder<Estado, Integer> builder = getDao().deleteBuilder();
            builder.where().in(IConstantesDatabase.ESTADO_ID, chaves);
            builder.delete();
            return true;
        } catch (Exception ex){
            throw new SQLException("Erro deletando os registros. Detalhe: " + ex.getMessage());
        }
    }
}
