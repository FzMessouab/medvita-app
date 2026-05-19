import React, { ReactElement, ReactNode } from "react";
import { ErrorMessage, Field } from "formik";
import useUserCountryCode from "@/hooks/useUserCountryCode";
import PhoneInput from 'react-phone-input-2'
import checkoutData from "@/data/checkout.json"
import 'react-phone-input-2/lib/style.css'

export interface FormFieldProps {
    label: string;
    type: "text" | "tel" | "date" | "textarea" | "email" | "password" | "number" | "telephone" | "select" | "hidden" | "country";
    name: string;
    icon?: ReactNode;
    rounded?: boolean;
    hideLabel?: boolean;
    className?: string;
    errorLabelClassName?: string;
    children?: ReactElement<any>[];
    success?: boolean;
    message?: string;
    disabled?: boolean;
}

export default function FormField({
    label,
    type,
    name,
    icon,
    rounded = false,
    hideLabel = false,
    className = "",
    errorLabelClassName = "",
    children,
    success,
    message,
    disabled = false
}: FormFieldProps) {
    const { countryCode } = useUserCountryCode();

    return (
        <div className={`form-field ${className}`}>
            {
                !hideLabel && (
                    <label className={`block text-sm font-medium my-2`}>
                        {label}
                    </label>
                )
            }

            <Field
                type={type}
                name={name}
                placeholder={label}
            >
                {({ field, form }: any) => {
                    const isError = (form.errors[name] && form.touched[name]) || success === false;
                    const roundedClass = rounded ? 'rounded-full' : 'rounded-2xl';
                    const className = `${icon ? '!pl-10' : 'px-4 py-3'} form-control w-full border bg-white/90 border-slate-200 ${roundedClass} text-slate-800 shadow-sm ${isError ? 'border-red-500' : ''} ${success ? 'border-green-500' : ''}`;

                    if (type === "select" || type == "country") {
                        return (
                            <select
                                {...field}
                                onChange={(e) => form.setFieldValue(field.name, e.target.value)}
                                className={className}
                                disabled={disabled}
                            >
                                {
                                    type == 'country' ? (
                                        checkoutData.countries.map(({ name, code }) => (
                                            <option key={code} value={code}>{name}</option>
                                        ))
                                    ) : children
                                }
                            </select>
                        );
                    }

                    if (type === "telephone") {
                        return (
                            <PhoneInput
                                {...field}
                                country={countryCode.toLowerCase()}
                                onChange={phone => form.setFieldValue(field.name, phone)}
                                inputClass={className}
                                disabled={disabled}
                            />
                        )
                    }

                    return (
                        <div className={icon ? `relative` : `block`}>
                            {icon && (
                                <div className="text-md absolute inset-y-0 left-3 flex items-center text-gray-500">
                                    {icon}
                                </div>
                            )}
                            {type === "textarea" ? (
                                <textarea className={'!min-h-20 ' + className} placeholder={label} {...field} disabled={disabled} />
                            ) : (
                                <input type={type} className={className} placeholder={label} {...field} disabled={disabled} />
                            )}
                        </div>
                    );
                }}
            </Field>

            <ErrorMessage name={name}>
                {(msg: string) => {
                    if (!msg) return null;

                    return (
                        <div className={`message text-xs font-medium text-red-700 mt-2 ${errorLabelClassName}`}>
                            {msg}
                        </div>
                    )
                }}
            </ErrorMessage>

            {
                success != undefined && (
                    <div className={`message ${success ? 'success' : 'error'}-message`}>
                        {message}
                    </div>
                )
            }
        </div>
    )
}
