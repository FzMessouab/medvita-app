import type { ProductType } from "@/components/products/Product";

const FALLBACK_IMAGES = [
    "/images/Produit1.png",
    "/images/Equipement.png",
    "/images/Diagnostic.png",
    "/images/Mobilier.png",
];

export function getProductImage(product: Pick<ProductType, "id" | "image" | "imageUrl" | "type">) {
    const rawImage = product.imageUrl || product.image;
    if (rawImage) {
        return rawImage.startsWith("/") ? rawImage : `/${rawImage}`;
    }

    return FALLBACK_IMAGES[product.id % FALLBACK_IMAGES.length];
}

export function getProductSummary(product: Pick<ProductType, "description" | "type">) {
    if (product.description?.trim()) {
        return product.description;
    }

    return product.type
        ? `Equipement medical de type ${product.type}.`
        : "Equipement medical certifie disponible a la location ou a l'achat.";
}

export function getCategoryVisual(category: string) {
    const normalized = category.toLowerCase();

    if (normalized.includes("resp")) {
        return { image: "/images/soins.png", accent: "from-sky-100 to-cyan-50" };
    }
    if (normalized.includes("mob") || normalized.includes("furniture") || normalized.includes("consult")) {
        return { image: "/images/Mobilier.png", accent: "from-amber-100 to-orange-50" };
    }
    if (normalized.includes("diag") || normalized.includes("monitor")) {
        return { image: "/images/Diagnostic.png", accent: "from-indigo-100 to-blue-50" };
    }
    if (normalized.includes("imag")) {
        return { image: "/images/scanner.png", accent: "from-violet-100 to-fuchsia-50" };
    }
    if (normalized.includes("emerg")) {
        return { image: "/images/Equipement.png", accent: "from-rose-100 to-red-50" };
    }

    return { image: "/images/Produit1.png", accent: "from-slate-100 to-slate-50" };
}
