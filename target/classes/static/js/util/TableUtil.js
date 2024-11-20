export let createTable = function (tableElem, data = []) {
    if (tableElem) {
        for (let i = 0; i < data.length; i++) {
            let tr = document.createElement("tr");
            let trData = data[i];
            for (let key in trData) {
                if (trData.hasOwnProperty(key)) {
                    let td = document.createElement("td");
                    let cellText = document.createTextNode(trData[value]);
                    td.appendChild(cellText);
                    tr.appendChild(td);
                }
            }
            tableElem.appendChild(tr);
        }
    }
};

export let createRow = function (tableElem, rowData = []) {
    if (tableElem) {
        let tr = document.createElement("tr");
        for (let i = 0; i < rowData.length; i++) {
            let td = document.createElement("td");
            let cellText = document.createTextNode(rowData[i]);
            td.appendChild(cellText);
            tr.appendChild(td);
        }
        tableElem.appendChild(tr);
    }
};