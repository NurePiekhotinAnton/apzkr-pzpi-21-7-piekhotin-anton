import Home from "../pages/Home"
import SignIn from "../pages/SignIn"
import Users from "../pages/Users"
import Houses from "../pages/Houses"
import Devices from "../pages/Devices"

export const publicRoutes = [
    {path: '/', component: Home, exact: false},
    {path: '/authorization', component: SignIn, exact: false},
]

export const adminRoutes = [
    {path: '/users', component: Users, exact: true},
    {path: '/houses', component: Houses, exact: true},
    {path: '/devices', component: Devices, exact: true},
]