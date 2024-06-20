package com.streamsets.dnstest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DnsTestRunnable implements Runnable {

	private Thread t;
	private int sleepMillis;
	private String[] hostnames;
	private String threadName;
	private boolean quietMode;

	DnsTestRunnable(String[] hostnames, String threadName, int sleepMillis, boolean quietMode) {
		this.sleepMillis = sleepMillis;
		this.hostnames = hostnames;
		this.threadName = threadName;
		this.quietMode = quietMode;
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	private void printMessage(String message) {
		System.out.println(sdf.format(new Date()) + ": " + threadName + ": " + message);
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

	@Override
	public void run() {

		printMessage("Thread is running");

		while (true) {

			for (int i = 0; i < hostnames.length; i++) {

				String hostname = hostnames[i];

				try {

					// Get the IP address for the hostname
					InetAddress[] iaddress = InetAddress.getAllByName(hostname);

					if (!quietMode) {

						for (InetAddress ipaddresses : iaddress) {
							printMessage(ipaddresses.toString());
						}
					}

				} catch (UnknownHostException e) {

					printMessage(e.getMessage());
					printMessage(e.toString());
					System.out.println(e.getStackTrace());
				} finally {
				    try {
						Thread.sleep(sleepMillis);
					} catch (InterruptedException e) {
						// ignore any exceptions caught here
					}
				}
			}
		}
	}
}
