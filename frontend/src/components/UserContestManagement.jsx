import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useEffect, useState} from "react";

const ProblemRow = ({contestId, problem}) => {
    return (<tr>
        <td><a href={`/contests/${contestId}/problems/${problem.id}`}>{problem.title}</a></td>
        <td><a href={`/contests/${contestId}/problems/${problem.id}/submit`}>Submit</a></td>
        <td> {problem.deadline}</td>
        <td></td>
    </tr>)
}

const UserContestManagement = ({contestId}) => {
    const axiosAuth = useAxiosAuth();
    const [contest, setContest] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchContestData()
    }, [])

    const fetchContestData = () => {
        axiosAuth.get(`/contests/${contestId}`)
            .then(response => setContest(response.data))
            .catch(error => setError(error))
            .finally(() => setLoading(false))
    }


    console.log(contest)
    if (loading) return <div>Loading...</div>
    if (error) return <div>Error: {error.message}</div>;

    return <div>
        <h1>{contest.title}</h1>
        <table>
            <thead>
            <tr>
                <th>Problem</th>
                <th>Submit</th>
                <th>Deadline</th>
            </tr>
            </thead>
            <tbody>
            {contest.problems.map(problem => (<ProblemRow key={problem.id} contestId={contestId} problem={problem}/>))}
            </tbody>
        </table>
    </div>

}

export default UserContestManagement