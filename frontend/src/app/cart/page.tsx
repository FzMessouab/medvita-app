'use client';

import Banner from "@/components/Banner";
import MedicalCart, { CartItem } from "@/components/cart/MedicalCart";
import PaymentMethod from "@/components/cart/PaymentMethod";
import FormField from "@/components/forms/FormField";
import { callApi } from "@/utils/http-client";
import { Form, Formik } from "formik";
import Link from "next/link";
import { useEffect, useState } from "react";
import { toast } from "sonner";
import * as Yup from "yup";

const IS_DEBUG = false;

const paymentMethods = [
  {
    value: "CARTE",
    label: "Carte de Crédit",
    image: "credit-card.svg",
    imageWidth: 200,
    subTitle: "Visa, Mastercard, American Express"
  },
  {
    value: "ESPECES",
    label: "Espèces",
    image: "cash.svg",
    imageWidth: 50,
    subTitle: "Paiement en espèces à la livraison"
  }
];

const validationSchema = Yup.object({
  fullName: Yup.string().required("Name is required"),
  email: Yup.string().email("Invalid email address").required("Email is required"),
  phone: Yup.string().required(),
  address: Yup.string().required(),
  city: Yup.string().required(),
  zipCode: Yup.number().required(),
  country: Yup.string().required(),
  socialSecurityNumber: Yup.number().required(),
  paymentMethod: Yup.string().required("Please select a payment method"),

  cvc: Yup.string().optional(),
  expiry: Yup.string().optional(),
  number: Yup.string().optional()
});

function formatCheckoutDate(dateValue?: Date | string) {
  if (!dateValue) {
    return undefined;
  }

  if (typeof dateValue === "string") {
    return dateValue.includes("T") ? dateValue.slice(0, 10) : dateValue;
  }

  const year = dateValue.getFullYear();
  const month = `${dateValue.getMonth() + 1}`.padStart(2, "0");
  const day = `${dateValue.getDate()}`.padStart(2, "0");

  return `${year}-${month}-${day}`;
}

