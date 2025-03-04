import {useAuth} from "../hooks/useAuth.js";
import {useEffect, useState} from "react";
import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useNavigate} from "react-router-dom";

const UserAdd = () => {
    const [username, setUsername] = useState('');
    const [results, setResults] = useState(null);
}
const Contests = () => {
    const axiosAuth = useAxiosAuth();
    const [contests, setContests] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const {auth} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        fetchContests()
    }, [])

    const fetchContests = () => {
        axiosAuth.get("/contests")
            .then((response) => {
                setContests(response.data);
            })
            .catch((error) => {
                setError(error)
            }).finally(() => setLoading(false))
    }


    if (loading) return <div>Loading...</div>
    if (error) return <div>Error: {error.message}</div>;

    const isTeacher = auth.roles.includes("ROLE_TEACHER")

    return (<div>
        <h1>{isTeacher ? "Manage contests" : "Your contests"}</h1>
        <ol>
            {contests.map(contest => (
                <div>
                    <li>
                        <a href={`/contests/${contest.id}`}>{contest.title}</a>
                    </li>
                </div>
            ))}
        </ol>
        <button onClick={createContest}>Create new contest</button>
    </div>)
}
export default Contests;