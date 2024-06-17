import React, {useContext, useState} from 'react';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';
import {AuthContext} from '../сontext';

function SignIn() {
    const {isAuth, setIsAuth} = useContext(AuthContext)
    const navigate = useNavigate();
    const [user, setUser] = useState({email: '', password: ''});
    const {userRole, setUserRole} = useContext(AuthContext)
    const [errorMessage, setErrorMessage] = useState("")

    async function signIn(e) {
        e.preventDefault()

        // Перевірка на валідність даних
        if (user.email != '' && user.password != '') {
            try {
                // Запит на сервер для авторизації
                // let response = await axios.post('http://localhost:8080/api/v1/auth/sign-in', user)
                // let response = await axios.post('https://api-gateway.icystone-70888b09.uksouth.azurecontainerapps.io/api/v1/auth/sign-in', user)
                let response = await axios.post('https://auth-service.icystone-70888b09.uksouth.azurecontainerapps.io/api/v1/auth/sign-in', user)

                if (response.data == null || response.data == '') {
                    console.log("empty response")
                    setErrorMessage("Wrong email or password")
                    return
                }
                // Перевірка ролі користувача
                if (response.data.role === 'CLIENT') {
                    setErrorMessage("You are not an admin")
                    console.log("client")
                    return
                }
                // Збереження даних в localStorage
                localStorage.setItem('auth', 'true')
                localStorage.setItem('token', response.data.token)
                localStorage.setItem('role', response.data.role)
                localStorage.setItem('id', response.data.guestId)
                setUserRole('ROLE_ADMIN')
                setIsAuth(true)

                console.log('auth: ' + localStorage.getItem('auth'))
                console.log("successful sign in")
                navigate('/');
            } catch (err) {
                console.log(err)
                setErrorMessage("Invalid data")
            }
        } else {
            console.log("invalid data")
            setErrorMessage("Invalid data")
        }
    }

    return (
        <>
            <form className='w-25 mt-5'>
                <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label">Email:</label>
                    <input type="email" className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp"
                           value={user.email} onChange={e => setUser({...user, email: e.target.value})}></input>
                </div>
                <div class="mb-3">
                    <label for="exampleInputPassword1" class="form-label">Password:</label>
                    <input type="password" className="form-control" id="exampleInputPassword1" value={user.password}
                           onChange={e => setUser({...user, password: e.target.value})}></input>
                </div>
                <button type="submit" className="btn btn-primary " onClick={signIn}>LogIn</button>
                <div className='text-danger'>{errorMessage}</div>
            </form>
        </>
    )
}

export default SignIn;