function getInvoiceDownloadUrl(responseData: any) {
  const invoice = responseData?.invoice;

  if (invoice?.downloadUrl) {
    if (typeof invoice.downloadUrl === "string" && invoice.downloadUrl.startsWith("/api/")) {
      return invoice.downloadUrl.replace(/^\/api\//, "/backend-api/");
    }

    return invoice.downloadUrl;
  }

  if (invoice?.id) {
    return `/backend-api/invoices/${invoice.id}/download`;
  }

  return "";
}

function formatCardNumber(value?: string) {
  if (!value) {
    return "•••• •••• •••• ••••";
  }

  const digits = value.replace(/\D/g, "").slice(0, 16);
  return digits.replace(/(.{4})/g, "$1 ").trim();
}

function formatCardExpiry(value?: string) {
  if (!value) {
    return "MM/AA";
  }

  if (value.includes("-")) {
    const [year, month] = value.split("-");
    return `${month}/${year.slice(-2)}`;
  }

  return value;
}

export default function Checkout() {
  const [items, setItems] = useState<CartItem[]>([]);
  const [invoiceUrl, setInvoiceUrl] = useState<string>("");

  async function submit(values: any, formikHelpers: any) {
    const cart = JSON.parse(localStorage.getItem('cart') || '[]') || [];

    const purchaseItems = cart.filter((item: any) => item.mode === 'buy').map((item: any) => ({
      equipmentId: item.product.id,
      quantity: item.quantity
    }));

    const rentalItems = cart.filter((item: any) => item.mode === 'rent').map((item: any) => ({
      equipmentId: item.product.id,
      quantity: item.quantity,
      startDate: formatCheckoutDate(item.rentStart),
      endDate: formatCheckoutDate(item.rentEnd)
    }));

    const data = {
      client: {
        fullName: values.fullName,
        email: values.email,
        phone: values.phone,
        address: values.address,
        city: values.city,
        state: values.state,
        postalCode: values.zipCode,
        country: values.country,
        socialSecurityNumber: values.socialSecurityNumber
      },
      paymentMethod: values.paymentMethod,
      purchaseItems,
      rentalItems
    }

    try {
      const response = await callApi({
        endpoint: "cart/checkout",
        method: "POST",
        body: data
      });

      setItems([]);
      formikHelpers.resetForm();
      localStorage.removeItem('cart');

      setInvoiceUrl(getInvoiceDownloadUrl(response.data));
    } catch (error: any) {
      console.error('Error loading data:', error);
      toast.error(error.message);
    }
  }

  useEffect(() => {
    setItems(
      JSON.parse(localStorage.getItem('cart') || '[]') || []
    );
  }, []);

  if (items.length === 0 && !invoiceUrl) {
    return (
      <section className="mx-auto max-w-3xl px-4 py-20 sm:px-6">
        <div className="glass-panel rounded-[32px] p-10 text-center">
          <p className="text-sm font-semibold uppercase tracking-[0.3em] text-[#0f4fa8]">Panier</p>
          <h2 className="section-title mt-4 text-slate-900">Votre panier est vide</h2>
          <p className="section-copy mt-4">Ajoutez des produits depuis le catalogue pour lancer une demande de devis.</p>
          <div className="mt-10">
            <Link href="/products" className="inline-flex cursor-pointer items-center rounded-full bg-gradient-to-r from-[#F28C38] to-[#e07b2c] px-6 py-3 text-white shadow-md transition-all duration-300 hover:scale-105 hover:shadow-lg">
              Voir les produits
            </Link>
          </div>
        </div>
      </section>
    );
  }

  return (
    <div>
      <Banner title="Votre Devis" />

      <div>
        <div>
          {invoiceUrl ? (
            <>
              <section className="mx-auto max-w-3xl px-4 py-16 sm:px-6">
                <div className="glass-panel rounded-[32px] p-10 text-center">
                  <p className="text-sm font-semibold uppercase tracking-[0.3em] text-[#0f4fa8]">Confirmation</p>
                  <h2 className="section-title mt-4 text-slate-900">Devis envoye avec succes</h2>
                  <p className="section-copy mt-4">Votre demande a bien ete enregistree. Le fichier PDF est disponible via le lien ci-dessous.</p>
                  <div className="mt-8 flex flex-col items-center gap-4 sm:flex-row sm:justify-center">
                    <a href={invoiceUrl} target="_blank" rel="noopener noreferrer" className="inline-flex min-w-56 cursor-pointer items-center justify-center rounded-full bg-gradient-to-r from-[#F28C38] to-[#e07b2c] px-6 py-3 text-white shadow-md transition-all duration-300 hover:scale-105 hover:shadow-lg">
                      Telecharger le devis
                    </a>
                    <Link href="/products" className="inline-flex min-w-56 items-center justify-center rounded-full border border-slate-200 bg-white px-6 py-3 text-slate-700 transition hover:border-[#0f4fa8] hover:text-[#0f4fa8]">
                      Retour au catalogue
                    </Link>
                  </div>
                </div>
              </section>
            </>
          ) : (
            <Formik
              validationSchema={validationSchema}
              initialValues={
                IS_DEBUG ? {
                  fullName: 'a',
                  email: 'a@gmail.com',
                  phone: '+2123456789',
                  address: 'a',
                  city: 'a',
                  state: 'a',
                  zipCode: 30000,
                  country: 'FR',
                  socialSecurityNumber: 12345678,
                  paymentMethod: paymentMethods[0].value,

                  cvc: "",
                  expiry: "",
                  number: ""
                } : {
                  fullName: '',
                  email: '',
                  phone: '',
                  address: '',
                  city: '',
                  state: '',
                  zipCode: 0,
                  country: 'FR',
                  socialSecurityNumber: 0,
                  paymentMethod: paymentMethods[0].value,

                  cvc: "",
                  expiry: "",
                  number: ""
                }}
              onSubmit={submit}
            >
              {({ setFieldValue, values }) => {
                return (
                  <Form>
                    <section className="mx-auto max-w-screen-xl px-4 py-14 sm:px-6 lg:px-8">
                      <div className="mb-8 max-w-2xl">
                        <p className="text-sm font-semibold uppercase tracking-[0.3em] text-[#0f4fa8]">Checkout</p>
                        <h2 className="section-title mt-4 text-slate-900">Finaliser votre demande de devis</h2>
                        <p className="section-copy mt-4">
                          Renseignez vos informations puis validez votre panier. Le backend generera une facture telechargeable une fois la demande traitee.
                        </p>
                      </div>

                      <div className="grid grid-cols-1 gap-6 lg:grid-cols-[1.1fr_0.9fr]">
                        <div className="glass-panel rounded-[32px] p-6 lg:p-8">
                          <div className="mb-8">
                            <p className="text-sm font-semibold uppercase tracking-[0.25em] text-slate-500">Informations client</p>
                            <h3 className="mt-3 text-2xl font-semibold text-slate-900">Coordonnees et paiement</h3>
                          </div>

                          <div className="grid grid-cols-12 gap-4 mb-3">
                            <div className="col-span-12 md:col-span-6">
                              <FormField
                                label="Full Name*"
                                type="text"
                                name="fullName"
                                hideLabel
                              />
                            </div>
                            <div className="col-span-12 md:col-span-6">
                              <FormField
                                label="Email Address*"
                                type="email"
                                name="email"
                                hideLabel
                              />
                            </div>
                          </div>

                          <div className="mb-8 mt-5">
                            <div className="grid grid-cols-12 gap-4 mb-3">
                              <div className="col-span-12">
                                <FormField
                                  label="Address"
                                  type="text"
                                  name="address"
                                  hideLabel
                                />
                              </div>
                            </div>

                            <div className="grid grid-cols-12 gap-4 mb-3">
                              <div className="col-span-12 md:col-span-6">
                                <FormField
                                  label="Phone"
                                  type="telephone"
                                  name="phone"
                                  hideLabel
                                />
                              </div>

                              <div className="col-span-12 md:col-span-6">
                                <FormField
                                  label="Social Security Number"
                                  type="number"
                                  name="socialSecurityNumber"
                                  hideLabel
                                />
                              </div>
                            </div>

                            <div className="grid grid-cols-12 gap-4 mb-3">
                              <div className="col-span-12 md:col-span-4">
                                <FormField
                                  label="City"
                                  type="text"
                                  name="city"
                                  hideLabel
                                />
                              </div>
                              <div className="col-span-12 md:col-span-4">
                                <FormField
                                  label="State"
                                  type="text"
                                  name="state"
                                  hideLabel
                                />
                              </div>
                              <div className="col-span-12 md:col-span-4">
                                <FormField
                                  label="ZIP Code"
                                  type="number"
                                  name="zipCode"
                                  hideLabel
                                />
                              </div>
                            </div>

                            <div className="grid grid-cols-12 gap-4 mb-3">
                              <div className="col-span-12">
                                <FormField
                                  label="Country"
                                  type="country"
                                  name="country"
                                  hideLabel
                                />
                              </div>
                            </div>

                            <div className="mt-10">
                              <h3 className="mb-5 text-sm font-semibold uppercase tracking-[0.25em] text-slate-500">
                                Mode de paiement
                              </h3>

                              {paymentMethods.map((method) => (
                                <PaymentMethod
                                  key={method.value}
                                  name="paymentMethod"
                                  value={method.value}
                                  label={method.label}
                                  image={method.image}
                                  imageWidth={method.imageWidth}
                                  onChange={() => setFieldValue('paymentMethod', method.value)}
                                  subTitle={method.subTitle}
                                />
                              ))}
                            </div>
                          </div>

                          {
                            values.paymentMethod === 'CARTE' && (
                              <div className="rounded-[28px] border border-slate-200 bg-slate-50/70 p-5">
                                <div className="mb-5 overflow-hidden rounded-[28px] bg-gradient-to-br from-[#0b3573] via-[#0f4fa8] to-[#1d7dd8] p-6 text-white shadow-xl">
                                  <div className="flex items-start justify-between">
                                    <div>
                                      <p className="text-xs uppercase tracking-[0.3em] text-blue-100">Carte</p>
                                      <p className="mt-6 text-2xl font-semibold tracking-[0.18em]">
                                        {formatCardNumber(values.number)}
                                      </p>
                                    </div>
                                    <div className="rounded-full border border-white/20 px-3 py-1 text-xs uppercase tracking-[0.2em] text-blue-50">
                                      {values.paymentMethod}
                                    </div>
                                  </div>

                                  <div className="mt-10 flex items-end justify-between gap-4">
                                    <div>
                                      <p className="text-[11px] uppercase tracking-[0.25em] text-blue-100">Titulaire</p>
                                      <p className="mt-2 text-base font-medium">
                                        {values.fullName || "Nom complet"}
                                      </p>
                                    </div>
                                    <div className="text-right">
                                      <p className="text-[11px] uppercase tracking-[0.25em] text-blue-100">Expiration</p>
                                      <p className="mt-2 text-base font-medium">
                                        {formatCardExpiry(values.expiry)}
                                      </p>
                                    </div>
                                    <div className="text-right">
                                      <p className="text-[11px] uppercase tracking-[0.25em] text-blue-100">CVC</p>
                                      <p className="mt-2 text-base font-medium">
                                        {values.cvc || "•••"}
                                      </p>
                                    </div>
                                  </div>
                                </div>

                                <div className="col-span-6">
                                  <div className="col-span-12 md:col-span-6">
                                    <FormField
                                      label="Numéro de Carte"
                                      type="tel"
                                      name="number"
                                      hideLabel
                                    />
                                  </div>

                                  <div className="grid grid-cols-12 gap-4 mt-3">
                                    <div className="col-span-12 md:col-span-6">
                                      <FormField
                                        label="Code CVC"
                                        type="number"
                                        name="cvc"
                                        hideLabel
                                      />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                      <FormField
                                        label="Date d'Expiration"
                                        type="date"
                                        name="expiry"
                                        hideLabel
                                      />
                                    </div>
                                  </div>
                                </div>
                              </div>
                            )
                          }
                        </div>

                        <div className="glass-panel h-fit rounded-[32px] p-6 lg:sticky lg:top-24 lg:p-8">
                          <div className="mb-6">
                            <p className="text-sm font-semibold uppercase tracking-[0.25em] text-slate-500">Recapitulatif</p>
                            <h3 className="mt-3 text-2xl font-semibold text-slate-900">Votre panier</h3>
                            <p className="section-copy mt-3 text-sm">Verifiez les quantites, les periodes de location et validez ensuite votre demande.</p>
                          </div>
                          <MedicalCart items={items} setItems={setItems} />
                        </div>
                      </div>
                    </section>
                  </Form>
                )
              }}
            </Formik>
          )}
        </div>
      </div>
    </div>
  );
}
