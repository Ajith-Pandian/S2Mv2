package com.example.domainlayer.database;


import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

/**
 * Created by thoughtchimp on 12/6/2016.
 */


public  class  CustomDao<T, ID> extends BaseDaoImpl<T, ID> {
     public CustomDao(final Class<T> dataClass) throws SQLException {
        super(dataClass);
    }

    public CustomDao(final ConnectionSource connectionSource, final Class<T> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public CustomDao(final ConnectionSource connectionSource, final DatabaseTableConfig<T> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public int create(final T data) throws SQLException {
        int result = super.create(data);
        // Send an event with EventBus or Otto
        return result;
    }
}