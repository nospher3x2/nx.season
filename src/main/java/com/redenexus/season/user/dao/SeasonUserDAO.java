package com.redenexus.season.user.dao;

import com.google.common.collect.Lists;
import com.redenexus.season.database.MySQL;
import com.redenexus.season.database.data.Parameters;
import com.redenexus.season.database.manager.MySQLManager;
import com.redenexus.season.database.table.Table;
import com.redenexus.season.database.table.TableColumn;
import com.redenexus.season.database.table.TableRow;
import com.redenexus.season.user.data.SeasonUser;
import com.redenexus.season.user.manager.SeasonUserManager;
import com.redenexus.season.util.serializer.ItemSerializer;

import java.sql.SQLException;

/**
 * @author oNospher
 **/
public class SeasonUserDAO<U extends SeasonUser> {

    private Table table = new Table("season_collect");

    public SeasonUserDAO() {
        MySQL mySQL = MySQLManager.getMySQL("general");
        Table.setDefaultConnection(mySQL.getConnection());
    }


    public void createTable() {
        table.addColumn("username", TableColumn.UUID);
        table.addColumn("items", TableColumn.TEXT);
        table.create();
    }

    public U insert(U element) throws SQLException {
        table.insert(
                "username",
                "items"
        ).one(
                element.getUsername(),
                ItemSerializer.toBase64List(element.getItems())
        );

        return element;
    }

    public <K, V> void update(Parameters<K, V> keys, U element) throws SQLException {
        Integer affectedRows = table.update(
                keys.getKey().toString()
        ).values(
                keys.getValue().toString()
        ).where(
                "username",
                element.getUsername()
        ).execute();

        if (affectedRows <= 0) this.insert(element);
    }

    public <K extends String, V> U find(K key, V value) {
        TableRow row = table.query().where(key, value).first();

        if (row == null) {
            SeasonUser seasonUser = new SeasonUser(
                    value.toString(),
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
