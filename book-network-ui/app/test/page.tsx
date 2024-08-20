"use client"
import { BookApi } from '@/services';
import React, { useEffect } from 'react'

const page = () => {
    useEffect(() => {
        const fetchData = async () => {
          const api = new BookApi();
          const data = await api.findAllBooks();
          console.log(data);
        };
        fetchData();
      }, []);
  return (
    <div>page</div>
  )
}

export default page