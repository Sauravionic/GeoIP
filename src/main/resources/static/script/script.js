$(document).ready(function() {
    $("form").keypress(function(e) {
        //Enter key
        if (e.which == 13) {
            return false;
        }
    });
    $(document).keypress(function(e){
        if (e.which == 13){
            $("#submit").click();
        }
    });
    $("#submit").click(function() {
        $.ajax({
            type : 'POST',
            url : 'https://iogeoip.herokuapp.com/ipinput',
            data : JSON.stringify({
                "ip" : $("#ip").val()
            }),
            contentType : "application/json; charset=utf-8",
            success : function(data) {
                $("#upperCard").css({display: "block"});
                $("#formGroup").css({display: "none"});
                $("#lbl").css({display: "none"});

                $("#hostnameP").html(data.query);
                $("#ispP").html(data.isp);
                $("#countryP").html(data.country);
                $("#stateP").html(data.regionName);
                $("#cityP").html(data.city);
                if(data.pinCode === "") {
                    $("#pincodeP").html("-");
                }
                else {
                    $("#pincodeP").html(data.pinCode);
                }
                if(data.mobile === false) {
                    $("#mobileP").html("false");
                }
                else {
                    $("#mobileP").html(data.mobile);
                }
                if(data.proxy1 === false) {
                    $("#proxyP").html("false");
                }
                else {
                    $("#proxyP").html(data.proxy1);
                }
            },
            error : function () {
                $("#upperCard").css({display:"none"});
                $("#formGroup").css({display: "block"});
                $("#lbl").css({display: "block"});
            }
        });
    });
    $("#logoH").click(function() {
        window.location = "/";
    })
    $("#foot").click(function() {
        window.open("https://github.com/Sauravionic","_blank");
        // window.location = "https://github.com/Sauravionic";
    })

});