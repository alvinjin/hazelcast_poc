package com.hazelcast.hbase;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.TransactionalMap;
import com.hazelcast.transaction.*;
import com.hazelcast.hbase.User;
import org.jruby.RubyProcess;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by cjin on 15-04-08.
 */
public class HCTransactionTest {

    public static void main(String[] args) {
       HazelcastInstance hz = Hazelcast.newHazelcastInstance();
       TransactionOptions txOptions = new TransactionOptions()
                .setTimeout(10, TimeUnit.MILLISECONDS)
                .setTransactionType(TransactionOptions.TransactionType.TWO_PHASE)
                .setDurability(1);

        ClientConfig clientConfig = new ClientConfig();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        //TransactionContext txCxt = client.newTransactionContext(txOptions);
        TransactionContext txCxt = hz.newTransactionContext();

        User user1 = new User();
        user1.setName("UserA");
        user1.setAge(29);
        user1.setLocation("Toronto");
        user1.setBalance(1000);

        //the second user
        User user2 = new User();
        user2.setName("UserB");
        user2.setAge(30);
        user2.setLocation("Toronto");
        user2.setBalance(2000);



        txCxt.beginTransaction();

        TransactionalMap<String,User> wallets = txCxt.getMap("wallets");
        try {

            wallets.put("id1", user1);
            wallets.put("id2", user2);

            User from = wallets.get("id1");
            User to = wallets.get("id2");


            from.rechargeMoney(-100);
            to.rechargeMoney(100);

            wallets.put("id1", from);
            wallets.put("id2", to);

            for (String id : wallets.keySet()) {
                System.out.println(wallets.get(id));
            }
            txCxt.commitTransaction();

        } catch (Throwable t) {
            txCxt.rollbackTransaction();
        }

        IMap<String, User> wallet_map = hz.getMap("wallets");
        if(wallet_map.isEmpty())
            System.out.println("wallets is empty");

        for (String id : wallet_map.keySet()) {
            System.out.println(wallet_map.get(id));
        }


        System.exit(0);

    }
}
