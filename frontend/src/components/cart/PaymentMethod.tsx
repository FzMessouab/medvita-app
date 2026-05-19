import { Field, useFormikContext } from "formik";

export interface PaymentMethodProps {
    name: string;
    value: string;
    label: string;
    image: string;
    imageWidth: number;
    disabled?: boolean;
    disabledMessage?: string;
    subTitle?: string;
    onChange: (value: { paymentMethodID: string, promoCode: string }, fetchPaymentMethods?: boolean) => void;
}

export default function PaymentMethod({
    name,
    value,
    label,
    image,
    imageWidth,
    disabled,
    disabledMessage,
    subTitle,
    onChange
}: PaymentMethodProps) {
    const { values }: any = useFormikContext();

    return (
        <div className="relative group">
            <label className={`payment-method flex items-center ${disabled ? "disabled opacity-50" : ""}`}>
                <div className="flex items-center">
                    <Field value={value}>
                        {({ field, form }: any) => {
                            return (
                                <input
                                    {...field}
                                    type="radio"
                                    name={name}
                                    className="mr-2"
                                    disabled={disabled}
                                    checked={values.paymentMethod === value}
                                    onChange={() => {
                                        onChange({
                                            paymentMethodID: value,
                                            promoCode: values.promoCode
                                        },
                                            false);

                                        form.setFieldValue(name, value);
                                    }}
                                />
                            )
                        }}
                    </Field>

                    <div>
                        <div className="text-sm font-medium mb-0.5">{label}</div>
                        <p className="text-xs opacity-70">{subTitle}</p>
                    </div>
                </div>

                <img
                    src={`/assets/${image}`}
                    width={imageWidth}
                    alt=""
                    className="ml-4"
                />
            </label>

            {
                disabled && disabledMessage && (
                    <div className="absolute hidden group-hover:block top-auto w-full md:top-0 left-0 md:left-full md:ml-2 md:w-64 p-3 bg-white border border-gray-200 shadow-lg rounded-lg z-10">
                        <p className="text-sm text-gray-700">
                            {disabledMessage}
                        </p>
                    </div>
                )
            }
        </div>
    );
}
