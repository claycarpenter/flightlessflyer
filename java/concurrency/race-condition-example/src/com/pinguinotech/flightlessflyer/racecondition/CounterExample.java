package com.pinguinotech.flightlessflyer.racecondition;

import java.util.HashMap;
import java.util.Vector;

public class CounterExample {

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
		while (idGenerator.hasMoreIds()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// Do nothing.
			}
		}

		// Print the number of IDs collected by each Thread.
		System.out.println("\n\n");
		HashMap<Integer, Vector<IdClient>> idMap = new HashMap<Integer, Vector<IdClient>>(
				ID_MAX);
		for (IdClient idClient : idClients) {
			System.out.println(idClient + " acquired "
					+ idClient.acquiredIds.size() + " IDs.");

			for (Integer id : idClient.acquiredIds) {
				if (!idMap.containsKey(id)) {
					idMap.put(id, new Vector<IdClient>());
				}

				idMap.get(id).add(idClient);
			}
		}

		// Tabulate and sort the IDs.
		System.out.println("\nIDs Acquired:\n");
//		for (Integer id : idMap.keySet()) {
//			System.out.println("ID " + id + " acquired by " + idMap.get(id));
//		}

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
