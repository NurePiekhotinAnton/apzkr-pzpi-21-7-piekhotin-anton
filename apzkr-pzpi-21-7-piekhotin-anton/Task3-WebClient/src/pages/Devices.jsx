import axios from "axios";
import {useEffect, useState} from "react";

function Devices() {
    const [devices, setDevices] = useState([]);

    async function fetchDevices() {
        let response = await axios.get('http://localhost:8080/api/v1/device/all', {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}});
        setDevices(response.data);
    }

    useEffect(() => {
        fetchDevices();
    }, []);

    async function changeDevice(device) {
        try {
            const response = await axios.post(`http://localhost:8080/api/v1/device/edit/${device.id}`, device, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                }
            });
            if (response.status === 200) {
                setDevices(devices.map(d => d.id === device.id ? device : d));
                console.log("Device data saved successfully");
                console.log(response.data)
            }
        } catch (error) {
            console.error("Error editing device:", error);
        }
    }

    const handleInputChange = (id, field, value) => {
        setDevices(devices.map(device => device.id === id ? {...device, [field]: value} : device));
    };

    return (
        <div style={{margin: 25}}>
            <h3>Devices</h3>
            <table className="table">
                <thead className="table-primary">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">houseId</th>
                    <th scope="col">Type</th>
                    <th scope="col">Action</th>
                </tr>
                </thead>
                <tbody>
                {devices.map(device => (
                    <tr key={device.id}>
                        <td className='m-5'>{device.id}</td>
                        <td className='m-5'>{device.houseId}</td>
                        <td className='m-5'>
                            <select className='form-select' value={device.type}
                                    onChange={(e) => handleInputChange(device.id, 'type', e.target.value)}>>
                                <option value="LIGHT">LIGHT</option>
                                <option value="FAN">FAN</option>
                                <option value="AC">AC</option>
                                <option value="TV">TV</option>
                                <option value="DOOR">DOOR</option>
                                <option value="WINDOW">WINDOW</option>
                                <option value="CAMERA">CAMERA</option>
                                <option value="ALARM">ALARM</option>
                                <option value="SENSOR">SENSOR</option>
                                <option value="OTHER">OTHER</option>
                            </select>
                        </td>
                        <td>
                            <button className='btn btn-outline-primary' onClick={() => changeDevice(device)}>
                                Save data
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default Devices;
