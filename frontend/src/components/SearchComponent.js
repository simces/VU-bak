// src/components/SearchComponent.js
import React, { useState, useEffect, useCallback } from 'react';
import debounce from 'lodash.debounce';
import fetchWithToken from '../utils/fetchUtils'; 

const SearchComponent = () => {
  const [searchType, setSearchType] = useState('users');
  const [searchTerm, setSearchTerm] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchResults = useCallback(async (term, type) => {
    try {
      setLoading(true);
      let queryParam = '';
      if (type === 'users') {
        queryParam = `username=${term}`;
      } else if (type === 'photos') {
        queryParam = `searchTerm=${term}`;
      } else if (type === 'tags') {
        queryParam = `tagName=${term}`;
      }

      const response = await fetchWithToken(`http://localhost:8080/search/${type}?${queryParam}`);
      setResults(response);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  }, []);

  // eslint-disable-next-line
  const debouncedFetchResults = useCallback(
    debounce((term, type) => {
      fetchResults(term, type);
    }, 300),
    [fetchResults] 
  );

  useEffect(() => {
    if (searchTerm) {
      debouncedFetchResults(searchTerm, searchType);
    } else {
      setResults([]);
    }

    return () => {
      debouncedFetchResults.cancel();
    };
  }, [searchTerm, searchType, debouncedFetchResults]);

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
    setResults([]);
  };

  const handleTypeChange = (e) => {
    setSearchType(e.target.value);
    setResults([]);
  };

  const handleResultClick = (url) => {
    window.location.href = url;
  };

  const getDetailUrl = (result) => {
    if (searchType === 'users') {
      return `/users/${result.username}`;
    } else {
      return `/photos/${result.id}`;
    }
  };

  return (
    <div>
      <div>
        <label htmlFor="searchType">Search Type: </label>
        <select id="searchType" value={searchType} onChange={handleTypeChange}>
          <option value="users">Users</option>
          <option value="photos">Photos</option>
          <option value="tags">Tags</option>
        </select>
      </div>
      <div>
        <input
          type="text"
          placeholder={`Search ${searchType}`}
          value={searchTerm}
          onChange={handleSearchChange}
        />
      </div>
      <div>
        {loading && <p>Loading...</p>}
        {error && <p>Error: {error}</p>}
        {!loading && !error && (
          <ul>
            {results.map((result) => (
              <li key={result.id}>
                <a
                  href={getDetailUrl(result)}
                  onClick={(e) => {
                    e.preventDefault();
                    handleResultClick(getDetailUrl(result));
                  }}
                  style={{ cursor: 'pointer', color: 'blue', textDecoration: 'underline' }}
                >
                  {result.username || result.title || result.name}
                </a>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default SearchComponent;
