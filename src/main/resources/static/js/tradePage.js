'use strict'
import {getCurrency, getCurrencyIcon, getCurrencySign} from "./modules/currencySelect.js";
import {
    checkAuthLoginRedirect,
    fetchAUTH,
    getAccesstoken,
    getUserId, logout,
    settingsAccess
} from "./modules/authModule.js";
import {getUrl} from "./modules/routing.js";
import {
    getCoin,
    getCoinAbbr,
    getCoinAbbrFromLocal,
    getCoinFromLocal,
    getCurrentCoinValue,
    setCoin
} from "./modules/coinSelect.js";
import {fetchCoinData} from "./coinHandler.js";

let input_amount_coin = document.querySelector("#input_amount")
let input_amount_valuta = document.querySelector("#input_valuta")
const btn_trade = document.querySelector(`#trade_button`)
const btn_select_buysell_buy = document.querySelector(`#btn_select_buysell_buy`)
const btn_select_buysell_sell = document.querySelector(`#btn_select_buysell_sell`)
/* modal  */
const modal = document.querySelector(`.modal`)
const modalSuccess = document.querySelector(`.modal_success`)
const modalFailure = document.querySelector(`.modal_failure`)
let tradeInfoConfirmation = document.querySelector(`.trade_info_confirmation`)
document.querySelector(`.modal_close`).addEventListener(`click`,e=>hideModal())

let currentCurrencyIcon = document.querySelector("#trade_currency_icon").className = getCurrencyIcon(getCurrency());

document.querySelector("#logout").addEventListener("click",logout)

/*  boolean for buy or sell event */
let buyEvent = true;

/* id for error message  */
let error_messages = document.querySelector("#error_messages");

//<--- coin selection dropdown -->
const dropdown = document.getElementById('coinSelectDropdown');

checkAuthLoginRedirect()

/*setting up standard value for dropdown element  */
    const setupDropdownDefault = () => {
        dropdown.length = 0;
        let defaultOption = document.createElement('option');
            defaultOption.value = getCoinFromLocal();
            defaultOption.text = getCoinAbbrFromLocal();
        dropdown.add(defaultOption);
        dropdown.selectedIndex = 0;
    }

    const getDropdown = () => {
        return dropdown;
    }

    const updateSelectedCoin = () => {
        let value = dropdown.options[dropdown.selectedIndex].value;
        let text = dropdown.options[dropdown.selectedIndex].text;
        setCoin(value)
        setSelectedCoinAbbreviation(text)
        fetchCoinData()
        clearInputField()
        setCoinPriceTextValues()
        selectOrder()
    }




    const selected_coin_abbr = document.querySelector(`.selected_coin_abbr`)
    const setSelectedCoinAbbreviation = (text) => {
        selected_coin_abbr.innerHTML = text;
    }

    /*  evenlistener for dropdownelement, trigeers when a change happens   */
    dropdown.addEventListener("change",updateSelectedCoin);

    /*  setting buy and sell state  */
    const setButtonSelectBuySellactive = (e) =>{
        let buttons = document.getElementsByClassName("btn_select_buysell")
        for (let i = 0; i < buttons.length; i++) {
            buttons[i].classList.remove("btn_select_buysell_active")
        }
        e.target.classList.add("btn_select_buysell_active")
        btn_trade.innerHTML= e.target.innerHTML;
        // change boolean buy or sell
    }

    // highlight focus within
input_amount_valuta.addEventListener("focus",e => setValutaStylngFocusWithin())
input_amount_valuta.addEventListener("focusout",e => setValutaStylngFocusOut())

const valuta_container = document.querySelector(".valuta_container")
const setValutaStylngFocusWithin = () => { valuta_container.style.backgroundColor = "var(--primary-color-light)";}
const setValutaStylngFocusOut = () => {valuta_container.style.backgroundColor = "var(--medium-grey)";}

input_amount_coin.addEventListener("focus",e => setCoinStylngFocusWithin())
input_amount_coin.addEventListener("focusout",e => setCoinStylngFocusOut())

