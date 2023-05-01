import { goldenContainerStyle } from '../../utils/styles'
import { useNavigate } from 'react-router-dom'

const Box = (value) => {
    return <div>{value}</div>
}

<<<<<<< HEAD
const ClickList = ({ active, list, setprojects, workers, item, path, id, styles, dropdownOpen, setDropdownOpen }) => {
=======
const ClickList = ({ active, list, item, path, id, styles, extraProps }) => {
>>>>>>> main
    const navigate = useNavigate()
    return list.map((value, index) => {
        let style = styles ? (Array.isArray(styles) ? styles[index] : styles) : goldenContainerStyle
        style = JSON.parse(JSON.stringify(style));
        style.cursor = 'pointer'
        return (
            <div key={index} style={style} onClick={(e) => {
                e.stopPropagation()
                if (path) {
                    let key = id ? value[id] : value
                    if (active !== index) navigate(path + '/' + key)
                    else navigate(path)
                }
            }}>
<<<<<<< HEAD
                {item ? item(value, setprojects, workers, active === index, dropdownOpen, setDropdownOpen) : Box(value)}
=======
                {item ? item(value, active === index, extraProps) : Box(value)}
>>>>>>> main
            </div>
        )
    })
}

export default ClickList