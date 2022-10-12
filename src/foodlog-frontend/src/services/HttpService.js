export const PostWithAuth = (url, body) => {
    url = "api/v1" + url;
    return fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem("tokenKey"),
        },
        body: JSON.stringify(body),
    });
}

export const PostWithoutAuth = (url, body) => {
    url = "api/v1" + url;
    return fetch(url, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
    });
}

export const PutWithAuth = (url, body) => {
    url = "api/v1" + url;
    return fetch(url, {
        method: 'PUT',
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem("tokenKey"),
        },
        body: JSON.stringify(body),
    });
}

export const GetWithAuth = (url) => {
    url = "api/v1" + url;
    return fetch(url, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem("tokenKey"),
        },
    });
}

export const GetWithoutAuth = (url) => {
    url = "api/v1" + url;
    return fetch(url, {
        method: 'GET',
    });
}

export const DeleteWithAuth = (url) => {
    url = "api/v1" + url;
    return fetch(url, {
        method: 'DELETE',
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem("tokenKey"),
        },
    });
}

export const RefreshToken = () => {
    return fetch("api/v1/auth/refresh", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            userId: localStorage.getItem("currentUser"),
            refreshToken: localStorage.getItem("refreshKey"),
        }),
    });
}
