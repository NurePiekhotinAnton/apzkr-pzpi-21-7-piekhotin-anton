import {useContext} from "react";
import {AuthContext} from "../сontext";


function Navbar() {
    const {isAuth, setIsAuth} = useContext(AuthContext)

    let navLinkActive = "nav-link active";
    if (!isAuth)
        navLinkActive = "nav-link disabled"

    function logOut() {
        setIsAuth(false)
        localStorage.removeItem('auth')
        localStorage.removeItem('token')
        localStorage.removeItem('role')
    }

    return (
        <nav className="navbar navbar-expand-lg bg-info-subtle">
            <div className="container-fluid">
                <a className="navbar-brand" href="/">SafeHouse</a>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">

                        <li className="nav-item">
                            <a className={navLinkActive} aria-disabled="true" href="/users">Users</a>
                        </li>
                        <li className="nav-item">
                            <a className={navLinkActive} href="/houses">Houses</a>
                        </li>
                        <li className="nav-item">
                            <a className={navLinkActive} aria-disabled="true" href="/devices">Devices</a>
                        </li>
                    </ul>
                    {isAuth === false
                        ? <button className="btn btn-outline-success" type="submit">
                            <a href='/authorization'
                               className="nav-link active"
                               aria-current="page">Login</a>
                        </button>
                        :
                        <button className="btn btn-outline-danger" type="submit">
                            <a href='/' className="nav-link active"
                               aria-current="page"
                               onClick={logOut}>Logout</a></button>
                    }
                </div>
            </div>
        </nav>
    )
}

export default Navbar