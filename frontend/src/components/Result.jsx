import React, {useEffect, useState} from 'react';
import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useParams} from "react-router-dom"; // CSS file defined below
import "../styles/Results.css";


const Result = () => {
    // JSON data
    const {problemId} = useParams();
    const axiosAuth = useAxiosAuth()
    const [results, setResults] = useState(null)
    const [error, setError] = useState(null)
    const [loading, setLoading] = useState(true)
    const [running, setRunning] = useState(true)

    const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

    // Fetch results with retry logic
    const fetchResults = () => {
        axiosAuth.get(`/tests/results/${problemId}`)
            .then(res => {
                console.log(res)
                if (res.status === 200) {
                    console.log("ESSA!")
                    setResults(res.data)
                    setRunning(false)
                } else sleep(1).then(() => {
                    fetchResults()
                })
            }).catch(err => {
                console.log(err)
            setError(`Error: ${err.message}`);
            setRunning(false);
        }).finally(() => setLoading(false))
    }


    // Run fetchResults on mount or when problemId changes
    useEffect(() => {
        fetchResults();
    }, []); // Dependency on problemId

    if (loading) {
        return <div>Loading...</div>
    }

    if (error) {
        return <div>Error: ${error}</div>
    }

    if (running) {
        return <div>Obliczanie...</div>
    }

    console.log(results)

    // Pair tests with results
    const pairedData = results.tests.map(test => {
        const result = results.results.find(r => r.testId === test.id) || {};
        return {test, result};
    });

    // Calculate total points
    const totalPossiblePoints = results.tests.reduce((sum, test) => sum + test.points, 0);
    const totalEarnedPoints = results.results.reduce((sum, result) => sum + result.points, 0);

    return (
        <div className="stos-container">
            <header className="stos-header">
                <h1>Wyniki Testów - STOS</h1>
                <p className="summary">
                    Punkty: {totalEarnedPoints} / {totalPossiblePoints}
                </p>
            </header>

            <section className="test-results">
                {pairedData.length > 0 ? (
                    pairedData.map(({test, result}) => (
                        <div key={test.id} className="test-case">
                            <h2>Test #{test.id}</h2>
                            <div className="test-details">
                                <div className="test-info">
                                    <p><strong>Wejście:</strong> {test.input}</p>
                                    <p><strong>Oczekiwane wyjście:</strong></p>
                                    <pre>{test.output}</pre>
                                </div>
                                <div className="result-info">
                                    <p><strong>Wyjście studenta:</strong></p>
                                    <pre>{result.output || 'Brak wyniku'}</pre>
                                    <p>
                                        <strong>Punkty:</strong> {result.points !== undefined ? result.points : 'N/A'} / {test.points}
                                    </p>
                                    <p><strong>Status:</strong> {result.points === test.points ?
                                        <span className="passing">Zaliczony</span> :
                                        <span className="failed">Zjebany</span>}</p>
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>Brak wyników testów do wyświetlenia.</p>
                )}
            </section>
        </div>
    );
};

export default Result;