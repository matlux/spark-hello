
## How to use?

```bash
sbt assembly
./run-on-yarn.sh
```

## How to check the job on Yarn

From command line it's possible to display the log:
```bash
yarn logs -applicationId <application ID>
```

From the Yarn UI, browse:
http://<yarn node manager host>:8088/


## How to kill the job

```
yarn application -kill <application ID>
```

## How to execute through spark-submit 

To execute the local driver:


```
spark-submit --master yarn-client --class SimpleApp --driver-class-path "/etc/hbase/conf:/etc/hadoop/conf:$(hbase classpath | tr ':' '\n' | grep htrace-core-3.1.0-incubating.jar)" --verbose --conf spark.driver.userClassPathFirst=false target/scala-2.10/simple-project-shaded.jar
```
