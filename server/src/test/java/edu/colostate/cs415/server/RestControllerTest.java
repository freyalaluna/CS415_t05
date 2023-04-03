package edu.colostate.cs415.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.hc.client5.http.fluent.Request;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import edu.colostate.cs415.db.DBConnector;
import edu.colostate.cs415.dto.ProjectDTO;
import edu.colostate.cs415.dto.WorkerDTO;
import edu.colostate.cs415.model.Company;
import edu.colostate.cs415.model.Project;
import edu.colostate.cs415.model.ProjectSize;
import edu.colostate.cs415.model.ProjectStatus;
import edu.colostate.cs415.model.Qualification;
import edu.colostate.cs415.model.Worker;

public class RestControllerTest {

    private Gson gson = new Gson();
    private static DBConnector dbConnector = mock(DBConnector.class);
    private static RestController restController = new RestController(4567, dbConnector);
    private static Company company;

    @BeforeClass
    public static void init(){
        when(dbConnector.loadCompanyData()).thenAnswer((i) -> company);
    }

    @Test
    public void testGetProjects1() throws IOException {
        // No workers assigned to project, has missing qualifications
            company = new Company("Company 1");
            Qualification java = company.createQualification("Java");
            Set<Qualification> quals = new HashSet<Qualification>();
            quals.add(java);
            company.createProject("Moon mission", quals, ProjectSize.BIG);
        
            restController.start();
            
            ProjectDTO[] projects = gson.fromJson(
                            Request.get("http://localhost:4567/api/projects").execute().returnContent().asString(),
                            ProjectDTO[].class);

            assertEquals(1, projects.length);
            assertEquals("Moon mission", projects[0].getName());
            assertEquals(ProjectSize.BIG, projects[0].getSize());
            assertEquals(ProjectStatus.PLANNED, projects[0].getStatus());
            assertEquals("Java", projects[0].getMissingQualifications()[0]);
            assertEquals("Java", projects[0].getQualifications()[0]);
            assertEquals(0, projects[0].getWorkers().length);
    }

    @Test
    public void testGetProjects2() throws IOException {
        // No projects returns empty list
            company = new Company("Company 1");
            restController.start();
            
            ProjectDTO[] projects = gson.fromJson(
                            Request.get("http://localhost:4567/api/projects").execute().returnContent().asString(),
                            ProjectDTO[].class);
            
            assertEquals(0, projects.length);
    }


}
