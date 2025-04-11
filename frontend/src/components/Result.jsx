import React, {useEffect, useState} from 'react';
import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useParams} from "react-router-dom";
import Loading from "./Loading.jsx";

const Result = () => {
    const {problemId} = useParams();
    const axiosAuth = useAxiosAuth();
    const [results, setResults] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [running, setRunning] = useState(true);

    const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

    const fetchResults = () => {
        axiosAuth.get(`/tests/results/${problemId}`)
            .then(res => {
                if (res.status === 200) {
                    setResults(res.data);
                    setRunning(false);
                } else sleep(1).then(() => {
                    fetchResults();
                });
            }).catch(err => {
            setError(`Error: ${err.message}`);
            setRunning(false);
        }).finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchResults();
    }, []);

    if (loading || running) return <Loading/>;

    if (error) {
        return <div>Error: ${error}</div>;
    }

    const pairedData = results.tests.map(test => {
        const result = results.results.find(r => r.testId === test.id) || {};
        return {test, result};
    });

    const totalPossiblePoints = results.tests.reduce((sum, test) => sum + test.points, 0);
    const totalEarnedPoints = results.results.reduce((sum, result) => sum + result.points, 0);

    return (
        <div className="stos-container">
            <header className="stos-header">
                <h1>Test results</h1>
                <p className="summary">
                    {pairedData.length > 0 ? `Total points: ${totalEarnedPoints} / ${totalPossiblePoints}` : ''}
                </p>
            </header>

            <section className="test-results">
                {pairedData.length > 0 ? (
                    pairedData.map(({test, result}) => (
                        <div key={test.id} className="test-case">
                            <h2>Test #{test.id}</h2>
                            <div className="test-details">
                                <div className="test-input">
                                    <p><strong>Input:</strong></p>
                                    <pre>{test.input}</pre>
                                </div>
                                <div className="test-outputs">
                                    <div className="expected-output">
                                        <p><strong>Expected output:</strong></p>
                                        <pre>{test.output}</pre>
                                    </div>
                                    <div className="your-output">
                                        <p><strong>Your output:</strong></p>
                                        <pre>{result.output || 'Invalid code'}</pre>
                                    </div>
                                </div>
                                <div className="result-info">
                                    <p>
                                        <strong>Points:</strong> {result.points !== undefined ? `${result.points}/${test.points}` : 0}
                                    </p>
                                    <p><strong>Status:</strong> {result.points === test.points ?
                                        <span className="passing">Passed</span> :
                                        <span className="failed">Failed :/</span>}
                                    </p>
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>No code submission.</p>
                )}
            </section>
        </div>
    );
};

export default Result;