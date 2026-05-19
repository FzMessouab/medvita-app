'use client';

import { callApi } from '@/utils/http-client';
import { ReactNode, useEffect, useState } from 'react';
import { AiOutlineLoading3Quarters } from 'react-icons/ai';
import { toast } from "sonner";

interface LoadingBoxProps<T> {
    endpoint: string;
    size?: number;
    children: (data: T) => ReactNode;
}

export default function LoadingBox<T>({
    endpoint,
    size = 35,
    children
}: LoadingBoxProps<T>) {
    const [data, setData] = useState<T>();
    const [loading, setLoading] = useState<boolean>(false);
    const [hasLoaded, setHasLoaded] = useState<boolean>(false);

    const loadData = async () => {
        setLoading(true);
        try {
            const { data } = await callApi({
                endpoint
            });

            setData(data);
        } catch (error: any) {
            console.error('Error loading data:', error);
            toast.error(error.message);
        } finally {
            setLoading(false);
            setHasLoaded(true);
        }
    }

    useEffect(() => {
        loadData();
    }, [endpoint]);

    if (loading) {
        return (
            <div className='flex justify-center items-center w-full p-20'>
                <AiOutlineLoading3Quarters size={size} className='animate-spin text-blue-800' />
            </div>
        )
    }

    if (!hasLoaded) {
        return (
            <div className='flex justify-center items-center w-full p-20'>
                <AiOutlineLoading3Quarters size={size} className='animate-spin text-blue-800' />
            </div>
        );
    }

    if (hasLoaded && (!data || (Array.isArray(data) && data.length === 0))) {
        return <div className='glass-panel rounded-3xl px-6 py-10 text-center text-gray-500'>Aucune donnee disponible pour le moment.</div>;
    }

    return children(data as T);
}
