'use client';

import Banner from '@/components/Banner';
import { useParams } from 'next/navigation';

export default function ProductsLayout({ children }: { children: React.ReactNode }) {
  const { name }: { name: string } = useParams();

  return (
    <div>
      <Banner title={name ? decodeURIComponent(name) : "Nos produits"} />

      {children}
    </div>
  );
}