const coin_container = document.querySelector("#coin_container")
const setCoinStylngFocusWithin = () => { coin_container.style.backgroundColor = "var(--primary-color-light)";}
const setCoinStylngFocusOut = () => { coin_container.style.backgroundColor = "var(--medium-grey)";}

    const changeTextToBuy = () => {
        text_valuta_payment.innerHTML="U betaalt:"
        text_coin_payment.innerHTML="U ontvangt ongeveer:"
    }

    const changeTextToSell = () => {
        text_valuta_payment.innerHTML="U ontvangt ongeveer:"
        text_coin_payment.innerHTML="U verkoopt:"
    }

    const setBuy = () => {
        changeTextToBuy()
        buyEvent=true;
    }

    const setSell = () => {
        buyEvent=false;
        changeTextToSell()
    }

    btn_select_buysell_buy.addEventListener("click",setBuy)
    btn_select_buysell_sell.addEventListener("click",setSell)

    /*   Setting saldo balance of user with a  fetch   */
    const saldo_user_text = document.querySelector(`#saldo_user`)

    fetchAUTH(getUrl("account"),settingsAccess("GET"))
    .then(response => response.json())
    .then(json => setBalance(json))
    .catch(error => console.log(error));

    const setBalance = (data) => {
        let {balance: balance} = data;
        saldo_user_text.innerHTML = balance + " Euro";
    }

    /* calculating amount*/
    const getPreferredLimitAmount = () => {
        return currency_value_select.value;
    }

    const updateCoinAmountFromValutaAmount = ()  => {
        let valuta_value = input_amount_valuta.value
        if (valuta_value != 0 || getPreferredLimitAmount() != 0) {
        input_amount_coin.value= valuta_value/getPreferredLimitAmount();
        }
        setTransactionsCostsToText()
    }

    const updateValutaFromCoinmountAmount = () => {
        let amount_coin = input_amount_coin.value;
        if (amount_coin != 0 || getPreferredLimitAmount() != 0) {
            input_amount_valuta.value = (amount_coin*getPreferredLimitAmount());
        }
        setTransactionsCostsToText()
    }

    const clearInputField = () => {
        currency_value_select.value=getCurrentCoinValue();
        currency_value_select.placeholder=getCurrentCoinValue();
        input_amount_coin.value = null;
        input_amount_valuta.value = null;
    }

     const disableTradeButton = () => {
        btn_trade.disabled = true;
    }

    /* functions for preparing trade and trading   */
    const makeTrade = () => {
        if (checkInputFields()) {
            // fetch latest
            modalSuccess.style.display = "flex"
            updateValutaFromCoinmountAmount()
            setConfirmationInfoText()
            showModal()
        }
    }

    const setConfirmationInfoText = () => {
        tradeInfoConfirmation.innerHTML =
            `U wilt <strong> ${input_amount_coin.value} ${getCoin()}</strong> voor <strong>${input_amount_valuta.value}
              ${getCurrencySign(getCurrency())}</strong>  ${buyEvent ? `kopen` : `verkopen`}.`
    }
   const checkInputFields = () => {
     removeErrorMessage()
       if (input_amount_coin.value == 0) {
           createErrorMessage()
           return false;
        } return true
    }

    const createErrorMessage = () => {
        let li = document.createElement('li');
        li.textContent = "Voer een hoeveelheid in";
        li.style.fontSize="14px";
        error_messages.appendChild(li);
        error_messages.style.display = "block";
    }

    /* removes previous error messages */
    const removeErrorMessage = () => {
        while(error_messages.lastElementChild) {
            error_messages.removeChild((error_messages.lastElementChild));
        }
        error_messages.style.display = "none";
    }

    const prepareMarketOrderTrade = () => {
    // direct market order
        let userId = getUserId(getAccesstoken())
        let trade = {
            buyer: getBuyer(userId),
            seller: getSeller(userId),
            assetname:getCoin(),
            assetamount: input_amount_coin.value,
            value: 0
        }
        return trade;
    }

    const prepareLimitOrderTrade = () => {
        let trade = {
            userId: getUserId(getAccesstoken()),
            buy: buyEvent,
            asset: getCoin(),
            amount: input_amount_coin.value,
            price: getPreferredLimitAmount(),
            valutaType: getCurrency()
        }
        return trade;
    }

    const prepareLimitOrderInternalMarketTrade = () => {

        let trade = {
            userId: getUserId(getAccesstoken()),
            buy: buyEvent,
            asset: getCoin(),
            amount: input_amount_coin.value,
            price: getPreferredLimitAmount(),
            valutaType: getCurrency()
        }

        console.log(trade)
        return trade;
    }

    const getTradeObject = () => {
        if (order_select.value === "marketorder") {
            return prepareMarketOrderTrade()
        }
        if (order_select.value === "limitorder") {
            return prepareLimitOrderTrade()
        }
        if (order_select.value === "limitorderinternal") {
            return prepareLimitOrderInternalMarketTrade();
        }
    }


