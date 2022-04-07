'use strict'

import {getUrl} from "./modules/routing.js";
import {checkAuthLoginRedirect, getAccesstoken, logout, isAdmin,settingsAccess} from "./modules/authModule.js";
import {fetchAUTH} from "./modules/authModule.js";
import {idToCode} from "./modules/idToCodeModule.js";


/////////////////////////////////////////////constants///////////////////////////////////////////
const coinData = `https://api.coingecko.com/api/v3/coins/`
const internalAssetDataEndPoint = getUrl("assets")
const internalAcountDataEndPoint = getUrl("account")
const internalHistoryDataEndPoint = getUrl("history")
const internalAssetRateEndPoint = getUrl("coinAPI");


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

checkAuthLoginRedirect();
document.querySelector("#logout").addEventListener("click",logout)
//////////////////////////////Functions////////////////////////////////////////////////////

/**
 * Checks if user had adminrights. If so, shows adminpage button in navbar
 */
const accesCheck= () =>{
    let root = isAdmin(getAccesstoken());
    if((root!=null)&&root){
        document.getElementById("adminButton").style.visibility = "visible";
    }
}

/**
 * Checks if an object is empty
 * @param obj
 * @returns {boolean}
 */
const isEmpty = (obj) => {
    return Object.keys(obj).length === 0;
}

//Gets all 20 asset thumbnails from coingecko, if thumbs is empty
const setThumbnails = () => {
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
                    console.error('Foutje', error);
                });
        })
    }
}


/**
 * Fills the currentIds array with all current userIds
 * @returns all current assetIds in this users wallet
 */
const setCurrentIds = (listOfCodes) => {
    let currentIds = []
    listOfCodes.forEach(code => {
        currentIds.push(codeToId[code])
    })
    return currentIds
}

/**
 * Fills the assetTable with the current user wallet data
 * @param currentIds
 */
const fillAssetTable = (arr, customerData) => {
    arr.forEach(element => setCoinDataForId(element, customerData))
}


/**
 * Fills the currentvalues object with the current values for the assets in the customers wallet
 * @param id
 * @param object
 */
const addToCurrentValue = (id, object) => {
    currentValues[id] = object
}

/**
 * Fetches the assetRate information for all customers assets to extract current values from, to use with addToCurrentValue
 */
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

/**
 * Sets content of returnedc tablecell
 * @param cellContent
 * @returns {*}
 */
const returnFullCell = (cellContent) => {
    let newCell = document.createElement(`td`)
    return newCell.appendChild(cellContent)
}

/**
 * Sets innerhtml in returned tablecell
 * @param innerHtml
 * @returns {HTMLTableCellElement}
 */
const returnFullCellWithText = (innerHtml) => {
    let newCell = document.createElement(`td`)
    newCell.innerHTML = innerHtml
    return newCell
}

/**
 * Fills one row with the relevant user data
 * @param The asset id, as used in coingecko
 */
const setCoinDataForId = (id, customerData) => {
            let newRow = document.createElement('tr')
            let thumb = document.createElement('img')
            thumb.src = thumbs[id]
            newRow.appendChild(returnFullCell(thumb))
            newRow.appendChild(returnFullCell(returnFullCellWithText(idToCode[id])))
            let coinAmount = customerData.assets_in_wallet[idToCode[id]]
            newRow.appendChild(returnFullCell(returnFullCellWithText(coinAmount)))
            newRow.appendChild(returnFullCell(returnFullCellWithText(currencyFormatter("EUR").format(currentValues[id]))))
            newRow.appendChild(returnFullCell(returnFullCellWithText(currencyFormatter("EUR").format(currentValues[id] * coinAmount))))
            document.getElementById("table-body").appendChild(newRow)
}


/**
 * Based on givven currency, sets formatter for dot,comma,currency sign representation of given currency
 * @param currentCurrency
 * @returns {Intl.NumberFormat}
 */
//https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/NumberFormat
const currencyFormatter = (currentCurrency) =>
        new Intl.NumberFormat('nl-NE', {
            style: 'currency',
            currency: currentCurrency,
        });


/**
 * Uses currencyFormatter to propperly format given money value for wallet
 * @param value
 */
const changeWalletValue = (value) => {
    document.getElementById("walletValue").innerHTML =
        currencyFormatter("EUR")
            .format(value)
}

/**
 * Uses currencyFormatter to propperly format given money value for account
 * @param value
 */
const changeAccountValue = (value) => {
    document.getElementById("accountvalue").innerHTML =
        currencyFormatter("EUR")
            .format(value)
}


/**
 * Gets the current user account data from the account edpoint and assigns it to the accountvalue attribute
 */
