package br.com.paraondeirapp.dao.impl;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.paraondeirapp.model.Estado;
import br.com.paraondeirapp.constantes.IConstantesDatabase;

public class EstadoDAO extends GenericDAO<Estado> {

    public EstadoDAO(Context context) throws SQLException {
        super(context, Estado.class);
    }

    /**
     * Deleta os estados que foram excluídos do servidor.
     * @param idsEstados
     * @return
     * @throws SQLException
     */
    public boolean deleteByListIdEstado(List<Integer> idsEstados) throws SQLException {
        try {
            DeleteBuilder<Estado, Integer> builder = getDao().deleteBuilder();
            builder.where().in(IConstantesDatabase.ESTADO_ID, idsEstados);
            builder.delete();
            return true;
        } catch (Exception ex){
            throw new SQLException("Erro deletando os registros. Detalhe: " + ex.getMessage());
        }
    }
}
