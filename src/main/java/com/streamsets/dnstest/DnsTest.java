package com.streamsets.dnstest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DnsTest {

	private static boolean quietMode;
	private static String[] hostnames;
	private static int numThreads;
	private static int sleepMillis;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	
	private static void printMessage(String message) {
		System.out.println(sdf.format(new Date()) + ": " + message);
	}

	public static void main(String[] args) {
		
		String hostnamesString = null;

		if (args.length != 4) {
			System.out.println("Error: Wrong number of args");
			System.out.println(
					"Usage: $ java -jar dnstest-1.0.0.jar <HOSTNAME> <NUM_THREADS> <SLEEP_MILLIS> <QUIET_MODE>");
			System.out.println("Usage Example: $ java -jar dnstest-1.0.0.jar microsoft.com,google.com 5 100 false");
			System.exit(-1);
		} else {
			hostnamesString = args[0];
			hostnames = makeArray(hostnamesString);
			numThreads = Integer.parseInt(args[1]);
			sleepMillis = Integer.parseInt(args[2]);
			quietMode = args[3].toLowerCase().equals("true");
		}

		System.out.println("-----------------------------------");
		printMessage("Starting dnstest utility:");
		printMessage(" - hostname(s) " + hostnamesString);
		printMessage(" - numThreads = " + numThreads);
		printMessage(" - sleepMillis = " + sleepMillis);
		if(quietMode) {
			printMessage(" - quiteMode is ON");
		} else {
			printMessage(" - quiteMode is OFF");
		}
		
		System.out.println("-----------------------------------");

		for (int n = 0; n < numThreads; n++) {
			new DnsTestRunnable(hostnames, "Thread-" + n, sleepMillis, quietMode).start();
		}
	}

	private static String[] makeArray(String hostnames) {
		String[] hostnamesArray = hostnames.split("[,]", 0);
		for (int i = 0; i < hostnamesArray.length; i++) {
			hostnamesArray[i] = hostnamesArray[i].trim();
		}
		return hostnamesArray;

	}
}
