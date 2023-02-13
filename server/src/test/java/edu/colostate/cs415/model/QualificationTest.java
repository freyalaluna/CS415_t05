package edu.colostate.cs415.model;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

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

	/**** EQUALS *****/

    @Test
    public void testEqualsReturnsFalseWithNullObject(){
        assertFalse("equals returns false with null object", qualificationWithValidDesc.equals(null));
    }

    @Test
    public void testEqualsReturnsFalseWithNonWorkerObject(){
        String nonQualification = "test";
        assertFalse("equals returns false with non qualification object", qualificationWithValidDesc.equals(nonQualification));
    }

	@Test
    public void testEqualsSelfValidOtherValid(){
        assertTrue("equals returns false with null name", qualificationWithValidDesc.equals(qualificationWithValidDesc));
    }

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
    public void testEqualsSelfNull(){
		thrown.expect(IllegalArgumentException.class);
		qualificationWithNullDesc.equals(qualificationWithValidDesc);
    }

	@Test
    public void testEqualsSelfEmpty(){
        assertFalse("equals returns false with null name", qualificationWithEmptyDesc.equals(qualificationWithValidDesc));
    }

	@Test
    public void testEqualsOtherNull(){
        assertFalse("equals returns false with null name", qualificationWithValidDesc.equals(qualificationWithNullDesc));
    }

	@Test
    public void testEqualsOtherEmpty(){
        assertFalse("equals returns false with null name", qualificationWithValidDesc.equals(qualificationWithEmptyDesc));
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
		Worker w1 = new Worker("W1", qualifications, 1.0);
		Worker w2 = new Worker("W2", qualifications, 1.0);
		Qualification qualWithWorkers = new Qualification("test");
		qualWithWorkers.addWorker(w1);
		qualWithWorkers.addWorker(w2);
		assertEquals(qualWithWorkers.getWorkers().size(), 2);
	 }
 
	 @Test
	 public void testAddWorkerWithDuplicateWorkersReturnsCorrectNumberOfWorkers() {
		 Set<Qualification> qualifications = new HashSet<Qualification>();
		 Worker w1 = new Worker("W1", qualifications, 1.0);
		 Qualification qualWithWorkers = new Qualification("test");
		 qualWithWorkers.addWorker(w1);
		qualWithWorkers.addWorker(w1);
		assertEquals(qualWithWorkers.getWorkers().size(), 1);
	 }
 
	 @Test
	 public void testAddWorkerssWithTwoDuplicateWorkersReturnsCorrectNumberOfWorkers() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 1.0);
		Worker w2 = new Worker("W2", qualifications, 1.0);
		Qualification qualWithWorkers = new Qualification("test");
		qualWithWorkers.addWorker(w1);
		qualWithWorkers.addWorker(w1);
		qualWithWorkers.addWorker(w2);
		qualWithWorkers.addWorker(w2);
		assertEquals(qualWithWorkers.getWorkers().size(), 2);
	 }

	/*** Remove Workers */
	@Test
	public void testremoveWorkerWithValidWorkerEmptySet() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 1.0);
		Qualification qualWithNoWorkers = new Qualification("test");
		qualWithNoWorkers.removeWorker((w1));
		assertEquals(qualWithNoWorkers.getWorkers().size(), 0);
	}

	@Test
	public void testRemoveWorkerWithValidWorkerLastInSet() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 1.0);
		Worker w2 = new Worker("W2", qualifications, 1.0);
		Worker w3 = new Worker("W3", qualifications, 1.0);
		Qualification qualWithWorkers = new Qualification("test");
		qualWithWorkers.addWorker(w1);
		qualWithWorkers.addWorker(w2);
		qualWithWorkers.addWorker(w3);
		qualWithWorkers.removeWorker((w3));
		assertEquals(qualWithWorkers.getWorkers().size(), 2);
	}

	@Test
	public void testRemoveWorkerWithValidWorkerMiddleOfSet() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 1.0);
		Worker w2 = new Worker("W2", qualifications, 1.0);
		Worker w3 = new Worker("W3", qualifications, 1.0);
		Qualification qualWithWorkers = new Qualification("test");
		qualWithWorkers.addWorker(w1);
		qualWithWorkers.addWorker(w2);
		qualWithWorkers.addWorker(w3);
		qualWithWorkers.removeWorker((w2));
		assertEquals(qualWithWorkers.getWorkers().size(), 2);
	}

	@Test
	public void testRemoveWorkerWithValidWorkerFirstInSet() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 1.0);
		Worker w2 = new Worker("W2", qualifications, 1.0);
		Worker w3 = new Worker("W3", qualifications, 1.0);
		Qualification qualWithWorkers = new Qualification("test");
		qualWithWorkers.addWorker(w1);
		qualWithWorkers.addWorker(w2);
		qualWithWorkers.addWorker(w3);
		qualWithWorkers.removeWorker((w1));
		assertEquals(qualWithWorkers.getWorkers().size(), 2);
	}

	@Test
	public void testRemoveWorkerWithInvalidWorker() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 1.0);
		Worker w2 = new Worker("W2", qualifications, 1.0);
		Worker w3 = new Worker("W3", qualifications, 1.0);
		Qualification qualWithWorkers = new Qualification("test");
		qualWithWorkers.addWorker(w1);
		qualWithWorkers.addWorker(w2);
		qualWithWorkers.removeWorker((w3));
		assertEquals(qualWithWorkers.getWorkers().size(), 2);
	}

	 /*** getWorkers *****/
	 @Test
	 public void testGetWorkersEmptySet() {
		Qualification q1 = new Qualification("test");
		assertEquals(q1.getWorkers().size(), 0);
	 }
 
	 @Test
	 public void testGetWorkersReturnsCorrectNumberOfWorkers() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 1.0);
		Worker w2 = new Worker("W2", qualifications, 1.0);
		Qualification qualWithWorkers = new Qualification("test");
		 qualWithWorkers.addWorker(w1);
		 qualWithWorkers.addWorker(w2);
		 assertEquals(qualWithWorkers.getWorkers().size(), 2);
	 }

	 /**** toDTO *****/

	@Test
	public void testtoDTOValidDescriptionFullSet() {
		Set<Qualification> qualifications = new HashSet<Qualification>();
		Worker w1 = new Worker("W1", qualifications, 1.0);
		Worker w2 = new Worker("W2", qualifications, 1.0);
		Qualification qualWithWorkers = new Qualification("test");
		qualWithWorkers.addWorker(w1);
		qualWithWorkers.addWorker(w2);
		assertEquals(qualWithWorkers.toDTO().getDescription(), "test");
	}

	public void testtoDTOValidDescriptionEmptySet() {
		assertEquals(qualificationWithValidDesc.toDTO().getDescription(), "test-description");
	}

	public void testtoDTOEmptyDescriptionEmptySet() {
		assertEquals(qualificationWithEmptyDesc.toDTO().getDescription(), "");
	}

	public void testtoDTONullDescriptionEmptySet() {
		assertEquals(qualificationWithNullDesc.toDTO().getDescription(), null);
	}


}
