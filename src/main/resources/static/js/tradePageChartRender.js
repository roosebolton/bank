
// setting graph container
import {getCurrency, getCurrencySign} from "./modules/currencySelect.js";

let margin = {top: 10, right: 40, bottom: 40, left: 10},
    width = 620 - margin.left - margin.right,
    height = 400 - margin.top - margin.bottom;

// append the svg object to the body of the page
let svg = d3.select("#coin_chart_render")
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g");


const drawChart =  (data) =>  {
    d3.selectAll("g > *").remove();
    d3.selectAll('.tooltip').remove();

     let x = d3.scaleTime()
            .domain(d3.extent(data, function (d) {
                return d.timestamp;
            }))
            .range([0, width]);
        let xAxis = svg.append("g")
            .attr("transform", "translate(0," + height + ")")
            .attr("class", "axisChart")
            .call(d3.axisBottom(x));

        // Add Y axis
        let y = d3.scaleLinear()
            .domain([0, d3.max(data, function (d) {
                return +d.value;
            })])
            .range([height, 0]);

        let yAxis = svg.append("g")
            .attr("class", "axisChart")
            .call(d3.axisRight(y).scale(y).tickSize(width))
            .call(g => g.select(".domain")
                .remove())
            .call(g => g.selectAll(".tick:not(:first-of-type) line")
                .attr("stroke-opacity", 0.5)
                .attr("stroke-dasharray", "2,2"))
            .call(g => g.selectAll(".tick text")
                .attr("x", 4)
                .attr("dy", -4).attr('transform', 'translate(' + (width) + ', 0)'));

            let bisect = d3.bisector(function (d) {
                return d.timestamp;
            }).left;

            let line = svg.append('g');

            // Add the line
            line.append("path")
                .datum(data)
                .attr("class", "line")  // I add the class line to be able to modify this line later on.
                .attr("fill", "none")
                .attr("stroke", "blue")
                .attr("stroke-width", 2)
                .attr("d", d3.line()
                    .x(function (d) {
                        return x(d.timestamp)
                    })
                    .y(function (d) {
                        return y(d.value)
                    })
                )

            let focus = svg.append('g')
                .append('circle')
                .style("fill", "black")
                .attr("stroke", "black")
                .attr("border", "1px solid white")
                .attr('r', 6)
                .attr("id", "price")
                .style("opacity", 0);

            let focus_opacity = svg
                .append('g')
                .append('circle')
                .style("fill", "black")
                .attr("stroke", "black")
                .attr("border", "1px solid white")
                .attr('r', 10)
                .style("opacity", 0);

            // Create the text that travels along the curve of chart

            let tooltip = d3.select("#coin_chart_render")
                .append("div")
                .style("opacity", 0)
                .style("position", "relative")
                .attr("class", "tooltip")
                .style("font-size", "12px")
                .style("width", "110px")
                .style("height", "15px")
                .style("background-color", "black")
                .style("color", "white")
                .style("border", "solid")
                .style("border-width", "1px")
                .style("border-radius", "5px")
                .style("padding", "10px")

            let tooltip_top = d3.select("#coin_chart_render")
                .append("div")
                .style("opacity", 0)
                .style("position", "relative")
                .style("display", "flex")
                .attr("class", "tooltip")
                .style("font-size", "12px")
                .style("width", "45px")
                .style("height", "15px")
                .style("background-color", "black")
                .style("color", "white")
                .style("border", "none")
                .style("border-radius", "5px")
                .style("padding", "10px")
                .style("margin", "none");


            let mouseG = svg.append("g")
                .attr("class", "mouse-over-effects");

            mouseG.append("path")
                // this is the black vertical line to follow mouse
                .attr("class", "mouse-line")
                .style("stroke", "black")
                .style("stroke-width", "2px")
                .style("pathLength", 10)
                .style("opacity", 0);

            mouseG.append('svg:rect') // append a rect to catch mouse movements on canvas
                .attr('width', width) // can't catch mouse events on a g element
                .attr('height', height)
                .attr('fill', 'none')
                .attr('pointer-events', 'all')
                .on('mouseout', mouseout)
                .on('mouseover', mouseover)
                .on('mousemove', mousemove)

            function mouseover() {
                // on mouse in show line, circles and text
                d3.select(".mouse-line").style("opacity", 1);
                focus.style("opacity", 1)
                focus_opacity.style("opacity", 0.2)
                tooltip.style("opacity", 1)
                tooltip_top.style("opacity", 1)
            }

            function mousemove() {
                // mouse moving over canvas
                var mouse = d3.mouse(this);

                d3.select(".mouse-line")
                    .attr("d", function () {
                        var d = "M" + mouse[0] + "," + height;
                        d += " " + mouse[0] + "," + 0;
                        return d;
                    });

                // recover coordinate we need
                var x0 = x.invert(d3.mouse(this)[0]);
                var i = bisect(data, x0, 1);
                let selectedData = data[i]


                focus_opacity
                    .attr("cx", x(selectedData.timestamp))
                    .attr("cy", y(selectedData.value))

                focus
                    .attr("cx", x(selectedData.timestamp))
                    .attr("cy", y(selectedData.value))

                tooltip_top
                    .html(getCurrencySign(getCurrency()) + selectedData.value.toFixed(4))
                    .style("left", (d3.mouse(this)[0]) + -25 + "px")
                    .style("top", "-460px")

                tooltip
                    .html((new Date(selectedData.timestamp).toLocaleDateString()))
                    .style("left", (d3.mouse(this)[0]) + -45 + "px")
                    .style("top", "-45px")
            }


            function mouseout() {
                // on mouse out hide line, circles and text
                d3.select(".mouse-line")
                    .style("opacity", "0");

                focus.style("opacity", 0)
                focus_opacity.style("opacity", 0)

                tooltip.style("opacity", 0)
                tooltip_top.style("opacity", 0)
            }

            // updating charts
            function updateChart(months) {
                /* Creates a reverse date object with given days/months */
                function createDate(months) {
                    let years = Math.floor(months / 12);
                    let months_divided = months % 12;

                    let date = new Date();
                    date.setMonth(date.getMonth() - months_divided);
                    date.setFullYear(date.getFullYear() - years);

                    return date.getTime();
                }


                let filterdate = createDate(months);

                let dataFiltereddate = data.filter(function (t) {
                    // console.log(x)
                    if (t.timestamp >= filterdate) {
                        return t
                    }
                })

                /* redraws the x axis, y axis and line based on filtered date */

                if (dataFiltereddate.length !== 0) {
                    x = d3.scaleTime()
                        .domain(d3.extent(dataFiltereddate, function (d) {
                            return d.timestamp;
                        }))
                        .range([0, width]);
                    xAxis.call(d3.axisBottom(x))
                    line.select('.line')
                        .attr("d", d3.line()
                            .x(function (d) {
                                return x(d.timestamp)
                            })
                            .y(function (d) {
                                return y(+d.value)
                            })
                        )
                }
            }


            /* Updates the chart according to the selected buttons*/
            let buttons = document.getElementsByClassName("chart_select_button");
            for (let i = 0; i < buttons.length; i++) {
                buttons[i].addEventListener('click', function () {
                    updateChart(this.name)
                    setChartButtonActive(this)
                }, false);
            }
}


/* Setting up css changes for button */
const setChartButtonActive = (element) => {
    let buttons = document.getElementsByClassName("chart_select_button");
    for (let i = 0; i < buttons.length; i++) {
        buttons[i].classList.remove("btn_chart_active");
        buttons[i].classList.remove("btn_chart_active_hover");
        buttons[i].classList.add("btn_chart_hover");
    }
    element.classList.add("btn_chart_active");
    element.classList.add("btn_chart_active_hover");
}


export {drawChart};


