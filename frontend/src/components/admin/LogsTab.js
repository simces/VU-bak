import React, { useState, useEffect } from 'react';
import fetchWithToken from '../../utils/fetchUtils';
import LogDetails from './LogDetails';
import '../../styles/Admin.css'; 
import '../../styles/LogsTab.css';

const LogsTab = () => {
    const [logs, setLogs] = useState([]);
    const [error, setError] = useState('');
    const [expanded, setExpanded] = useState(null); 

    useEffect(() => {
        const fetchLogs = async () => {
            try {
                const data = await fetchWithToken('/api/audits');
                setLogs(data);
            } catch (err) {
                setError('Error fetching logs: ' + err.message);
            }
        };
        fetchLogs();
    }, []);

    const toggleDetails = (id) => {
        setExpanded(expanded === id ? null : id); 
    };

    return (
        <div className="logs-container">
            <h2>Audit Logs</h2>
            {error && <p className="error">{error}</p>}
            <table className="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Admin ID</th>
                        <th>Action Type</th>
                        <th>Table Name</th>
                        <th>Record ID</th>
                        <th>Created At</th>
                        <th>Changes made</th>
                    </tr>
                </thead>
                <tbody>
                    {logs.map(log => (
                        <React.Fragment key={log.id}>
                            <tr>
                                <td>{log.id}</td>
                                <td>{log.adminId}</td>
                                <td>{log.actionType}</td>
                                <td>{log.tableName}</td>
                                <td>{log.recordId}</td>
                                <td>{new Date(log.createdAt).toLocaleString()}</td>
                                <td>
                                    <button className="toggle-details" onClick={() => toggleDetails(log.id)}>
                                        {expanded === log.id ? '▼' : '►'}
                                    </button>
                                </td>
                            </tr>
                            {expanded === log.id && (
                                <tr className="log-details">
                                    <td colSpan="7">
                                        <LogDetails log={log} />
                                    </td>
                                </tr>
                            )}
                        </React.Fragment>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default LogsTab;