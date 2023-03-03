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
		if(name == null || name.trim().isEmpty()){
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
		return name.hashCode();
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
		Set<Worker> availableClone = new HashSet<>(available);
		return availableClone;
	}

	public Set<Worker> getUnavailableWorkers() {
		if(employees.size()< available.size()){
			return null;
		}
		Set<Worker> availableClone = new HashSet<>(available);
		Set<Worker> unavailableWorkers = new HashSet<>(employees);
		unavailableWorkers.removeAll(availableClone);
		return unavailableWorkers;
	}

	public Set<Worker> getAssignedWorkers() {
		Set<Worker> assignedClone = new HashSet<>(assigned);
		return assignedClone;
	}

	public Set<Worker> getUnassignedWorkers() {
		Set<Worker> unassignedWorkers = new HashSet<>();
		for(Worker w: employees){
			if(!assigned.contains(w)){
				unassignedWorkers.add(w);
			}
		}
		return unassignedWorkers;
	}

	public Set<Project> getProjects() {
		Set<Project> projectsClone = new HashSet<Project>(projects);
		return projectsClone;
	}

	// Return a copy of qualifications so there's no reference to the private set
	public Set<Qualification> getQualifications() {
		Set<Qualification> qualificationsClone = new HashSet<Qualification>(qualifications);
		return qualificationsClone;
	}

	public Worker createWorker(String name, Set<Qualification> qualifications, double salary) {
		if(name == null || name.trim().isEmpty() || salary < 0.0 || qualifications == null || qualifications.isEmpty() || !this.qualifications.containsAll(qualifications)){
			return null;
		}
		
		Worker worker = new Worker(name, qualifications, salary);
		employees.add(worker);
		available.add(worker);
		for(Qualification wq : qualifications){
			wq.addWorker(worker);
		}
		return worker;
	}

	public Qualification createQualification(String description) {
		if(description == null || description.trim().isEmpty()){
			return null;
		}
		Qualification q = new Qualification(description);
		if(qualifications.contains(q)){
			return null;
		}
		qualifications.add(q);
		return q;
	}

	public Project createProject(String name, Set<Qualification> qualifications, ProjectSize size) {
		return null;
	}

	public void start(Project project) {
		if(project == null){
			throw new IllegalArgumentException();
		}
		ProjectStatus pStatus = project.getStatus();
		Set<Qualification> missingQuals = project.getMissingQualifications();
		if(pStatus == ProjectStatus.PLANNED || pStatus == ProjectStatus.SUSPENDED){
			if(missingQuals.size() == 0){
				project.setStatus(ProjectStatus.ACTIVE);
			}
		}
	}

	public void finish(Project project) {
		if(project == null){
			throw new IllegalArgumentException();
		}
		if(project.getStatus() == ProjectStatus.ACTIVE){
			project.setStatus(ProjectStatus.FINISHED);
			//iterate through project's set of workers
			for(Worker w : project.getWorkers()){
				//removes project from worker's set of projects
				w.removeProject(project);
				available.add(w);
				//checks if this was the worker's only project
				if(w.getProjects().isEmpty()){
					assigned.remove(w);
				}
			}
			//clears project's set of workers
			project.removeAllWorkers();
		}
	}

	public void assign(Worker worker, Project project) {
	}

	public void unassign(Worker worker, Project project) {
	}

	public void unassignAll(Worker worker) {
		if(worker == null){
			throw new IllegalArgumentException();
		}
		for(Project p : worker.getProjects()){
			p.removeWorker(worker);
			if(!(p.getMissingQualifications().isEmpty()) && p.getStatus() == ProjectStatus.ACTIVE){
				p.setStatus(ProjectStatus.SUSPENDED);
			}
		}
		assigned.remove(worker);
		available.add(worker);
	}
}
