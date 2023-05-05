import { useEffect, useState } from 'react'
import Select from 'react-select'
import makeAnimated from 'react-select/animated'
import ClickList from '../components/ClickList'
import LocationID from '../utils/location'
import { clickListStyle, darkGrayContainerStyle, grayContainerStyle,pageStyle } from '../utils/styles'
import { getQualifications, getWorkers, createWorker } from '../services/dataService'

const animatedComponents = makeAnimated();

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
            <div style={clickListStyle}><b>Salary:</b> ${Worker.salary}</div>
            <div style={clickListStyle}><b>Workload Value:</b> {Worker.workload}</div>
            <div style={clickListStyle}>
                <b>Qualifications:</b> <ClickList list={Worker.qualifications} styles={darkGrayContainerStyle} path="/qualifications" />
            </div>
            <div style={clickListStyle}>
                <b>Projects:</b> <ClickList list={Worker.projects} styles={darkGrayContainerStyle} path="/projects" />
            </div>
        </div>
    )
}

const qualsDescription = (quals) => {
    const qualOptions = [];

    for(let i = 0; i < quals.length; i++){
        qualOptions.push({value: quals[i].description, label: quals[i].description});
    }

    return qualOptions;
}

const CreateWorkerForm = (props) => {
    const { setworkers } = props
    const [quals, setQualifications] = useState([])
    const [selectedQuals, setSelectedQuals] = useState(null);

    const handleQualsChange = (selectedOptions) => {
        setSelectedQuals(Array.isArray(selectedOptions) ? selectedOptions : []);
    };

    useEffect(() => {
        getQualifications().then((quals) => {
            setQualifications(quals)
        })

    }, [])

    return(
        <div className="card">
            <div className="card-body">
                <form>
                    <div className="form-group">
                        <input 
                            id="name"
                            type="text"
                            className="form-control"
                            placeholder="Enter the worker's name..."
                            required={true}
                        /><br />

                        <input
                            id="salary"
                            type="number"
                            className="form-control"
                            placeholder="Enter worker's salary..."
                            min="0"
                            step=".01"
                            required={true}
                        /><br />                    
                        
                        <Select
                            id="quals"
                            placeholder="Worker's qualifications..."
                            options={qualsDescription(quals)}
                            value={selectedQuals}
                            onChange={handleQualsChange}
                            isSearchable
                            isMulti
                            components={animatedComponents}
                            required={true}
                        /><br />

                        <button type="button" className="btn btn-outline-primary"
                            onClick={() => {
                                const name = document.getElementById("name").value
                                const salary = document.getElementById("salary").value
                                const quals = selectedQuals.map(x=>x.value)
                                if (name && salary && quals){
                                    createWorker(name, quals, salary).then(response => {
                                        if (response?.data === 'OK') {
                                            document.getElementById("name").value = ''
                                            document.getElementById("salary").value = ''
                                            setSelectedQuals([])
                                            getWorkers().then(setworkers)
                                        } else {
                                            alert("Error: " + response?.data)
                                        }
                                    })
                                }
                            }}>
                        Create Worker
                        </button>
                    </div>  
                </form>
            </div>
        </div>
    )
}

const Workers = () => {
    const [workers, setworkers] = useState([])
    useEffect(() => { getWorkers().then(setworkers) }, [])
    const active = LocationID('workers', workers, 'name')
    return (
        <div style={pageStyle}>
            <h2>Create a new worker:</h2>
            <CreateWorkerForm setworkers={setworkers}/>
            
            <h2>Click on a worker below to view their details:</h2>
            <ClickList active={active} list={workers} item={Worker} path='/workers' id='name' />
        </div>
    )
}



export default Workers