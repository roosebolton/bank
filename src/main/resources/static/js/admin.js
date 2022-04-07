'use strict'

import {getUrl} from "./modules/routing.js";
import {checkAuthLoginRedirect, fetchAUTH, logout, settingsAccess} from "./modules/authModule.js";

//////////////////////////////////////////////Constants///////////////////////////////////////////
const coinData = `https://api.coingecko.com/api/v3/coins/`
const internalAssetRateEndPoint = getUrl("coinAPI");

checkAuthLoginRedirect()

document.querySelector("#logout").addEventListener("click",logout)

/////////////////////////////////////////////Get gebruiker//////////////////////////////////////
const getUserInformationById = (id) => {
    fetchAUTH("/admin/userdata/" + id, settingsAccess('GET'))
        .then(response =>
            response.json())
        .then(json => {
            let userInformation = json
            displayUserInformation(userInformation)
            clearWalletTable()
            displayWallet(userInformation.customer.userId)
            clearUserSearchInputField()
        })
        .catch(error => {
            error.json().then(json => {
                alert(json.message);
            })
        })
}

const getUserInformationByUserName = (userName) => {
    fetchAUTH("/admin/userdataname/" + userName, settingsAccess('GET'))
        .then(response =>
            response.json())
        .then(json => {
            let userInformation = json
            displayUserInformation(userInformation)
            clearWalletTable()
            displayWallet(userInformation.customer.userId)
            clearUserSearchInputField()
        })
        .catch(error => {
            error.text().then(text => {
                alert(text);
            })
        })
}

function displayUserInformation(userInformation) {
    document.getElementById('name').innerHTML = displayFullName(userInformation)
    document.getElementById('street').innerHTML = displayFullAddress(userInformation)
    document.getElementById('zipcode').innerHTML = userInformation.customer.address.postalCode + " " + userInformation.customer.address.city
    document.getElementById('dateOfBirth').innerHTML = displayDateOfBirth(userInformation)
    document.getElementById('IBAN').innerHTML = userInformation.customer.identifyingInformation.ibanNumber
    document.getElementById('BSN').innerHTML = userInformation.customer.identifyingInformation.bsnNumber
    document.getElementById('balance').innerHTML = currencyFormatter("EUR").format(userInformation.account.balance)
}

//https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/NumberFormat
let currencyFormatter = (currentCurrency) =>
    new Intl.NumberFormat('nl-NE', {
        style: 'currency',
        currency: currentCurrency,
    });

const displayFullName = (userInformation) => {
    let firstName = userInformation.customer.personalDetails.firstName.toString()
    let lastName = userInformation.customer.personalDetails.lastName.toString()
    return firstName + " " + infix(userInformation) + " " + lastName
}

const infix = (userInformation) => {
    if (userInformation.customer.personalDetails.hasOwnProperty('inFix')) {
        return userInformation.customer.personalDetails.inFix
    } else {
        return ""
    }
}

const displayDateOfBirth = (userInformation) => {
    let dateOfBirth = (userInformation.customer.identifyingInformation.dateOfBirth.toString()).split(",")
    let displayDate = new Date(dateOfBirth[0], dateOfBirth[1] - 1, dateOfBirth[2]);
    return displayDate.toLocaleDateString()
}

const displayFullAddress = (userInformation) => {
    return userInformation.customer.address.street + " " + userInformation.customer.address.houseNumber + " " + userInformation.customer.address.houseNumberAddition
}

///////////////////////////////Laat wallet zien/////////////////////////////////////////////////////////////////////
const displayWallet = (userId) => {
    fetchAUTH("/admin/assets/" + userId, settingsAccess('GET'))
        .then(response =>
            response.json())
        .then(json => {
            let userWallet = json
            let codes = Object.keys(userWallet.assets_in_wallet)
            setThumbnailsUserWallet()
            setCurrentValues()
            fillAssetTable(setCurrentIds(codes), userWallet)
            document.getElementById('wallet_value').innerHTML =
                currencyFormatter("EUR").format(userWallet.total_value)
        })
        .catch((error) => {
            console.error('Wallet data niet geladen', error);
        })
}

