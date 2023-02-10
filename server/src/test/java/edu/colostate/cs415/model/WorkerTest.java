package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.lang.Integer;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import org.junit.Rule;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class WorkerTest {
    private Worker worker;
    private Set<Qualification> qualifications;

    @Before
    public void setUp() throws Exception {
		qualifications = new HashSet<Qualification>();
        worker = new Worker("Test", qualifications, 1.0);
    }

    @Rule public ExpectedException thrown = ExpectedException.none();

    /*** Constructor */
    @Test
    public void testConstructorThrowsExceptionWithNullName() {
        thrown.expect( IllegalArgumentException.class );
        Worker nullWorker = new Worker(null, qualifications, 1);
    }

    @Test
    public void testConstructorThrowsExceptionWithEmptyName() {
        thrown.expect( IllegalArgumentException.class );
        Worker nullWorker = new Worker("", qualifications, 1);
    }

    @Test
    public void testConstructorThrowsExceptionWithNullQuals() {
        thrown.expect( IllegalArgumentException.class );
        Worker nullWorker = new Worker("Test", null, 1);
    }

    @Test
    public void testConstructorThrowsExceptionWithZeroSalary() {
        thrown.expect( IllegalArgumentException.class );
        Worker nullWorker = new Worker("Test", qualifications, 0);
    }

    /**** EQUALS *****/
    @Test
    public void testEqualsReturnsTrue() {
        assertTrue("worker should equal worker", worker.equals(worker));
    }

    @Test
    public void testEqualsReturnsFalse(){
        Worker testWorker2 = new Worker("test", qualifications, 1.0);
        assertFalse("worker should not equal test worker", worker.equals(testWorker2));
    }

    @Test
    public void testEqualsReturnsFalseWithNullObject(){
        assertFalse("equals returns false with null object", worker.equals(null));
    }

    @Test
    public void testEqualsReturnsFalseWithNonWorkerObject(){
        String nonWorker = "test";
        assertFalse("equals returns false with non worker object", worker.equals(nonWorker));
    }

    /***** TOSTRING *****/
    @Test
    public void testToString() {
       assertEquals("Worker.toString returns Test:0:0:1", worker.toString(), "Test:0:0:1");
    }

    @Test
    public void testToStringWithQuals() {
       Qualification q = new Qualification("test");
       qualifications.add(q);
       Worker workerWithName = new Worker("test2", qualifications, 1.0);
       assertEquals(workerWithName.toString(), "test2:0:1:1");
    }

    @Test
    public void testToStringWithQualsAndNonZeroSalary() {
       Set<Qualification> qualifications = new HashSet<Qualification>();
       Qualification q = new Qualification("test");
       qualifications.add(q);
       Worker workerWithName = new Worker("test3", qualifications, 5122.1232);
       assertEquals(workerWithName.toString(), "test3:0:1:5122");
    }

    @Test
    public void testToStringWithIntSalary() {
       Worker workerWithName = new Worker("name", qualifications, 4);
       assertEquals(workerWithName.toString(), "name:0:0:4");
    }

    // Test for displaying number of projects when addProjects is done.

    /***** HASHCODE *****/
	@Test
    public void testHashCodeWithValidString() {
		Worker workerWithValidName = new Worker(("test"), qualifications,1.0);
        assertThat("Worker.hashCode returns a non 0 code with a valid name", workerWithValidName.hashCode(), is(not(0)));
    }

    /**** getName ****/

    @Test
    public void testGetNameWithNonEmptyName() {
        Worker worker = new Worker("test", qualifications, 1.0);
        assertEquals(worker.getName(), "test");
    }

    /*** getSalary - setSalary */
    @Test
    public void testGetSalary() {
        assertEquals(worker.getSalary(), 1.0, 0.001);
    }

    @Test
    public void testSetSalary() {
        double expectedSalary = 1000.99;
        worker.setSalary(expectedSalary);
        assertEquals(worker.getSalary(), expectedSalary, 0.001);
    }

    @Test
    public void testSetSalaryToZero() {
        thrown.expect( IllegalArgumentException.class );
        worker.setSalary(0);
    }

    @Test
    public void testSetSalaryToNegativeThrowsException() {
        thrown.expect( IllegalArgumentException.class );
        worker.setSalary(-1234.12);
    }

    /*** getQualificiations */
    @Test
    public void testGetQualificationsReturnsEmptySet() {
        assertEquals(worker.getQualifications().size(), 0);
    }

    @Test
    public void testGetQualificationsReturnsCorrectNumberOfQuals() {
        Qualification q1 = new Qualification("q1");
        Qualification q2 = new Qualification("q2");
        qualifications.add(q1);
        qualifications.add(q2);
        Worker workerWithQuals = new Worker("test", qualifications, 1.0);
        assertEquals(workerWithQuals.getQualifications().size(), 2);
    }

    /*** addQualifications */
    @Test
    public void testAddQualificationsReturnsCorrectNumberOfQuals() {
        Qualification q1 = new Qualification("q1");
        Qualification q2 = new Qualification("q2");
        worker.addQualification(q1);
        worker.addQualification(q2);
        assertEquals(worker.getQualifications().size(), 2);
    }

    @Test
    public void testAddQualificationsWithDuplicateQualsReturnsCorrectNumberOfQuals() {
        Qualification q1 = new Qualification("q1");
        worker = new Worker("test", qualifications, 1.0);
        worker.addQualification(q1);
        worker.addQualification(q1);
        assertEquals(worker.getQualifications().size(), 1);
    }

    @Test
    public void testAddQualificationsWithTwoDuplicateQualsReturnsCorrectNumberOfQuals() {
        Qualification q1 = new Qualification("q1");
        Qualification q2 = new Qualification("q2");
        worker = new Worker("test", qualifications, 1.0);
        worker.addQualification(q1);
        worker.addQualification(q1);
        worker.addQualification(q2);
        worker.addQualification(q2);
        assertEquals(worker.getQualifications().size(), 2);
    }

    @Test
    public void testAddNullQualificationThrowsException(){
        thrown.expect( IllegalArgumentException.class );
        worker.addQualification(null);
    }

    /*** getProjects - addProjects - removeProjects */
    @Test
    public void testGetProjectsReturnsEmptySet(){
        assertEquals(worker.getProjects().size(), 0);
    }

    @Test
    public void testGetProjectsReturnsCorrectNumberOfProjects(){
        Project proj = new Project("p1", qualifications, ProjectSize.BIG);
        worker.addProject(proj);
        assertEquals(worker.getProjects().size(), 1);
    }

    @Test
    public void testAddingDuplicateProjects(){
        Project proj = new Project("p1", qualifications, ProjectSize.BIG);
        worker.addProject(proj);
        worker.addProject(proj);
        assertEquals(worker.getProjects().size(), 1);
    }

    @Test
    public void testWorkerDoesntLimitAddingProjects(){
        int numberSmallProjects = 13;
        for(Integer i = 0; i < numberSmallProjects; i++){
            String projectName = "p" + i.toString();
            Project p = new Project(projectName, qualifications, ProjectSize.BIG);
            worker.addProject(p);
        }
        assertEquals(worker.getProjects().size(), numberSmallProjects);
    }

    @Test 
    public void testAddNullProjectThrowsException(){
        thrown.expect( IllegalArgumentException.class );
        worker.addProject(null);
    }

    @Test
    public void testRemoveProject(){
        Project project = new Project("test", qualifications, ProjectSize.BIG);
        worker.addProject(project);
        assertTrue(worker.getProjects().size() == 1);
        worker.removeProject(project);
        assertTrue(worker.getProjects().size() == 0);
    }

    @Test
    public void testRemoveProjectNotInWorkersProjects(){
        Project project = new Project("test", qualifications, ProjectSize.BIG);
        Project notWorkersProject = new Project("bad proj", qualifications, ProjectSize.SMALL);
        worker.addProject(project);
        assertTrue(worker.getProjects().size() == 1);
        worker.removeProject(notWorkersProject);
        assertTrue(worker.getProjects().size() == 1);
    }

    @Test
    public void testRemoveProjectWhenMultipleProjects(){
        Project project1 = new Project("test1", qualifications, ProjectSize.SMALL);
        Project project2 = new Project("test2", qualifications, ProjectSize.SMALL);
        Project project3 = new Project("test3", qualifications, ProjectSize.SMALL);
        worker.addProject(project1);
        worker.addProject(project2);
        worker.addProject(project3);
        assertTrue(worker.getProjects().size() == 3);
        worker.removeProject(project2);
        LinkedList<String> names = new LinkedList<String>();
        for(Project proj: worker.getProjects()){
            names.add(proj.getName());
        }
        assertTrue(names.contains("test1"));
        assertTrue(names.contains("test3"));
        assertTrue(worker.getProjects().size() == 2);
    }

    @Test
    public void testRemoveNullProjectThrowsException(){
        thrown.expect( IllegalArgumentException.class );
        worker.removeProject(null);
    }
}
