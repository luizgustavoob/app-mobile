package br.com.paraondeirapp.dao.impl;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.constantes.IConstantesDatabase;

public class AvaliacaoDAO extends GenericDAO<Avaliacao> {

    public AvaliacaoDAO(Context context) throws SQLException {
        super(context, Avaliacao.class);
    }

    @Override
    public boolean save(Avaliacao avaliacao) throws SQLException {
        Avaliacao avaliacaoTemp = findByIdEstabelecimentoAndUsuario(avaliacao);
        if (avaliacaoTemp == null){
            avaliacao.setIdAvaliacao( getProximoId(avaliacao.getUsuario()) );
            return super.insert(avaliacao);
        } else {
            avaliacao.setIdAvaliacao(avaliacaoTemp.getIdAvaliacao());
            return super.update(avaliacao);
        }
    }

    private int getProximoId(String usuario) throws SQLException {
        try {
            QueryBuilder<Avaliacao, Integer> builder = getDao().queryBuilder();
            builder.selectRaw("MAX(" + IConstantesDatabase.AVALIACAO_ID + ")")
                    .where()
                    .eq(IConstantesDatabase.AVALIACAO_USER, usuario);

            String[] resultado = getDao().queryRaw(builder.prepareStatementString()).getFirstResult();
            if (resultado[0] != null ){
                return Integer.parseInt(resultado[0]) + 1;
            } else {
                return 1;
            }
        } catch (Exception ex){
            throw new SQLException("Erro consultando próximo ID. Detalhe: " + ex.getMessage());
        }
    }

    private Avaliacao findByIdEstabelecimentoAndUsuario(Avaliacao avaliacao) throws SQLException {
        try {
            QueryBuilder<Avaliacao, Integer> builder = getDao().queryBuilder();
            builder.where()
                    .eq(IConstantesDatabase.AVALIACAO_USER, avaliacao.getUsuario())
                    .and()
                    .eq(IConstantesDatabase.AVALIACAO_ESTABELECIMENTO, avaliacao.getEstabelecimento().getIdEstabelecimento());

            List<Avaliacao> avaliacoes = builder.query();
            if (avaliacoes.size() > 0) {
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
     * @param idsEstabelecimentos
     * @return
     * @throws SQLException
     */
    public boolean deleteByListIdEstabelecimento(List<Integer> idsEstabelecimentos) throws SQLException {
        try {
            DeleteBuilder<Avaliacao, Integer> builder = getDao().deleteBuilder();
            builder.where().in(IConstantesDatabase.AVALIACAO_ESTABELECIMENTO, idsEstabelecimentos);
            builder.delete();
            return true;
        } catch (Exception ex){
            throw new SQLException("Erro deletando os registros. Detalhe: " + ex.getMessage());
        }
    }
}
