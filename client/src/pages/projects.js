import { useEffect, useState } from 'react'
import { darkGrayContainerStyle, grayContainerStyle, pageStyle, missingStyle, notMissingStyle } from '../utils/styles'
import ClickList from '../components/ClickList'
import LocationID from '../utils/location'
import { getProjects, getWorkers, assignWorker, unasignWorker } from '../services/dataService'
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';

const Project = (project, active, extraProps) => {
    return(
        <div>
            <div>{project.name}</div>
            {/* <button>Test</button> */}
            {active === true ?  ProjectBody(project, extraProps)  : null}
        </div>
    )
}

const ProjectBody = (project, extraProps) => {
    const {assignDropdownOpen, setAssignDropdownOpen, unassignDropdownOpen, setUnassignDropdownOpen, workers, setprojects} = extraProps;
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
            {project.missingQualifications.length === 0 ? <div> - </div> : 
                <div>
                    <br></br>
                    <Dropdown isOpen={assignDropdownOpen} toggle={(e) => {
                        e.stopPropagation();
                        setAssignDropdownOpen(prevState => !prevState);
                    }}>
                        <DropdownToggle caret>
                            Assign Worker
                        </DropdownToggle>
                        <DropdownMenu>
                            {assignList(project, workers)?.map((worker, idx) => <DropdownItem
                                key={idx}
                                onClick={() => {
                                    assignWorker(worker, project.name).then((response) => getProjects().then(setprojects))
                                }}
                            >
                                {worker}
                            </DropdownItem>
                            
                            )}
                        </DropdownMenu>
                    </Dropdown>    
                </div>}

            {project.workers.length === 0 ? <div>-</div> : 
                <div>
                    <Dropdown isOpen={unassignDropdownOpen} toggle={(e) => {
                        e.stopPropagation();
                        setUnassignDropdownOpen(prevState => !prevState)
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

const assignList = (project, workers) => {
    const missingQuals = project.missingQualifications;
    const preassignedWorkers = project.workers;
    const eligibleWorkers = new Array();

    for (let i = 0; i < workers.length; i++){  //For each worker, check if they're already assigned, and if they can take the workload
        const thisWorker = workers[i];
        if((preassignedWorkers.indexOf(thisWorker) > -1)
             || (thisWorker.workload === 12)
             || ((thisWorker.workload >= 11) && (project.size === "MEDIUM"))
             || ((thisWorker.workload >= 10) && (project.size === "BIG"))){
                continue;
             }
        eligibleWorkers.push(workers[i]);
    }

    const assignableWorkers = new Array();
    for (let i = 0; i < eligibleWorkers.length; i++){ //Add each worker to the list if they have any of the missing qualifications
        const workerQuals = eligibleWorkers[i].qualifications;
        if(missingQuals.some(r=> workerQuals.indexOf(r) >= 0)){
            assignableWorkers.push(eligibleWorkers[i].name);
        }
    }

    return assignableWorkers;
}

const Projects = () => {
    const [assignDropdownOpen, setAssignDropdownOpen] = useState(false);
    const [unassignDropdownOpen, setUnassignDropdownOpen] = useState(false);
    // Add this to extraProps and then use to toggle the assign button
    // [assignDropdownOpen, setAssignDropdownOpen] = useState(false);
    const [projects, setprojects] = useState([])
    const [workers, setworkers] = useState([])
    useEffect(() => { getProjects().then(setprojects) }, [])
    useEffect(() => { getWorkers().then(setworkers) }, [])
    const active = LocationID('projects', projects, 'name');
    const extraProps = {
            assignDropdownOpen: assignDropdownOpen,
            unassignDropdownOpen: unassignDropdownOpen,
            setAssignDropdownOpen: setAssignDropdownOpen,
            setUnassignDropdownOpen: setUnassignDropdownOpen,
            workers: workers,
            setprojects: setprojects
        }
    return (
        <div style={pageStyle}>
            {/* <h1>
                This page displays all of the projects & will soon allow clicking to view project details.
            </h1> */}
            <h2>
                Click on the projects below to view their details.
            </h2>
            <br/><br/>
            <ClickList active={active} list={projects} item={Project} path='/projects' id='name' extraProps={extraProps} />
        </div>
    )
}

export default Projects