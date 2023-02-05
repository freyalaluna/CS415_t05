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
		assert (true);
	}

	@Test
	public void testGetNameWithEmptyName() {
		assert (true);
	}

	@Test
	public void testGetNameWithValidNameSingle() {
		assert (true);
	}

	@Test
	public void testGetNameWithValidNameMultiWords() {
		assert (true);
	}

	@Test
	public void testGetNameWithDigits() {
		assert (true);
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
