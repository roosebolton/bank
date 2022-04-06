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
     console.log(getAccesstoken())
     console.log(getRefreshtoken())
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
                if (response.status === 401) {
                    /* tries to fetch new accestoken from  */
                    console.error("Accesstoken invalid, trying to retrieving new accesstoken")
                    fetch(getUrl("refreshTokenAuthAPI"), settingsRefresh("GET"))
                        .then(res =>
                            res.json())
                                .then(json => {
                                    saveAccesstoken(json.Authorization)
                                    fetch(url, options).then(response => {
                                        if (response.ok) {resolve(response)
                                        } else  {
                                            response.text().then(text => { reject(text)
                                            })
                                        }
                                    }).catch(error => reject(error))
                                })
                        .catch( error => reject(error))
                } else {reject(response)
                }
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