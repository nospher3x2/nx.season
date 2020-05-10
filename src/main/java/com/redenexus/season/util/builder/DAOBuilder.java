package com.redenexus.season.util.builder;

import com.redenexus.season.database.MySQL;
import com.redenexus.season.database.data.Parameters;
import com.redenexus.season.database.manager.MySQLManager;
import com.redenexus.season.database.table.Table;

import java.sql.SQLException;

/**
 * @author oNospher
 **/
public abstract class DAOBuilder<T> {

    public Table table = new Table(this.getTableName());

    public DAOBuilder() {
        MySQL mySQL = MySQLManager.getMySQL(this.getDatabaseName());
        Table.setDefaultConnection(mySQL.getConnection());
    }

    public abstract String getDatabaseName();

    public abstract String getTableName();

    public abstract void createTable();

    public abstract T insert(T element) throws SQLException;

    public abstract <K, V> void update(Parameters<K, V> keys, T element) throws SQLException;

    public abstract <K, V> T findOne(K key, V value);
}

