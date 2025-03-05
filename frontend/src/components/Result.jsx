import {useEffect, useState} from "react";
import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useParams} from "react-router-dom";

const Result = () => {
    const {problemId} = useParams();
    const axiosAuth = useAxiosAuth();
    const [error, setError] = useState(null)
    const [loading, setLoading] = useState(true)
    const [results, setResults] = useState(null)

    useEffect(() => {
        fetchResults()
    }, []);

    const fetchResults = () => {
        axiosAuth.get(`/tests/results/${problemId}`)
            .then((response) => {
                setResults(`${response.data}`)
                console.log(response.data.results)

            }).catch(error => {
            setError(`Error ${error}`)
        }).finally(() => setLoading(false))
    }


    if (error) return <div> Error: ${error}</div>
    if (loading) return <div>Loading...</div>

    return (<div>
        <h1>Results</h1>
        <div>
            <p>{results}</p>
        </div>
    </div>)
}
export default Result;