const getMarketUrl = () => {
        if (order_select.value === "marketorder" || order_select.value === "limitorder") {
            return getUrl("tradeAPI")
        }
        if (order_select.value === "limitorderinternal") {
            return getUrl("marketTradeAPI")
        }
}

const sendTrade = () => {
    hideModal()
    fetchAUTH(getMarketUrl(),settingsAccess('POST', getTradeObject()))
        .then(response => response.text())
        .then(text => {
            console.log(text)
            parseSuccesTransactionResponse(text)
        })
        .catch(error =>  error.text()).then(errortext => {
            try {
                const data = JSON.parse(errortext);
                parseJSONErrorMessage(data)
                // Try to parse the response as JSON
                // The response was a JSON object
                // Do your JSON handling here
            } catch (err) {
                parseNormalErrorMessage(err)
                // The response wasn't a JSON object
                // Do your text handling here
            }
        })
        clearInputField()
}

const parseJSONErrorMessage = (json) => {
        deletePreviousErrormessage()
        let errorMessage = json

    if(errorMessage.hasOwnProperty('message')) {
        const { message: message } =errorMessage
        createErrorMessages(message)
    }

    if (isIterable(errorMessage)) {
        let offset = 5;
        for (const errorText of errorMessage) {
            offset = offset + 45;
            createErrorMessages(errorText, offset);
        }
    }
}

const isIterable = (value) => {
    return Symbol.iterator in Object(value);
}

const parseNormalErrorMessage = (data) => {
        console.log(data)
        let errormsg = data.toString();
        if (errormsg.includes("order")||errormsg.includes("geslaagd")
            ||errormsg.includes("portefeuille")||errormsg.includes("Authorisatie")) {
            createErrorMessages(errormsg, 5);
            // } else {
            //     let order= "Order niet geslaagd, probeer opnieuw";
            //     createErrorMessages(order,5)
            // }
        }
}

const deletePreviousErrormessage = () => {
       let errormessages = document.querySelectorAll(".confirmation_server_modal")
    errormessages.forEach( error => {
        error.remove();
    })
}

const createErrorMessages = (text,offset) => {
    setTimeout(deletePreviousErrormessage, 2000)
    createConfirmationFromServerModal(text,false,offset)
}

    const getBuyer = (userid) =>  buyEvent ? userid : 0;
    const getSeller = (userid) => !buyEvent ? userid : 0;

        // Hiding & showing of container elements on the webpage
        const showModal = () => modal.style.display = "block"
        const hideModal = () => modal.style.display = "none"

        const hideInnerContainerModal = () => {
            modalSuccess.style.display = "none"
            modalFailure.style.display = "none"
        }


/* adding eventlisteners  */
input_amount_valuta.addEventListener("input", updateCoinAmountFromValutaAmount)
input_amount_coin.addEventListener("input", updateValutaFromCoinmountAmount)
const buttonConfirmationTrade = document.querySelector(`#btn_confirmation`).addEventListener(`click`,sendTrade)

btn_trade.addEventListener("click", makeTrade )
document.querySelector('#btn_select_buysell_buy').addEventListener('click',setButtonSelectBuySellactive)
document.querySelector('#btn_select_buysell_sell').addEventListener('click',setButtonSelectBuySellactive)
setupDropdownDefault();

const order_select = document.querySelector(`.order_select`)
const currency_value_select = document.querySelector(`.currency_value_select`)

const selectOrder = () => {
    let selectedorder = order_select.options[order_select.selectedIndex].value;
    if(selectedorder === "limitorder"||selectedorder==="limitorderinternal") {
        currency_value_select.disabled=false
        highLightCurrencyValueSelect()
        showOrderTable()
        showOrderBookTable()
    }
    if(selectedorder === "marketorder") {
        normalCurrencyValueSelect();
        currency_value_select.disabled=true
       setCoinPriceTextValues()
       showTransactionTable()
    }
}


