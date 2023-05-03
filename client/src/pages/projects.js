import { useEffect, useState } from 'react'
import Select from 'react-select'
import makeAnimated from 'react-select/animated';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'

import { darkGrayContainerStyle, grayContainerStyle, pageStyle, missingStyle, notMissingStyle } from '../utils/styles'
import ClickList from '../components/ClickList'
import LocationID from '../utils/location'
import { createProject, getProjects, getQualifications, getWorkers, unasignWorker, startProject  } from '../services/dataService'

const animatedComponents = makeAnimated();


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
    const {unassignDropdownOpen, setUnassignDropdownOpen, setprojects} = extraProps;
    return(
        <div>
            <div style={grayContainerStyle}>
                <b>Size:</b> {project.size} <br />
                <b>Status:</b> {project.status} <br /><br />
                <b>Assigned Employees:</b> 
                {project.workers.length === 0 ? <div>-</div> : <ClickList list={project.workers} styles={darkGrayContainerStyle} path="/workers" />}
                <br />
                <b>Qualifications:</b> <ClickList list={project.missingQualifications} styles={missingStyle} path="/qualifications"/>
                                <ClickList list={greenQuals(project)} styles={notMissingStyle} path="/qualifications"/>
            </div>
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

const qualsDescriptions = (quals) => {
    const qualOptions = [];

    for (let i = 0; i < quals.length; i++) {
        qualOptions.push({value: quals[i].description, label: quals[i].description});
    }

    return qualOptions;
}

const CreateProjectForm = (props) => { 
    const { setProjects } = props
    const [quals, setQualifications] = useState([])
    const [selectedQuals, setSelectedQuals] = useState(null);
    const [selectedSize, setSelectedSize] = useState(null);

    const handleQualsChange = (selectedOptions) => {
        setSelectedQuals(Array.isArray(selectedOptions) ? selectedOptions : []);
    };

    const handleSizeChange = (selectedOption) => {
        setSelectedSize(selectedOption);
    };

    useEffect(() => {
        getQualifications().then((quals) => { 
            setQualifications(quals)
        })
    }, [])
    
    return (
        <div className="card">
            <div className="card-body">
                <form>
                    <div className="form-group">
                        <input type="text" className="form-control" id="name" placeholder="Enter the project name..." required/><br/>

                        <Select
                            id="quals"
                            placeholder="Select the qualifications..."
                            options={qualsDescriptions(quals)}
                            value={selectedQuals}
                            onChange={handleQualsChange}
                            isSearchable
                            isMulti
                            closeMenuOnSelect={false}
                            components={animatedComponents}
                            required
                        /><br/>

                        <Select 
                            id="size"
                            placeholder="Select the project size..."
                            options={[
                                { key:'SMALL', value:'SMALL', label: 'SMALL' },
                                { key:'MEDIM', value: 'MEDIUM', label: 'MEDIUM' },
                                { value: 'BIG', label: 'BIG' }
                            ]}
                            value={selectedSize}
                            isSearchable
                            onChange={handleSizeChange} 
                            required
                        /><br/>

                        <button type="button" className="btn btn-outline-primary"
                            onClick={() => {
                                const name = document.getElementById('name').value
                                const size = selectedSize.value
                                const quals = selectedQuals.map(x=>x.value)
                                if (name && quals && size) {
                                    createProject(name, quals, size).then(response => {
                                        if (response?.data === 'OK') {
                                            setSelectedQuals([])
                                            setSelectedSize('')
                                            document.getElementById('name').value = ''
                                        } else { 
                                            alert('Error: ' + response?.data)
                                        }
                                    }).then(() => { 
                                        Promise.all([getProjects(), getWorkers()]).then(setProjects())
                                    })
                                }
                            }}>
                        Create Project</button>
                    </div>
                </form>
            </div>
        </div>
    )
}

const Projects = () => {
    const [unassignDropdownOpen, setUnassignDropdownOpen] = useState(false);
    const [data] = useState([])
    // Add this to extraProps and then use to toggle the assign button
    // [assignDropdownOpen, setAssignDropdownOpen] = useState(false);
    const [projects, setprojects] = useState([])
    useEffect(() => { getProjects().then(setprojects) }, [])
    const active = LocationID('projects', projects, 'name');
    const extraProps = {
            unassignDropdownOpen: unassignDropdownOpen,
            setUnassignDropdownOpen: setUnassignDropdownOpen,
            setprojects: setprojects
        }
    return (
        <div style={pageStyle}>
            {/* <h1>
                This page displays all of the projects & will soon allow clicking to view project details.
            </h1> */}
            <h2>Create a new project with the</h2>
            <CreateProjectForm setData={data} />
            <br/><br/>
            <h2>
                Click on the projects below to view their details.
            </h2>
            <ClickList active={active} list={projects} item={Project} path='/projects' id='name' extraProps={extraProps} />
        </div>
    )
}

export default Projects