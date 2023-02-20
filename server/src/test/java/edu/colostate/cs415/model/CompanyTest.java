package edu.colostate.cs415.model;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CompanyTest {
	@Rule public ExpectedException thrown = ExpectedException.none();

	private Company company;
	private Set<Qualification> qualifications;

	@Before
	public void setUp() {
		company = new Company("test co");
		qualifications = new HashSet<>();
		Qualification q = new Qualification("q1");
		qualifications.add(q);
	}

/****** CONSTRUCTOR ******/
	@Test
	public void testCompanyConstructor() {
		// add assert name when getName is done
		// assertEquals("test co", company.getName());
	}

	@Test
	public void testCompanyConstructorThrowsExceptionWithNullName() {
		thrown.expect(IllegalArgumentException.class);
		company = new Company(null);
	}

	@Test
	public void testCompanyConstructorThrowsExceptionWithEmptyName() {
		thrown.expect(IllegalArgumentException.class);
		company = new Company("");
	}

	/* getEmployedWorkers */

	@Test
	public void testGetEmployedWorkers() {
		// update to add workers when createWorker is done
		// company.createWorker("w1", qualifications, 10);
		// company.createWorker("w2", qualifications, 10);
		// assertEquals(2, company.getEmployedWorkers());
	}

	@Test
	public void testGetEmployedWorkersEmpty() {
		assertTrue( company.getEmployedWorkers().isEmpty());
	}


/****** GETNAME ******/
	@Test
	public void testGetName(){
		//Constructor tests for nullness/emptiness.
		assertEquals(company.getName(), "test co");
	}
}
