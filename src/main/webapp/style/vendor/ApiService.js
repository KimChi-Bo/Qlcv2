function isInteger(num) {
    return (num ^ 0) === num;
}

function initApiService(restUrl) {
    apiservice = {};
    apiservice.AjaxJson = new ApiService(restUrl);
}
function ApiService(url) {
    this.serviceURL = (url == null) ? "/" : url;
}

ApiService.prototype.executeQuery = function (jsonData, method, isNormalHtml, isAsync) {
    var self = apiservice.AjaxJson;
    // console.log("--------------serviceURL:", self.serviceURL);
    var objJson = new Object();
    objJson.params = JSON.stringify(jsonData);
    var rt = $.ajax({
        url: self.serviceURL,
        type: method,
        async: isAsync,
        dataType: 'json',
        data: objJson,
        error: function (data) {
            console.log("error", data);
        }
    }).responseJSON;

    return isNormalHtml ? rt : ((rt && !isInteger(rt) && rt.length > 0) ? JSON.parse(escapeHTML(JSON.stringify(rt))) : rt);
}


function escapeHTML(string) {
    var pre = document.createElement('pre');
    var text = document.createTextNode(string);
    pre.appendChild(text);
    return pre.innerHTML.replace(/&amp;/g, '&');
}
