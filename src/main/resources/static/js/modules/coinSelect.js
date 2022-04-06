 /*
module for getting and selecting + saving coins throughout the application
uses localstorage to remember selected coins between pages
  */

 let selectedCoin;
 let selectedCoinAbbr;

 let currentCoinValue;

 // setting standard select

 const getCoinFromLocal = () => {
    return window.localStorage.getItem('coin');
}

const getCoinAbbrFromLocal = () => {
     return window.localStorage.getItem('coinAbbr');
}

const setCoin = (e) => {
    window.localStorage.setItem('coin', e);
    selectedCoin = e;
}

 const setCoinAbbr = (e) => {
     window.localStorage.setItem('coinAbbr', e);
     selectedCoinAbbr = e;
 }

 const getCoin = () => {
     return selectedCoin;
 }

 const getCoinAbbr = () => {
     return selectedCoinAbbr;
 }



const getCurrentCoinValue = () => {
    return currentCoinValue
}

const setCurrentCoinValue = (value) => {
        currentCoinValue =  value;
}

export { getCoin, getCoinFromLocal, setCoin,setCoinAbbr,getCoinAbbr, getCoinAbbrFromLocal, getCurrentCoinValue,setCurrentCoinValue };