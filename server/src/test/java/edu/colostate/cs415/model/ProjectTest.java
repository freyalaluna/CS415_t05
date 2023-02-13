package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import edu.colostate.cs415.dto.ProjectDTO;

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

	/*** PROJECT ***/

	@Test
	public void testProjectAllNull() {
		thrown.expect(IllegalArgumentException.class);
		Project validProject = new Project(null, null, null);
		validProject.toString();
	}

	@Test
	public void testProjectOnlyNameNull() {
		thrown.expect(IllegalArgumentException.class);
		Project validProject = new Project(null, new HashSet<Qualification>(), size);
		validProject.getName();
	}

	@Test
	public void testProjectOnlyQualsNull() {
		thrown.expect(IllegalArgumentException.class);
		Project validProject = new Project("Project Name", null, size);
		validProject.getRequiredQualifications();
	}

	@Test
	public void testProjectOnlySizeNull() {
		thrown.expect(IllegalArgumentException.class);
		Project validProject = new Project(null, new HashSet<Qualification>(), null);
		validProject.getSize();
	}

	@Test
	public void testProjectAllValid() {
		Set<Qualification> quals = new HashSet<Qualification>();
		Qualification qual1 = new Qualification("qual1");
		Qualification qual2 = new Qualification("qual2");
		quals.add(qual1);
		quals.add(qual2);

		Project validProject = new Project("Valid Name", quals, size);

		assertEquals(validProject.getName(), "Valid Name");
		assertEquals(validProject.getRequiredQualifications().size(), 2);
		assertEquals(validProject.getSize(), ProjectSize.SMALL);
	}

	/*** EQUALS ***/
	private Project projectValidName = new Project("test1", new HashSet<Qualification>(), size);
	private Project projectEmptyName = new Project("", new HashSet<Qualification>(), size);

	@Test
    public void testEqualsReturnsTrue() {
		assertTrue("Project.equals() returns TRUE with a valid description", projectValidName.equals(projectValidName));
    }
	
    @Test
    public void testEqualsReturnsFalse(){
		Project projectValidName2 = new Project("test2", new HashSet<Qualification>(), size);
        assertFalse("Project.equals() returns FALSE with two different project names", projectValidName.equals(projectValidName2));
    }

	@Test
    public void testEqualsReturnsFalseWithEmptyName(){
		assertFalse("Project.equals() returns FALSE with an empty name", projectValidName.equals(projectEmptyName));
    }

	@Test
	public void testEqualsReturnsFalseWithNullName(){
		thrown.expect(IllegalArgumentException.class);
		Project projectNullName = new Project(null, new HashSet<Qualification>(), size);
		projectValidName.equals(projectNullName);
	}

    @Test
    public void testEqualsReturnsFalseWithNullObject(){
		assertFalse("Project.equals() returns FALSE with null object", projectValidName.equals(null));
    }
	
	@Test
    public void testEqualsReturnsFalseWithNonProjectObject(){
		String nonProjectObject = "test";
        assertFalse("Project.equals() returns FALSE with non Project object", projectValidName.equals(nonProjectObject));
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
    
  	/*** toString ***/
	
	@Test
	public void testToStringAllNull() {
		thrown.expect( IllegalArgumentException.class );
		Project p = new Project(null, null, size);
		p.setStatus(null);
		p.toString();
	}

	@Test
	public void testToStringAllEmptyStatusNull() {
		thrown.expect( IllegalArgumentException.class );
		Project p = new Project("", new HashSet<Qualification>(), size);
		// Note, there isn't a way to test status as 'empty' because it is an enum
		p.setStatus(null);
		p.toString();
	}

	@Test
	public void testToStringOnlyStatusValid() {
		Project p = new Project("", new HashSet<Qualification>(), size);
		p.setStatus(ProjectStatus.ACTIVE);
		assertEquals(p.toString(), ":0:ACTIVE");
	}

	@Test
	public void testToStringAllValidOneWorker() {
		Project p = new Project("test", new HashSet<Qualification>(), size);
		Worker w = new Worker("name", new HashSet<Qualification>(), 100);
		p.addWorker(w);
		assertEquals(p.toString(), "test:1:PLANNED");
	}

	@Test
	public void testToStringAllValidMultipleWorkers() {
		Project p = new Project("Test", new HashSet<Qualification>(), size);
		p.setStatus(ProjectStatus.SUSPENDED);
		Worker w1 = new Worker("name one", new HashSet<Qualification>(), 100);
		Worker w2 = new Worker("name two", new HashSet<Qualification>(), 100);
		Worker w3 = new Worker("name three", new HashSet<Qualification>(), 100);
		p.addWorker(w1);
		p.addWorker(w2);
		p.addWorker(w3);
		assertEquals(p.toString(), "Test:3:SUSPENDED");
	}
		
	@Test
	public void testToStringOnlyWorkersEmpty() {
		Project p = new Project("Test1", new HashSet<Qualification>(), size);
		p.setStatus(ProjectStatus.FINISHED);
		assertEquals(p.toString(), "Test1:0:FINISHED");
	}

	@Test
	public void testToStringOnlyNameEmpty() {
		Project p = new Project("", new HashSet<Qualification>(), size);
		Worker w1 = new Worker("name one", new HashSet<Qualification>(), 100);
		Worker w2 = new Worker("name two", new HashSet<Qualification>(), 100);
		p.addWorker(w1);
		p.addWorker(w2);
		assertEquals(p.toString(), ":2:PLANNED");
	}

	@Test
	public void testToStringOnlyStatusNull() {
		thrown.expect( IllegalArgumentException.class );
		Project p = new Project("test1", new HashSet<Qualification>(), size);
		Worker w1 = new Worker("name one", new HashSet<Qualification>(), 100);
		Worker w2 = new Worker("name two", new HashSet<Qualification>(), 100);
		p.addWorker(w1);
		p.addWorker(w2);
		// Note, there isn't a way to test status as 'empty' because it is an enum
		p.setStatus(null);
		p.toString();
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

	@Test
	public void testSetStatusNull() {
		thrown.expect( IllegalArgumentException.class );
		project.setStatus(null);
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

	/***** removeAllWorkers *****/

	@Test
	public void testRemoveAllWorkersWithNoWorkers(){
		project.removeAllWorkers();
		assertEquals(0, project.getWorkers().size());
	}

	@Test
	public void testRemoveAllWorkersWithMultipleWorkers(){
		Set<Qualification> quals = new HashSet<Qualification>();
		Worker w1 = new Worker("test1", quals, 1.0);
		Worker w2 = new Worker("test2", quals, 2.0);
		Worker w3 = new Worker("test3", quals, 3.0);
		project.addWorker(w1);
		project.addWorker(w2);
		project.addWorker(w3);
		assertEquals(3, project.getWorkers().size());
		project.removeAllWorkers();
		assertEquals(0, project.getWorkers().size());
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
	
	/***** addQualification *****/
	
	@Test
    public void testAddQualificationsReturnsCorrectNumberOfQuals() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Project projectWithQuals = new Project("", qualifications, size);
        Qualification q1 = new Qualification("q1");
        Qualification q2 = new Qualification("q2");
        projectWithQuals.addQualification(q1);
        projectWithQuals.addQualification(q2);
        assertEquals(projectWithQuals.getRequiredQualifications().size(), 2);
    }

    @Test
    public void testAddQualificationsWithDuplicateQualsReturnsCorrectNumberOfQuals() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Project projectWithQuals = new Project("", qualifications, size);
        Qualification q1 = new Qualification("q1");
        projectWithQuals.addQualification(q1);
        projectWithQuals.addQualification(q1);
        assertEquals(projectWithQuals.getRequiredQualifications().size(), 1);
    }

    @Test
    public void testAddQualificationsWithTwoDuplicateQualsReturnsCorrectNumberOfQuals() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Project projectWithQuals = new Project("", qualifications, size);
        Qualification q1 = new Qualification("q1");
        Qualification q2 = new Qualification("q2");
        projectWithQuals.addQualification(q1);
        projectWithQuals.addQualification(q1);
        projectWithQuals.addQualification(q2);
        projectWithQuals.addQualification(q2);
        assertEquals(projectWithQuals.getRequiredQualifications().size(), 2);
    }

    @Test
    public void testAddNullQualificationThrowsException(){
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Project projectWithQuals = new Project("", qualifications, size);
        thrown.expect( IllegalArgumentException.class );
        projectWithQuals.addQualification(null);
	}

	/***** getMissingQualifications *****/

	@Test
	public void testMissingQualificationsWithZeroMissing(){
		Set<Qualification> requiredQuals = new HashSet<Qualification>();
		Qualification q1 = new Qualification("can read");
		Qualification q2 = new Qualification("can write");
		requiredQuals.add(q1);
		requiredQuals.add(q2);
		Set<Qualification> w1Quals = new HashSet<Qualification>();
		w1Quals.add(q1);
		Set<Qualification> w2Quals = new HashSet<Qualification>();
		w2Quals.add(q2);
		Project p1 = new Project("testProject", requiredQuals, ProjectSize.BIG);
		Worker w1 = new Worker("w1", w1Quals, 10.0);
		Worker w2 = new Worker("w2", w2Quals, 5.0);
		p1.addWorker(w1);
		p1.addWorker(w2);
		assertTrue("getMissingQualifications returns 0 missing qualifications", p1.getMissingQualifications().isEmpty());
		
	}

	@Test
	public void testMissingQualificationsWithOneMissing(){
		Set<Qualification> requiredQuals = new HashSet<Qualification>();
		Qualification q1 = new Qualification("can read");
		Qualification q2 = new Qualification("can write");
		Qualification q3 = new Qualification("can sing");
		requiredQuals.add(q1);
		requiredQuals.add(q2);
		requiredQuals.add(q3);
		Set<Qualification> w1Quals = new HashSet<Qualification>();
		w1Quals.add(q1);
		Set<Qualification> w2Quals = new HashSet<Qualification>();
		w2Quals.add(q2);
		Project p1 = new Project("testProject", requiredQuals, ProjectSize.BIG);
		Worker w1 = new Worker("w1", w1Quals, 10.0);
		Worker w2 = new Worker("w2", w2Quals, 5.0);
		p1.addWorker(w1);
		p1.addWorker(w2);
		assertFalse("getMissingQualifications returns 0 missing qualifications", p1.getMissingQualifications().isEmpty());
	}

	@Test
	public void testMissingQualificationsWithMultipleMissingWithSpecifications(){
		Set<Qualification> requiredQuals = new HashSet<Qualification>();
		Qualification q1 = new Qualification("can read");
		Qualification q2 = new Qualification("can write");
		Qualification q3 = new Qualification("can sing");
		Qualification q4 = new Qualification("can hulahoop");
		requiredQuals.add(q1);
		requiredQuals.add(q2);
		requiredQuals.add(q3);
		requiredQuals.add(q4);
		Set<Qualification> w1Quals = new HashSet<Qualification>();
		w1Quals.add(q1);
		Set<Qualification> w2Quals = new HashSet<Qualification>();
		w2Quals.add(q2);
		Project p1 = new Project("testProject", requiredQuals, ProjectSize.BIG);
		Worker w1 = new Worker("w1", w1Quals, 10.0);
		Worker w2 = new Worker("w2", w2Quals, 5.0);
		p1.addWorker(w1);
		p1.addWorker(w2);
		Set<Qualification> qualComparison = new HashSet<Qualification>();
		qualComparison.add(q3);
		qualComparison.add(q4);
		assertEquals("getMissingQualifications return set containing q3 and q4 qualifications", qualComparison, p1.getMissingQualifications());
	}

	@Test
	public void testMissingQualificationsWithMultipleQualsPerWorker(){
		Set<Qualification> requiredQuals = new HashSet<Qualification>();
		Qualification q1 = new Qualification("can read");
		Qualification q2 = new Qualification("can write");
		Qualification q3 = new Qualification("can sing");
		Qualification q4 = new Qualification("can hulahoop");
		requiredQuals.add(q1);
		requiredQuals.add(q2);
		requiredQuals.add(q3);
		requiredQuals.add(q4);
		Set<Qualification> w1Quals = new HashSet<Qualification>();
		w1Quals.add(q1);
		w1Quals.add(q3);
		Set<Qualification> w2Quals = new HashSet<Qualification>();
		w2Quals.add(q2);
		w2Quals.add(q4);
		Project p1 = new Project("testProject", requiredQuals, ProjectSize.BIG);
		Worker w1 = new Worker("w1", w1Quals, 10.0);
		Worker w2 = new Worker("w2", w2Quals, 5.0);
		p1.addWorker(w1);
		p1.addWorker(w2);
		assertTrue("getMissingQualifications return 0 missing qualifications", p1.getMissingQualifications().isEmpty());
	}

	/***** getRequiredQualifications *****/
	@Test
    public void testgetRequiredQualificationsReturnsCorrectNumberOfQuals() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Project projectWithQuals = new Project("", qualifications, size);
        Qualification q1 = new Qualification("q1");
        Qualification q2 = new Qualification("q2");
        projectWithQuals.addQualification(q1);
        projectWithQuals.addQualification(q2);
		projectWithQuals.addQualification(q1);
        projectWithQuals.addQualification(q2);
        assertEquals(projectWithQuals.getRequiredQualifications().size(), 2);
    }

	/***** isHelpful *****/
	@Test
	public void testIsHelpfulWithNullWorker(){
		thrown.expect(IllegalArgumentException.class);
		project.isHelpful(null);
	}

	@Test
	public void testIsHelpfulWhenHelpful(){
		Set<Qualification> requiredQuals = new HashSet<Qualification>();
		Qualification q1 = new Qualification("can read");
		Qualification q2 = new Qualification("can write");
		Qualification q3 = new Qualification("can sing");
		Qualification q4 = new Qualification("can hulahoop");
		requiredQuals.add(q1);
		requiredQuals.add(q2);
		requiredQuals.add(q3);
		requiredQuals.add(q4);
		Set<Qualification> w1Quals = new HashSet<Qualification>();
		w1Quals.add(q1);
		w1Quals.add(q2);
		w1Quals.add(q3);
		Set<Qualification> w2Quals = new HashSet<Qualification>();
		w2Quals.add(q4);
		Project p1 = new Project("testProject", requiredQuals, ProjectSize.BIG);
		Worker w1 = new Worker("w1", w1Quals, 10.0);
		Worker w2 = new Worker("w2", w2Quals, 5.0);
		p1.addWorker(w1);
		for(Qualification mq : p1.getMissingQualifications()){
			System.out.println(mq);
		}
		assertTrue(p1.isHelpful(w2));
	}

	@Test
	public void testIsHelpfulWhenNotHelpful(){
		Set<Qualification> requiredQuals = new HashSet<Qualification>();
		Qualification q1 = new Qualification("can read");
		Qualification q2 = new Qualification("can write");
		Qualification q3 = new Qualification("can sing");
		Qualification q4 = new Qualification("can hulahoop");
		requiredQuals.add(q1);
		requiredQuals.add(q2);
		requiredQuals.add(q3);
		requiredQuals.add(q4);
		Set<Qualification> w1Quals = new HashSet<Qualification>();
		w1Quals.add(q1);
		w1Quals.add(q2);
		w1Quals.add(q3);
		w1Quals.add(q4);
		Set<Qualification> w2Quals = new HashSet<Qualification>();
		w2Quals.add(q4);
		Project p1 = new Project("testProject", requiredQuals, ProjectSize.BIG);
		Worker w1 = new Worker("w1", w1Quals, 10.0);
		Worker w2 = new Worker("w2", w2Quals, 5.0);
		p1.addWorker(w1);
		for(Qualification mq : p1.getMissingQualifications()){
			System.out.println(mq);
		}
		assertFalse(p1.isHelpful(w2));
	}
	
	/*** ProjectDTO ***/

	@Test
	public void testToDTOAllValid() {
		Set<Qualification> quals = new HashSet<Qualification>();
		Qualification qual1 = new Qualification("qualification test-1");
		Qualification qual2 = new Qualification("qualification test-2");

		quals.add(qual1);
		quals.add(qual2);

		Project validProject = new Project("projectName",  quals, size);

		Worker w1 = new Worker("test1", quals, 5.0);
		Worker w2 = new Worker("test2", quals, 4.0);
		validProject.addWorker(w1);
		validProject.addWorker(w2);

		ProjectDTO projectDTO = validProject.toDTO();

		assertEquals(projectDTO.getName(), validProject.getName());
		assertEquals(projectDTO.getSize(), validProject.getSize());
		assertEquals(projectDTO.getStatus(), validProject.getStatus());
	}
}
