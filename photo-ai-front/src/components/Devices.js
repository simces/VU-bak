import React, { useState, useEffect } from 'react';
import fetchWithToken from '../utils/fetchUtils';

const Devices = ({ userId }) => {
    const [devices, setDevices] = useState([]);
    const [catalog, setCatalog] = useState({ Cameras: [], Phones: [] });
    const [newDevice, setNewDevice] = useState({ deviceCatalogId: null, type: '', model: '' });
    const [showForm, setShowForm] = useState(false);
    const [isCustom, setIsCustom] = useState(false);
    const [selectedType, setSelectedType] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            const deviceData = await fetchWithToken(`/api/users/${userId}/devices`);
            const allCatalogData = await fetchWithToken(`/api/devices/catalog`);
            const cameras = allCatalogData.filter(item => item.type === "Camera");
            const phones = allCatalogData.filter(item => item.type === "Phone");
            setDevices(deviceData || []);
            setCatalog({ Cameras: cameras, Phones: phones });
        };
        fetchData();
    }, [userId]);

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setNewDevice(prev => ({ ...prev, [name]: value }));
    };

    const handleCatalogSelection = (event) => {
        const selectedModel = event.target.value;
        const selectedItem = catalog[selectedType + 's'].find(item => item.model === selectedModel);
        if (selectedItem) {
            setNewDevice({
                deviceCatalogId: selectedItem.id,
                type: selectedItem.type,
                model: selectedItem.model
            });
        }
    };

    const handleTypeChange = (event) => {
        const type = event.target.value;
        setSelectedType(type);
        setNewDevice(prev => ({ ...prev, type: '', model: '', deviceCatalogId: null }));
    };

    const submitDevice = async (event) => {
        event.preventDefault();
        const method = newDevice.id ? 'PUT' : 'POST';
        const endpoint = `/api/users/${userId}/devices${newDevice.id ? `/${newDevice.id}` : ''}`;
        try {
            await fetchWithToken(endpoint, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newDevice),
            });
            setDevices(await fetchWithToken(`/api/users/${userId}/devices`));
            setShowForm(false);
            setIsCustom(false);
            setNewDevice({ deviceCatalogId: null, type: '', model: '' });
            setSelectedType('');
        } catch (error) {
            console.error('Failed to submit device:', error);
        }
    };

    const deleteDevice = async (deviceId) => {
        try {
            await fetchWithToken(`/api/users/${userId}/devices/${deviceId}`, {
                method: 'DELETE',
            });
            setDevices(devices.filter(device => device.id !== deviceId));
        } catch (error) {
            console.error('Failed to delete device:', error);
        }
    };

    return (
        <div>
            <h2>Manage Devices</h2>
            {devices.length > 0 ? (
                devices.map(device => (
                    <div key={device.id}>
                        {device.type} - {device.model}
                        <button onClick={() => {
                            setNewDevice({ ...device });
                            setShowForm(true);
                            setIsCustom(!device.deviceCatalogId);
                        }}>Edit</button>
                        <button onClick={() => deleteDevice(device.id)}>Delete</button>
                    </div>
                ))
            ) : (
                <p>You haven't added a device yet!</p>
            )}

            {!showForm && <button onClick={() => { setShowForm(true); setIsCustom(false); }}>Add Device</button>}

            {showForm && (
                <>
                    <button onClick={() => setIsCustom(false)}>Popular</button>
                    <button onClick={() => setIsCustom(true)}>Your Own</button>
                    <form onSubmit={submitDevice}>
                        {isCustom ? (
                            <>
                                <select name="type" value={newDevice.type} onChange={handleInputChange} required>
                                    <option value="">Select Type</option>
                                    <option value="Camera">Camera</option>
                                    <option value="Phone">Phone</option>
                                    <option value="Other">Other</option>
                                </select>
                                <input name="model" placeholder="Model" value={newDevice.model} onChange={handleInputChange} required />
                            </>
                        ) : (
                            <>
                                <select value={selectedType} onChange={handleTypeChange} required>
                                    <option value="">Select Device Type</option>
                                    <option value="Camera">Camera</option>
                                    <option value="Phone">Phone</option>
                                </select>
                                {selectedType && (
                                    <select value={newDevice.model} onChange={handleCatalogSelection} required>
                                        <option value="">Select Model</option>
                                        {catalog[selectedType + 's'].map(item => (
                                            <option key={item.id} value={item.model}>{item.model}</option>
                                        ))}
                                    </select>
                                )}
                            </>
                        )}
                        <button type="submit">Save</button>
                        <button type="button" onClick={() => { setShowForm(false); setIsCustom(false); setNewDevice({ deviceCatalogId: null, type: '', model: '' }); setSelectedType(''); }}>Cancel</button>
                    </form>
                </>
            )}
        </div>
    );
};

export default Devices;
