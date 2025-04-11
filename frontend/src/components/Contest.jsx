import {useAuth} from "../hooks/useAuth.js";
import {useEffect, useState} from "react";
import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useParams} from "react-router-dom";
import Select from 'react-select';
import UserContestManagement from "./UserContestManagement.jsx";

const Contest = () => {
    const {contestId} = useParams();
    const {auth} = useAuth();

    const isTeacher = auth.roles.includes("ROLE_TEACHER")
    if (!isTeacher) {
        return <UserContestManagement contestId={contestId}/>
    }

    return null
}
export default Contest;