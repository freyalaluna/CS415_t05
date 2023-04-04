package edu.colostate.cs415.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.fluent.Request;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.gson.Gson;

import edu.colostate.cs415.db.DBConnector;
import edu.colostate.cs415.dto.ProjectDTO;
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

    @Rule public ExpectedException thrown = ExpectedException.none();

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

    @Test
    public void testGetProjects3() throws IOException {
        // Project with worker, no missing qualifications
            company = new Company("Company 1");
            Qualification java = company.createQualification("Java");
            Set<Qualification> quals = new HashSet<Qualification>();
            quals.add(java);
            company.createProject("Moon mission", quals, ProjectSize.BIG);
            Worker worker = new Worker("w1", quals, 10);
            Project project =company.getProjects().iterator().next();
            company.start(project);
            company.createWorker("w1", quals, 10);
            company.assign(worker, project);
            restController.start();
            ProjectDTO[] projects = gson.fromJson(
                            Request.get("http://localhost:4567/api/projects").execute().returnContent().asString(),
                            ProjectDTO[].class);
            
            assertEquals(1, projects.length);
            assertEquals("Moon mission", projects[0].getName());
            assertEquals(ProjectSize.BIG, projects[0].getSize());
            assertEquals(ProjectStatus.PLANNED, projects[0].getStatus());
            assertEquals(0, projects[0].getMissingQualifications().length);
            assertEquals("Java", projects[0].getQualifications()[0]);
            assertEquals("w1", projects[0].getWorkers()[0]);
    }


    @Test
    public void testGetProjects4() throws IOException {
        // Workers assigned to project, has missing qualifications, multiple projects
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Qualification python = company.createQualification("Python");
        Qualification c = company.createQualification("C");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        quals.add(python);
        quals.add(c);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        company.createProject("Teleportation", quals, ProjectSize.BIG);
        Worker worker = new Worker("w1", new HashSet<Qualification>(Arrays.asList(java)), 10);
        Worker worker2 = new Worker("w2", new HashSet<Qualification>(Arrays.asList(python)), 10);
        Project project =company.getProjects().iterator().next();
        company.start(project);
        company.createWorker("w1", new HashSet<Qualification>(Arrays.asList(java)), 10);
        company.createWorker("w2", new HashSet<Qualification>(Arrays.asList(python)), 10);
        company.assign(worker, project);
        company.assign(worker2, project);
        restController.start();
        ProjectDTO[] projects = gson.fromJson(
                        Request.get("http://localhost:4567/api/projects").execute().returnContent().asString(),
                        ProjectDTO[].class);
        
        assertEquals(2, projects.length);
        assertEquals("Moon mission", projects[0].getName());
        assertEquals("Teleportation", projects[1].getName());
        assertEquals(ProjectSize.BIG, projects[0].getSize());
        assertEquals(ProjectStatus.PLANNED, projects[0].getStatus());
        assertEquals("C", projects[0].getMissingQualifications()[0]);
        assertEquals(3, projects[0].getQualifications().length);
        assertEquals(2, projects[0].getWorkers().length);
    }

    @Test
    public void testGetProjects5() throws IOException {
        // Multiple workers assigned to project, no missing qualifications
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Qualification python = company.createQualification("Python");
        Qualification c = company.createQualification("C");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        quals.add(python);
        quals.add(c);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        company.createProject("Teleportation", quals, ProjectSize.BIG);
        Worker worker = new Worker("w1", new HashSet<Qualification>(Arrays.asList(java, c)), 10);
        Worker worker2 = new Worker("w2", new HashSet<Qualification>(Arrays.asList(python)), 10);
        Project project =company.getProjects().iterator().next();
        company.start(project);
        company.createWorker("w1", new HashSet<Qualification>(Arrays.asList(java, c)), 10);
        company.createWorker("w2", new HashSet<Qualification>(Arrays.asList(python)), 10);
        company.assign(worker, project);
        company.assign(worker2, project);
        restController.start();
        ProjectDTO[] projects = gson.fromJson(
                        Request.get("http://localhost:4567/api/projects").execute().returnContent().asString(),
                        ProjectDTO[].class);
        
        assertEquals(2, projects.length);
        assertEquals("Moon mission", projects[0].getName());
        assertEquals("Teleportation", projects[1].getName());
        assertEquals(ProjectSize.BIG, projects[0].getSize());
        assertEquals(ProjectStatus.PLANNED, projects[0].getStatus());
        assertEquals(0, projects[0].getMissingQualifications().length);
        assertEquals(3, projects[0].getQualifications().length);
        assertEquals(2, projects[0].getWorkers().length);
    }

    @Test
    public void testPostWorker1() throws IOException {
        // Valid worker returns OK
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        String body = "{ \"name\": \"Daisy\", \"salary\": 150000.0," +
            "\"qualifications\": [\"Java\"]}";
        restController.start();
        String response = gson.fromJson(
                        Request.post("http://localhost:4567/api/workers/Daisy")
                        .bodyByteArray(body.getBytes())
                        .execute().returnContent().asString(), String.class);
        
        assertEquals("OK", response);
        assertEquals("Daisy", company.getEmployedWorkers().iterator().next());
    }


    @Test
    public void testPostWorker2() throws IOException {
        // Param name doesn't match body name
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        String body = "{ \"name\": \"Daisy\", \"salary\": 150000.0," +
            "\"qualifications\": [\"Java\"]}";
        thrown.expect(HttpResponseException.class);
        restController.start();
        Request.post("http://localhost:4567/api/workers/aisy")
                .bodyByteArray(body.getBytes())
                .execute().returnContent().asString();
    }

    @Test
    public void testPostWorker3() throws IOException {
        // Salary not supplied, no worker created
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        String body = "{ \"name\": \"Daisy\"," +
            "\"qualifications\": [\"Java\"]}";
        restController.start();
        Request.post("http://localhost:4567/api/workers/Daisy")
                .bodyByteArray(body.getBytes())
                .execute().returnContent().asString();

        assertEquals(0, company.getEmployedWorkers().size());
    }


    @Test
    public void testPostWorker4() throws IOException {
        // Quals not supplied, no worker created
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        String body = "{ \"name\": \"Daisy\", \"salary\": 150000.0";
        restController.start();
        Request.post("http://localhost:4567/api/workers/Daisy")
                .bodyByteArray(body.getBytes())
                .execute().returnContent().asString();

        assertEquals(0, company.getEmployedWorkers().size());
    }

    @Test
    public void testPostWorker5() throws IOException {
        // Quals supplied but empty, no worker created
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        String body = "{ \"name\": \"Daisy\", \"salary\": 150000.0," +
            "\"qualifications\": []}";
        restController.start();
        Request.post("http://localhost:4567/api/workers/Daisy")
                    .bodyByteArray(body.getBytes())
                    .execute().returnContent().asString();

        assertEquals(0, company.getEmployedWorkers().size());
    }

    @Test
    public void testPostWorker6() throws IOException {
        // Salary negative, no worker created
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        String body = "{ \"name\": \"Daisy\", \"salary\": -150000.0," +
            "\"qualifications\": [\"Java\"]}";
        restController.start();
        Request.post("http://localhost:4567/api/workers/Daisy")
                .bodyByteArray(body.getBytes())
                .execute().returnContent().asString();

        assertEquals(0, company.getEmployedWorkers().size());
    }

    @Test
    public void testPostWorker7() throws IOException {
        // null name, no worker created
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        String body = "{ \"name\": \"null\", \"salary\": -150000.0," +
            "\"qualifications\": [\"Java\"]}";
        restController.start();
        Request.post("http://localhost:4567/api/workers/null")
                .bodyByteArray(body.getBytes())
                .execute().returnContent().asString();

        assertEquals(0, company.getEmployedWorkers().size());
    }
}
