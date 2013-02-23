package com.pinguinotech.flightlessflyer.racecondition;

import java.util.HashMap;
import java.util.Vector;

public class RaceConditionExample {

	private static final int	THREAD_COUNT	= 10;
	private static final int	ID_MAX			= 1 * 1000;

	public static void main(String[] args) {
		System.out.println("Multi-Threaded Counter Example");

		System.out.println("Starting " + THREAD_COUNT + " threads.");
		System.out.println("Generating " + ID_MAX + " IDs.");

		IdGenerator idGenerator = new IdGenerator(ID_MAX);
		Vector<IdClient> idClients = new Vector<IdClient>(THREAD_COUNT);
		for (int i = 1; i <= THREAD_COUNT; i++) {
			IdClient idClient = new IdClient(String.valueOf(i), idGenerator);
			idClients.add(idClient);

			idClient.start();
		}

		// Wait for the IdGenerator to finish handing out IDs.
		for (IdClient idClient : idClients) {
			while (idClient.isAlive()) {
				try {
					System.out.println(idClient + " is still alive. Waiting.");
					idClient.join(0);
				} catch (InterruptedException e) {
					// Do nothing.
				}
			}
		}

		// Print the number of IDs collected by each Thread.
		System.out.println("\n\n");
		HashMap<Integer, Vector<IdClient>> idMap = new HashMap<Integer, Vector<IdClient>>(
				ID_MAX);
		int totalIdsAcquired = 0;
		for (IdClient idClient : idClients) {
			System.out.println(idClient + " acquired "
					+ idClient.acquiredIds.size() + " IDs.");

			// Add the IdClient's acquired IDs count to the total.
			totalIdsAcquired += idClient.acquiredIds.size();

			// Sort the acquired IDs by ID value, allowing for tracking of any
			// ID that was acquired by multiple IdClients.
			for (Integer id : idClient.acquiredIds) {
				if (!idMap.containsKey(id)) {
					idMap.put(id, new Vector<IdClient>());
				}

				idMap.get(id).add(idClient);
			}
		}

		System.out.println("\nID Acquisition Errors:\n");
		if (totalIdsAcquired != ID_MAX) {
			System.out.println(ID_MAX + " IDs were available; "
					+ totalIdsAcquired + " IDs were acquried by clients.");
		}
		for (int i = 1; i < ID_MAX; i++) {
			if (!idMap.containsKey(i)) {
				System.out.println("ID " + i + " was never acquired.");
			} else if (idMap.get(i).size() > 1) {
				System.out.println("ID " + i
						+ " was acquired by multiple clients: " + idMap.get(i));
			}
		}
	}
}
