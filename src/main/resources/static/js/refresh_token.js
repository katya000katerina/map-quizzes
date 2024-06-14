export async function fetchWithAuth(uri, options) {
    const response = await fetch(uri, options);
    const status = response.status;
    if (status !== 401) {
        return response;
    } else {
        const refreshResponse = await fetch('/api/v1/auth/refresh-token', {method : 'POST'});
        if (refreshResponse.ok){
            return await fetch(uri, options);
        } else{
            return response;
        }
    }
}