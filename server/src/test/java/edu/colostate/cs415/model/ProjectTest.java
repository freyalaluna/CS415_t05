package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ProjectTest {

	private Project project;
	private ProjectSize size;

	@Before
    public void setUp() throws Exception {
        Set<Qualification> qualifications = new HashSet<Qualification>();
        project = new Project("", qualifications, size);
    }


	@Test
	public void testProjectConstruct() {
		assert (true);
	}

	/*** getName ***/

	@Test
	public void testGetNameWithNullName() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Project projectNullName = new Project(null, qualifications, size);
		assertEquals("", projectNullName.getName(), "");
	}

	@Test
	public void testGetNameWithEmptyName() {
		assertEquals("", project.getName(), "");
	}

	@Test
	public void testGetNameWithValidNameSingle() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Project projectValidName = new Project("test", qualifications, size);
		assertEquals("", projectValidName.getName(), "test");
	}

	@Test
	public void testGetNameWithValidNameMultiWords() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Project projectValidNameMultiWords = new Project("Test Name", qualifications, size);
		assertEquals("", projectValidNameMultiWords.getName(), "Test Name");
	}

	@Test
	public void testGetNameWithDigits() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Project projectNameDigits = new Project("twentyT00", qualifications, size);
		assertEquals("", projectNameDigits.getName(), "twentyT00");
	}


	@Test
	public void testGetStatus(){
		assertEquals("Project.getStatus() returns PLANNED upon construction", ProjectStatus.PLANNED, project.getStatus());
	}


	@Test
	public void testSetStatus(){
		project.setStatus(ProjectStatus.PLANNED);
		assertEquals("project.setStatus() sets status to PLANNED", ProjectStatus.PLANNED, project.getStatus());
		project.setStatus(ProjectStatus.ACTIVE);
		assertEquals("project.setStatus() sets status to ACTIVE after first change", ProjectStatus.ACTIVE, project.getStatus());
	}


	@Test
	public void testGetWorkers(){
		assertEquals(null, project.getWorkers());
	}

	//Will add more tests once addWorker is done
}
