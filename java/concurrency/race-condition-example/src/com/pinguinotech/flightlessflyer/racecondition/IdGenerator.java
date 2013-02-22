package com.pinguinotech.flightlessflyer.racecondition;

public class IdGenerator {
	private int	allocationLimit;
	private int	idCounter;

	public IdGenerator(int allocationLimit) {
		this.allocationLimit = allocationLimit;
	}

	public class IdAllocationLimitException extends Exception {
		public IdAllocationLimitException() {
			super("Unable to allocate more than " + allocationLimit + " IDs");
		}
	}
	
	public boolean hasMoreIds() {
		return (idCounter + 1) < allocationLimit;
	}

	public int getNextId() throws IdAllocationLimitException {
		if (!hasMoreIds()) {
			throw new IdAllocationLimitException();
		}

		idCounter++;

		return idCounter;
	}

}
