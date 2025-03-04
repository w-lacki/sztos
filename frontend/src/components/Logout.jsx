import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useAuth} from "../hooks/useAuth.js";
import {useNavigate} from "react-router-dom";
import {useState} from "react";

const Logout = () => {
    const {auth, logout} = useAuth();
    const axiosAuth = useAxiosAuth();
    const navigate = useNavigate();
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    axiosAuth.post(`/auth/logout?refreshToken=${auth.refreshToken}`)
        .then(() => {
            navigate('/')
            logout()
        })
        .catch(() => {
            setError('Error occurred while logging in.');
        })
        .finally(() => setLoading(false));

    return <div>
        {loading && <p>Logging out...</p>}
        {error && <p>error</p>}
    </div>
}
export default Logout;