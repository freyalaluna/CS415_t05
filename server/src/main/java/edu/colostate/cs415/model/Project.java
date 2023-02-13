package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;

import edu.colostate.cs415.dto.ProjectDTO;

public class Project {

	private String name;
	private ProjectSize size;
	private ProjectStatus status;
	private Set<Worker> workers;
	private Set<Qualification> qualifications;

	public Project(String name, Set<Qualification> qs, ProjectSize size) throws IllegalArgumentException {
		if (size == null || name == null || qs == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.qualifications = qs;
		this.size = size;
		this.status = ProjectStatus.PLANNED;
		this.workers = new HashSet<>();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != Project.class) {
			return false;
		}
		return this.name.equals(((Project)other).getName());
	}

	@Override
	public int hashCode() {
		if (name == null || name.isEmpty()) {
			return 0;
		}

		return name.hashCode();
	}

	@Override
	public String toString() {
		return this.name + ":" + this.workers.size() + ":" + this.status;
	}

	public String getName() {
		return this.name;
	}

	public ProjectSize getSize() {
		return this.size;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		if (status == null) {
			throw new IllegalArgumentException();
		}
		this.status = status;
	}

	public void addWorker(Worker worker) {
		if(worker == null){
			throw new IllegalArgumentException();
		}
		this.workers.add(worker);
	}

	public void removeWorker(Worker worker) {
		if(worker == null){
			throw new IllegalArgumentException();
		}
		workers.remove(worker);
	}

	public Set<Worker> getWorkers() {
		return this.workers;
	}

	public void removeAllWorkers() {
		workers.removeAll(workers);
	}

	public Set<Qualification> getRequiredQualifications() {
		return qualifications;
	}

	public void addQualification(Qualification qualification) {
		if(qualification == null){
			throw new IllegalArgumentException();
		}
		qualifications.add(qualification);
	}

	public Set<Qualification> getMissingQualifications() {
		Set<Qualification> presentQualifications = new HashSet<Qualification>();
		Set<Qualification> missingQualifications = new HashSet<Qualification>(qualifications);
		for(Worker w : workers){
			presentQualifications.addAll(w.getQualifications());
		}
		missingQualifications.removeAll(presentQualifications);
		
		return missingQualifications;
	}

	public boolean isHelpful(Worker worker) {
		if(worker == null){
			throw new IllegalArgumentException();
		}
		for(Qualification mq : getMissingQualifications()){
			if(worker.getQualifications().contains(mq)){
				return true;
			}
		}
		return false;
	}

	public ProjectDTO toDTO() {
		return null;
	}
}
