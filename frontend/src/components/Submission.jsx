import { useState } from "react";
import { useAxiosAuth } from "../hooks/axiosProvider.js";
import { useNavigate, useParams } from "react-router-dom";

const SubmissionManagement = () => {
    const { contestId, problemId } = useParams();
    const axiosAuth = useAxiosAuth();
    const [error, setError ] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const [formData, setFormData] = useState({
        problem: `${problemId}`,
    });
    const navigation = useNavigate();

    const handleSubmit = (event) => {
        setError(null);
        setSubmitting(true);
        event.preventDefault();
        axiosAuth.post('/submissions', formData)
            .then(() => {
                navigation(`/contests/${contestId}/problems/${problemId}/results`);
            })
            .catch(error => {
                setError(`Error ${error}`);
            })
            .finally(() => setSubmitting(false));
    };

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div className="stos-container">
            <header className="stos-header">
                <h1>Submit your code</h1>
            </header>
            <section className="submission-form">
                <form className="form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="code" className="form-label">Code:</label>
                        <textarea
                            rows={20}
                            cols={85}
                            onChange={handleChange}
                            id="code"
                            name="code"
                            className="form-textarea"
                        />
                    </div>
                    <button type="submit" className="form-button" disabled={submitting}>
                        {submitting ? 'Submitting...' : 'Submit'}
                    </button>
                </form>
            </section>
        </div>
    );
};

export default SubmissionManagement;