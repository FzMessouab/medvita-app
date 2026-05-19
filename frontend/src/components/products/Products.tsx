'use client';

import React from "react";
import Product, { ProductType } from "./Product";
import LoadingBox from "../LoadingBox";

export default function Products() {
    return (
        <section className="py-24">
            <div className="max-w-screen-xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="max-w-2xl">
                    <p className="text-sm font-semibold uppercase tracking-[0.3em] text-[#0f4fa8]">Catalogue</p>
                    <h2 className="section-title mt-4">
                    Nouveaux équipements technologiques disponibles
                    </h2>
                    <p className="section-copy mt-4">
                        Une selection recente d'equipements medicaux prets pour l'achat ou la location.
                    </p>
                </div>

                <LoadingBox endpoint="equipment">
                    {
                        (products: ProductType[]) => (
                            <div className="grid grid-cols-1 gap-6 mt-10 sm:grid-cols-2 xl:grid-cols-4">
                                {products.map((product, index) => (
                                    <Product key={index} product={product} />
                                ))}
                            </div>
                        )
                    }
                </LoadingBox>
            </div>
        </section>
    );
}
