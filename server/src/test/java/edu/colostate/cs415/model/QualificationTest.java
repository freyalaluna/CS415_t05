package edu.colostate.cs415.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class QualificationTest {
	/***** HASHCODE *****/
	@Test
	public void testHashCodeWithNullString() {
		Qualification qualificationWithNullDesc = new Qualification("");
		assertEquals("Qualification.hashCode returns 0 with a null string", qualificationWithNullDesc.hashCode(), 0);
	}

	@Test
	public void testHashCodeWithEmptyString() {
		Qualification qualificationWithEmptyDesc = new Qualification("");
		assertEquals( "Qualification.hashCode returns 0 with an empty string", qualificationWithEmptyDesc.hashCode(), 0);
	}

	@Test
	public void testHashCodeWithValidString() {
		Qualification qualificationWithValidDesc = new Qualification("test");
		assertThat("Qualification.hashCode returns a non 0 code with a valid name", qualificationWithValidDesc.hashCode(), is(not(0)));
	}
}
