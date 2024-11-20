'use strict';

export function capitalizeFirst(str) {
    return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

export function empty(elem) {
    if (elem) {
        while (elem.firstChild) {
            elem.removeChild(elem.firstChild);
        }
    }
}

export function trim(str) {
    return str.substr(0, str.length - 2);
}