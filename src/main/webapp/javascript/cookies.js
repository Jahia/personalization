
function incrementCookie(cookieName) {
    cookieValue = $.cookie(cookieName);
    if (cookieValue == null) {
        cookieValue = 1;
    } else {
        cookieValue++;
    }
    $.cookie(cookieName, cookieValue, { path: '/', expires: 365 });
}