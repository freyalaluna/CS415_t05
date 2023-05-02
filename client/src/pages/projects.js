import { useEffect, useState } from 'react'
import { darkGrayContainerStyle, grayContainerStyle, pageStyle, missingStyle, notMissingStyle } from '../utils/styles'
import ClickList from '../components/ClickList'
import LocationID from '../utils/location'
import { getProjects, unasignWorker, startProject } from '../services/dataService'
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';


const Project = (project, setprojects, active, dropdownOpen, setDropdownOpen) => {
    return(
        <div>
            <div>{project.name}</div>
            {/* <button>Test</button> */}
            {active === true ?  ProjectBody(project, setprojects, dropdownOpen, setDropdownOpen)  : null}
        </div>
    )
}

const ProjectBody = (project, setprojects, dropdownOpen, setDropdownOpen) => {
    return(
        <div>
            <div style={grayContainerStyle}>
                Size: {project.size} <br />
                Status: {project.status} <br />
                Assigned Employees: 
                {project.workers.length === 0 ? <div>-</div> : <ClickList list={project.workers} styles={darkGrayContainerStyle} path="/workers" />}
                <br />
                Qualifications: <ClickList list={project.missingQualifications} styles={missingStyle} path="/qualifications"/>
                        <ClickList list={greenQuals(project)} styles={notMissingStyle} path="/qualifications"/>
            </div>
            {project.workers.length === 0 ? <div>-</div> : 
                <div>
                    <Dropdown isOpen={dropdownOpen} toggle={(e) => {
                        e.stopPropagation();
                        setDropdownOpen(prevState => !prevState)
                    }}>
                        <DropdownToggle caret>
                            Unassign Worker
                        </DropdownToggle>
                        <DropdownMenu>
                            {project.workers?.map((worker, idx) => <DropdownItem
                                key={idx}
                                onClick={() => {
                                    unasignWorker(worker, project.name).then((response) => getProjects().then(setprojects))
                                }}
                            >
                                {worker}
                            </DropdownItem>

                            )}
                        </DropdownMenu>
                    </Dropdown>
                </div>}
                <br />
                {project.missingQualifications.length !==  0  ||  project.status.toString() !== "PLANNED" ? <div>-</div> :
                <div>
                    <button
                        type="button" className="btn btn-outline-primary"
                        onClick={(e) => {
                            e.stopPropagation();
                            console.log("Clicked Start");
                            console.log(project.name);
                            startProject(project.name).then((response) => getProjects().then(setprojects))
                        }}
                    >
                        Start Project
                    </button>
                </div>}
        </div>
    )
}
    


const greenQuals = (project) => {
    const quals = project.qualifications;
    const missingQuals = project.missingQualifications;
    const greenQuals = [];

    // check if quals contain missing quals
    for (let i = 0; i < quals.length; i++) {
        // if quals do NOT contain missing quals
        if (!missingQuals.includes(quals[i])) {
            // add to greenQuals
            greenQuals.push(quals[i]);
        }
    }

    return greenQuals;
}

const Projects = () => {
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const [projects, setprojects] = useState([])
    useEffect(() => { getProjects().then(setprojects) }, [])
    const active = LocationID('projects', projects, 'name');
    return (
        <div style={pageStyle}>
            {/* <h1>
                This page displays all of the projects & will soon allow clicking to view project details.
            </h1> */}
            <h2>
                Click on the projects below to view their details.
            </h2>
            <br/><br/>
            <ClickList active={active} list={projects} setprojects={setprojects} item={Project} path='/projects' id='name' dropdownOpen={dropdownOpen} setDropdownOpen={setDropdownOpen} />
        </div>
    )
}

export default Projects