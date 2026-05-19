import Link from 'next/link';
import sharedData from '@/utils/shared.json';

const businessName = sharedData.businessName;

export default function Footer() {
    return (
        <footer className="mt-24 border-t border-white/50 bg-[#09152d] py-12 text-white">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="grid grid-cols-1 gap-8 md:grid-cols-3">
                    <div>
                        <h3 className="text-xl font-bold text-[#F28C38]">
                            {businessName}
                        </h3>
                        <p className="text-gray-300 mt-2">
                            Votre partenaire de confiance pour l’equipement medical professionnel.
                        </p>
                    </div>

                    <div>
                        <h3 className="text-lg font-semibold">Liens utiles</h3>
                        <ul className="mt-2 space-y-2">
                            <li>
                                <Link href="/products" className="text-gray-300 hover:text-[#F28C38]">Nos Produits</Link>
                            </li>
                            <li>
                                <Link href="/about" className="text-gray-300 hover:text-[#F28C38]">À Propos</Link>
                            </li>
                            <li>
                                <Link href="/contact" className="text-gray-300 hover:text-[#F28C38]">Contact</Link>
                            </li>
                        </ul>
                    </div>

                    <div>
                        <h3 className="text-lg font-semibold">Besoin d’un devis ?</h3>
                        <p className="mt-2 text-gray-300">
                            Selectionnez vos produits, validez votre panier et transmettez votre demande directement depuis la plateforme.
                        </p>
                        <p className="mt-3 text-gray-300">
                            Notre equipe reprend ensuite votre besoin pour vous adresser une proposition adaptee.
                        </p>
                    </div>
                </div>

                <div className="mt-8 text-center text-gray-400">
                    <p>© 2025 {businessName}. Tous droits réservés.</p>
                </div>
            </div>
        </footer>
    );
}