function clearWalletTable() {
    while (walletTable.rows[0]) {
        walletTable.deleteRow(0);
    }
}

const idToCode = {
    "bitcoin": "BTC",
    "ethereum": "ETH",
    "ripple": "XRP",
    "eos": "EOS",
    "cardano": "ADA",
    "solana": "SOL",
    "avalanche-2": "AVAX",
    "polkadot": "DOT",
    "dogecoin": "DOGE",
    "monero": "XMR",
    "matic-network": "MATIC",
    "crypto-com-chain": "CRO",
    "cosmos": "ATOM",
    "litecoin": "LTC",
    "near": "NEAR",
    "chainlink": "LINK",
    "uniswap": "UNI",
    "tron": "TRX",
    "ftx-token": "FTT",
    "algorand": "ALGO"
}

const codeToId = {
    "BTC": "bitcoin",
    "ETH": "ethereum",
    "XRP": "ripple",
    "EOS": "eos",
    "ADA": "cardano",
    "SOL": "solana",
    "AVAX": "avalanche-2",
    "DOT": "polkadot",
    "DOGE": "dogecoin",
    "XMR": "monero",
    "MATIC": "matic-network",
    "CRO": "crypto-com-chain",
    "ATOM": "cosmos",
    "LTC": "litecoin",
    "NEAR": "near",
    "LINK": "chainlink",
    "UNI": "uniswap",
    "TRX": "tron",
    "FTT": "ftx-token",
    "ALGO": "algorand"
}
const thumbs = {}
const currentValues = {}

const fillAssetTable = (arr, userWallet) => {
    arr.forEach(element => setCoinDataForUserWallet(element, userWallet))
}

const addToCurrentValue = (id, object) => {
    currentValues[id] = object
}

const setCurrentValues = () => {
    if (isEmpty(currentValues)) {
        let idList = Object.keys(idToCode)
        idList.forEach(id => {
            let getString = `${internalAssetRateEndPoint}${id}`
            fetch(getString, {
                method: 'GET',
                headers: {},
            })
                .then(response => {
                        return response.json()
                    }
                )
                .then(json => {
                    addToCurrentValue([id], json.current_price)
                })
                .catch((error) => {
                    console.error('Foutje', error);
                });
        })
    }
}

const isEmpty = (obj) => {
    return Object.keys(obj).length === 0;
}

const setThumbnailsUserWallet = () => {
    if (isEmpty(thumbs)) {
        let idList = Object.keys(idToCode)
        idList.forEach(id => {
            let getString = `${coinData}${id}`
            fetch(getString, {
                method: 'GET'
            })
                .then(response => {
                        return response.json()
                    }
                )
                .then(json => {
                    thumbs[id] = json.image.thumb;
                })
                .catch((error) => {
                    console.error('Probleem bij het ophalen van de thumbnails', error);
                });
        })
    }
}

let setCurrentIds = (listOfCodes) => {
    let currentIds = []
    listOfCodes.forEach(code => {
        currentIds.push(codeToId[code])
    })
    return currentIds
}

const walletTable = document.getElementById('table-body')

