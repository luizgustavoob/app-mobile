package br.com.paraondeirapp.persistence.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;

import br.com.paraondeirapp.entity.Avaliacao;
import br.com.paraondeirapp.entity.Cidade;
import br.com.paraondeirapp.entity.Endereco;
import br.com.paraondeirapp.entity.Estabelecimento;
import br.com.paraondeirapp.entity.Estado;
import br.com.paraondeirapp.interfaces.IConstantesDatabase;

public class DatabaseHelper<E> extends OrmLiteSqliteOpenHelper {

    public DatabaseHelper(Context ctx) {
        super(ctx, IConstantesDatabase.NAME_DATABASE, null, IConstantesDatabase.VERSION_DATABASE);
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