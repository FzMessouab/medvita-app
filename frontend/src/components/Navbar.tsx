'use client';

import Link from 'next/link';
import { useState } from 'react';
import { IoSearchOutline } from 'react-icons/io5';

export default function Navbar() {
    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    return (
        <nav className="sticky top-0 z-50 border-b border-white/50 bg-white/85 backdrop-blur-xl">
            <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                <div className="flex min-h-20 items-center justify-between gap-4">
                    <div className="flex-shrink-0">
                        <Link href="/">
                            <span className="text-2xl font-bold tracking-[0.24em] text-[#F28C38]">MED</span>
                            <span className="text-xl font-semibold text-[#003087]">Vita</span>
                        </Link>
                    </div>

                    <div className="hidden md:flex space-x-6 items-center">
                        <Link href="/" className="text-gray-700 hover:text-[#003087]">Accueil</Link>
                        <Link href="/products" className="text-gray-700 hover:text-[#003087]">Catalogue</Link>
                        <Link href="/about" className="text-gray-700 hover:text-[#003087]">A propos</Link>
                        <Link href="/contact" className="text-gray-700 hover:text-[#003087]">Contact</Link>
                    </div>

                    <div className="flex items-center space-x-4">
                        <div className="relative hidden lg:block">
                            <input
                                type="text"
                                placeholder="Je cherche..."
                                className="w-56 rounded-full border border-gray-300 bg-white/80 py-2 pl-4 pr-10 text-sm focus:outline-none focus:ring-2 focus:ring-[#003087]"
                            />
                            <IoSearchOutline className='absolute top-3 right-2.5' />
                        </div>
                        <Link href="/cart" className="flex items-center space-x-1 rounded-full bg-[#F28C38] px-4 py-2 text-white hover:bg-[#e07b2c]">
                            <svg
                                className="w-5 h-5"
                                fill="none"
                                stroke="currentColor"
                                viewBox="0 0 24 24"
                                xmlns="http://www.w3.org/2000/svg"
                            >
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M3 3h18M7 7v10a2 2 0 002 2h6a2 2 0 002-2V7m-8 0h8" />
                            </svg>
                            <span>Mon Devis</span>
                        </Link>
                        <div className="md:hidden">
                            <button
                                onClick={toggleMenu}
                                className="text-gray-700 focus:outline-none"
                                aria-label="Toggle menu"
                            >
                                <svg
                                    className="w-6 h-6"
                                    fill="none"
                                    stroke="currentColor"
                                    viewBox="0 0 24 24"
                                    xmlns="http://www.w3.org/2000/svg"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth="2"
                                        d={isOpen ? 'M6 18L18 6M6 6l12 12' : 'M4 6h16M4 12h16M4 18h16'}
                                    />
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>

                {isOpen && (
                    <div className="border-t border-slate-200 py-3 md:hidden">
                        <div className="px-2 pt-2 pb-3 space-y-1">
                            <Link href="/" className="block px-3 py-2 text-gray-700 hover:bg-gray-100">Accueil</Link>
                            <Link href="/products" className="block px-3 py-2 text-gray-700 hover:bg-gray-100">Catalogue</Link>
                            <Link href="/about" className="block px-3 py-2 text-gray-700 hover:bg-gray-100">A propos</Link>
                            <Link href="/contact" className="block px-3 py-2 text-gray-700 hover:bg-gray-100">Contact</Link>
                        </div>
                    </div>
                )}
            </div>
        </nav>
    );
}
