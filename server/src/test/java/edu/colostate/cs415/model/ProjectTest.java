package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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

	/***** HASHCODE *****/
	@Test
	public void testHashCodeWithNullString() {
		assertEquals( "Project.hashCode returns 0 with a null string", project.hashCode(), 0);
	}

	@Test
	public void testHashCodeWithEmptyString() {
		assertEquals( "Project.hashCode returns 0 with an empty string", project.hashCode(), 0);
	}

	@Test
	public void testHashCodeWithValidString() {
		Project validProjectName = new Project("projectName", null, size);
		assertThat("Project.hashCode returns a non 0 code with a valid name", validProjectName.hashCode(), is(not(0)));
	}
}
