import {BrowserRouter, Route, Routes} from "react-router-dom";
import Home from "./components/Home.jsx";
import Login from "./components/Login.jsx";
import Navbar from "./components/Navbar.jsx";
import Me from "./components/Me.jsx";
import {AuthProvider} from "./context/AuthContext.jsx";
import PrivateRoute from "./components/PrivateRoute.jsx";
import Logout from "./components/Logout.jsx";
import Contests from "./components/Contests.jsx";
import CreateContest from "./components/CreateContest.jsx";
import Contest from "./components/Contest.jsx";
import SubmissionManagement from "./components/Submission.jsx";
import Result from "./components/Result.jsx";

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Navbar/>
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route element={<PrivateRoute/>}>
                        <Route path="/me" element={<Me/>}/>
                        <Route path="/logout" element={<Logout/>}/>
                        <Route path="/contests/create" element={<CreateContest/>}/>
                        <Route path="/contests/:contestId/problems/:problemId/submit"
                               element={<SubmissionManagement/>}/>
                        <Route path="/contests/:contestId/problems/:problemId/results" element={<Result/>}/>
                        <Route path="/contests/:contestId" element={<Contest/>}/>
                        <Route path="/contests" element={<Contests/>}/>
                    </Route>
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
