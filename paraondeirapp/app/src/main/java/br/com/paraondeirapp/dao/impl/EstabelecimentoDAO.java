package br.com.paraondeirapp.dao.impl;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.paraondeirapp.model.Endereco;
import br.com.paraondeirapp.model.Estabelecimento;
import br.com.paraondeirapp.constantes.IConstantesDatabase;

public class EstabelecimentoDAO extends GenericDAO<Estabelecimento> {

    public EstabelecimentoDAO(Context contexto) throws SQLException {
        super(contexto, Estabelecimento.class);
    }

    public boolean deleteByListIdEstabelecimento(List<Integer> idsEstabelecimentos) throws SQLException {
        try {
            DeleteBuilder<Estabelecimento, Integer> builder = getDao().deleteBuilder();
            builder.where().in(IConstantesDatabase.ESTABELECIMENTO_ID, idsEstabelecimentos);
            builder.delete();
            return true;
        } catch (Exception ex){
            throw new SQLException("Erro deletando os registros. Detalhe: " + ex.getMessage());
        }
    }

    public List<Estabelecimento> findEstabelecimentosByNomeOrEndereco(String nomeOrEndereco) throws SQLException {
        try {
            QueryBuilder<Estabelecimento, Integer> builder = getDao().queryBuilder();
            QueryBuilder<Endereco, Integer> leftJoinEndereco =
                    (QueryBuilder<Endereco, Integer>) getDao(Endereco.class).queryBuilder();

            leftJoinEndereco.where().like(IConstantesDatabase.ENDERECO_LOGRADOURO,
                                          "%" + nomeOrEndereco.toUpperCase() + "%")
                                    .or().like(IConstantesDatabase.ENDERECO_BAIRRO,
                                          "%" + nomeOrEndereco.toUpperCase()+ "%");
            builder.leftJoinOr(leftJoinEndereco).where()
                                    .like(IConstantesDatabase.ESTABELECIMENTO_NOME,
                                          "%" + nomeOrEndereco.toUpperCase() + "%").prepare();
            return builder.query();
        } catch (SQLException ex){
            throw new SQLException("Erro selecionando os registros. Detalhe: " + ex.getMessage());
        }
    }
}