let limitOrderValue;

const setLimitOrderValue = () => {
    limitOrderValue = currency_value_select.value;
    updateCoinAmountFromValutaAmount()
    updateValutaFromCoinmountAmount()
}

const highLightCurrencyValueSelect = () => {
    currency_value_select.style.border = "1px solid";
    currency_value_select.style.borderColor = "var(--primary-color-light)";
}

const normalCurrencyValueSelect = () => {
    currency_value_select.style.border="none"
}

const setCoinPriceTextValues = () => {
    if(getCurrentCoinValue() != null|| getCurrentCoinValue() != undefined) {
        currency_value_select.value = getCurrentCoinValue();
        currency_value_select.placeholder = getCurrentCoinValue();
    }
}

let selected_valuta_abbr = document.querySelector(`.selected_valuta_abbr`);

const setCurrencyTextValues = () => {
    currentCurrencyIcon = getCurrencyIcon(getCurrency());
    selected_valuta_abbr.innerHTML = getCurrency()
}

setCurrencyTextValues()

//TransactionsFees
const text_transactionfee_payment = document.querySelector("#text_transactionfee_payment")

fetchAUTH(getUrl("tradeFeeAPI"),settingsAccess("GET"))
    .then(response => response.json())
    .then(json => setTransactionCost(json))
    .catch(error => console.log(error));

let transactionCostFee

const setTransactionCost = (transactionfee) => {
    if (transactionfee !== null) {
        transactionCostFee = transactionfee;
    }
}

const calculateTransactionCosts = () =>  (input_amount_valuta.value *transactionCostFee);

const setTransactionsCostsToText = () => {
    text_transactionfee_payment.innerHTML = getCurrencySign(getCurrency())+calculateTransactionCosts().toFixed(2)
}

// GraphContainer
const chartContainer = document.querySelector(".chart_container")

const hideChartContainer = () => chartContainer.style.display = "none";
const showChartContainer = () => chartContainer.style.display = "block";

//Order/transaction Containers
const transactionContainer = document.querySelector(".transaction_container")
const orderContainer = document.querySelector(".order_container")
const orderBookContainer = document.querySelector(".orderbook_container")

const hideOrderTable = () => orderContainer.style.display = "none";
const hideOrderBook = () => orderBookContainer.style.display = " none";
const hideTransactionTable = () => transactionContainer.style.display = "none";

// Clear Data from Tables
const clearTable = (tableBody) => {
    if (tableBody != null) {
        let child = tableBody.lastElementChild
        while(child) {
            tableBody.removeChild(child)
            child = tableBody.lastElementChild
        }
    }
}

//TRANSACTION TABLE
const transactionTable = document.querySelector(".transaction_table")
const tableHeadTransactionTable = transactionTable.querySelector("thead")

const getTransactionData = () => {
    fetchAUTH(getUrl("tradehistoryAPI"),settingsAccess("GET"))
        .then(response => response.json())
        .then(json => {
            setTransactionDataTable(json)}
        )
        .catch(error => console.log(error));
}

const showTransactionTable = () => {
    hideOrderTable()
    hideOrderBook()
    getTransactionData()
    showChartContainer()
    transactionContainer.style.display = "block";
}

showTransactionTable();

const setHeadersTransactionTable = () => {
    let headers =["Coin", "Aantal","Prijs(€)","Type","Datum afgesloten"]
    tableHeadTransactionTable.innerHTML = "<tr></tr>"
    tableHeadTransactionTable.style.margin="auto"

    for (const headerText of headers) {
        tableHeadTransactionTable.querySelector("tr").appendChild(makeHeaderElement(headerText));
    }
}

const transactionTableBody = transactionTable.querySelector("tbody")
transactionTableBody.innerHTML = "";

const setTransactionDataTable = (data) => {
    // clear old data if present
    clearTable(transactionTableBody)

    setHeadersTransactionTable()
    setRowTransactionTable(data)
}

