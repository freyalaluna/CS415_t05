import { useEffect, useState } from 'react'
import ClickList from '../components/ClickList'
import LocationID from '../utils/location'
import { darkGrayContainerStyle, grayContainerStyle,pageStyle } from '../utils/styles'
import { getWorkers } from '../services/dataService'

const Worker = (Worker, active) => {
    return (
        <div>
            <div>{Worker.name}</div>
            {active === true ? WorkerBody(Worker) : null}
        </div>
    )
}

const WorkerBody = (Worker) => {
    return (
        <div style={grayContainerStyle}>
            <b>Salary:</b> ${Worker.salary} <br></br>
            <b>Workload Value:</b> {Worker.workload} <br></br><br></br>
            <b>Qualifications:</b> <ClickList list={Worker.qualifications} styles={darkGrayContainerStyle} path="/qualifications" /><br></br>
            <b>Projects:</b> <ClickList list={Worker.projects} styles={darkGrayContainerStyle} path="/projects" />
        </div>
    )
}

const Workers = () => {
    const [workers, setworkers] = useState([])
    useEffect(() => { getWorkers().then(setworkers) }, [])
    const active = LocationID('workers', workers, 'name')
    return (
        <div style={pageStyle}>
            <h1>
                This page displays a table containing all the workers.
            </h1>
            <ClickList active={active} list={workers} item={Worker} path='/workers' id='name' />
        </div>
    )
}

export default Workers