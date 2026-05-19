import Banner from "@/components/Banner";

export default function AboutPage() {
  return (
    <div>
      <Banner title="A propos" />
      <section className="mx-auto max-w-5xl px-4 py-16 sm:px-6 lg:px-8">
        <div className="glass-panel rounded-[32px] p-8 md:p-12">
          <p className="text-sm font-semibold uppercase tracking-[0.3em] text-[#0f4fa8]">MedVita</p>
          <h1 className="section-title mt-4">Un partenaire dédié à l’équipement médical professionnel.</h1>
          <p className="section-copy mt-6">
            MedVita met à disposition une sélection d’équipements médicaux destinés aux établissements de santé, aux cabinets
            spécialisés et aux professionnels à la recherche de solutions fiables pour l’achat ou la location.
          </p>
          <div className="mt-10 grid gap-4 md:grid-cols-3">
            <div className="rounded-2xl bg-slate-50 p-5">
              <h2 className="font-semibold text-slate-900">Sélection professionnelle</h2>
              <p className="section-copy mt-2 text-sm">
                Une offre pensée pour les besoins concrets des structures de soins, avec des références adaptées aux usages du terrain.
              </p>
            </div>
            <div className="rounded-2xl bg-slate-50 p-5">
              <h2 className="font-semibold text-slate-900">Achat et location</h2>
              <p className="section-copy mt-2 text-sm">
                MedVita permet de répondre à des besoins ponctuels comme à des projets d’équipement plus durables.
              </p>
            </div>
            <div className="rounded-2xl bg-slate-50 p-5">
              <h2 className="font-semibold text-slate-900">Suivi commercial</h2>
              <p className="section-copy mt-2 text-sm">
                Chaque demande peut être étudiée avec attention afin d’orienter le choix du matériel selon vos contraintes.
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
