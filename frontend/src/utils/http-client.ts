export interface CallApiI {
    endpoint: string
    method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
    body?: any
}

const API_BASE_URL = (process.env.NEXT_PUBLIC_API_URL || '/backend-api').replace(/\/+$/, '');

export const callApi = async (callApiBody: CallApiI): Promise<any> => {
    const { method = 'GET', endpoint, body } = callApiBody;
    const requestUrl = `${API_BASE_URL}/${endpoint.replace(/^\/+/, '')}`;
    const headers: HeadersInit = {
        'Content-Type': 'application/json'
    };
    const fetchOptions: RequestInit = {
        method,
        headers
    };

    if (body !== undefined && method !== 'GET') {
        fetchOptions.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(requestUrl, fetchOptions);
        const responseText = await response.text();
        const result = responseText ? safelyParseJson(responseText) : null;

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}, message: ${result?.message || 'Unknown error'}`);
        }

        return {
            data: result,
            status: response.status
        };
    } catch (error: any) {
        console.error('Error calling API:', error);
        throw error;
    }
}

function safelyParseJson(payload: string) {
    try {
        return JSON.parse(payload);
    } catch {
        return payload;
    }
}
