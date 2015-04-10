package com.hazelcast.hbase;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.conf.Configuration;
import java.io.IOException;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;


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

            config.setInt("timeout", 120000);
            config.set("hbase.master", "*" + "127.0.0.1" + ":9000*");
            config.set("hbase.zookeeper.quorum", "127.0.0.1");
            config.set("hbase.zookeeper.property.clientPort", "2181");


            HBaseAdmin admin = new HBaseAdmin(config);

            // Instantiating table descriptor class
            HTableDescriptor tableDescriptor = new HTableDescriptor("paytm");

            // Adding column families to table descriptor
            tableDescriptor.addFamily(new HColumnDescriptor("account"));

            // Execute the table through admin
            admin.createTable(tableDescriptor);

            htable = new HTable(config, "paytm");
            System.out.println("Table created");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
