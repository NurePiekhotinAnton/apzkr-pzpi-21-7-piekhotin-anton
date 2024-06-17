import React, {useEffect, useState} from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {AuthContext} from './сontext';
import Navbar from './components/Navbar'
import './styles/App.css'
import {adminRoutes, publicRoutes} from './router';

function App() {
    // Створення змінних для авторизації
    const [userId, setUserId] = useState(null)
    const [isAuth, setIsAuth] = useState(false)
    const [userRole, setUserRole] = useState("ROLE_USER")

    // Перевірка чи користувач авторизований
    useEffect(() => {
        if (localStorage.getItem('auth')) {
            setIsAuth(true);
        }
        if (localStorage.getItem('role') === "ADMIN") {
            setUserRole("ROLE_ADMIN")
        }
    }, [])

    return (
        <AuthContext.Provider value={{isAuth, setIsAuth, userId, setUserId, userRole, setUserRole}}>
            <Router>
                <Navbar></Navbar>
                {isAuth
                    ?
                    <Routes>
                        {publicRoutes.map(route =>
                            <Route
                                Component={route.component}
                                path={route.path}
                                exact={route.exact}
                            ></Route>
                        )}
                        {userRole === "ROLE_ADMIN"
                            ? adminRoutes.map(route =>
                                <Route
                                    Component={route.component}
                                    path={route.path}
                                    exact={route.exact}
                                ></Route>
                            )
                            : null
                        }
                    </Routes>
                    :
                    <Routes>
                        {publicRoutes.map(route =>
                            <Route
                                Component={route.component}
                                path={route.path}
                                exact={route.exact}
                            ></Route>
                        )}
                    </Routes>}

            </Router>
        </AuthContext.Provider>
    );
}

export default App;
