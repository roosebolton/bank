/* globals Chart:false, feather:false */

(function () {
    'use strict'


    /////////////////////////////////////////////constants///////////////////////////////////////////
    const coinData = `https://api.coingecko.com/api/v3/coins/`

    const idToCode = {
        "bitcoin": "BTC",
        "ethereum":"ETH",
        "ripple":"XRP",
        "eos":"EOS",
        "cardano":"ADA",
        "solana":"SOL",
        "avalanche-2":"AVAX",
        "polkadot":"DOT",
        "dogecoin":"DOGE",
        "monero":"XMR",
        "matic-network":"MATIC",
        "crypto-com-chain":"CRO",
        "cosmos":"ATOM",
        "litecoin":"LTC",
        "near":"NEAR",
        "chainlink":"LINK",
        "uniswap":"UNI",
        "tron":"TRX",
        "ftx-token":"FTT",
        "algorand":"ALGO"
    }

    //////////////////////////////Functions////////////////////////////////////////////////////


    /**
     * Fills the assetTable with the current user wallet data
     * @param currentIds
     */
    let fillAssetTable = ()=>{
        let idList = Object.keys(idToCode)
        console.log(idList)
        Object.keys(idToCode).forEach(element => setCoinDataForId(element))
    }

    /**
     * Fills one row with the relevant user data
     * @param The asset id, as used in coingecko
     */
    let setCoinDataForId = (id)=>{
        let getString = `${coinData}${id}`
        fetch(getString, {
            method: 'GET',
            headers: {
            },
        })
            .then(response => {
                console.log(response)
                return response.json() }
            )
            .then(json => {
                let newRow = document.createElement('tr')
                /////////////////cell1////////////////////
                let newCell1 = document.createElement('td')
                let thumb = document.createElement('img')
                thumb.src = json.image.thumb
                newCell1.appendChild(thumb)
                newRow.appendChild(newCell1)
                ////////////////cell2////////////////////
                let newCell2 = document.createElement('td')
                newCell2.innerHTML= json.name
                newRow.appendChild(newCell2)
                /////////////////cell3//////////////////
                let newCell4 = document.createElement('td')
                let currentPrice = json.market_data.current_price.eur
                newCell4.innerHTML= currencyFormatter("EUR").format(currentPrice)
                newRow.appendChild(newCell4)

                document.getElementById("table-body").appendChild(newRow)
            })
            .catch((error) => {
                console.error('Foutje', error);
            });
    }

    /**
     * Based on givven currency, sets formatter for dot,comma,currency sign representation of given currency
     * @param currentCurrency
     * @returns {Intl.NumberFormat}
     */
        //https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/NumberFormat
    let currencyFormatter = (currentCurrency) =>
            new Intl.NumberFormat('nl-NE', {
                style: 'currency',
                currency: currentCurrency,
            });



    ///////////////////////////////////Calls////////////////////////////////////////////////////////
    fillAssetTable()

})()