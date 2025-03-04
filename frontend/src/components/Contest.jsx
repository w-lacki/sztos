import {useAuth} from "../hooks/useAuth.js";
import {useEffect, useState} from "react";
import {useAxiosAuth} from "../hooks/axiosProvider.js";
import {useParams} from "react-router-dom";
import Select from 'react-select';
import UserContestManagement from "./UserContestManagement.jsx";

/*const UserAdd = () => {
    const axios = useAxiosAuth();
    const [options, setOptions] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    // Function to fetch users from the endpoint
    const fetchUsers = async (name) => {
        if (!name.trim()) {
            setOptions([])
            return;
        }

        setIsLoading(true);
        try {
            const response = await axios.get(`/user/${name}`)

            const formattedOptions = response.data.map(user => ({
                value: user.id,
                label: `${user.username} (${user.email})`,
            }));

            setOptions(formattedOptions);
        } catch (error) {
            console.error('Error fetching users:', error);
            setOptions([]);
        } finally {
            setIsLoading(false);
        }
    };

    // Debounce the search
    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            fetchUsers(searchTerm);
        }, 300);

        return () => clearTimeout(delayDebounceFn);
    }, [searchTerm]);

    // Handle selection change
    const handleChange = (selectedOption) => {
        console.log("selectedOption", selectedOption);
        const selected = options.find(option => option.value === selectedOption?.value);
        setSelectedUser(selected ? {
            id: selected.value,
            username: selected.label.split(' (')[0],
            email: selected.label.match(/\(([^)]+)\)/)?.[1] || '',
        } : null);
    };

    return (
        <div>
            <Select
                options={options}
                onChange={handleChange}
                onInputChange={(inputValue) => setSearchTerm(inputValue)}
                placeholder="Search users by username..."
                isLoading={isLoading}
                isMulti={true}
                isClearable
            />

            {selectedUser && (
                <div style={{marginTop: '1rem'}}>
                    <h3>Selected User:</h3>
                    <p>ID: {selectedUser.id}</p>
                    <p>Username: {selectedUser.username}</p>
                    <p>Email: {selectedUser.email}</p>
                </div>
            )}
        </div>
    );
}; */

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