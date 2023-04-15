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
            Workers: <ClickList list={Worker.workers} styles={darkGrayContainerStyle} path="/workers" />
        </div>
    )
}

const Workers = () => {
    const [workers, setworkers] = useState([])
    useEffect(() => { getWorkers().then(setworkers) }, [])
    const active = LocationID('workers', workers, 'description')
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