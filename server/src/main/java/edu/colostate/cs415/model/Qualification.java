package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;

import edu.colostate.cs415.dto.QualificationDTO;

public class Qualification {

	private String description;
	private Set<Worker> workers;

	public Qualification(String description) {
		this.description = description;
		this.workers = new HashSet<Worker>();
	}

	@Override
	public boolean equals(Object other) {
		if(other == null || other.getClass() != Qualification.class || ((Qualification)other).toString() == null){
			return false;
		}
		return this.description.equals(((Qualification)other).toString());
	}
 
	@Override
	public int hashCode() {
		if(description == null || description.isEmpty()){
			return 0;
		}
		return description.hashCode();
	}

	@Override
	public String toString() {
		return description;
	}

	public Set<Worker> getWorkers() {
		return workers;
	}

	public void addWorker(Worker worker) {
		workers.add(worker);
	}

	public void removeWorker(Worker worker) {
		workers.remove(worker);
	}

	public QualificationDTO toDTO() {
		return null;
	}
}
