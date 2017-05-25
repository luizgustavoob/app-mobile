package br.com.paraondeirapp.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.model.Cidade;
import br.com.paraondeirapp.model.Endereco;
import br.com.paraondeirapp.model.Estabelecimento;
import br.com.paraondeirapp.model.Estado;
import br.com.paraondeirapp.constantes.IConstantesDatabase;

public class DatabaseHelper<E> extends OrmLiteSqliteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, IConstantesDatabase.NOME_DATABASE, null, IConstantesDatabase.VERSAO_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Estado.class);
            TableUtils.createTable(connectionSource, Cidade.class);
            TableUtils.createTable(connectionSource, Endereco.class);
            TableUtils.createTable(connectionSource, Estabelecimento.class);
            TableUtils.createTable(connectionSource, Avaliacao.class);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {

        // Executa ao atualizar o banco.
    }

    @Override
    public void close() {
        super.close();
    }
}