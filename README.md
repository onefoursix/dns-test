# dns-test

This project is a tiny utility that can be used to stress-test DNS lookups from a Java-based app.
It takes one or more hostnames, and will lookup the IPs using a user-defined number of concurrent threads, with a user-defined sleep interval between calls in any given thread. If any <code>unknownHostExceptions</code> are thrown, they will be captured in the utility's log

## Building the Utility

You can build the project using [maven](https://maven.apache.org/) using the following commands:

```
# Set this to point to your desired JDK. For example, to use JDK8 on macOS:
$ export JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home 

# Build the jar file 
$ mvn package -DskipTests 
```

That will give you the jar file <code>target/dnstest-1.0.0.jar</code>

A [build.sh](build.sh) file is provided as well.

A pre-built jar file, built using JDK8, is also included [here](dnstest-1.0.0.jar) for your convenience.




## Configuring the Utility
The utility takes four arguments:

1) Either a single hostname or a comma-delimited list of hostnames the utiolity will cycle through.
2) The number of threads to spawn
3) The millis to sleep between subsequent calls within each thread
4) quietMode - set this to <code>true</code> or <code>false</code>.  (See below for an explanation)


Here is how to launch the utility:

```
$ java -jar dnstest-1.0.0.jar <HOSTNAME> <NUM_THREADS> <SLEEP_MILLIS> <QUIET_MODE>
```

For example, here is a command that will make calls to resolve three hostnames, using 5 threads with 1000 millis sleep between calls, with <code>quietMode</code> disabled:

```
$ java -jar dnstest-1.0.0.jar microsoft.com,google.com,apple.com 5 1000 false

```

If one started the utility with that command, the output would look like this:

```
$ java -jar dnstest-1.0.0.jar microsoft.com,google.com,apple.com 5 1000 false

-----------------------------------
2024-06-17 18:35:34.754: Starting dnstest utility:
2024-06-17 18:35:34.754:  - hostname(s) microsoft.com,google.com,apple.com
2024-06-17 18:35:34.754:  - numThreads = 5
2024-06-17 18:35:34.754:  - sleepMillis = 1000
2024-06-17 18:35:34.754:  - quiteMode is OFF
-----------------------------------
2024-06-17 18:35:34.755: Thread-0: Thread is running
2024-06-17 18:35:34.755: Thread-1: Thread is running
2024-06-17 18:35:34.755: Thread-2: Thread is running
2024-06-17 18:35:34.755: Thread-4: Thread is running
2024-06-17 18:35:34.755: Thread-3: Thread is running
2024-06-17 18:35:34.807: Thread-1: microsoft.com/20.70.246.20
2024-06-17 18:35:34.807: Thread-2: microsoft.com/20.70.246.20
2024-06-17 18:35:35.841: Thread-3: google.com/142.250.191.46
2024-06-17 18:35:35.841: Thread-4: google.com/142.250.191.46
2024-06-17 18:37:05.370: Thread-2: apple.com/17.253.144.10
2024-06-17 18:37:05.370: Thread-0: apple.com/17.253.144.10
...
```

## Test the Utility

To test the utility, you should run it in the foreground using the command above with <code>quietMode</code> set to false, and pipe the output to a log like this:

```
$ java -jar dnstest-1.0.0.jar microsoft.com,google.com,apple.com 5 1000 false > /tmp/dns-test.log

```
Make sure the file <code>/tmp/dns-test.log</code> has output like that shown above.


## Run the Utility
To run the utility for real, set <code>quietMode</code> to true, and run the utility as a background process as shown below. As before, pipe the output to a log file, so that if any <code>unknownHostExceptions</code> are thrown they will be captured in the log. Adjust the number of threads and sleepMillis as you see fit, while being careful not to crash the JVM or be flagged for a DDOS of your DNS servers.

For example, here is a command that launches the utility as a background process with  <code>quietMode</code> set to true, with 10 threads and a 20ms sleep time:

```
$ nohup java -jar dnstest-1.0.0.jar microsoft.com,google.com,apple.com 10 20 true > /tmp/dns-test.log &
```
You should see the PID of the launched process:

```
$ nohup java -jar dnstest-1.0.0.jar microsoft.com,google.com,apple.com 10 20 true > /tmp/dns-test.log &
[1] 6157
```

Cat the log file to make sure the expected startup messages are there, but no other output, for example, like this:

```
$ cat /tmp/dns-test.log

-----------------------------------
2024-06-17 18:55:06.207: Starting dnstest utility:
2024-06-17 18:55:06.207:  - hostname(s) microsoft.com,google.com,apple.com
2024-06-17 18:55:06.207:  - numThreads = 10
2024-06-17 18:55:06.207:  - sleepMillis = 20
2024-06-17 18:55:06.207:  - quiteMode is ON
-----------------------------------
2024-06-17 18:55:06.208: Thread-0: Thread is running
2024-06-17 18:55:06.208: Thread-2: Thread is running
2024-06-17 18:55:06.208: Thread-1: Thread is running
2024-06-17 18:55:06.208: Thread-3: Thread is running
2024-06-17 18:55:06.208: Thread-5: Thread is running
2024-06-17 18:55:06.208: Thread-4: Thread is running
2024-06-17 18:55:06.208: Thread-6: Thread is running
2024-06-17 18:55:06.208: Thread-7: Thread is running
2024-06-17 18:55:06.208: Thread-8: Thread is running
2024-06-17 18:55:06.208: Thread-9: Thread is running
```

You can periodically check that file to see if any <code>unknownHostExceptions</code> are caught.

Stop the test by killing the process.  You can find the process' PID like this:

```
$ ps -ef | grep dnstest
502  6157 97709   0  6:55PM ttys000    0:02.35 /usr/bin/java -jar dnstest-1.0.0.jar microsoft.com,google.com,apple.com 10 20 true
```

And then:

```
$ kill 6157 97709
[1]  + exit 143   nohup java -jar dnstest-1.0.0.jar microsoft.com,google.com,apple.com 10 20  >
```





