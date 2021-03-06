#/bin/bash


./zookeeper-3.4.10/bin/zkServer.sh start &

sleep 3

java -cp .:./target/lib/zookeeper-3.4.8.jar:./target/lib/slf4j-api-1.7.1.jar:./target/lib/slf4j-ext-1.7.2.jar:./target/lib/slf4j-log4j12-1.6.1.jar:./target/lib/log4j-1.2.16.jar:./target/ZooKeeper-Book-0.0.1-SNAPSHOT.jar edu.yu.distributed.DistributedCalculator.Master $1 &

sleep 5
for ((i = 1; i <= $2; i++)); do
    java -cp .:./target/lib/zookeeper-3.4.8.jar:./target/lib/slf4j-api-1.7.1.jar:./target/lib/slf4j-ext-1.7.2.jar:./target/lib/slf4j-log4j12-1.6.1.jar:./target/lib/log4j-1.2.16.jar:./target/ZooKeeper-Book-0.0.1-SNAPSHOT.jar edu.yu.distributed.DistributedCalculator.Worker $1 &
done

sleep 5
java -cp .:./target/lib/zookeeper-3.4.8.jar:./target/lib/slf4j-api-1.7.1.jar:./target/lib/slf4j-ext-1.7.2.jar:./target/lib/slf4j-log4j12-1.6.1.jar:./target/lib/log4j-1.2.16.jar:./target/ZooKeeper-Book-0.0.1-SNAPSHOT.jar edu.yu.distributed.DistributedCalculator.Client $1 $3 &
