package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ProjectTest {

	private Project project;
	private ProjectSize size = ProjectSize.SMALL;

	
	@Before
    public void setUp() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
        project = new Project("", qualifications, size);
    }

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testProjectConstruct() {
		assert (true);
	}


	/*** getName ***/

	@Test
	public void testGetNameWithNullName() {
		thrown.expect( IllegalArgumentException.class );
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Project projectNullName = new Project(null, qualifications, size);
		projectNullName.getName();
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

	/***** getSize *****/

	@Test
	public void testGetSizeNull() throws Exception {
		thrown.expect( IllegalArgumentException.class );
		final ProjectSize invalidSize = null;
		Project invalidProjectSize = new Project("", new HashSet<Qualification>(), invalidSize);
		invalidProjectSize.getSize();
	}

	@Test
	public void testGetSizeSMALL() {
		assertEquals(project.getSize(), ProjectSize.SMALL);
	}

	@Test
	public void testGetSizeEmptyMEDIUM() {
		Project mediumProject = new Project("", new HashSet<Qualification>(), ProjectSize.MEDIUM);
		assertEquals(mediumProject.getSize(), ProjectSize.MEDIUM);
	}

	@Test
	public void testGetSizeEmptyBIG() {
		Project bigProject = new Project("", new HashSet<Qualification>(), ProjectSize.BIG);
		assertEquals(bigProject.getSize(), ProjectSize.BIG);
	}

	/***** getStatus *****/
  
	@Test
	public void testGetStatus(){
		assertEquals("Project.getStatus() returns PLANNED upon construction", ProjectStatus.PLANNED, project.getStatus());
	}

	/***** setStatus *****/

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
		thrown.expect( IllegalArgumentException.class );
		Set<Qualification> qualifications = new HashSet<Qualification>();
        Project emptyProject = new Project(null, qualifications, size);
		emptyProject.hashCode();
	}

	@Test
	public void testHashCodeWithEmptyString() {
		assertEquals( "Project.hashCode returns 0 with an empty string", project.hashCode(), 0);
	}

	@Test
	public void testHashCodeWithValidString() {
		Project validProjectName = new Project("projectName",  new HashSet<Qualification>(), size);
		assertThat("Project.hashCode returns a non 0 code with a valid name", validProjectName.hashCode(), is(not(0)));
	}

	/***** getWorkers *****/

	@Test
	public void testGetWorkers(){
		assertTrue(project.getWorkers().isEmpty());
	}

	@Test
	public void testGetWorkersWithMultipleWorkers(){
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("test1", qualifications, 10);
		project.addWorker(w1);
		assertTrue(project.getWorkers().contains(w1));
	}

	/***** addWorker *****/

	@Test
	public void testAddWorkerWithOneWorker(){
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("test1", qualifications, 10.0);
		project.addWorker(w1);
		assertEquals(1, project.getWorkers().size());
	}

	@Test
	public void testAddWorkerWithTwoIdenticalyWorkers(){
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("test1", qualifications, 10.0);
		project.addWorker(w1);
		project.addWorker(w1);
		assertEquals(1, project.getWorkers().size());
	}

	@Test
	public void testAddWorkersWithMultipleWorkers(){
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("test1", qualifications, 10.0);
		Worker w2 = new Worker("Ron Weasley", qualifications, 99.0);
		project.addWorker(w1);
		project.addWorker(w2);
		assertEquals(2, project.getWorkers().size());
	}

	@Test
	public void testAddWorkerWithNullWorker(){
		thrown.expect(IllegalArgumentException.class);
		project.addWorker(null);
	}

	/***** removeWorker *****/
	
	@Test
	public void testRemovingNullWorker(){
		thrown.expect(IllegalArgumentException.class);
		project.removeWorker(null);
	}

	@Test
	public void testRemovingNonExistentWorker(){
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("test1", qualifications, 5.0);
		Worker w2 = new Worker("test2", qualifications, 4.0);
		project.addWorker(w1);
		project.removeWorker(w2);
		assertEquals(1, project.getWorkers().size());
	}

	@Test
	public void testRemovingValidWorker(){
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("test1", qualifications, 10.0);
		project.addWorker(w1);
		assertEquals(1, project.getWorkers().size());
		project.removeWorker(w1);
		assertEquals(0, project.getWorkers().size());
	}
	

	
}