const setRowTransactionTable = (data) => {
    for(const row of data) {
        const {  asset: {abbreviation: abbreviation}, assetAmount: assetamount, value: value, buyer: buyer, transactionDate: transactionDate} = row;
        let rowElement = document.createElement("tr")
        rowElement.appendChild(makeTableElement(abbreviation));
        rowElement.appendChild(makeTableElement(assetamount))
        rowElement.appendChild(makeTableElement(value))
        rowElement.appendChild(makeTableElement(checkBuyerorSeller(buyer)))
        rowElement.appendChild(makeTableElementDate(transactionDate))
        transactionTableBody.appendChild(rowElement)
    }
}

const checkBuyerorSeller = (buyer) => {
    if (getAccesstoken() != null) {
        return buyer.userId === getUserId(getAccesstoken()) ? "Aankoop" : "Verkoop";
    }
}

////Order table
const orderTable = document.querySelector(".order_table");
const orderTableBody = orderTable.querySelector("tbody")
orderTableBody.innerHTML = "";
const tableHeadOrderTable = orderTable.querySelector("thead")

const showOrderTable = () => {
    hideTransactionTable()
    hideChartContainer()
    orderContainer.style.display = "block";
    getOrderData();
}

const getOrderData = () => {
    fetchAUTH(getUrl("orderhistoryAPI"), settingsAccess("GET"))
        .then(response => response.json())
        .then(json => setOrderData(json))
        .catch(error => console.log(error));

}
const setHeadersOrderTable = () => {
    let headers =["Coin", "Aantal","Prijs(€)","Type","Datum geplaatst"]
    tableHeadOrderTable.innerHTML = "<tr></tr>"
    tableHeadOrderTable.style.margin="auto"

    for (const headerText of headers) {
        tableHeadOrderTable.querySelector("tr").appendChild(makeHeaderElement(headerText));
    }
}

const setOrderData = (data) => {
    // clear old data if present
    clearTable(orderTableBody)
    setHeadersOrderTable()
    setRowOrderTable(data)
}

const setRowOrderTable = (data) => {
    for(const row of data) {
        const {  asset: {abbreviation: abbreviation}, amount: amount, limitPrice: price, buy: buy, timeOrderPlaced: timeOrderPlaced } = row;
        const rowElement = document.createElement("tr")
        rowElement.appendChild(makeTableElement(abbreviation));
        rowElement.appendChild(makeTableElement(amount));
        rowElement.appendChild(makeTableElement(price))
        rowElement.appendChild(makeTableElementBuyOrSellOrder(buy));
        rowElement.appendChild(makeTableElementDate(timeOrderPlaced));
        orderTableBody.appendChild(rowElement)
    }
}

// End OrderTABLE
// Orderbook table
const orderBookHeader = document.querySelector(".orderbook_header");

// OrderBookBuy Selectors
const orderBookBuyTable = document.querySelector(".orderbookbuy_table");
const orderBookBuyTableBody = orderBookBuyTable.querySelector("tbody");
orderBookBuyTableBody.innerHTML = "";
const tableHeadBuyOrderBookTable = orderBookBuyTable.querySelector("thead")

//OrderBookSell Selectors
const orderBookSellTable = document.querySelector(".orderbooksell_table");
const orderBookSellTableBody = orderBookSellTable.querySelector("tbody");
orderBookSellTableBody.innerHTML = "";
const tableHeadSellOrderBookTable = orderBookSellTable.querySelector("thead")

const showOrderBookTable = () => {
    hideTransactionTable()
    hideChartContainer()
    orderBookContainer.style.display = "block";
    getOrderBookData();
}


const getOrderBookData = () => {
    fetchAUTH(createOrderBookUrl(), settingsAccess("GET"))
        .then(response => response.json())
        .then(json => setOrderBookData(json))
        .catch(error => console.log(error));
}

const createOrderBookUrl = () => {
   return getUrl("marketAPI")+"?id="+getCoin();
}

const setHeadersOrderBookTable = (tableHead) => {
    let headers =["Aantal","Prijs(€)"]
    tableHead.innerHTML = "<tr></tr>"
    tableHead.style.margin="auto"
    for (const headerText of headers) {
        tableHead.querySelector("tr").appendChild(makeHeaderElement(headerText));
    }
}

