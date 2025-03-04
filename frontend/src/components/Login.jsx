import {useNavigate} from "react-router-dom";
import {useAuth} from "../hooks/useAuth";
import {axiosInstance} from "../hooks/axiosProvider";
import {useState} from "react";

const Login = () => {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
    })
    const [error, setError] = useState(null)
    const [submitting, setSubmitting] = useState(false)
    const navigate = useNavigate();
    const {login} = useAuth();

    const handleSubmit = (event) => {
        setError(null)
        setSubmitting(true)
        event.preventDefault()
        axiosInstance.post('/auth/login', formData).then((response) => {
            console.log(response)
            login(response.data.token, response.data.refreshToken)
            navigate('/me')
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
        <h1>Login ur self nigga!</h1>
        <div>
            <form onSubmit={handleSubmit}>
                <label htmlFor="username">Username: </label>
                <input onChange={handleChange} id="username" name="username" placeholder="Enter your username"/>
                <label htmlFor="password">Password: </label>
                <input value={formData['password']} onChange={handleChange} id="password" name="password"
                       placeholder="Enter your password"/>
                {error && (<p>{error}</p>)}
                <button type="submit" disabled={submitting}> {submitting ? 'Signing In...' : 'Sign In'} </button>
            </form>
        </div>
    </div>)
}

export default Login;