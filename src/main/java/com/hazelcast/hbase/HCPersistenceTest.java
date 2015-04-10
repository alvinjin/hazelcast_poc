package com.hazelcast.hbase;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import java.util.Properties;
import java.util.UUID;

public class HCPersistenceTest {
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(getConfig());
        IMap<String, User> map = hz.getMap("wallets");
        System.out.println( map.get("id1") );
        User user = new User();
        user.setName("UserA");
        user.setAge(29);
        user.setLocation("Toronto");
        user.setBalance(100);

        map.put("id1",user);

        User user2 = new User();
        user2.setName("UserB");
        user2.setAge(30);
        user2.setLocation("Toronto");
        user2.setBalance(200);

        map.put("id2",user2);
       // System.out.println( map.get("id1") );
       // System.out.println( map.get("id2") );
        map.remove("id1");
       // System.out.println( map.get("id2") );

    }

    public static Config getConfig() {

        final Config cfg = new Config();
        cfg.setInstanceName(UUID.randomUUID().toString());

        final Properties props = new Properties();
        props.put("hazelcast.rest.enabled", false);
        props.put("hazelcast.logging.type", "slf4j");
        props.put("hazelcast.connect.all.wait.seconds", 45);
        props.put("hazelcast.operation.call.timeout.millis", 30000);

        // group configuration
        cfg.setGroupConfig(new GroupConfig("test-group", "test"));
        // network configuration initialization
        final NetworkConfig netCfg = new NetworkConfig();
        netCfg.setPortAutoIncrement(true);
        netCfg.setPort(5701);
        // multicast
        final MulticastConfig mcCfg = new MulticastConfig();
        mcCfg.setEnabled(false);
        // tcp
        final TcpIpConfig tcpCfg = new TcpIpConfig();
        tcpCfg.addMember("127.0.0.1");
        tcpCfg.setEnabled(true);
        // network join configuration
        final JoinConfig joinCfg = new JoinConfig();
        joinCfg.setMulticastConfig(mcCfg);
        joinCfg.setTcpIpConfig(tcpCfg);
        netCfg.setJoin(joinCfg);
        // ssl
        netCfg.setSSLConfig(new SSLConfig().setEnabled(false));

        // Adding mapstore
        String storeType = "wallets";
        final MapConfig mapCfg = cfg.getMapConfig(storeType);

        HBaseMapStore store = new HBaseMapStore();
        final MapStoreConfig mapStoreCfg = new MapStoreConfig();
        mapStoreCfg.setImplementation(store);
        mapStoreCfg.setWriteDelaySeconds(1);
        // to load all map at same time
        mapStoreCfg.setInitialLoadMode(MapStoreConfig.InitialLoadMode.EAGER);
        mapCfg.setMapStoreConfig(mapStoreCfg);
        cfg.addMapConfig(mapCfg);
        return cfg;
    }

}