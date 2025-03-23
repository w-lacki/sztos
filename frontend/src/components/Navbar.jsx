import {useAuth} from "../hooks/useAuth.js";

const Navbar = () => {
    const {auth} = useAuth();

    return (
        <div className="navbar">
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
