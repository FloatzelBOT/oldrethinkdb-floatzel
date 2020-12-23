package com.eziosoft.oldrethink;

import com.google.gson.Gson;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

public class DbCode implements GenaricDatabase {

    private static Cursor cur = null;
    private static final RethinkDB r = RethinkDB.r;
    private static Connection thonk;
    private static Gson gson = new Gson();
    private ConnInfo info;

    // direct carryover from floatzel's codebase
    private static String banktable = "bank";
    private static String loantable = "loan";
    private static String bloanperm = "bloan";
    private static String stocktable = "stocks";
    private static String stockbuy = "boughtstock";
    private static String tweets = "tweets";
    private static String tagperm = "gtagperm";
    private static String tags = "tags";
    private static String ass = "ass";

    // create all the tables!
    private static void makeTables(){
        // run a bunch of rethink commands
        r.tableCreate(banktable).run(thonk);
        r.tableCreate(loantable).run(thonk);
        r.tableCreate(bloanperm).run(thonk);
        r.tableCreate(stocktable).run(thonk);
        r.tableCreate(tweets).run(thonk);
        r.tableCreate(tagperm).run(thonk);
        r.tableCreate(tags).run(thonk);
        r.tableCreate(stockbuy).run(thonk);
        r.tableCreate(ass).run(thonk);
    }


    @Override
    public void Conninfo(String info) {
        this.info = gson.fromJson(info, ConnInfo.class);
    }

    @Override
    public String getProfile(String id) {
        return null;
    }

    @Override
    public void saveProfile(String json) {

    }

    @Override
    public void initDatabase() {
        // lifted straight from floatzel 2.5.6.4's codebase, with minor tweaks
        Connection.Builder builder = r.connection().hostname("localhost").port(28015);
        if (!info.getUser().equals("null")) {
            builder.user(info.getUser(), !info.getPass().equals("null") ? info.getPass() : "");
        } else {
            builder.user("admin", !info.getPass().equals("null") ? info.getPass() : "");
        }

        thonk = builder.connect();
        System.out.println("Eziosoft RethinkDB Driver v1.0 now starting up...");
        // first, check if the database file exists
        if (!(boolean) r.dbList().contains("floatzel").run(thonk)){
            System.out.println("No database found! Creating a new db!");
            // okay, it hasnt been initalized yet, so do that
            r.dbCreate("floatzel").run(thonk);
            thonk.use("floatzel");
            System.out.println("Creating tables...");
            makeTables();
        } else {
            // set the default db for rethonk
            thonk.use("floatzel");
            System.out.println("ReThinkDB started!");
        }
        return;
    }
}