package edu.colostate.cs415.server;

import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.redirect;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.ObjectOutputStream.PutField;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gson.Gson;

import edu.colostate.cs415.db.DBConnector;
import edu.colostate.cs415.dto.QualificationDTO;
import edu.colostate.cs415.dto.WorkerDTO;
import edu.colostate.cs415.dto.AssignmentDTO;
import edu.colostate.cs415.dto.ProjectDTO;
import edu.colostate.cs415.model.Company;
import edu.colostate.cs415.model.Project;
import edu.colostate.cs415.model.Qualification;
import edu.colostate.cs415.model.Worker;
import spark.Request;
import spark.Response;
import spark.Spark;

public class RestController {

	private static Logger log = Logger.getLogger(RestController.class.getName());
	private static String OK = "OK";
	private static String KO = "KO";

	private DBConnector dbConnector;
	private Company company;
	private Gson gson;

	public RestController(int port, DBConnector dbConnector) {
		port(port);
		this.dbConnector = dbConnector;
		gson = new Gson();
	}

	public void start() {
		// Load data from DB
		company = dbConnector.loadCompanyData();

		// Redirect
		redirect.get("/", "/helloworld");

		// Logging
		after("/*", (req, res) -> logRequest(req, res));
		exception(Exception.class, (exc, req, res) -> handleException(exc, res));

		// Hello World
		get("/helloworld", (req, res) -> helloWorld());

		// API
		path("/api", () -> {
			// Enable CORS
			options("/*", (req, res) -> optionsCORS(req, res));
			after("/*", (req, res) -> enableCORS(res));

			// Qualifications
			path("/qualifications", () -> {
				get("", (req, res) -> getQualifications(), gson::toJson);
				get("/:description", (req, res) -> getQualification(req.params("description")),
						gson::toJson);
				post("/:description", (req, res) -> createQualification(req));
			});

			// Workers
			path("/workers", () -> {
				post("/:name", (req, res) -> createWorker(req));
			});

			// Projects
			path("/projects", () -> {
				get("", (req, res) -> getProjects(), gson::toJson);
				post("/:name", (req, res) -> createProject(req));
			});

			// Company
			path("/assign", () -> {
				put("", (req,res) -> assign(req));
			});

			path("/start", () -> {
				put("", (req,res) -> start(req));
			});

			path("/finish", () -> {
				put("", (req,res) -> finish(req));
			});
		});
	}

	public void stop() {
		Spark.stop();
	}

	private String helloWorld() {
		return "Hello World!";
	}

	private QualificationDTO[] getQualifications() {
		// TODO: write actual implementation
		return new QualificationDTO[] { new QualificationDTO("JavaScript", new String[] { "John Walker" }) };
	}

	private QualificationDTO getQualification(String description) {
		// TODO: write actual implementation
		return new QualificationDTO("JavaScript", new String[] { "John Walker" });
	}

	private String createQualification(Request request) {
		QualificationDTO assignmentDTO = gson.fromJson(request.body(), QualificationDTO.class);
		if (request.params("description").equals(assignmentDTO.getDescription())) {
			company.createQualification(assignmentDTO.getDescription());
		} else
			throw new RuntimeException("Qualification descriptions do not match.");
		return OK;
	}

	private ProjectDTO[] getProjects() {
		Set<Project> projects = company.getProjects();
		ProjectDTO[] projectsDTO = new ProjectDTO[projects.size()];
		
		int index = 0;
		for(Project project: projects){
			projectsDTO[index] = project.toDTO();
			index++;
		}
		
		return projectsDTO;
	}

	private String createProject(Request request){
		ProjectDTO projectDTO = gson.fromJson(request.body(), ProjectDTO.class);

		if(projectDTO.getName() == null 
			|| projectDTO.getName().length() == 0
			|| projectDTO.getQualifications() == null
			|| projectDTO.getQualifications().length == 0 
			|| projectDTO.getSize() == null){
			throw new RuntimeException("Required fields not supplied");
		}
		
		if(!request.params("name").equals(projectDTO.getName())){
			throw new RuntimeException("Names do not match");
		}

		String[] quals = projectDTO.getQualifications();
		Set<Qualification> projectQualifications = new HashSet<>();
		for(String q : quals){
			Qualification newQ = new Qualification(q);
			projectQualifications.add(newQ);
		}
		company.createProject(projectDTO.getName(), projectQualifications, projectDTO.getSize());
		return OK;
	}

