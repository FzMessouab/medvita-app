import Link from 'next/link';
import { FaPlus } from 'react-icons/fa';

interface BannerProps {
    title?: string;
}

export default function Banner({ title }: BannerProps) {
    return (
        <section
            className={`relative overflow-hidden bg-cover bg-center ${!title ? 'min-h-[620px]' : 'min-h-[260px]'}`}
            style={{
                backgroundImage: "url('/images/banner.png')",
            }}
        >
            <div className="absolute inset-0 bg-gradient-to-r from-[#0b3573]/92 via-[#0f4fa8]/72 to-[#f28c38]/22" />

            <div className="relative z-10 mx-auto flex h-full max-w-7xl flex-col justify-center px-4 py-16 sm:px-6 lg:px-8">
                {
                    title ? (
                        <h1 className="w-full text-center text-5xl font-bold text-white md:text-6xl">
                            {title}
                        </h1>
                    ) : (
                        <div className="max-w-3xl">
                            <p className="text-sm font-semibold uppercase tracking-[0.4em] text-[#ffd9bd]">MedVita</p>
                            <h1 className="mt-6 text-4xl font-bold text-white md:text-6xl md:leading-[1.05]">
                                Equipements medicaux fiables pour l'achat et la location.
                            </h1>
                            <p className="mt-6 max-w-2xl text-base leading-7 text-blue-50 md:text-lg">
                                Une vitrine plus claire pour votre catalogue, avec un parcours devis qui pointe enfin vers les vrais endpoints du backend.
                            </p>
                            <div className="mt-10 flex flex-wrap items-center gap-4">
                            <Link href="/contact">
                                <button className="rounded-full bg-white px-6 py-3 font-semibold text-[#003087] hover:bg-blue-50">
                                    Nous Contacter
                                </button>
                            </Link>
                            <Link href="/products">
                                <button className="flex items-center space-x-2 rounded-full border border-white/30 px-6 py-3 text-white hover:bg-white/10">
                                    <span>Explorer Le Catalogue</span>
                                    <FaPlus className="w-5 h-5" />
                                </button>
                            </Link>
                            </div>
                        </div>
                    )
                }
            </div>
        </section>
    );
}
