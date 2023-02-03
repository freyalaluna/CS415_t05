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


	@Test
	public void testGetStatus(){
		assertEquals("Project.getStatus() return PLANNED upon construction", project.getStatus(), ProjectStatus.PLANNED);
	}
}
