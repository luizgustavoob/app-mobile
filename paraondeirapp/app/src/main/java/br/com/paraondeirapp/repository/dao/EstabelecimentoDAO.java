package br.com.paraondeirapp.repository.dao;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.paraondeirapp.model.Endereco;
import br.com.paraondeirapp.model.Estabelecimento;
import br.com.paraondeirapp.constantes.IConstantesDatabase;

public class EstabelecimentoDAO extends GenericDAO<Estabelecimento> {

    public EstabelecimentoDAO(Context ctx) throws SQLException {
        super(ctx, Estabelecimento.class);
    }

    /**
     * Deleta os estabelecimentos que foram excluídos do servidor.
     * @param chaves
     * @return
     * @throws SQLException
     */
    public boolean deleteByListIdEstabelecimento(List<Integer> chaves) throws SQLException {
        try {
            DeleteBuilder<Estabelecimento, Integer> builder = getDao().deleteBuilder();
            builder.where().in(IConstantesDatabase.ESTABELECIMENTO_ID, chaves);
            builder.delete();
            return true;
        } catch (Exception ex){
            throw new SQLException("Erro deletando os registros. Detalhe: " + ex.getMessage());
        }
    }

    /**
     * Pesquisa os estabelcimentos pelo nome ou endereço.
     * @param nome/endereço estabelecimento
     * @return List<Esatbelecimento>
     * @throws SQLException
     */
    public List<Estabelecimento> getEstabelecimentosByNomeOrEndereco(String where)
            throws SQLException {
        try {
            QueryBuilder<Estabelecimento, Integer> builder = getDao().queryBuilder();
            QueryBuilder<Endereco, Integer> leftJoinEndereco =
                    (QueryBuilder<Endereco, Integer>) getDao(Endereco.class).queryBuilder();

            leftJoinEndereco.where().like(IConstantesDatabase.ENDERECO_LOGRADOURO,
                                          "%" + where.toUpperCase() + "%")
                                    .or().like(IConstantesDatabase.ENDERECO_BAIRRO,
                                          "%" + where.toUpperCase()+ "%");
            builder.leftJoinOr(leftJoinEndereco).where()
                                    .like(IConstantesDatabase.ESTABELECIMENTO_NOME,
                                          "%" + where.toUpperCase() + "%").prepare();
            return builder.query();
        } catch (SQLException ex){
            throw new SQLException("Erro selecionando os registros. Detalhe: " + ex.getMessage());
        }
    }
}
