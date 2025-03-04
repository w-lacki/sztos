import {useAuth} from "../hooks/useAuth.js";
import {useEffect, useState} from "react";
import {axiosInstance, useAxiosAuth} from "../hooks/axiosProvider.js";
import {useNavigate} from "react-router-dom";

const CreateContest = () => {
    const axiosAuth = useAxiosAuth();
    const [error, setError] = useState(null)
    const [submitting, setSubmitting] = useState(false)
    const [formData, setFormData] = useState(null)
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        setError(null)
        setSubmitting(true)
        event.preventDefault()
        axiosAuth.post('/contests', formData).then((response) => {
            navigate(`/contests/${response.data.id}`)
        }).catch(error => {
            // TODO: Handle errors BETTER
            setError(`Invalid credentials ${error}`)
            setFormData((prev) => ({...prev, ["password"]: ''}))
        }).finally(() => setSubmitting(false))
    }

    const handleChange = (event) => {
        const {name, value} = event.target
        setFormData((prev) => ({...prev, [name]: value}))
    }

    return (<div>
        <h1>Create contest</h1>
        <div>
            <form onSubmit={handleSubmit}>
                <label htmlFor="title">Title: </label>
                <input onChange={handleChange} id="title" name="title" placeholder="Enter contest title"/>

                <label htmlFor="description">Description: </label>
                <textarea onChange={handleChange} id="description" name="description" placeholder="Enter contest description"/>

                <button type="submit" disabled={submitting}> {submitting ? 'Creating contest...' : 'Create contest'} </button>
            </form>
        </div>
    </div>)
}
export default CreateContest;