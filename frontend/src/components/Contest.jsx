import {useAuth} from "../hooks/useAuth.js";
import {useEffect, useState} from "react";
import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useNavigate, useParams} from "react-router-dom";

const Contest = () => {
    const axiosAuth = useAxiosAuth();
    const {contestId} = useParams();
    const [contestData, setContestData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const {auth} = useAuth();

    useEffect(() => {
        fetchContestData()
    }, [])

    const fetchContestData = () => {
        axiosAuth.get(`/contests/${contestId}`)
            .then(response => setContestData(response.data))
            .catch(error => setError(error))
            .finally(() => setLoading(false))
    }

    if (loading) return <div>Loading...</div>
    if (error) return <div>Error: {error.message}</div>;

    const isTeacher = auth.roles.includes("ROLE_TEACHER")

    return (<div>
        <h1>{isTeacher ? "Edit contest" : `Contest ${contestData.title}`}</h1>
    </div>)
}
export default Contest;