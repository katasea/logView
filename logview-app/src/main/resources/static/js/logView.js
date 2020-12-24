
var appId = "1A3VL0KVK0000B020A0A0000CC3F48AD";
var encryptType = "Plain";
var signType = "Plain";

/**
 * 拼凑接口全局参数
 * @param params
 * @param method
 * @returns {{method: *, appId: string, bizContent: string, signType: string, encryptType: string, timestamp: number}}
 */
function getGlobalParams(params,method) {
    var timestamp = (new Date()).valueOf();
    return {
        "appId": appId,
        "bizContent": JSON.stringify(params),
        "encryptType": encryptType,
        "method": method,
        "signType": signType,
        "timestamp": timestamp
    };
}

/**
 * 获取url的参数
 * @param name
 * @returns {string}
 */
function getUrlParam (name) {
    var reg = new RegExp('(^|&)' + name + '=(.*)(&[^&=]+=)');
    var regLast = new RegExp('(^|&)' + name + '=(.*)($)');
    var r = window.location.search.substr(1).match(reg) || window.location.search.substr(1).match(regLast);
    if (r != null) {
        var l = r[2].match(/&[^&=]+=/)
        if (l) {
            return decodeURIComponent(r[2].split(l[0])[0]);
        } else return decodeURIComponent(r[2]);
    }
    return '';
}

/**
 * 获取项目根路径
 * @returns {string}
 */
function getRootPath() {
    var strFullPath = window.document.location.href;
    var strPath = document.location.pathname;
    if(!(strPath.lastIndexOf("/") === strPath.indexOf("/"))) {
        strPath = strPath.substring(0,strPath.lastIndexOf("/"));
    }
    var pos = strFullPath.indexOf(strPath);
    var prePath = strFullPath.substring(0, pos);
    // var postPath = strPath.substring(0,strPath.substring(1).indexOf("/")+1);
    return prePath+strPath;
}

/**
 * 处理接口返回消息，全局判断是否登录过期等处理
 * @param responseVo
 * @returns {boolean}
 */
function handlerResult(responseVo) {
    if(responseVo!=null && responseVo.retCode != null) {

        if(responseVo.retCode === '0000') {
            return true;
        }else if(responseVo.retCode === '9998') {
            alert(responseVo.retMsg);
            window.location.href = getRootPath()+"/login";
            return false;
        }else {
            alert(responseVo.retMsg);
            return false;
        }
    }else {
        return false;
    }
}