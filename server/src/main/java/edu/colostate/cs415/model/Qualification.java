package edu.colostate.cs415.model;

import java.util.Set;

import edu.colostate.cs415.dto.QualificationDTO;

public class Qualification {

	private String description;
	private Set<Worker> workers;

	public Qualification(String description) {
	}

	@Override
	public boolean equals(Object other) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}

	public Set<Worker> getWorkers() {
		return null;
	}

	public void addWorker(Worker worker) {
	}

	public void removeWorker(Worker worker) {
	}

	public QualificationDTO toDTO() {
		return null;
	}
}
