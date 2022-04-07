/*
Module for enabling currency selection and remembering preferred currency throughout the application
uses localstorage
 */
if (window.localStorage.getItem('currency') === null) {
    window.localStorage.setItem('currency', 'EUR');
}

let selectedCurrency;

const currency_signs = new Map ( [
    ["EUR",'€'],["USD",'$'],["POUND",'£']]
)

const currency_icon_classname = new Map ( [
    ["EUR","fa-solid fa-euro-sign"],["USD","fa-solid fa-dollar-sign"],["pound","fa-solid fa-sterling-sign"]]
)

const getCurrencySign = (currency) => {
    return currency_signs.get(currency);
}

const getCurrencyIcon = (currency) => {
   return  currency_icon_classname.get(currency);
}

const setCurrency = (e) => {
    window.localStorage.setItem('currency', e);
    selectedCurrency = e;
}

const getCurrency = () => {
    return selectedCurrency
}

const getCurrencyFromLocal = () => {
    return window.localStorage.getItem('currency');
}

selectedCurrency = getCurrencyFromLocal();

// linking currency dropdown navbar with
    document.getElementById("selectedCurrency").innerText= currency_signs.get(getCurrencyFromLocal().toString());

    document.getElementById("dropdown_euro").addEventListener("click",  function () {
        setCurrency('EUR');
        document.location.reload()

    });

    document.getElementById("dropdown_dollar").addEventListener("click",  function () {
        setCurrency("USD")
        document.location.reload()

    });


export { getCurrency, getCurrencyFromLocal, getCurrencyIcon, getCurrencySign};
