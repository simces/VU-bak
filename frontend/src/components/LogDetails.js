import React from 'react';
import '../styles/LogDetails.css'; 

const LogDetails = ({ log }) => {
    const dataBefore = JSON.parse(log.dataBefore);
    const dataAfter = JSON.parse(log.dataAfter);

    const keys = new Set([...Object.keys(dataBefore), ...Object.keys(dataAfter)]);
    const sortedKeys = Array.from(keys).sort();

    const renderFieldRow = (key) => {
        const before = dataBefore[key];
        const after = dataAfter[key];
        const isRemoved = before !== undefined && after === undefined;
        const isAdded = before === undefined && after !== undefined;
        const isChanged = before !== after && !isAdded && !isRemoved;
        const isUnchanged = before === after;

        const beforeClass = isChanged ? 'changed-before' : (isUnchanged ? 'unchanged' : '');
        const afterClass = isAdded ? 'added' : (isRemoved ? 'removed' : (isChanged ? 'changed-after' : ''));

        return (
            <div key={key} className="field-row">
                <div className="field-name">{key}</div>
                <div className={`before ${beforeClass}`}>{before ?? 'N/A'}</div>
                <div className={`after ${afterClass}`}>{after ?? 'N/A'}</div>
            </div>
        );
    };

    return (
        <div className="diff-container">
            {sortedKeys.map(renderFieldRow)}
        </div>
    );
};



export default LogDetails;
