import { useEffect, useState } from 'react'
import ClickList from '../components/ClickList'
import { createQualification, getQualifications } from '../services/dataService'
import LocationID from '../utils/location'
import { grayContainerStyle, darkGrayContainerStyle, pageStyle } from '../utils/styles'

const Qualification = (qualification, active, extraProps) => {
    return (
        <div>
            <div>{qualification.description}</div>
            {active === true ? QualificationBody(qualification, extraProps) : null}
        </div>
    )
}

const QualificationBody = (qualification) => {
    
    return (
        <div>
            <div style={grayContainerStyle}>
                <b>Description:</b> {qualification.description} <br /> <br />
                <b>Workers:</b> 
                {qualification.workers.length === 0 ? <div>-</div> :
                    <ClickList list={qualification.workers} styles={darkGrayContainerStyle} path="/workers"/>}
            </div>
        </div>
    )
}

const CreateQualificationForm = (props) => {
    const { setQualifications } = props
    return (
        <div className="card">
            <div className="card-body">
                <form>
                    <div className="form-group">
                        <input type="text" className="form-control" id="qualDesc" placeholder="Enter a description" />
                    </div>
                    <button
                        type="button" className="btn btn-outline-primary"
                        onClick={() => {
                            const description = document.getElementById('qualDesc').value
                            if (description) {
                                createQualification(description).then(response => {
                                    if (response?.data === 'OK') {
                                        getQualifications().then(setQualifications)
                                        document.getElementById('qualDesc').value = ""
                                    }
                                })
                            }
                        }}
                    >
                        Create a Qualification
                    </button>
                </form>
            </div>
        </div>
    )
}

const Qualifications = () => {
    const [qualifications, setQualifications] = useState([])
    useEffect(() => { getQualifications().then(setQualifications) }, [])
    const active = LocationID('qualifications', qualifications, 'description')
    return (
        <div style={pageStyle}>
            <h2>Create a new qualification</h2>
            <CreateQualificationForm setQualifications={setQualifications} />
            <br></br>
            <h2>Click on a Qualification to view details.</h2>
            <ClickList active={active} list={qualifications} item={Qualification} path='/qualifications' id='description' />
        </div>
    )
}




export default Qualifications