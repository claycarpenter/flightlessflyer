package com.pinguinotech.flightlessflyer.racecondition;

import java.util.Vector;

import com.pinguinotech.flightlessflyer.racecondition.IdGenerator.IdAllocationLimitException;

public class IdClient extends Thread {
	private IdGenerator		idGenerator;
	private boolean			isRunning;
	private String			name;
	protected Vector<Integer>	acquiredIds	= new Vector<Integer>();

	public IdClient(String name, IdGenerator idGenerator) {
		this.name = name;
		this.idGenerator = idGenerator;
	}

	public void run() {
		isRunning = true;

		while (isRunning) {
			try {
				int newId = idGenerator.getNextId();
				acquiredIds.add(newId);

//				System.out.println(this.toString() + " acquired ID " + newId);

				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// Do nothing but proceed with the loop.
				}
			} catch (IdAllocationLimitException e) {
				System.out
						.println("Caught exception; no more IDs to allocate. Quitting.");
				isRunning = false;
			}
		}
	}

	public String toString() {
		return "Thread " + name;
	}
}
