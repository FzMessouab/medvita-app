'use client';

import AddToQuote from "@/components/cart/AddToQuote";
import Zoom from 'react-medium-image-zoom';
import 'react-medium-image-zoom/dist/styles.css';
import Product, { ProductType } from "@/components/products/Product";
import LoadingBox from "@/components/LoadingBox";
import { useParams } from "next/navigation";
import { getProductImage, getProductSummary } from "@/utils/product";

export default function ProductPage() {
    const { name } = useParams();

    return (
        <>
            <LoadingBox endpoint={`equipment/details/${name}`}>
                {
                    (product: ProductType) => (
                        <div className="mx-auto flex max-w-screen-xl flex-col gap-9 px-4 py-16 sm:px-6 lg:flex-row lg:px-8">
                            <div className="glass-panel rounded-[28px] p-6 lg:sticky lg:top-24 lg:w-1/2">
                                <div className="mx-auto max-w-md">
                                    <Zoom>
                                        <img
                                            src={getProductImage(product)}
                                            alt={product.name}
                                            className="w-full rounded-2xl cursor-zoom-in object-cover"
                                        />
                                    </Zoom>
                                </div>

                            </div>

                            <div className="glass-panel w-full rounded-[28px] p-8">
                                <div className="text-4xl font-bold text-[#003087] mb-6">
                                    {product.name}
                                </div>

                                <p className="section-copy max-w-2xl text-base">
                                    {getProductSummary(product)}
                                </p>

                                <div className="mt-8 grid gap-4 md:grid-cols-3">
                                    <div className="rounded-2xl bg-slate-50 p-4">
                                        <p className="text-sm uppercase tracking-[0.2em] text-slate-500">Achat</p>
                                        <p className="mt-2 text-2xl font-semibold text-slate-900">{product.purchasePrice} EUR</p>
                                    </div>
                                    <div className="rounded-2xl bg-slate-50 p-4">
                                        <p className="text-sm uppercase tracking-[0.2em] text-slate-500">Location</p>
                                        <p className="mt-2 text-2xl font-semibold text-slate-900">{product.dailyRentalPrice} EUR / jour</p>
                                    </div>
                                    <div className="rounded-2xl bg-slate-50 p-4">
                                        <p className="text-sm uppercase tracking-[0.2em] text-slate-500">Stock</p>
                                        <p className="mt-2 text-2xl font-semibold text-slate-900">{product.availableQuantity ?? product.stockQuantity ?? 0}</p>
                                    </div>
                                </div>

                                <div className="mt-10">
                                    <AddToQuote product={product} />
                                </div>
                            </div>
                        </div>
                    )
                }
            </LoadingBox>

            {/* similar products */}
            <LoadingBox endpoint="equipment">
                {
                    (products: ProductType[]) => (
                        <div className="mx-auto max-w-screen-xl px-4 pb-24 sm:px-6 lg:px-8">
                            <div className="text-2xl font-bold text-[#003087] text-center mb-6">Produits similaires</div>

                            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 xl:grid-cols-4">
                                {products.map((product: ProductType, index: number) => (
                                    <Product key={index} product={product} />
                                ))}
                            </div>
                        </div>
                    )
                }
            </LoadingBox>
        </>
    );
}
