import {useState} from "react";
import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useParams} from "react-router-dom";

const SubmissionManagement = () => {
    const {problemId} = useParams();
    const axiosAuth = useAxiosAuth();
    const [error, setError] = useState(null)
    const [submitting, setSubmitting] = useState(false)
    const [formData, setFormData] = useState({
        problemId: `${problemId}`,
    })

    const handleSubmit = (event) => {
        setError(null)
        setSubmitting(true)
        event.preventDefault()
        axiosAuth.post('/submissions', formData)
            .then((response) => {
                console.log(response)
            }).catch(error => {
            setError(`Error ${error}`)
        }).finally(() => setSubmitting(false))
    }

    const handleChange = (event) => {
        const {name, value} = event.target
        setFormData((prev) => ({...prev, [name]: value}))
    }

    if (error) {
        return <div> Error: ${error}</div>
    }

    return (<div>
        <h1>Create contest</h1>
        <div>
            <form onSubmit={handleSubmit}>
                <label htmlFor="code">Code: </label>
                <textarea onChange={handleChange} id="code" name="code"/>
                <button type="submit" disabled={submitting}> {submitting ? 'Submitting...' : 'Submit'} </button>
            </form>
        </div>
    </div>)
}
export default SubmissionManagement;