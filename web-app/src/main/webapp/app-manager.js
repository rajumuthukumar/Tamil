function _list_AppNames() {


    return JSON.parse(jQuery.ajax({
        type:"GET",
        url:server + "app-management/apps/",
        async:false
    }).responseText).results;


}

function _describeApp(appname, code) {

    return JSON.parse(jQuery.ajax({
        type:"GET",
        headers:{
            "X-TAMIL-APP-ACCESS-CODE":"" + code
        },
        accept:"application/json",
        url:server + "app-management/apps/name/" + encodeURI(appname) + "/resources/",
        async:false
    }).responseText).value;


}


function _describeAppResource(appname, resname, code) {

    return JSON.parse(jQuery.ajax({
        type:"GET",
        headers:{
            "X-TAMIL-APP-ACCESS-CODE":"" + code
        },
        accept:"application/json",
        url:server + "app-management/apps/name/" + encodeURI(appname) + "/resources/resource/" + encodeURI(resname),
        async:false
    }).responseText).value;


}


function _deleteApp(app, code) {


    jQuery.ajax({
        type:"DELETE",
        headers:{
            "X-TAMIL-APP-ACCESS-CODE":"" + code
        },
        url:server + "app-management/apps/name/" + encodeURI(app) + "/",
        contentType:"text/plain; charset=utf-8",
        async:false,
        statusCode:{
            500:function (data, status, XHR) {
                console.log(data.responseText);
                alert("Could not delete!:" + JSON.parse(data.responseText).message);
            }
        }
    });


}


function _deleteAppResource(app, resource, code) {


    jQuery.ajax({
        type:"DELETE",
        headers:{
            "X-TAMIL-APP-ACCESS-CODE":"" + code
        },
        url:server + "app-management/apps/name/" + encodeURI(app) + "/resources/resource/" + encodeURI(resource),
        contentType:"text/plain; charset=utf-8",
        async:false,
        statusCode:{
            500:function (data, status, XHR) {
                console.log(data);
                alert("Could not delete!:" + JSON.parse(data.responseText).message);
            }
        }
    });


}


function _createApp(app, code) {


    jQuery.ajax({
        type:"POST",
        headers:{
            "X-TAMIL-APP-ACCESS-CODE":"" + code
        },
        url:server + "app-management/apps/name/" + encodeURI(app) + "/",
        contentType:"text/plain; charset=utf-8",
        async:false
    });


}


function _createAppAs(app, newapp, code) {


    jQuery.ajax({
        type:"POST",
        headers:{
            "X-TAMIL-APP-ACCESS-CODE":"" + code
        },
        url:server + "app-management/apps/name/" + encodeURI(app) + "/?as=" + encodeURI(newapp),
        contentType:"text/plain; charset=utf-8",
        async:false
    });


}





function _createAppClassloader(app, libs, code) {


    jQuery.ajax({

        type:"PUT",
        headers:{
            "X-TAMIL-APP-ACCESS-CODE":"" + code
        },
        url:server + "app-management/apps/name/" + encodeURI(app) + "/libs/text/",
        contentType:"text/plain; charset=utf-8",
        async:false,
        data : "" + libs,
        statusCode:{
            500:function (data, status, XHR) {
                console.log(data);
                alert("Class loader could not be updated!:" + JSON.parse(data.responseText).message);
            } ,

            200:function (data, status, XHR) {
                console.log(data);
                alert("libraries Updated with:\n\n" + data);
            }
        }

    });


}


function _createAppResource(app, resource, code) {


    jQuery.ajax({

        type:"POST",
        headers:{
            "X-TAMIL-APP-ACCESS-CODE":"" + code
        },
        url:server + "app-management/apps/name/" + encodeURI(app) + "/resources/resource/" + encodeURI(resource),
        contentType:"text/plain; charset=utf-8",
        async:false,
        statusCode:{
            500:function (data, status, XHR) {
                console.log(data);
                alert("Could not create resource!:" + JSON.parse(data.responseText).message);
            }
        }

    });


}



function _saveAppDetails(code, app,  welcome, parentapps, searchscheme, desc ,extroots) {
    var q = "/?";

    if (welcome) {
        q += "welcome=" + welcome +"&";
    }

    if (parentapps) {
        q += "parents=" + parentapps +"&";
    }
    if (searchscheme) {
        q += "inheritanceorder=" + searchscheme +"&" ;
    }

    if (extroots) {
        q += "externalroots=" + extroots ;
    }

    jQuery.ajax({

        type:"PUT",
        headers:{
            "X-TAMIL-APP-ACCESS-CODE":"" + code
        },
        url:server + "app-management/apps/name/" + encodeURI(app) + q,
        contentType:"text/plain; charset=utf-8",
        data : ""+desc,
        async:false,
        statusCode:{
            500:function (data, status, XHR) {
                console.log(data);
                alert("Could not update app!:" + JSON.parse(data.responseText).message);
            }
        }

    });


}


function _updateAppResourceContent(app, resource, content, type, code) {

    return jQuery.ajax({

        type:"PUT",
        headers:{
            "X-TAMIL-APP-ACCESS-CODE":"" + code
        },
        data:"" + content,
        url:server + "app-management/apps/name/" + encodeURI(app) + "/resources/resource/" + encodeURI(resource) + "?type=" + type,
        contentType:"application/x-www-form-urlencoded;charset=utf-8",
        async:false,
        statusCode:{
            500:function (data, status, XHR) {
                console.log(data);
                alert("Could not Save!:" + JSON.parse(data.responseText).message);
            }
        }
    });


}


function _list_Context_Paths() {


    return JSON.parse(jQuery.ajax({
        type:"GET",
        url:server + "apps/",
        contentType:"text/plain; charset=utf-8",
        async:false
    }).responseText).results;


}