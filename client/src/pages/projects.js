import { useEffect, useState } from 'react'
import { darkGrayContainerStyle, grayContainerStyle, pageStyle, missingStyle, notMissingStyle } from '../utils/styles'
import ClickList from '../components/ClickList'
import LocationID from '../utils/location'
import { getProjects } from '../services/dataService'

const Project = (project, active) => {
    return(
        <div>
            <div>{project.name}</div>
            {/* <button>Test</button> */}
            {active === true ? ProjectBody(project) : null}
        </div>
    )
}

const ProjectBody = (project) => (
    <div style={grayContainerStyle}>
        Size: {project.size} <br />
        Status: {project.status} <br />
        Assigned Employees: 
        {project.workers.length === 0 ? <br/> : <ClickList list={project.workers} styles={darkGrayContainerStyle} path="/projects" id='workers' />}

        {/* I will ask Sudipto about Quals and Missing Quals reqs */}
        Qualifications: <ClickList list={project.missingQualifications} styles={missingStyle} path="/projects" id='qualifications'/>
                <ClickList list={greenQuals(project)} styles={notMissingStyle} path="/projects" id='qualifications'/>
    </div>
)

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
    const [projects, setprojects] = useState([])
    useEffect(() => { getProjects().then(setprojects) }, [])
    const active = LocationID('projects', projects, 'name');
    return (
        <div style={pageStyle}>
            {/* <h1>
                This page displays all of the projects & will soon allow clicking to view project details.
            </h1> */}
            <h1>
                Click on the projects below to view their details.
            </h1>
            <br/><br/>
            <ClickList active={active} list={projects} item={Project} path='/projects' id='name'/>
        </div>
    )
}

export default Projects