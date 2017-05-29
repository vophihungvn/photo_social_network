function statusChangeCallback(response) {
    if (response.status === 'connected') {
        // Logged into your app and Facebook.
        callAPI(response.authResponse.accessToken);
    }
}

function checkLoginState() {
    FB.getLoginStatus(function(response) {
        statusChangeCallback(response);
    });
}
window.fbAsyncInit = function() {
    FB.init({
        appId      : '282232762125724',
        cookie     : true,  // enable cookies to allow the server to access the session
        xfbml      : true,  // parse social plugins on this page
        version    : 'v2.5' // use graph api version 2.5
    });
    
//    FB.getLoginStatus(function(response) {
//        statusChangeCallback(response);
//    });
};

// Load the SDK asynchronously
(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));


let callAPI = (access_token) => {
    return $.get('api/entity.user/login/' + access_token, (res)=> {
        console.log("get data from api")
        $.cookie('user_id', res.id);
        var cookieValue = $.cookie("user_id");
        location.href = 'mainpage.zul'
    })
}

let CallAfterLogin = () => {
    FB.getLoginStatus(function(response) {
        statusChangeCallback(response);
    });
}
