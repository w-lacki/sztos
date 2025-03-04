import {NavLink} from "react-router-dom";
import {useAuth} from "../hooks/useAuth.js";

const Navbar = () => {
    const {auth} = useAuth();

    return (
        <div>
            <a href="/">Home</a>
            {auth.isAuthenticated ? (
                <>
                    <a href="/contests">Contests</a>
                    <a href="/me">Dashboard</a>
                    <a href="/logout">Logout</a>
                </>
            ) : (
                <a href="/login">Login</a>
            )}
        </div>
    )
}
export default Navbar;