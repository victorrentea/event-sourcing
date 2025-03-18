//package victor.training.sourcing;
//
//import kafka.server.KafkaConfig;
//import kafka.server.KafkaRaftServer;
//import org.apache.kafka.common.utils.Time;
//import org.apache.kafka.raft.RaftConfig;
//import scala.jdk.javaapi.CollectionConverters;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//
//public class KafkaStandaloneKRaft {
//    public static void main(String[] args) throws Exception {
//        // Define Kafka KRaft mode configuration
//        Map<String, String> config = new HashMap<>();
//        config.put(KafkaConfig.ProcessRolesProp(), "broker,controller"); // Kafka in KRaft mode
//        config.put(KafkaConfig.NodeIdProp(), "1"); // Unique node id for the broker
//        config.put(KafkaConfig.QuorumVotersProp(), "1@localhost:9093"); // Define quorum voters
//        config.put(KafkaConfig.ControllerListenerNamesProp(), "PLAINTEXT");
//        config.put(KafkaConfig.ListenersProp(), "PLAINTEXT://localhost:9092,CONTROLLER://localhost:9093"); // Broker and controller listeners
//        config.put(KafkaConfig.LogDirsProp(), "/tmp/kafka-logs"); // Log directories
//        config.put(KafkaConfig.MetadataLogDirProp(), "/tmp/kafka-metadata"); // Metadata directory for KRaft
//        config.put(KafkaConfig.NumPartitionsProp(), "1"); // Number of partitions
//        config.put(KafkaConfig.DefaultReplicationFactorProp(), "1"); // Replication factor
//
//        // Create Kafka configuration
//        KafkaConfig kafkaConfig = new KafkaConfig(CollectionConverters.asScala(config).toMap());
//
//        // Create and start Kafka server (KRaft)
//        KafkaRaftServer kafkaRaftServer = new KafkaRaftServer(kafkaConfig, Time.SYSTEM, scala.Option.apply(null));
//        kafkaRaftServer.startup(); // Start Kafka server
//
//        System.out.println("Kafka in KRaft mode is started on localhost:9092.");
//
//        // Keep the server running for demonstration purposes (replace with logic as needed)
//        Thread.sleep(Long.MAX_VALUE);
//    }
//}