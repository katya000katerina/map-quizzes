export async function fetchWithAuth(path, requestInit) {
    const response = await fetch(path, requestInit);
    const status = response.status;
    if (status !== 401) {
        return response;
    } else {
        const refreshResponse = await fetch('/api/v1/auth/refresh-token', {method : 'POST'});
        if (refreshResponse.ok){
            return await fetch(path, requestInit);
        } else{
            return response;
        }
    }
}