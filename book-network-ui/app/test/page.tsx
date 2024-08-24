"use client"
import { BookApi, Configuration, FindAllBooks200Response, AuthenticationApi } from '@/services/generated';
import React, { useEffect, useState } from 'react'

const page = () => {
  const [data, setData] = useState(null);
  const [genData, setGenData] = useState<FindAllBooks200Response>({});
  useEffect(() => {
    const fetchData = async () => {
      try {
        const config:Configuration = new Configuration({
          accessToken: localStorage.getItem('token')||''
        });
        const bookService = new BookApi(config);
              const generatedData = await bookService.findAllBooksByOwner(
                0, 4
              );
              
              setGenData(generatedData.data);

          } catch (error) {
              console.error('Error fetching data:', error);
          }
      };

      fetchData();
  }, []);

  return (
      <div className='w-[80%] bg-primary text-muted '>
          <h1>Data from Generated API:</h1>
          <pre>{JSON.stringify(genData.content, null, 2)}</pre>
      </div>
      
  );
}

export default page