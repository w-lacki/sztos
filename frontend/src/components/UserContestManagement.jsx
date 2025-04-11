import { useAxiosAuth } from "../hooks/axiosProvider.js";
import { useEffect, useState } from "react";
import Loading from "./Loading.jsx";

const ProblemRow = ({ contestId, problem }) => {
    return (
        <tr className="problem-row">
            <td><a href={`/contests/${contestId}/problems/${problem.id}`}>{problem.title}</a></td>
            <td><a href={`/contests/${contestId}/problems/${problem.id}/submit`} className="action-link">Submit</a></td>
            <td><a href={`/contests/${contestId}/problems/${problem.id}/results`} className="action-link">Result</a></td>
            <td>{problem.deadline}</td>
            <td></td>
        </tr>
    );
};

const UserContestManagement = ({ contestId }) => {
    const axiosAuth = useAxiosAuth();
    const [contest, setContest] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchContestData();
    }, []);

    const fetchContestData = () => {
        axiosAuth.get(`/contests/${contestId}`)
            .then(response => setContest(response.data))
            .catch(error => setError(error))
            .finally(() => setLoading(false));
    };

    if (loading) return <Loading/>;
    if (error) return <div>Error: {error.message}</div>;

    return (
        <div className="stos-container">
            <header className="stos-header">
                <h1>{contest.title}</h1>
                <p className="contest-description">{contest.description}</p>
            </header>
            <section className="problems-table">
                {contest.problems.length > 0 ? (
                    <table>
                        <thead>
                        <tr>
                            <th>Problem</th>
                            <th>Submit</th>
                            <th>Result</th>
                            <th>Deadline</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {contest.problems.map(problem => (
                            <ProblemRow key={problem.id} contestId={contestId} problem={problem} />
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>No problems available for this contest.</p>
                )}
            </section>
        </div>
    );
};

export default UserContestManagement;