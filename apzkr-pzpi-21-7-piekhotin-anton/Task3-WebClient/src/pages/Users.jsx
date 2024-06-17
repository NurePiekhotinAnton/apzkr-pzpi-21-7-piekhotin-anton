import axios from "axios";
import {useEffect, useState} from "react";

function Users() {
    const [users, setUsers] = useState([]);
    const [exportMessage, setExportMessage] = useState('');

    // Отримання списку користувачів з сервера та збереження їх у стані компонента
    async function fetchUsers() {
        let response = await axios.get('https://auth-service.icystone-70888b09.uksouth.azurecontainerapps.io/api/v1/auth/users', {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}});
        response.data.forEach(user => {
            user.isEnabled = user.isEnabled ? 'true' : 'false';
            user.created = user.created.substring(0, 10);
        });
        setUsers(response.data);
    }

    // Експорт даних користувачів
    async function exportUsers() {
        try {
            await axios.get('https://auth-service.icystone-70888b09.uksouth.azurecontainerapps.io/api/v1/auth/export-users', {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}});
            setExportMessage("Successfully exported users data");
        } catch (error) {
            setExportMessage("Error while exporting users data");
        }
    }

    // Зміна даних користувача
    async function changeUser(user) {
        try {
            const response = await axios.post(`https://auth-service.icystone-70888b09.uksouth.azurecontainerapps.io/api/v1/auth/edit-user/${user.id}`, user, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                }
            });
            if (response.status === 200) {
                setUsers(users.map(u => u.id === user.id ? user : u));
                console.log("User data saved successfully");
            }
        } catch (error) {
            console.error("Error editing user:", error);
        }
    }

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleInputChange = (id, field, value) => {
        setUsers(users.map(user => user.id === id ? {...user, [field]: value} : user));
    };

    return (
        <div style={{margin: 25}}>
            <h3>Users</h3>
            <table className="table">
                <thead className="table-primary">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Email</th>
                    <th scope="col">Name</th>
                    <th scope="col">isEnabled</th>
                    <th scope="col">Role</th>
                    <th scope="col">Registred at</th>
                    <th scope="col">Actions</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td>{user.id}</td>
                        <td><input className='input-group-text' type="email" value={user.email}
                                   onChange={(e) => handleInputChange(user.id, 'email', e.target.value)}/></td>
                        <td><input className='input-group-text' type="text" value={user.name}
                                   onChange={(e) => handleInputChange(user.id, 'name', e.target.value)}/></td>
                        <td>
                            <select className='form-select' value={user.isEnabled}
                                    onChange={(e) => handleInputChange(user.id, 'isEnabled', e.target.value)}>
                                <option value="true">true</option>
                                <option value="false">false</option>
                            </select>
                        </td>
                        <td>
                            <select className='form-select' value={user.role}
                                    onChange={(e) => handleInputChange(user.id, 'role', e.target.value)}>
                                <option value="CLIENT">CLIENT</option>
                                <option value="ADMIN">ADMIN</option>
                            </select>
                        </td>
                        <td className='my-3'>{user.created}</td>
                        <td>
                            <button className='btn btn-outline-primary' onClick={() => changeUser(user)}>
                                Save data
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <h5 className="text-info">{exportMessage}</h5>
            <p><a href="#" onClick={exportUsers}>Export user data</a></p>
        </div>
    );
}

export default Users;
