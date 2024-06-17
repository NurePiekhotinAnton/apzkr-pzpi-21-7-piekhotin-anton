import axios from "axios";
import {useEffect, useState} from "react";

function Houses() {
    const [houses, setHouses] = useState([]);

    async function fetchHouses() {
        let response = await axios.get('https://house-service.icystone-70888b09.uksouth.azurecontainerapps.io/api/v1/house/all', {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}});
        setHouses(response.data);
    }

    useEffect(() => {
        fetchHouses();
    }, []);

    async function changeHouse(house) {
        try {
            const response = await axios.post(`https://house-service.icystone-70888b09.uksouth.azurecontainerapps.io/api/v1/house/edit/${house.id}`, house, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                }
            });
            if (response.status === 200) {
                setHouses(houses.map(u => u.id === house.id ? house : u));
                console.log("House data saved successfully");
            }
        } catch (error) {
            console.error("Error editing house:", error);
        }
    }

    const handleInputChange = (id, field, value) => {
        setHouses(houses.map(house => house.id === id ? {...house, [field]: value} : house));
    };

    return (
        <div style={{margin: 25}}>
            <h3>Houses</h3>
            <table className="table">
                <thead className="table-primary">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">UserId</th>
                    <th scope="col">Name</th>
                    <th scope="col">Address</th>
                    <th scope="col">Action</th>
                </tr>
                </thead>
                <tbody>
                {houses.map(house => (
                    <tr key={house.id}>
                        <td className='mt-5'>{house.id}</td>
                        <td className='my-5'>{house.userId}</td>
                        <td className='m-5'>
                            <input className='input-group-text' type='text' value={house.name}
                                   onChange={(e) => handleInputChange(house.id, 'name', e.target.value)}/></td>
                        <td className='m-5'>
                            <input className='input-group-text' type='text' value={house.address}
                                   onChange={(e) => handleInputChange(house.id, 'address', e.target.value)}/></td>
                        <td>
                            <button className='btn btn-outline-primary' onClick={() => changeHouse(house)}>
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

export default Houses;
