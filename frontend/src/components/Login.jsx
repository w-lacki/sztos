import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { axiosInstance } from "../hooks/axiosProvider";
import { useState } from "react";

const Login = () => {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
    });
    const [error, setError] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleSubmit = (event) => {
        setError(null);
        setSubmitting(true);
        event.preventDefault();
        axiosInstance
            .post('/auth/login', formData)
            .then((response) => {
                login(response.data.token, response.data.refreshToken);
                navigate('/me');
            })
            .catch((error) => {
                setError("Invalid username or password!");
                setFormData((prev) => ({ ...prev, password: "" }));
            })
            .finally(() => setSubmitting(false));
    };

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    return (
        <div className="centered">
            <form
                className="form"
                onSubmit={handleSubmit}
                style={{ marginTop: "-10rem" }} // Reduced top margin
            >
                <h1>Sign In</h1>
                <div className="form-group">
                    <label className="form-label" htmlFor="username">
                        Username
                    </label>
                    <input
                        className="form-input"
                        onChange={handleChange}
                        id="username"
                        name="username"
                        value={formData.username}
                        placeholder="Enter your username"
                        autoComplete="username"
                    />
                </div>
                <div className="form-group">
                    <label className="form-label" htmlFor="password">
                        Password
                    </label>
                    <input
                        className="form-input"
                        value={formData.password}
                        onChange={handleChange}
                        id="password"
                        name="password"
                        type="password"
                        placeholder="Enter your password"
                        autoComplete="current-password"
                    />
                </div>
                {error && <p className="form-error">{error}</p>}
                <button
                    className="form-button"
                    type="submit"
                    disabled={submitting}
                >
                    {submitting ? "Signing In..." : "Sign In"}
                </button>
            </form>
        </div>
    );
};

export default Login;