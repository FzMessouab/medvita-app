"use client";

import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import * as countriesAndTimezones from "countries-and-timezones";

const UserCountryContext = createContext<{ countryCode: string }>({
    countryCode: "",
});

export interface UserCountryProviderProps {
    children: ReactNode;
}

export const UserCountryProvider = ({ children }: UserCountryProviderProps) => {
    const [countryCode, setCountryCode] = useState<string>("");

    const getUserCountry = async () => {
        const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;

        if (timezone === "" || !timezone) {
            return;
        }

        const countryCode = countriesAndTimezones.getTimezone(timezone)?.countries[0];
        
        if (countryCode) {
            setCountryCode(countryCode);
        }
    };

    useEffect(() => {
        getUserCountry();
    }, []);

    return (
        <UserCountryContext.Provider value={{ countryCode }}>
            {children}
        </UserCountryContext.Provider>
    );
};

const useUserCountryCode = () => useContext(UserCountryContext);

export default useUserCountryCode;
