package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;

public class Company {

	private String name;
	private Set<Worker> employees;
	private Set<Worker> available;
	private Set<Worker> assigned;
	private Set<Project> projects;
	private Set<Qualification> qualifications;

	public Company(String name) {
		if(name == null || name.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.name = name;
		employees = new HashSet<>();
		available = new HashSet<>();
		assigned = new HashSet<>();
		projects = new HashSet<>();
		qualifications = new HashSet<>();
	}

	@Override
	public boolean equals(Object other) {
		if (other.getClass() != Company.class) {
			return false;
		}
		return this.name.equals(((Company)other).getName());
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}

	public String getName() {
		return name;
	}

	public Set<Worker> getEmployedWorkers() {
		return employees;
	}

	public Set<Worker> getAvailableWorkers() {
		return null;
	}

	public Set<Worker> getUnavailableWorkers() {
		return null;
	}

	public Set<Worker> getAssignedWorkers() {
		return null;
	}

	public Set<Worker> getUnassignedWorkers() {
		Set<Worker> unassignedWorkers = new HashSet<>();
		if((employees.size() < assigned.size())){
			throw new IllegalArgumentException("Company has more assigned then employed.");
		}
		for(Worker w: employees){
			if(!assigned.contains(w)){
				unassignedWorkers.add(w);
			}
		}
		return unassignedWorkers;
	}

	public Set<Project> getProjects() {
		return null;
	}

	public Set<Qualification> getQualifications() {
		return null;
	}

	public Worker createWorker(String name, Set<Qualification> qualifications, double salary) {
		return null;
	}

	public Qualification createQualification(String description) {
		return null;
	}

	public Project createProject(String name, Set<Qualification> qualifications, ProjectSize size) {
		return null;
	}

	public void start(Project project) {
	}

	public void finish(Project project) {
	}

	public void assign(Worker worker, Project project) {
	}

	public void unassign(Worker worker, Project project) {
	}

	public void unassignAll(Worker worker) {
	}
}
