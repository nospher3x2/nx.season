package com.redenexus.season.user.dao;

import com.google.common.collect.Lists;
import com.redenexus.season.database.data.Parameters;
import com.redenexus.season.database.table.TableColumn;
import com.redenexus.season.database.table.TableRow;
import com.redenexus.season.user.data.SeasonUser;
import com.redenexus.season.user.manager.SeasonUserManager;
import com.redenexus.season.util.builder.DAOBuilder;

import java.sql.SQLException;
import java.util.UUID;

/**
 * @author oNospher
 **/
public class SeasonUserDAO<U extends SeasonUser> extends DAOBuilder<U> {

    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "season_collect";
    }

    @Override
    public void createTable() {
        table.addColumn("uuid", TableColumn.UUID);
        table.addColumn("items", TableColumn.STRING);
        table.create();
    }

    @Override
    public U insert(U element) throws SQLException {
        table.insert(
                "uuid",
                "items"
        ).one(
                element.getUniqueId(),
                element.getItems()
        );
        return element;
    }

    @Override
    public <K, V> void update(Parameters<K, V> keys, U element) throws SQLException {
        Integer affectedRows = table.update(
                keys.getKey().toString()
        ).values(
                keys.getValue().toString()
        ).where(
                "uuid",
                element.getUniqueId()
        ).execute();

        if (affectedRows <= 0) this.insert(element);
    }

    @Override
    public <K, V> U findOne(K key, V value) {
        TableRow row = table.query().where((String) key, value).first();

        if (row == null) {
            SeasonUser seasonUser = new SeasonUser(
                    (UUID) value,
                    Lists.newArrayList()
            );

            SeasonUserManager.getUsers().add(seasonUser);
            return (U) seasonUser;
        }

        SeasonUser seasonUser = SeasonUserManager.toUser(row);
        SeasonUserManager.getUsers().add(seasonUser);
        return (U) seasonUser;
    }
}