const setAccountValue = () => {
    fetchAUTH(internalAcountDataEndPoint,settingsAccess('GET'))
        .then(response => {
            response.json()
                .then(json => {
                    changeAccountValue(json.balance)
                })
                .catch((error) => {
                    console.error('Foutje', error);
                });
        })
}

/**
 * Gets the current user asset data from the assets edpoint and assigns it to the currenUserWalletData attribute
 */
const setCurrentUserData = () => {
    fetchAUTH(internalAssetDataEndPoint,settingsAccess('GET'))
        .then(response => {
            response.json()
                .then(json => {
                    let currentUserData = json
                    let codes = Object.keys(currentUserData.assets_in_wallet)
                    changeWalletValue(currentUserData.total_value)
                    fillAssetTable(setCurrentIds(codes), currentUserData)
                })
                .catch((error) => {
                    console.error('Foutje', error);
                });
        })
}


//////////////////////////////////// graph logic ///////////////////////////////////////////////////

/**
 * Gets the string belonging to today minus minusDays so woensdag 30 maart - 1 = dinsdag 29 maart
 * @param minusDays
 * @returns {string}
 */
const getDayString = (minusDays) => {
    var today = new Date();
    var nextweek = new Date(today.getFullYear(), today.getMonth(), today.getDate() - minusDays);
    const options = {weekday: 'long', month: 'long', year: 'numeric', day: 'numeric'};
    return nextweek.toLocaleDateString('nl-NL', options);
}

//array to store all days, one year back, as Strings in Dutch
let yearDayStringList = []

/**
 *fills the yearStringList for 366 days max
 */
const fillYearDayStringLists = () => {
    for (let i = 0; i < 365; i++) {
        yearDayStringList.push(getDayString(i))
    }
}

//variable to store selected timeframe in
let selectedTimeFrame;

/**
 * Eventlistener that gets changes in selected timeframe and plots a new graph accordingly
 */
document.getElementById("timeList").addEventListener('click', function (e) {
    selectedTimeFrame = e.target.innerText;
    console.log("Gekozen: " + selectedTimeFrame)
    plot()
})

/**
 * Plots the graph depending on selected timeframe on walletpage
 */
const plot = () =>{
    fetch(internalHistoryDataEndPoint, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${getAccesstoken()}`,
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            response.json()
                .then(json => {
                    let returnList = []
                    let timeStamps = Object.keys(json.walletHistory)
                    timeStamps.forEach(timeStamp => {
                        returnList.push(json.walletHistory[timeStamp])
                    })
                    if (selectedTimeFrame === "Maand") {
                        plotMonth(returnList.slice(0, 30))
                    } else if (selectedTimeFrame === "Jaar") {
                        plotYear(returnList.slice(0, 366))
                    } else {
                        plotWeek(returnList.slice(0, 8))
                    }
                })
                .catch((error) => {
                    console.error('Foutje', error);
                });
        })
}

/**
 * Lets the graph be plotted given a certain timeframe.
 * @param list
 * @param timeFrame
 */
const plotTimeFrame = (list, timeFrame) => {
    let dateList = []
    for (let i = 0; i < timeFrame; i++) {
        dateList.push(list[i])
    }
    myChart.data.datasets[0].data = dateList
    let dateStringList = yearDayStringList.slice(0, timeFrame)
    dateStringList.reverse()
    myChart.data.labels = dateStringList
    myChart.update()
}

/**
 * Plots a graph for the past month
 * @param list
 */
const plotMonth = (list) => {
    plotTimeFrame(list, 30)
}

/**
 * Plots a graph for the past year
 * @param list
 */
const plotYear = (list) => {
    plotTimeFrame(list, 365)
}

/**
 * Plots a graph for the past 8 days
 * @param list
 */
const plotWeek = (list) => {
    plotTimeFrame(list, 8)
}




///////////////////////////////////Calls////////////////////////////////////////////////////////

accesCheck()

//call to setThumbnails to get all 20 asset thumbnails
setThumbnails()

//call to set the current asses values for all assets in the customers wallet
setCurrentValues()

//call to setAccountValue to set the current accountvalue
setAccountValue()

// calls currentUserData and thereby sets all data in table and total wallet value
setCurrentUserData()

//call to set all days of past year in Strings in Dutch
fillYearDayStringLists()

//plots the graph
plot()

/////////////////////////////////////////// Graph////////////////////////////////////////////////
let ctx = document.getElementById('myChart')
// eslint-disable-next-line no-unused-vars
const myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: yearDayStringList,
        datasets: [{
            data: []
            ,
            lineTension: 0,
            backgroundColor: 'transparent',
            borderColor: '#0051FF',
            borderWidth: 4,
            pointBackgroundColor: '#0051FF'
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: false
                },
                scaleLabel: {
                    display: true,
                    labelString: '\u20AC',
                }
            }]
        },
        legend: {
            display: false
        }
    }
})

export {idToCode};