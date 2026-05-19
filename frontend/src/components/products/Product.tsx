import React from "react";
import Image from 'next/image';
import Link from 'next/link';
import { getProductImage, getProductSummary } from "@/utils/product";

export interface ProductType {
    id: number;
    image?: string;
    imageUrl?: string;
    name: string;
    description?: string;
    purchasePrice: number;
    dailyRentalPrice: number;
    type?: string;
    availableQuantity?: number;
    stockQuantity?: number;
}

interface ProductProps {
    product: ProductType
}

export default function Product({ product }: ProductProps) {
    const imageSrc = getProductImage(product);
    const summary = getProductSummary(product);

    return (
        <div className="glass-panel overflow-hidden rounded-[28px] border border-white/60 transform hover:-translate-y-2 transition-all duration-300 ease-in-out hover:border-[#F28C38]/50">
            <div className="relative w-full h-64">
                <Image
                    src={imageSrc}
                    alt={product.name}
                    fill
                    style={{ objectFit: "cover" }}
                    className="rounded-t-xl transition-opacity duration-300"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-[#003087]/70 via-transparent to-transparent opacity-0 hover:opacity-100 transition-opacity duration-300 flex items-end p-4">
                    <span className="text-white text-sm font-medium bg-[#F28C38]/80 px-2 py-1 rounded">{summary}</span>
                </div>
            </div>
            <div className="p-6">
                <h3 className="text-xl font-bold text-[#003087] mb-3 line-clamp-2">{product.name}</h3>
                <div className="mb-5 space-y-1 text-sm text-slate-600">
                    <p>Achat: {product.purchasePrice} EUR</p>
                    <p>Location: {product.dailyRentalPrice} EUR / jour</p>
                </div>
                <Link href={`/products/${product.name}`}>
                    <button className="w-full bg-gradient-to-r from-[#F28C38] to-[#e07b2c] text-white px-6 py-3 rounded-lg hover:from-[#e07b2c] hover:to-[#F28C38] transition-all duration-300 shadow-md hover:shadow-lg transform hover:scale-105">
                        Voir Détails
                    </button>
                </Link>
            </div>
        </div>
    );
}
