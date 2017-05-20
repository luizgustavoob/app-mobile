package br.com.paraondeirapp.dao.impl;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.paraondeirapp.model.Endereco;
import br.com.paraondeirapp.constantes.IConstantesDatabase;

public class EnderecoDAO extends GenericDAO<Endereco> {

    public EnderecoDAO(Context ctx) throws SQLException {
        super(ctx, Endereco.class);
    }

    /**
     * Deleta os endereços que foram excluídos do servidor.
     * @param chaves
     * @return
     * @throws SQLException
     */
    public boolean deleteByListIdEndereco(List<Integer> chaves) throws SQLException {
        try {
            DeleteBuilder<Endereco, Integer> builder = getDao().deleteBuilder();
            builder.where().in(IConstantesDatabase.ENDERECO_ID, chaves);
            builder.delete();
            return true;
        } catch (Exception ex){
            throw new SQLException("Erro deletando os registros. Detalhe: " + ex.getMessage());
        }
    }
}
