import { useState, useEffect } from 'react';

const useGoogleMaps = (apiKey) => {
  const [isLoaded, setIsLoaded] = useState(false);

  useEffect(() => {
    const existingScript = document.getElementById('google-maps-script');

    if (!existingScript) {
      const script = document.createElement('script');
      script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}`;
      script.id = 'google-maps-script';
      document.body.appendChild(script);

      script.onload = () => {
        setIsLoaded(true);
      };
    } else {
      setIsLoaded(true);
    }
  }, [apiKey]);

  return isLoaded;
};

export default useGoogleMaps;
