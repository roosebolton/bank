'use strict'
/* authentication module for using fetch with JWT accesstoken and retry refreshtokens */
import {getUrl} from "./routing.js";

const saveAccesstoken = (item) => localStorage.setItem("Authorization",item)
const saveRefreshtoken  = (item) => localStorage.setItem("Refresh_Token", item)
const removeAccesstoken = () => localStorage.removeItem("Authorization")
const removeRefreshtoken  = () => localStorage.removeItem("Refresh_Token")
const getAccesstoken = () => localStorage.getItem("Authorization")
const getRefreshtoken = () => localStorage.getItem("Refresh_Token")

/** Authentication fetch   */
const authenticate = (login_data, showError) => {
    fetch(getUrl("authenticate"), {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(login_data)
    }).then(r => {
        if (r.ok) {
            r.json()
                .then(json => {
                        saveAccesstoken(json.Authorization)
                        saveRefreshtoken(json.Refresh_Token)
                        window.location.assign(getUrl("walletPage"))
                    }
                )
        } if (!r.ok) {
            r.json().then(json =>
            showError(json))
            }
        }
    )
}

const authenticateDev = (login_data) => {
    fetch(getUrl("authenticate"), {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(login_data)
    }).then(r => {
                if(r.ok) {
                    r.json()
                .then(json => {
                        saveAccesstoken(json.Authorization)
                        saveRefreshtoken(json.Refresh_Token)
                        window.location.assign(getUrl("adminPage"))
                })}
                else(r.json().then(json => {
                    console.log(json)
                }))
        })
}


const checkAuthLoginRedirect = () => {
    fetch("/users/authenticate/check", {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            Authorization: `Bearer `+getAccesstoken()
        },
    }).then(r => {
            if(r.status === 401) {
                window.location.assign("/index.html");
            }}).catch( error => {
                console.log(error)
    })
}

 const logout = () => {
    removeAccesstoken()
    removeRefreshtoken()
    return window.location.assign("/index.html");
 }

const settingsAccess = (type, data) => {
    return {
        method: type,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getAccesstoken()}`
        },
        body: JSON.stringify(data)
    }
}

const settingsRefresh = (type, data) => {
    return {
        method: type,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Refresh_Token': getRefreshtoken()
        },
        body: JSON.stringify(data)
    }
}

/**
 * Methode om een not authorized eerst op te vangen en te proberen een nieuw accestoken te krijgen.
 * @param status
 * @param response
 * @param url
 * @param options
 */
let handle401 = (status,response,url,options) => {
    if (status) {
        /* tries to fetch new accestoken from  */
        fetch(getUrl("refreshTokenAuthAPI"), settingsRefresh("GET"))
            .then(res =>
                res.json())
            .then(json => {
                saveAccesstoken(json.Authorization)
                fetch(url, options).then(response => {
                    handleResponse(response)
                }).catch(error => reject(error))
            })
            .catch(error => reject(error))
    } else {
        reject(response)
    }
}

/**
 * Hulpmethode om de reactie op een nieuw verzoek tot authorisatie af te handelen in handle401
 * @param response
 */
const handleResponse = (response) =>{
    if (response.ok) {
        resolve(response)
    } else {
        response.text().then(text => {
            reject(text)
        })
    }
}

/** Custom fetch for using both access and refreshtoken
 * if first fetch with access token fails, it retrieves a new accesstoken via the refreshtoken
 * has ait custom promise which only resolves on correct response
 * it returns the response as promise so that you can customize the desired response*/
const fetchAUTH = (url, options = {}) => {
    let authPromise = new Promise((resolve, reject) => {
        // initiate first fetch to targeted url.
        fetch(url, options)
            .then(response => {
                if (response.ok)   resolve(response)
                handle401(response.status===401,response,url,options)
            }).catch( error => reject(error))
    })
    return authPromise;
}




const refreshFetch= (url, options = {}) => {
   fetch(url, options)
        .then(res => res.json())
        .then(json => {
            saveAccesstoken(json.Authorization)
        })
}

const fetchRetry = (url, options) => {
    fetch(url, options)
        .then(response => {
            if (response.ok) {
                return response
            }
        })
}

const parseJwt = (token) => {
    let base64Url = token.split('.')[1];
    let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    let jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
}


const getUserId = (token) => {
   let destructuredToken = parseJwt(token)
    let { id: id} = destructuredToken;
   return id;
}

const isAdmin = (token) => {
    let destructuredToken = parseJwt(token)
    let { admin: admin} = destructuredToken;
    return admin;
}



export {getAccesstoken,getRefreshtoken,authenticate, authenticateDev, checkAuthLoginRedirect, settingsAccess, fetchAUTH, getUserId, logout, isAdmin};