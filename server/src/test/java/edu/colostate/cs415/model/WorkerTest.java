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

}
