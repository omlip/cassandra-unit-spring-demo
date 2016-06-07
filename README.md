> Coffee. The finest organic suspension ever devised... I beat the Borg with it.
> - Captain Janeway

**bold**
*italic*

```java
if (isAwesome){
  return true;
}
```

`var machine = truc`

Jour | Nuit
-----|------
9h|21h


# Write integration tests with Spring Boot and Cassandra in 2 minutes


### context why goal
The following document will describe an how-to write integration tests with Spring-Boot against an embedded Cassandra database with the use of cassandra-unit framework.


### cassandra-unit
Cassandra-unit, as indicated by its name, is a unit testing library wich add to your test the ability to start/stop a Cassandra database server and also inject a CQL dataset into it.

The project provides two modules:
- **cassandra-unit** : the core, contains everything to start the server, injection of dataset etc...
- **cassandra-unit-spring** : A library which fill the gap between Spring-Boot dependency injection and the Cassandra related stuff. The second ones include the first.

More info on the project [GitHub's page](https://github.com/jsevellec/cassandra-unit)

### installation

#### groovy configuration


### usage

### examples

if you got this ...
```java
java.lang.NoSuchMethodError: com.codahale.metrics.Snapshot: method <init>()V not found
	at com.codahale.metrics.UniformSnapshot.<init>(UniformSnapshot.java:39) ~[metrics-core-3.1.0.jar:3.0.2]
	at org.apache.cassandra.metrics.EstimatedHistogramReservoir$HistogramSnapshot.<init>(EstimatedHistogramReservoir.java:77) ~[cassandra-all-2.2.2.jar:2.2.2]
	at org.apache.cassandra.metrics.EstimatedHistogramReservoir.getSnapshot(EstimatedHistogramReservoir.java:62) ~[cassandra-all-2.2.2.jar:2.2.2]
	at com.codahale.metrics.Histogram.getSnapshot(Histogram.java:54) ~[metrics-core-3.0.2.jar:3.0.2]
	at com.codahale.metrics.Timer.getSnapshot(Timer.java:142) ~[metrics-core-3.0.2.jar:3.0.2]
	at org.apache.cassandra.db.ColumnFamilyStore$3.run(ColumnFamilyStore.java:435) ~[cassandra-all-2.2.2.jar:2.2.2]
	at org.apache.cassandra.concurrent.DebuggableScheduledThreadPoolExecutor$UncomplainingRunnable.run(DebuggableScheduledThreadPoolExecutor.java:118) ~[cassandra-all-2.2.2.jar:2.2.2]
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511) [na:1.8.0_25]
	at java.util.concurrent.FutureTask.runAndReset(FutureTask.java:308) [na:1.8.0_25]
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$301(ScheduledThreadPoolExecutor.java:180) [na:1.8.0_25]
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:294) [na:1.8.0_25]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [na:1.8.0_25]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [na:1.8.0_25]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_25]

```

Exclude from the configuration de *com.codahale.metrics* dependency
```gradle
configurations {
	all*.exclude group: 'com.codahale.metrics'
}
```
