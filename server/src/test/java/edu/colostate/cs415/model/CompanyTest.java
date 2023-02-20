package edu.colostate.cs415.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CompanyTest {
	@Rule public ExpectedException thrown = ExpectedException.none();

	private Company company;

/****** SETUP ******/
	@Before
	public void setup(){
		company = new Company("test co");
	}


/****** CONSTRUCTOR ******/
	@Test
	public void testCompanyConstructor() {
		// add assert name when getName is done
		company = new Company("test co");
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


/****** GETNAME ******/
	@Test
	public void testGetName(){
		//Constructor tests for nullness/emptiness.
		assertEquals(company.getName(), "test co");
	}
}
