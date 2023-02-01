package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class WorkerTest {
    private Worker worker;

    @Before
    public void setUp() throws Exception {
		Set<Qualification> qualifications = new HashSet<Qualification>();
        worker = new Worker("", qualifications, 0.0);
    }

    /***** TOSTRING *****/
    @Test
    public void testToString() {
       assertEquals("Worker.toString returns :0:0:0", worker.toString(), ":0:0:0");
    }

    @Test
    public void testToStringOnlyNameNotEmpty() {
       Set<Qualification> qualifications = new HashSet<Qualification>();
       Worker workerWithName = new Worker("test1", qualifications, 0.0);
       assertEquals(workerWithName.toString(), "test1:0:0:0");
    }

    @Test
    public void testToStringWithQuals() {
       Set<Qualification> qualifications = new HashSet<Qualification>();
       Qualification q = new Qualification("test");
       qualifications.add(q);
       Worker workerWithName = new Worker("test2", qualifications, 0.0);
       assertEquals(workerWithName.toString(), "test2:0:1:0");
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
    public void testToStringWithOnlySalaryNotEmpty() {
       Set<Qualification> qualifications = new HashSet<Qualification>();
       Worker workerWithName = new Worker("", qualifications, 12345.9999);
       assertEquals(workerWithName.toString(), ":0:0:12345");
    }

    @Test
    public void testToStringWithIntSalary() {
       Set<Qualification> qualifications = new HashSet<Qualification>();
       Worker workerWithName = new Worker("", qualifications, 4);
       assertEquals(workerWithName.toString(), ":0:0:4");
    }

    // Test for displaying number of projects when addProjects is done.

    /***** HASHCODE *****/
	@Test
    public void testHashCodeWithNullString() {
        assertEquals( "Worker.hashCode returns 0 with a null string", worker.hashCode(), 0);
    }

	@Test
    public void testHashCodeWithEmptyString() {
        assertEquals( "Worker.hashCode returns 0 with an empty string", worker.hashCode(), 0);
    }

	@Test
    public void testHashCodeWithValidString() {
		Worker workerWithValidName = new Worker(("test"), null, 0);
        assertThat("Worker.hashCode returns a non 0 code with a valid name", workerWithValidName.hashCode(), is(not(0)));
    }

    /**** getName ****/
    @Test
    public void testGetNameWithEmptyName() {
        assertEquals(worker.getName(),"");
    }

    @Test
    public void testGetNameWithNonEmptyName() {
        Worker worker = new Worker("test", null, 0);
        assertEquals(worker.getName(), "test");
    }

}
