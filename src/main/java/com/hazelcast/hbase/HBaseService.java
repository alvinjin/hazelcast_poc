package com.hazelcast.hbase;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.conf.Configuration;
import java.io.IOException;


public class HBaseService {
    private static HBaseService ourInstance = new HBaseService();
    private HTable htable;
    private Configuration config = HBaseConfiguration.create();

    public static HBaseService getInstance() {
        return ourInstance;
    }

    public HTable getHtable() {
        return htable;
    }

    public void setHtable(HTable htable) {
        this.htable = htable;
    }

    private HBaseService() {
        try {
            htable = new HTable(config, "user");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
