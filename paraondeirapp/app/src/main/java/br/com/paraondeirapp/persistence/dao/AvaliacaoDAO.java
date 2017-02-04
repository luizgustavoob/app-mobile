package br.com.paraondeirapp.persistence.dao;

import android.content.Context;
import android.content.Intent;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.paraondeirapp.entity.Avaliacao;
import br.com.paraondeirapp.entity.Estabelecimento;
import br.com.paraondeirapp.entity.Usuario;
import br.com.paraondeirapp.enumeration.YesNo;
import br.com.paraondeirapp.interfaces.IConstantesDatabase;

public class AvaliacaoDAO extends GenericDAO<Avaliacao> {

    public AvaliacaoDAO(Context ctx) throws SQLException {
        super(ctx, Avaliacao.class);
    }

    @Override
    public boolean save(Avaliacao obj) throws SQLException {
        Avaliacao avaliacaoTemp = findByIdEstabelecimentoAndUsuario(obj);
        if (avaliacaoTemp == null){
            obj.setIdAvaliacao( getNextId(obj.getUsuario(), obj.getEstabelecimento()) );
            return super.insert(obj);
        } else {
            obj.setIdAvaliacao(avaliacaoTemp.getIdAvaliacao());
            return super.update(obj);
        }
    }

    /**
     * Retorna o próximo ID da avaliação, sendo um sequencial por usuário e estabelecimento.
     * @param usuario
     * @param estabelecimento
     * @return
     * @throws SQLException
     */
    private int getNextId(String usuario, Estabelecimento estabelecimento) throws SQLException{
        try {
            QueryBuilder<Avaliacao, Integer> builder = getDao().queryBuilder();
            builder.selectRaw("MAX(" + IConstantesDatabase.AVALIACAO_ID + ") + 1")
                    .where()
                    .eq(IConstantesDatabase.AVALIACAO_USER, usuario)
                    .and()
                    .eq(IConstantesDatabase.AVALIACAO_ESTABELECIMENTO, estabelecimento.getIdEstabelecimento());

            String[] resultado = getDao().queryRaw(builder.prepareStatementString()).getFirstResult();
            return Integer.parseInt(resultado[0]);
        } catch (Exception ex){
            throw new SQLException("Erro consultando próximo ID. Detalhe: " + ex.getMessage());
        }
    }

    /**
     * Consulta as avaliações pela chave Estabelecimento + Usuario.
     * @param obj
     * @return
     * @throws SQLException
     */
    private Avaliacao findByIdEstabelecimentoAndUsuario(Avaliacao obj) throws SQLException {
        try {
            QueryBuilder<Avaliacao, Integer> builder = getDao().queryBuilder();
            builder.where()
                    .eq(IConstantesDatabase.AVALIACAO_USER, obj.getUsuario())
                    .and()
                    .eq(IConstantesDatabase.AVALIACAO_ESTABELECIMENTO, obj.getEstabelecimento().getIdEstabelecimento());

            List<Avaliacao> list = builder.query();
            if (list.size() > 0) {
                return builder.query().get(0);
            } else {
                return null;
            }
        } catch (Exception ex){
            throw new SQLException("Erro consultando registros. Detalhe: " + ex.getMessage());
        }
    }

    /**
     * Deleta as avaliações cujos estabelecimentos foram excluídos do servidor.
     * @param chaves
     * @return
     * @throws SQLException
     */
    public boolean deleteByListIdEstabelecimento(List<Integer> chaves) throws SQLException {
        try {
            DeleteBuilder<Avaliacao, Integer> builder = getDao().deleteBuilder();
            builder.where().in(IConstantesDatabase.AVALIACAO_ESTABELECIMENTO, chaves);
            builder.delete();
            return true;
        } catch (Exception ex){
            throw new SQLException("Erro deletando os registros. Detalhe: " + ex.getMessage());
        }
    }
}