const setCoinDataForUserWallet = (id, userWallet) => {
    let getString = `${coinData}${id}`
    fetch(getString, {
        method: 'GET',
        headers: {},
    })
        .then(response => {
                return response.json()
            }
        )
        .then(json => {
            let newRow = document.createElement('tr')
            /////////////////cell1////////////////////
            let newCell1 = document.createElement('td')
            let thumb = document.createElement('img')
            thumb.src = thumbs[id]
            newCell1.appendChild(thumb)
            newRow.appendChild(newCell1)
            ////////////////cell2////////////////////
            let newCell2 = document.createElement('td')
            let code = idToCode[id]
            newCell2.innerHTML = code
            newRow.appendChild(newCell2)
            /////////////////cell3//////////////////
            let newCell3 = document.createElement('td')
            let amount = userWallet.assets_in_wallet[code]
            newCell3.innerHTML = amount
            newRow.appendChild(newCell3)
            /////////////////cell4/////////////////
            let newCell4 = document.createElement('td')
            //get latest data from own db
            let currentPrice = currentValues[id]
            newCell4.innerHTML = currencyFormatter("EUR").format(currentPrice)
            newRow.appendChild(newCell4)
            /////////////////cell5/////////////////
            let newCell5 = document.createElement('td')
            let totalValue = currentPrice * amount
            newCell5.innerHTML = currencyFormatter("EUR").format(totalValue)
            newRow.appendChild(newCell5)

            walletTable.appendChild(newRow)
        })
        .catch((error) => {
            console.error('Probleem bij het vullen van de tabel', error);
        });
}

///////////////////////////////Haal nieuwe gebruiker op ////////////////////////////////////////////////////////////


document.getElementById('search_input').onchange = function () {
    processInput()
};

function processInput() {
    let searchInput = document.getElementById('search_input').value
    if (parseInt(searchInput)) {
        if (parseInt(searchInput) > 0) {
            getUserInformationById(parseInt(searchInput))
        } else {
            alert("Vul een geldige userId in")
        }
    } else {
        let userName = searchInput.toString()
        checkUserName(userName)
    }
}

function checkUserName(searchInput) {
    let maxlength = 320
    let pattern = "[^@\s]+@[^@\s]+\.[^@\s]+"
    let regex = new RegExp(pattern);
    if (regex.test(
        searchInput.trim()) && searchInput.length <= maxlength) {
        getUserInformationByUserName(searchInput)
    } else {
        alert("Vul een geldige gebruikersnaam in")
    }
}

const clearUserSearchInputField = () => {
    document.getElementById('search_input').value = null;
}

/////////////////////////////////////////////Get transactiepercentage/////////////////////////////
const showCurrentPercentage = (string) => {
    document.getElementById(`percentage`).innerHTML = numberFormatter(string) + "%"
}

function numberFormatter(string) {
    let localeString = string.toLocaleString()
    return (parseFloat(localeString) * 100).toFixed(2)
}

async function getCurrentTransactionPercentage() {
    await fetchAUTH('/admin/transactionfee', settingsAccess('GET'))
        .then(response =>
            response.json())
        .then(data => {
            showCurrentPercentage(data.toString())
        })
        .catch((error) => {
            console.error('Percentage niet uit de database ontvangen', error);
        });
}

/////////////////////////////////////////////Put transactiepercentage///////////////////////////////////
const clearTransactionCostInputField = () => {
    document.getElementById('transaction_fee').value = null;
}

function putNewTransactionPercentage(newPercentage) {
    fetchAUTH("/admin/newtransactionfee", settingsAccess('PUT', newPercentage))
        .then(response => {
            response.text()
        })
        .then(() => {
            alert('Het percentage is gewijzigd')
            getCurrentTransactionPercentage()
            clearTransactionCostInputField()
        })
        .catch(error => {
            console.log(error, 'Percentage is niet gewijzigd')
        });
}

const makePercentage = () => {
    let inputPercentage = document.getElementById('transaction_fee').value
    if (inputPercentage > 0 && inputPercentage < 100) {
        let updatePercentage = inputPercentage / 100
        putNewTransactionPercentage(updatePercentage)
    } else {
        alert("Voer een geldig percentage in tussen 0.1 en 100%")
    }
}

const btn_aanpassenperc = document.getElementById('btn_aanpassenperc')
btn_aanpassenperc.addEventListener('click', makePercentage)
document.getElementById('transaction_fee').addEventListener('keypress', function (e){
    if (e.key === 'Enter') {
        e.preventDefault()
    }
});

/////////////////////////////////////////////Calls/////////////////////////////////////////////////
getCurrentTransactionPercentage()
getUserInformationById(1000)

