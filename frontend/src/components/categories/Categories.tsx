'use client';

import React from "react";
import Category from "./Category";
import LoadingBox from "../LoadingBox";

export default function Categories() {
    return (
        <section className="max-w-screen-xl mx-auto px-4 py-24 sm:px-6 lg:px-8">
            <h2 className="text-blue-600 font-bold text-sm uppercase tracking-[0.3em]">Plus de categories</h2>
            <p className="section-title mt-3 max-w-3xl">
                Explorez notre gamme complète d’équipements médicaux classés par catégorie.
            </p>


            <LoadingBox endpoint="categories/all">
                {
                    (categories: string[]) => (
                        <div className="grid grid-cols-2 gap-6 mt-10 md:grid-cols-3 xl:grid-cols-4">
                            {categories.map((category, index) => (
                                <Category key={index} category={{ name: category }} />
                            ))}
                        </div>
                    )
                }
            </LoadingBox>
        </section>
    );
}
