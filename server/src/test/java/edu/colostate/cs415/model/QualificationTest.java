package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class QualificationTest {
	private Qualification qualificationWithNullDesc;
	private Qualification qualificationWithEmptyDesc;
	private Qualification qualificationWithValidDesc;

	@Before
    public void setUp() throws Exception {
        qualificationWithNullDesc = new Qualification(null);
		qualificationWithEmptyDesc = new Qualification("");
		qualificationWithValidDesc = new Qualification("test-description");
    }
	/***** HASHCODE *****/
	@Test
	public void testHashCodeWithNullString() {
		assertEquals("Qualification.hashCode returns 0 with a null string", qualificationWithNullDesc.hashCode(), 0);
	}

	@Test
	public void testHashCodeWithEmptyString() {
		assertEquals( "Qualification.hashCode returns 0 with an empty string", qualificationWithEmptyDesc.hashCode(), 0);
	}

	@Test
	public void testHashCodeWithValidString() {
		assertThat("Qualification.hashCode returns a non 0 code with a valid name", qualificationWithValidDesc.hashCode(), is(not(0)));
	}

	/***** TOSTRING *****/
	@Test
	public void testToStringWithValidString() {
		assertEquals("Qualification.toString returns the description \"test-description\"", qualificationWithValidDesc.toString(), "test-description");
	}

	@Test
	public void testToStringWithEmptyString() {
		assertEquals( "Qualification.toString returns an empty string", qualificationWithEmptyDesc.toString(), "");
	}

	@Test
	public void testToStringWithNullString() {
		assertEquals("Qualification.toString returns null", qualificationWithNullDesc.toString(), null);
	}

	 /*** addWorkers */
	 @Test
	 public void testAddWorkerReturnsCorrectNumberOfWorkers() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 0.0);
		Worker w2 = new Worker("W2", qualifications, 0.0);
		Qualification qualWithWorkers = new Qualification("test");
		 qualWithWorkers.addWorker(w1);
		 qualWithWorkers.addWorker(w2);
		 assertEquals(qualWithWorkers.getWorkers().size(), 2);
	 }
 
	 @Test
	 public void testAddWorkerWithDuplicateWorkersReturnsCorrectNumberOfWorkers() {
		 Set<Qualification> qualifications = new HashSet<Qualification>();
		 Worker w1 = new Worker("W1", qualifications, 0.0);
		 Qualification qualWithWorkers = new Qualification("test");
		 qualWithWorkers.addWorker(w1);
		qualWithWorkers.addWorker(w1);
		assertEquals(qualWithWorkers.getWorkers().size(), 1);
	 }
 
	 @Test
	 public void testAddWorkerssWithTwoDuplicateWorkersReturnsCorrectNumberOfWorkers() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 0.0);
		Worker w2 = new Worker("W2", qualifications, 0.0);
		Qualification qualWithWorkers = new Qualification("test");
		 qualWithWorkers.addWorker(w1);
		 qualWithWorkers.addWorker(w1);
		 qualWithWorkers.addWorker(w2);
		 qualWithWorkers.addWorker(w2);
		 assertEquals(qualWithWorkers.getWorkers().size(), 2);
	 }

	 /*** getWorkers */
	 @Test
	 public void testGetWorkersReturnsEmptySet() {
		Qualification q1 = new Qualification("test");
		assertEquals(q1.getWorkers().size(), 0);
	 }
 
	 @Test
	 public void testGetWorkerssReturnsCorrectNumberOfWorkerss() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 0.0);
		Worker w2 = new Worker("W2", qualifications, 0.0);
		Qualification qualWithWorkers = new Qualification("test");
		 qualWithWorkers.addWorker(w1);
		 qualWithWorkers.addWorker(w2);
		 assertEquals(qualWithWorkers.getWorkers().size(), 2);
	 }

}
