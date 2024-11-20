export let prepareDateForInput = function (millisecond) {
    let date = new Date(millisecond);
    let year = date.getFullYear();
    let month = date.getMonth() + 1 + "";
    let dayOfMonth = date.getDate() + "";
    return year + "-" + (month.length < 2 ? "0" + month : month) + "-" + (dayOfMonth.length < 2 ? "0" + dayOfMonth : dayOfMonth);
};

export let prepareDateForText = function (millisecond) {
    let date = new Date(millisecond);
    let year = date.getFullYear();
    let month = date.getMonth() + 1 + "";
    let dayOfMonth = date.getDate() + "";
    //let time = date.toString().substr(16, 5);
    return year + "-" + (month.length < 2 ? "0" + month : month) + "-" + (dayOfMonth.length < 2 ? "0" + dayOfMonth : dayOfMonth);
};

export let prepareDateTimeForInput = function (millisecond) {
    let date = new Date(millisecond);
    let year = date.getFullYear();
    let month = date.getMonth() + 1 + "";
    let dayOfMonth = date.getDate() + "";
    let formattedDate = year + "-" + (month.length < 2 ? "0" + month : month) + "-" + (dayOfMonth.length < 2 ? "0" + dayOfMonth : dayOfMonth);
    let time = date.toString().substr(16, 5);
    return formattedDate + "T" + time;
};

export let prepareDateTimeForText = function (millisecond) {
    let date = new Date(millisecond);
    let year = date.getFullYear();
    let month = date.getMonth() + 1 + "";
    let dayOfMonth = date.getDate() + "";
    let formattedDate = year + "-" + (month.length < 2 ? "0" + month : month) + "-" + (dayOfMonth.length < 2 ? "0" + dayOfMonth : dayOfMonth);
    let time = date.toString().substr(16, 8);

    let hours = parseInt(time.substr(0, 2));
    let amPm = hours >= 12 ? "PM" : "AM";
    let amPmHour = hours % 12 || 12;

    return formattedDate + " " + amPmHour + ":" + time.substr(3, 2) + amPm;
};

export let prepareUTCTime = function (time) {
    let date = new Date("1990-01-02T" + time);
    let hours = date.getUTCHours();
    let minutes = date.getUTCMinutes();
    if (hours < 10) {
        hours = "0" + hours;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    return hours + ":" + minutes;
};

export let prepareTimeForInput = function (utcTime) {
    let date = new Date("1990-01-01T" + utcTime + "Z");
    let hours = date.getHours();
    let minutes = date.getMinutes();
    if (hours < 10) {
        hours = "0" + hours;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    return hours + ":" + minutes;
};

export let prepareTimeForText = function (utcTime) {
    let date = new Date("1990-01-01T" + utcTime + "Z");
    let hours = date.getHours();
    let minutes = date.getMinutes();
    let seconds = date.getSeconds();
    let amPm = hours > 12 ? "PM" : "AM";
    let amPmHour = hours % 12 || 12;

    if (amPmHour < 10) {
        amPmHour = "0" + amPmHour;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    if (seconds < 10) {
        seconds = "0" + seconds;
    }
    return amPmHour + ":" + minutes + ":" + seconds + " " + amPm;
};

export let attachDateTimePicker = function (elem) {
    $(elem).daterangepicker({
        singleDatePicker: true,
        timePicker: true,
        startDate: moment().startOf('hour'),
        locale: {
            format: 'YYYY-MM-DD hh:mm A'
        }
    });
};