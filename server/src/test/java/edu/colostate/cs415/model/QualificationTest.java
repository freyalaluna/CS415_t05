package edu.colostate.cs415.model;

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

}
