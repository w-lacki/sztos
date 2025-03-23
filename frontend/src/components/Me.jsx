import {useEffect, useState} from "react";
import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useAuth} from "../hooks/useAuth.js";

const Me = () => {
    const {auth} = useAuth();
    const axiosAuth = useAxiosAuth()
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        axiosAuth.get("/user/me")
            .then((response) => {
                setUserData(response.data);
            })
            .catch((error) => setError(error))
            .finally(() => setLoading(false))
    }

    if (loading) return <div>Loading...</div>

    if (error) return <div>Error: {error.message}</div>;


    return (<div>
        <p>Welcome back {userData.username}</p>
        <p>Your email: {userData.email}</p>
        <p>Your roles: {auth.roles.join(", ")}</p>
    </div>)
}
export default Me