	private String createWorker(Request request) {
		WorkerDTO workerDTO = gson.fromJson(request.body(), WorkerDTO.class);

		if (!request.params("name").equals(workerDTO.getName()))
			throw new RuntimeException("Names do not match.");

		if (workerDTO.getQualifications() == null
			|| workerDTO.getQualifications().length == 0
		 	|| workerDTO.getName() == null
		  	|| !request.body().contains("salary"))
			throw new IllegalArgumentException("Required fields not supplied");

		String[] quals = workerDTO.getQualifications();
		Set<Qualification> workerQualifications = new HashSet<>();
		for(int i = 0; i < quals.length; i++){
			Qualification qualification = new Qualification(quals[i]);
			workerQualifications.add(qualification);
		}
		company.createWorker(workerDTO.getName(), workerQualifications, workerDTO.getSalary());
		return OK;
	}

	private String assign(Request request) {
		AssignmentDTO assignmentDTO = gson.fromJson(request.body(), AssignmentDTO.class);

		if(assignmentDTO.getWorker() == null || assignmentDTO.getWorker().isEmpty() ||
			assignmentDTO.getWorker() == null || assignmentDTO.getWorker().isEmpty()){
			throw new IllegalArgumentException("Project or worker are empty or null");
		}

		Set<Worker> companyWorkers = company.getEmployedWorkers();
		Set<Project> companyProjects = company.getProjects();
		Worker worker = null;
		Project project = null;

		for (Project p : companyProjects) {
			if(p.getName() == assignmentDTO.getProject()){
				project = p;
			}
		}

		for (Worker w : companyWorkers) {
			if(w.getName() == assignmentDTO.getWorker()){
				worker = w;
			}
		}

		company.assign(worker, project);
		return OK;
	}

	private String start(Request request) {
		ProjectDTO projectDTO = gson.fromJson(request.body(), ProjectDTO.class);

		if (projectDTO.getName() == null || projectDTO.getName().isEmpty())
			throw new IllegalArgumentException("Name is empty or null");

		Set<Project> projects = company.getProjects();
		Project matchingProject = null;
		for (Project project : projects) {
			if(project.getName() == projectDTO.getName()){
				matchingProject = project;
			}
		}
		company.start(matchingProject);
		return OK;
	}

	private String finish(Request request) {
		ProjectDTO projectDTO = gson.fromJson(request.body(), ProjectDTO.class);

		if (projectDTO.getName() == null || projectDTO.getName().isEmpty())
			throw new IllegalArgumentException("Name is empty or null");

		Set<Project> projects = company.getProjects();
		Project matchingProject = null;
		for (Project project : projects) {
			if(project.getName() == projectDTO.getName()){
				matchingProject = project;
			}
		}
		company.finish(matchingProject);
		return OK;
	}

	// Logs every request received
	private void logRequest(Request request, Response response) {
		log.info(request.requestMethod() + " " + request.pathInfo() + "\nREQUEST:\n" + request.body() + "\nRESPONSE:\n"
				+ response.body());
	}

	// Exception handling
	private void handleException(Exception exception, Response response) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace();
		exception.printStackTrace(pw);
		log.severe(sw.toString());
		response.body(KO);
		response.status(500);
	}

	// Enable CORS
	private void enableCORS(Response response) {
		response.header("Access-Control-Allow-Origin", "*");
	}

	// Enable CORS
	private String optionsCORS(Request request, Response response) {
		String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
		if (accessControlRequestHeaders != null)
			response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);

		String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
		if (accessControlRequestMethod != null)
			response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
		return OK;
	}
}