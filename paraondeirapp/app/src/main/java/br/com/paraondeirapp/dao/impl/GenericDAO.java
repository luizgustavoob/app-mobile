package br.com.paraondeirapp.dao.impl;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.List;

import br.com.paraondeirapp.dao.helper.DatabaseHelper;

public abstract class GenericDAO<E> extends DatabaseHelper<E> {

    protected Dao<E, Integer> dao;

    public GenericDAO(Context ctx, Class<E> type) throws SQLException {
        super(ctx);

        try {
            dao = DaoManager.createDao(getConnectionSource(), type);
        } catch (SQLException ex){
            throw new SQLException(ex.getMessage());
        }
    }

    public E findByID(int id) throws SQLException {
        try {
            E obj = dao.queryForId(id);
            return obj;
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
    }

    public List<E> findAll() throws SQLException {
        try {
            List<E> list = dao.queryForAll();
            return list;
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
    }

    public boolean save(E obj) throws SQLException {
        try {
            int numLinesChanged = dao.createOrUpdate(obj).getNumLinesChanged();
            return numLinesChanged > 0;
        } catch (SQLException ex){
            throw new SQLException(ex.getMessage());
        }

    }

    public boolean insert(E obj) throws SQLException {
        try {
            int result = dao.create(obj);
            return result > 0;
        } catch (SQLException ex){
            throw new SQLException(ex.getMessage());
        }
    }

    public boolean delete(E obj) throws SQLException {
        try {
            int result = dao.delete(obj);
            return result > 0;
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
    }

    public boolean update(E obj) throws SQLException {
        try {
            int result = dao.update(obj);
            return result > 0;
        } catch (SQLException ex){
            throw new SQLException(ex.getMessage());
        }
    }

    public Dao<E, Integer> getDao() {
        return dao;
    }
}
