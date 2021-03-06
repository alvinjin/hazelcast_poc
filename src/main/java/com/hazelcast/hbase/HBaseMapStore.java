package com.hazelcast.hbase;

import com.hazelcast.core.MapStore;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


public class HBaseMapStore implements MapStore<String, User> {
    private Logger logger;



    @Override
    public synchronized User load(String key) {
        User user = new User();
        HTable htable = HBaseService.getInstance().getHtable();
        try {
            Get get = new Get(Bytes.toBytes(key));
            Result r = htable.get(get);
            if(r.isEmpty())
                return null;
            byte[] bname = r.getValue(Bytes.toBytes("account"), Bytes.toBytes("name"));
            byte[] blocation = r.getValue(Bytes.toBytes("account"), Bytes.toBytes("location"));
            byte[] bage = r.getValue(Bytes.toBytes("account"), Bytes.toBytes("age"));
            byte[] bbalance = r.getValue(Bytes.toBytes("account"), Bytes.toBytes("balance"));

            user.setName(Bytes.toString(bname));
            user.setLocation(Bytes.toString(blocation));
            user.setAge(Bytes.toInt(bage));
            user.setBalance(Bytes.toInt(bbalance));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }


    @Override
    public synchronized void store(String key, User user) {
        HTable htable = HBaseService.getInstance().getHtable();
        Put put = new Put(Bytes.toBytes(key));
        put.add(Bytes.toBytes("account"), Bytes.toBytes("name"), Bytes.toBytes(user.getName()));
        put.add(Bytes.toBytes("account"), Bytes.toBytes("age"), Bytes.toBytes(user.getAge()));
        put.add(Bytes.toBytes("account"), Bytes.toBytes("location"), Bytes.toBytes(user.getLocation()));
        put.add(Bytes.toBytes("account"), Bytes.toBytes("balance"), Bytes.toBytes(user.getBalance()));

        try {
            htable.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void storeAll(Map<String, User> userMap) {
        HTable htable = HBaseService.getInstance().getHtable();
        List<Row> rlist = new LinkedList<Row>();
        try {
            for (String key : userMap.keySet()) {
                Put put = new Put(Bytes.toBytes(key));
                User user = userMap.get(key);
                put.add(Bytes.toBytes("account"), Bytes.toBytes("name"), Bytes.toBytes(user.getName()));
                put.add(Bytes.toBytes("account"), Bytes.toBytes("age"), Bytes.toBytes(user.getAge()));
                put.add(Bytes.toBytes("account"), Bytes.toBytes("location"), Bytes.toBytes(user.getLocation()));
                put.add(Bytes.toBytes("account"), Bytes.toBytes("balance"), Bytes.toBytes(user.getBalance()));

                rlist.add(put);
            }
            htable.batch(rlist);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void delete(String key) {
        HTable htable = HBaseService.getInstance().getHtable();
        Delete delete = new Delete(Bytes.toBytes(key));
        try {
            htable.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteAll(Collection<String> keys) {
        HTable htable = HBaseService.getInstance().getHtable();
        List<Row> rlist = new LinkedList<Row>();
        try {
            for (String key : keys) {
                Delete delete = new Delete(Bytes.toBytes(key));
                rlist.add(delete);
            }
            htable.batch(rlist);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Map<String, User> loadAll(Collection<String> strings) {
        // will not be implemented
        return null;
    }

    @Override
    public Set<String> loadAllKeys() {
        // will not be implemented
        return null;
    }
}
