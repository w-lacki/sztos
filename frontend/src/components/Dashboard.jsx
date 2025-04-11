import { useEffect, useState } from "react";
import { useAxiosAuth } from "../hooks/axiosProvider.js";
import { useAuth } from "../hooks/useAuth.js";
import Loading from "./Loading.jsx";

const Dashboard = () => {
    const { auth } = useAuth();
    const axiosAuth = useAxiosAuth();
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        axiosAuth.get("/user/me")
            .then((response) => {
                setUserData(response.data);
            })
            .catch((error) => setError(error))
            .finally(() => setLoading(false));
    };

    if (loading) return <Loading/>;
    if (error) return <div>Error: {error.message}</div>;

    return (
        <div className="stos-container">
            <header className="stos-header">
                <h1>Welcome back, {userData.username}!</h1>
            </header>
            <section className="user-info">
                <div className="user-card">
                    <p><strong>Your email:</strong> {userData.email}</p>
                    <p><strong>Your roles:</strong> {auth.roles.join(", ")}</p>
                </div>
            </section>
        </div>
    );
};

export default Dashboard;