import type { Metadata } from "next";
import "./globals.css";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import 'react-datepicker/dist/react-datepicker.css';
import { Toaster } from "sonner";

export const metadata: Metadata = {
  title: "MedVita - Votre partenaire médical",
  description: "Catalogue, location et achat d'equipements medicaux MedVita.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="fr">
      <body className="antialiased app-shell">
        <Toaster position="bottom-right" richColors expand />
        <Navbar />
        {children}
        <Footer />
      </body>
    </html>
  );
}
