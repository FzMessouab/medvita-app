import React from "react";
import { getCategoryVisual } from "@/utils/product";

interface CategoryProps {
    category: {
        name: string;
    };
}

export default function Category({ category }: CategoryProps) {
    const visual = getCategoryVisual(category.name);

    return (
        <div className="glass-panel flex flex-col items-center rounded-[28px] p-6 text-center">
            <div className={`flex h-36 w-36 items-center justify-center overflow-hidden rounded-full border border-gray-200 bg-gradient-to-br ${visual.accent} shadow-md`}>
                <img src={visual.image} alt={category.name} className="h-24 w-24 object-contain" />
            </div>
            <span className="mt-4 text-md font-medium text-slate-800">{category.name}</span>
        </div>
    );
}
