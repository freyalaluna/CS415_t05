package edu.colostate.cs415.model;

import java.util.Set;

import edu.colostate.cs415.dto.WorkerDTO;

public class Worker {

	public static final int MAX_WORKLOAD = 12;

	private String name;
	private double salary;
	private Set<Project> projects;
	private Set<Qualification> qualifications;

	public Worker(String name, Set<Qualification> qualifications, double salary) {
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

	public String getName() {
		return null;
	}

	public double getSalary() {
		return 0.0;
	}

	public void setSalary(double salary) {
	}

	public Set<Qualification> getQualifications() {
		return null;
	}

	public void addQualification(Qualification qualification) {
	}

	public Set<Project> getProjects() {
		return null;
	}

	public void addProject(Project project) {
	}

	public void removeProject(Project project) {
	}

	public int getWorkload() {
		return 0;
	}

	public boolean willOverload(Project project) {
		return false;
	}

	public boolean isAvailable() {
		return false;
	}

	public WorkerDTO toDTO() {
		return null;
	}
}
