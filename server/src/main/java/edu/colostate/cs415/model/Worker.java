package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;
import edu.colostate.cs415.dto.WorkerDTO;

public class Worker {

	public static final int MAX_WORKLOAD = 12;

	private String name;
	private double salary;
	private Set<Project> projects;
	private Set<Qualification> qualifications;

	public Worker(String name, Set<Qualification> qualifications, double salary) {
		this.name = name;
		this.qualifications = qualifications;
		this.salary = salary;
		this.projects = new HashSet<Project>();
	}

	@Override
	public boolean equals(Object other) {
		if(other == null || other.getClass() != Worker.class || ((Worker)other).getName() == null){
			return false;
		}
		return this.name.equals(((Worker)other).getName());
	}

	@Override
	public int hashCode() {
		if(name.isEmpty() || name == null){
			return 0;
		}
		return name.hashCode();
	}

	@Override
	public String toString() {
		return this.name + ":" + this.projects.size() + ":" + this.qualifications.size() + ":" + ((int)this.salary);
	}

	public String getName() {
		return this.name;
	}

	public double getSalary() {
		return this.salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Set<Qualification> getQualifications() {
		return this.qualifications;
	}

	// NOTE: caller's responsibility to ensure that this qualification is from the company's set of qualifications
	public void addQualification(Qualification qualification) {
		qualifications.add(qualification);
	}

	public Set<Project> getProjects() {
		return this.projects;
	}
	
	// NOTE: caller's responsibility to check if the project can be added to the worker and also to ensure that the worker is added to the project
	public void addProject(Project project) {
		projects.add(project);
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
