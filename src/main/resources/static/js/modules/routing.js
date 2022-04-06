'use strict'
//Frontend routing
const locationUrl = "/";

const routingMap = new Map([
    ["loginPage","login/login.html"],
    ["walletPage", "wallet/wallet.html"],
    ["registerPage", "register/register.html"],
    ["assets", "portefeuille/assets"],
    ["history","portefeuille/history"],
    ["register","user/register"],
    ["account","account"],
    ["authenticate","users/authenticate"],
    ["refreshTokenAuthAPI","users/authenticate/refresh"],
    ["coinAPI","coins/"],
    ["tradeAPI","trade/"],
    ["tradeFeeAPI","trade/transactionfee"],
    ["tradehistoryAPI","trade/history"],
    ["marketTradeAPI","market/trade"],
    ["marketAPI","market"],
    ["orderhistoryAPI","market/orders"],
    ["adminPage","admin/admin.html"],
    ["adminGetTransactionFee","admin/transactionfee"],
    ["adminPutTransactionFee","admin/newTransactionfee" ],
    ["adminGetUserDataId","admin/userdata/"]

]);

const getLocationUrl = () => {
    return locationUrl;
}

const getUrl = (page) => {
    return getLocationUrl()+routingMap.get(page);
}

export {getLocationUrl, getUrl};