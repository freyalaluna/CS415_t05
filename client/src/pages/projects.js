import { useEffect, useState } from 'react'
import { darkGrayContainerStyle, grayContainerStyle, pageStyle } from '../utils/styles'
import ClickList from '../components/ClickList'
import LocationID from '../utils/location'
import { getProjects } from '../services/dataService'

const Project = (project, active) => {
    return(
        <div>
            <div>{project.name}</div>
            {active === true ? ProjectBody(project) : null}
        </div>
    )
}

const ProjectBody = (project) => {
    return(
        <div style={grayContainerStyle}>
            Projects: <ClickList list={project} styles={darkGrayContainerStyle} path="/workers" />
        </div>
    )
}

const Projects = () => {
    const [projects, setprojects] = useState([])
    useEffect(() => { getProjects().then(setprojects) }, [])
    const active = LocationID('projects', projects, 'name');
    return (
        <div style={pageStyle}>
            <h1>
                This page displays all of the projects & will soon allow clicking to view project details.
            </h1>
            <br/><br/>
            <ClickList active={active} list={projects} item={Project} path='/projects' id='name'/>
        </div>
    )
}

export default Projects