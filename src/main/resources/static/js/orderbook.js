const demandTable = document.getElementById(`demand`)
const supplyTable = document.getElementById(`supply`)
let dropdown = document.getElementById(`cryptoDropdown`)
let currentTimer;
const refreshInterval = 5000;


//logica

//view klaarzetten, dropdown instellen
killGraphOpeningAnimation()
fetchCoinListForDropdownMenu().then(returnedArray => fillDropdown(returnedArray))
    .catch((error) => {console.log(error)})

//fetch om standaard bitcoin op het scherm te krijgen als de gebruiker binnenkomt
fetchTablesAndGraph("bitcoin")
currentTimer = setInterval(() => fetchTablesAndGraph("bitcoin"), refreshInterval);

//als er een klik komt van de gebruiker, eerst huidige timer eruit halen, dan onmiddellijk fetchen
//vervolgens weer een nieuwe update timer instellen, zo blijft het scriptje doorlopen
dropdown.addEventListener("change", function() {
    clearInterval(currentTimer)
    fetchTablesAndGraph(this.value)
    currentTimer = setInterval(() => fetchTablesAndGraph(this.value), refreshInterval);
});



//hieronder de functies die de logica ondersteunen
function fetchTablesAndGraph(coinName) {
    let url = `/market/orderbook?asset=${coinName}`
     fetch(url)
        .then(response => response.json())
        .then(data => {
                let buyOrderArray = parsePriceDataToFloat(data.buyOrderListSum);
                let sellOrderArray = parsePriceDataToFloat(data.sellOrderListSum)
                clearTable(demandTable)
                clearTable(supplyTable)
                fillTable(buyOrderArray, demandTable)
                fillTable(sellOrderArray, supplyTable)
                renderDepthChart(sellOrderArray, buyOrderArray, coinName)
            }
        ).catch((error) => {console.log(error)})
}


function parsePriceDataToFloat(orderList) {
    let parsedArray = [];
    Object.entries(orderList).forEach(([key, value]) => {
        let array = [parseFloat(key), value]
        parsedArray.push(array)
    });
    return parsedArray;
}


function fillTable(dataArray, tableToFill) {
    dataArray.forEach(([key, value]) => {
        const row = document.createElement('tr')
        const cell1 = document.createElement('td')
        cell1.innerHTML = key;
        const cell2 = document.createElement('td')
        cell2.innerHTML = value;
        row.appendChild(cell1)
        row.appendChild(cell2)
        tableToFill.appendChild(row)
    })
}


    function clearTable(tableToClear) {
        for (let i = tableToClear.rows.length - 1; i > 0; i--) {
            tableToClear.deleteRow(i)
        }
    }


    function renderDepthChart(sellers, buyers, coin) {
        Highcharts.chart('pirateDepthChart', {
            chart: {
                type: 'area',
                zoomType: 'xy',
            },
            title: {
                text: `Overzicht orders ${coin}`
            },
            xAxis: {
                minPadding: 0,
                maxPadding: 0,
                // plotLines: [{
                //     color: '#888',
                //     value: 1,
                //     width: 1,
                //     label: {
                //         text: 'Actual price',
                //         rotation: 90
                //     }
                // }],
                title: {
                    text: 'Price (in €)'
                }
            },
            yAxis: [{
                lineWidth: 1,
                gridLineWidth: 1,
                title: null,
                tickWidth: 1,
                tickLength: 5,
                tickPosition: 'inside',
                labels: {
                    align: 'left',
                    x: 8
                }
            }, {
                opposite: true,
                linkedTo: 0,
                lineWidth: 1,
                gridLineWidth: 0,
                title: null,
                tickWidth: 1,
                tickLength: 5,
                tickPosition: 'inside',
                labels: {
                    align: 'right',
                    x: -8
                }
            }],
            legend: {
                enabled: false
            },
            plotOptions: {
                area: {
                    fillOpacity: 0.2,
                    lineWidth: 1,
                    step: 'center'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size=10px;">Price: {point.key}</span><br/>',
                valueDecimals: 2
            },
            series: [{
                name: 'Sell',
                data: sellers,
                color: '#ff0000'
            }, {
                name: 'Buy',
                data: buyers,
                color: '#00ff04'
            }]
        });
}

 async function fetchCoinListForDropdownMenu() {
    let cryptoNameArray = []
    await fetch("/coins/list")
        .then(response => response.json())
        .then(data => {
            Object.values(data).forEach((value) => {
                cryptoNameArray.push(value)
            });
        }).catch((error) => {console.log(error)})
   return cryptoNameArray
}

 function fillDropdown(coinlist) {
     for (let i = 0; i < coinlist.length; i++) {
         let option = document.createElement('option')
         option.value = coinlist[i]
         option.innerHTML = coinlist[i]
         dropdown.appendChild(option)
     }
}

//zet de standaard openingsanimatie van de grafiek stil zodat die zonder dat de gebruiker het door heeft
//geupdate kan worden

function killGraphOpeningAnimation() {
    Highcharts.setOptions({
        plotOptions: {
            series: {
                animation: false
            }
        }
    });
}