const setOrderBookData = (data) => {
    //clears old data
    const [ coinData ] = data
    const { asset: { abbreviation: abbreviation}} = coinData
    const { buyorders } = coinData;
    const { sellOrders } = coinData
    clearTable(orderBookBuyTableBody)
    clearTable(orderBookSellTableBody)
    // set OrderBook name
    setOrderBookSelectedName(abbreviation)
    setHeadersOrderBookTable(tableHeadBuyOrderBookTable)
    setHeadersOrderBookTable(tableHeadSellOrderBookTable)
    //setBuyORderbook
    setRowOrderBookTable(buyorders,orderBookBuyTableBody)
    //setSellOrderbook
    setRowOrderBookTable(sellOrders, orderBookSellTableBody)
}

const setOrderBookSelectedName = (abbreviation) => {
    orderBookHeader.innerHTML="Intern orderbook: <strong>"+abbreviation+"<strong>";
}

// same data different table -> send the corresponding
const setRowOrderBookTable = (orderListData, orderbookBody) => {
    for(const row of orderListData) {
        const {  amount: amount, limitPrice: price } = row;
        let rowElement = document.createElement("tr")
        rowElement.appendChild(makeTableElement(amount));
        rowElement.appendChild(makeTableElement(price))
        orderbookBody.appendChild(rowElement)
    }
}


//GENERIC TABLE MAKE ELEMENTS
const makeHeaderElement = (headerText) => {
    const headerElement = document.createElement("th");
    headerElement.textContent = headerText;
    headerElement.style.color="var(--dark-grey)";
    headerElement.style.fontWeight="600"
    headerElement.display = "flex";
    return headerElement;
}

const makeTableElement = (name) => {
    let cellElement = document.createElement("td")
    cellElement.textContent = name;
    return cellElement
}

const makeTableElementDate = (timeOrderPlaced) => {
    let cellTime = document.createElement("td")
    cellTime.textContent = new Date(timeOrderPlaced).toLocaleDateString("en-UK").replace(/\//g, '-');
    return cellTime
}

const makeTableElementBuyOrSellOrder = (typeOrder) => {
    let cellType = document.createElement("td")
    typeOrder === true ? cellType.textContent = "kopen" : cellType.textContent = "verkopen";
    return cellType;
}


const parseSuccesTransactionResponse = (text) => {
    if (text.includes("geslaagd")) {
        // create geslaagd modal
        createConfirmationFromServerModal(text,true)
        // set timeout update
       setTimeout(getTransactionData, 5000);
    } if (text.includes("Order")) {
        createConfirmationFromServerModal(text,true)
        // set timeout update
        setTimeout(getOrderBookData, 5000);
        setTimeout(getOrderData, 5000);
    }
}


/// Fetch respond
const createConfirmationFromServerModal = (message, success, offset) => {
    const confirmation = document.createElement ("button")
    const text = document.createElement("p")
    text.innerHTML = message;
    confirmation.appendChild(text)
    if (success == true) {
        confirmation.appendChild(setCheckIcon())
    } else {
        confirmation.appendChild(setErrorIcon())
    }
    let positioning =  50;
    confirmation.style.display = "flex"
    confirmation.id = 'confirmation';
    confirmation.className="confirmation_server_modal";
    confirmation.classList.add("conf_error_hidden");
    confirmation.style.position = "relative";
    confirmation.style.top = positioning+"px";
    confirmation.addEventListener("click", function (e){
        hideOnClick(e.target)
    })
    let container = document.querySelector(".price_container")

    container.appendChild(confirmation)
}
const setErrorIcon = () => {
    let icon = document.createElement('i');
    icon.className = "fa-solid fa-circle-xmark";
    icon.id = "error_icon";
    icon.style.color = "red"
    return icon;
}

const setCheckIcon = () => {
    let icon = document.createElement('i');
    icon.className = "fa-solid fa-circle-check";
    icon.id = "check_icon";
    icon.style.color = "green";
    return icon;
}

const hideOnClick = (e) => {
    // let confirmation = document.querySelector("#confirmation")
    e.style.display = "none";
}
order_select.addEventListener("change",selectOrder);
currency_value_select.addEventListener("input",setLimitOrderValue)
currency_value_select.addEventListener("input",highLightCurrencyValueSelect)

export {getDropdown, setCoinPriceTextValues,setSelectedCoinAbbreviation};
