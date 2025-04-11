import {useAuth} from "../hooks/useAuth.js";
import {useEffect, useState} from "react";
import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useNavigate} from "react-router-dom";
import Loading from "./Loading.jsx";

const Contests = () => {
    const axiosAuth = useAxiosAuth();
    const [contests, setContests] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const {auth} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        fetchContests();
    }, []);

    const fetchContests = () => {
        axiosAuth.get("/contests")
            .then((response) => {
                setContests(response.data);
            })
            .catch((error) => {
                setError(error);
            })
            .finally(() => setLoading(false));
    };

    const createContest = () => {
        navigate("/contests/create");
    };

    if (loading) return <Loading/>;
    if (error) return <div>Error: {error.message}</div>;

    const isTeacher = auth.roles.includes("ROLE_TEACHER");

    return (
        <div className="stos-container">
            <header className="stos-header">
                <h1>{isTeacher ? "Manage contests" : "Your contests"}</h1>
            </header>
            <section className="contests-list">
                {contests.length > 0 ? (
                    <ol>
                        {contests.map(contest => (
                            <li key={"contest" + contest.id} className="contest-item">
                                <a href={`/contests/${contest.id}`}>{contest.title}</a>
                            </li>
                        ))}
                    </ol>
                ) : (
                    <p>No contests available.</p>
                )}
            </section>
            {isTeacher && (
                <div className="create-contest">
                    <button className="form-button" onClick={createContest}>
                        Create new contest
                    </button>
                </div>
            )}
        </div>
    );
};

export default Contests;