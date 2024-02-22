const fetchWithToken = async (url, options = {}) => {
    
    const token = localStorage.getItem('token');
    const headers = {
      ...options.headers,
      Authorization: `Bearer ${token}`,
    };
    const response = await fetch(url, { ...options, headers });
    if (!response.ok) throw new Error('Network response was not ok');
  
    // Check if the response is JSON before parsing
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.indexOf("application/json") !== -1) {
      return response.json();
    } else {
      return response.text(); // Or handle non-JSON responses differently
    }
  };

  export default fetchWithToken;
  