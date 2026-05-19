import Banner from "@/components/Banner";

export default function ContactPage() {
  return (
    <div>
      <Banner title="Contact" />
      <section className="mx-auto max-w-5xl px-4 py-16 sm:px-6 lg:px-8">
        <div className="grid gap-6 md:grid-cols-[1.15fr_0.85fr]">
          <div className="glass-panel rounded-[32px] p-8 md:p-12">
            <p className="text-sm font-semibold uppercase tracking-[0.3em] text-[#0f4fa8]">Parlons de vos besoins</p>
            <h1 className="section-title mt-4">Une équipe disponible pour vos demandes d’équipement médical.</h1>
            <p className="section-copy mt-6">
              MedVita accompagne les cliniques, cabinets, centres de soins et professionnels de santé dans la recherche
              d’équipements adaptés à leurs besoins. Pour une demande de devis, une disponibilité produit, une location
              ponctuelle ou un achat, notre équipe vous répond avec une proposition claire et un suivi personnalisé.
            </p>
            <div className="mt-10 grid gap-4 md:grid-cols-2">
              <div className="rounded-2xl bg-slate-50 p-5">
                <h2 className="text-lg font-semibold text-slate-900">Demandes traitées</h2>
                <p className="section-copy mt-3 text-sm">
                  Devis d’achat, location courte ou longue durée, renouvellement de matériel, disponibilité catalogue et
                  accompagnement sur le choix des références.
                </p>
              </div>
              <div className="rounded-2xl bg-slate-50 p-5">
                <h2 className="text-lg font-semibold text-slate-900">Réponse commerciale</h2>
                <p className="section-copy mt-3 text-sm">
                  Chaque demande est étudiée selon votre activité, vos contraintes de délai et le niveau d’équipement recherché.
                </p>
              </div>
            </div>
          </div>
          <div className="glass-panel rounded-[32px] p-8">
            <h2 className="text-xl font-semibold text-slate-900">Nous contacter</h2>
            <div className="mt-6 space-y-5 text-slate-600">
              <div>
                <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">Parcours recommandé</p>
                <p className="mt-2 text-sm leading-7">
                  Pour une demande rapide, ajoutez vos produits au devis puis validez votre panier. Notre équipe reprend ensuite votre demande.
                </p>
              </div>
              <div>
                <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">Accompagnement</p>
                <p className="mt-2 text-sm leading-7">
                  Si votre besoin concerne plusieurs références, un renouvellement d’équipement ou une location planifiée,
                  MedVita peut préparer une proposition adaptée à votre structure.
                </p>
              </div>
              <div>
                <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">Disponibilité</p>
                <p className="mt-2 text-sm leading-7">
                  Les demandes sont traitées par ordre de réception, avec retour commercial dès validation des références demandées.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
