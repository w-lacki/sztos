import {useAuth} from "../hooks/useAuth.js";

const Navbar = () => {
    const {auth} = useAuth();

    return (
        <div className="navbar">
            <a href="/" className="navbar-brand">Home</a>
            <div className="navbar-links">
                {auth.isAuthenticated ? (
                    <>
                        <a href="/contests" className="navbar-link">Contests</a>
                        <a href="/dashboard" className="navbar-link">Dashboard</a>
                        <a href="/logout" className="navbar-link">Logout</a>
                    </>
                ) : (
                    <a href="/login" className="navbar-link">Login</a>
                )}
            </div>
        </div>

    )
}
export default Navbar;
