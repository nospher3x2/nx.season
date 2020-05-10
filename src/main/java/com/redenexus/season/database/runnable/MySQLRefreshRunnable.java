package com.redenexus.season.database.runnable;

import com.redenexus.season.database.manager.MySQLManager;

/**
 * @author oNospher
 **/
public class MySQLRefreshRunnable implements Runnable {

    @Override
    public void run() {
        new MySQLManager().refresh();
    }
}
