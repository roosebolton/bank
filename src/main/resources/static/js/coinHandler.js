import {getCoinFromLocal, setCoin, setCoinAbbr, setCurrentCoinValue} from './modules/coinSelect.js';
import {getCurrency, getCurrencySign, getCurrencyIcon} from "./modules/currencySelect.js";
import {drawChart} from "./tradePageChartRender.js";
import {settingsAccess, fetchAUTH, getAccesstoken, getRefreshtoken} from "./modules/authModule.js";
import {getLocationUrl, getUrl} from "./modules/routing.js";
import {getDropdown, setCoinPriceTextValues, setSelectedCoinAbbreviation} from "./tradePage.js";

const checkLocalStorage = () => {
if (window.localStorage.getItem('coin') == null || window.localStorage.getItem('coin') == undefined ) {
    window.localStorage.setItem('coin', 'bitcoin');
    window.localStorage.setItem('coinAbbr', 'BTC');
        }
}

checkLocalStorage()
const coinListUrl = getLocationUrl()+'coins/fulllist';
const page_container = document.querySelector('#page_container');
const loading_icon = document.querySelector('#loading_icon');
let coinDataUrl = getUrl("coinAPI") + getCoinFromLocal() + "?valuta=" + getCurrency();

const asyncLoad = () => {
    Promise.all([fetchAUTH(coinListUrl, settingsAccess("GET")), fetchAUTH(coinDataUrl, settingsAccess("GET"))])
        .then(function (response) {
            if (response[0].status !== 200 || response[1].status !== 200) {
                console.warn('Looks like there was a problem. Status Code: ' +
                    response.status);
                return;
            }
            response[0].json().then(data => resolveCoinListdata(data))
            response[1].json().then(data => resolveCoindata(data))
            setLoadingFalse()
        })
}


const setLoadingFalse = () => {
    loading_icon.style.display  = 'none';
    page_container.style.display = 'block';
}

// currency data
let currentCoinValue = document.getElementById("currentCoinValue");

const fetchCoinData = () => {
    let coinDataUrl = getUrl("coinAPI") + getCoinFromLocal() + "?valuta=" + getCurrency();

    fetchAUTH(coinDataUrl,settingsAccess("GET"))
        .then(response => response.json())
        .then(json => resolveCoindata(json))
        .catch(error => console.log(error));
}

const setCoinValue = (value) => {
    setCurrentCoinValue(value)
    currentCoinValue.innerText = getCurrencySign(getCurrency())+" "+ value.toFixed(2)
    setCoinPriceTextValues();
}

    /* resolves and sets coinlist from rest Api  */
    const resolveCoinListdata = (data) => {
        let option;
        for (let i = 0; i < data.length; i++) {
            const {name, abbreviation } = data[i]
            option = document.createElement('option');
            option.text = abbreviation;
            option.value = name;
            getDropdown().add(option);
        }
    }

    /* resolves and sets coindata + initiates chart render from rest Api  */
    const resolveCoindata = (data) => {
        // destructure data to get current
        const {current_price: current_price} = data;
        setCoinValue(current_price);
        const {name: coin, abbreviation: abbreviation} = data;
        setCoin(coin)
        setCoinAbbr(abbreviation)
        setSelectedCoinAbbreviation(abbreviation)
        // destructure data get price history array -
        const {pricehistory: pricehistorydata} = data;
        // initiate draw chart function wtih
        drawChart(pricehistorydata)
    }

    // initiate async load
    asyncLoad();

export {fetchCoinData}



