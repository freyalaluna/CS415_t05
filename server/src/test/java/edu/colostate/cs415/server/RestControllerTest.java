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
import edu.colostate.cs415.dto.QualificationDTO;
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
        assertEquals("Daisy", company.getEmployedWorkers().iterator().next().getName());
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
        thrown.expect(HttpResponseException.class);
        restController.start();
        Request.post("http://localhost:4567/api/workers/Daisy")
                .bodyByteArray(body.getBytes())
                .execute().returnContent().asString();
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
        thrown.expect(HttpResponseException.class);
        restController.start();
        Request.post("http://localhost:4567/api/workers/Daisy")
                .bodyByteArray(body.getBytes())
                .execute().returnContent().asString();
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
        thrown.expect(HttpResponseException.class);
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

    @Test
    public void testPostWorker8() throws IOException {
        // null quals, no worker created
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        String body = "{ \"name\": \"Daisy\", \"salary\": 150000.0," +
            "\"qualifications\": null}";
        thrown.expect(HttpResponseException.class);
        restController.start();
        Request.post("http://localhost:4567/api/workers/Daisy")
                .bodyByteArray(body.getBytes())
                .execute().returnContent().asString();
    }

    @Test
    public void testPostWorker9() throws IOException {
        // null salary
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        String body = "{ \"name\": \"Daisy\", \"salary\": null," +
            "\"qualifications\": null}";
        thrown.expect(HttpResponseException.class);
        restController.start();
        Request.post("http://localhost:4567/api/workers/Daisy")
                .bodyByteArray(body.getBytes())
                .execute().returnContent().asString();
    }

    @Test
    public void testPostWorker10() throws IOException {
        // null salary
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        company.createProject("Moon mission", quals, ProjectSize.BIG);
        String body = "{ \"name\": \"Daisy\", \"salary\": \"null\"," +
            "\"qualifications\": null}";
        thrown.expect(HttpResponseException.class);
        restController.start();
        Request.post("http://localhost:4567/api/workers/Daisy")
                .bodyByteArray(body.getBytes())
                .execute().returnContent().asString();
    }

    @Test
    public void testGetQuals1() throws IOException {
        company = new Company("Company 1");
        assertEquals(0, company.getQualifications().size());
        Qualification java = company.createQualification("Java");
        restController.start();
        String response = Request.get("http://localhost:4567/api/qualifications")
                            .execute().returnContent().asString();
        assertEquals(1, company.getQualifications().size());
        assertEquals(java.toString(), company.getQualifications().iterator().next().toString());
        assertEquals("[{\"description\":\"Java\",\"workers\":[]}]", response);
    }

    @Test
    public void testGetQuals2() throws IOException {
        company = new Company("Company 1");
        restController.start();
        String response = Request.get("http://localhost:4567/api/qualifications")
                            .execute().returnContent().asString();

        assertEquals(0, company.getQualifications().size());
        assertEquals("[]", response);
    }

    @Test
    public void testGetQuals3() throws IOException {
        company = new Company("Company 1");
        restController.start();
        String response = Request.get("http://localhost:4567/api/qualifications/q1")
                            .execute().returnContent().asString();

                            System.out.println(response);
        assertEquals(0, company.getQualifications().size());
        assertEquals("null", response);
    }

    @Test
    public void testGetQuals4() throws IOException {
        company = new Company("Company 1");
        assertEquals(0, company.getQualifications().size());
        Qualification java = company.createQualification("Java");
        restController.start();
        String response = Request.get("http://localhost:4567/api/qualifications/Java")
                            .execute().returnContent().asString();
        System.out.println(response);
        assertEquals(1, company.getQualifications().size());
        assertEquals(java.toString(), company.getQualifications().iterator().next().toString());
        assertEquals("{\"description\":\"Java\",\"workers\":[]}", response);
    }
    
    public void testPutStart() throws IOException {
        // Valid project returns OK
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        Worker w1 = company.createWorker("w", quals, 10);
        Project p1 = company.createProject("Moon mission", quals, ProjectSize.SMALL);
        company.assign(w1, p1);
        String body = "{ \"name\": \"Moon mission\"}";
        restController.start();
        String response = gson.fromJson(
                        Request.put("http://localhost:4567/api/start")
                        .bodyByteArray(body.getBytes())
                        .execute().returnContent().asString(), String.class);
        
        assertEquals("OK", response);
        assertEquals(ProjectStatus.ACTIVE, company.getProjects().iterator().next().getStatus());
        
    }

    @Test
    public void testPutStart1() throws IOException {
        // project names dont match
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        Worker w1 = company.createWorker("w", quals, 10);
        Project p1 = company.createProject("Moon mission", quals, ProjectSize.SMALL);
        company.assign(w1, p1);
        String body = "{ \"name\": \"Moon\"}";
        thrown.expect(HttpResponseException.class);
        restController.start();
        Request.put("http://localhost:4567/api/start")
        .bodyByteArray(body.getBytes())
        .execute().returnContent();
    }

    @Test
    public void testPutStart2() throws IOException {
        // blank project name
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        Worker w1 = company.createWorker("w", quals, 10);
        Project p1 = company.createProject("Moon mission", quals, ProjectSize.SMALL);
        company.assign(w1, p1);
        String body = "{ \"name\": \"\"}";
        thrown.expect(HttpResponseException.class);
        restController.start();
        Request.put("http://localhost:4567/api/start")
        .bodyByteArray(body.getBytes())
        .execute().returnContent();
    }

    @Test
    public void testPutStart3() throws IOException {
        // null project
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        Worker w1 = company.createWorker("w", quals, 10);
        Project p1 = company.createProject("Moon mission", quals, ProjectSize.SMALL);
        company.assign(w1, p1);
        String body = "{ \"name\": null}";
        thrown.expect(HttpResponseException.class);
        restController.start();
        Request.put("http://localhost:4567/api/start")
        .bodyByteArray(body.getBytes())
        .execute().returnContent();
    }

    @Test
    public void testPutStart4() throws IOException {
        // company has no projects
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        String body = "{ \"name\": \"Moon mission\"}";
        thrown.expect(HttpResponseException.class);
        restController.start();
        Request.put("http://localhost:4567/api/start")
        .bodyByteArray(body.getBytes())
        .execute().returnContent();
    }

    @Test
    public void testPutStart5() throws IOException {
        // project already started 
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        Worker w1 = company.createWorker("w", quals, 10);
        Project p1 = company.createProject("Moon mission", quals, ProjectSize.SMALL);
        company.assign(w1, p1);
        company.start(p1);
        String body = "{ \"name\": \"Moon mission\"}";
        restController.start();
        Request.put("http://localhost:4567/api/start");
        String response = gson.fromJson(
                        Request.put("http://localhost:4567/api/start")
                        .bodyByteArray(body.getBytes())
                        .execute().returnContent().asString(), String.class);
        assertEquals("OK", response);
        assertEquals(ProjectStatus.ACTIVE, company.getProjects().iterator().next().getStatus());
    }

    @Test
    public void testPutStart6() throws IOException {
        // missing quals
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Qualification python = company.createQualification("Python");
        Set<Qualification> qualsWorker = new HashSet<Qualification>();
        qualsWorker.add(java);
        Set<Qualification> qualsProj = new HashSet<Qualification>();
        qualsProj.add(java);
        qualsProj.add(python);
        Worker w1 = company.createWorker("w", qualsWorker, 10);
        Project p1 = company.createProject("Moon mission", qualsProj, ProjectSize.SMALL);
        company.assign(w1, p1);
        String body = "{ \"name\": \"Moon mission\"}";
        restController.start();
        String response = gson.fromJson(
                        Request.put("http://localhost:4567/api/start")
                        .bodyByteArray(body.getBytes())
                        .execute().returnContent().asString(), String.class);
        assertEquals("OK", response);
        assertEquals(ProjectStatus.PLANNED, company.getProjects().iterator().next().getStatus());
    }

    @Test
    public void testPutStart7() throws IOException {
        // suspended project
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Qualification python = company.createQualification("Python");
        Set<Qualification> qualsW1 = new HashSet<Qualification>();
        qualsW1.add(java);
        Set<Qualification> qualsW2 = new HashSet<Qualification>();
        qualsW2.add(python);
        Set<Qualification> qualsProj = new HashSet<Qualification>();
        qualsProj.add(python);
        qualsProj.add(java);
        Worker w1 = company.createWorker("w1", qualsW1, 10);
        Worker w2 = company.createWorker("w2", qualsW2, 10);
        Project p1 = company.createProject("Moon mission", qualsProj, ProjectSize.SMALL);
        company.assign(w1, p1);
        company.assign(w2, p1);
        company.start(p1);
        company.unassign(w2, p1);
        String body = "{ \"name\": \"Moon mission\"}";
        restController.start();
        String response = gson.fromJson(
                        Request.put("http://localhost:4567/api/start")
                        .bodyByteArray(body.getBytes())
                        .execute().returnContent().asString(), String.class);
        assertEquals("OK", response);
        assertEquals(ProjectStatus.ACTIVE, company.getProjects().iterator().next().getStatus());
    }

    public void testPutFinish() throws IOException {
        // Valid project returns OK
        company = new Company("Company 1");
        Qualification java = company.createQualification("Java");
        Set<Qualification> quals = new HashSet<Qualification>();
        quals.add(java);
        Worker w1 = company.createWorker("w", quals, 10);
        Project p1 = company.createProject("Moon mission", quals, ProjectSize.SMALL);
        company.assign(w1, p1);
        company.start(p1);
        String body = "{ \"name\": \"Moon mission\"}";
        company.start(p1);
        restController.start();
        String response = gson.fromJson(
                        Request.put("http://localhost:4567/api/finish")
                        .bodyByteArray(body.getBytes())
                        .execute().returnContent().asString(), String.class);
        
        assertEquals("OK", response);
        assertEquals(ProjectStatus.FINISHED, company.getProjects().iterator().next().getStatus());
    }

}
