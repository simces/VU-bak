const fetchWithToken = async (url, options = {}) => {
    
    const token = localStorage.getItem('token');
    const headers = {
      ...options.headers,
      Authorization: `Bearer ${token}`,
    };
    const response = await fetch(url, { ...options, headers });
    if (!response.ok) throw new Error('Network response was not ok');
  
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.indexOf("application/json") !== -1) {
      return response.json();
    } else {
      return response.text(); 
    }
  };

  export default fetchWithToken;
  