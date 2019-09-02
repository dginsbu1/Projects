<!DOCTYPE html>

<html lang ='en'>
<body>
<div>
    <h1 id="demo"></h1>
    <h1 id="console"></h1>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js">
        gapi.load('auth2', function() {
        // Library loaded.
    });
    </script>
</div>
<script>

    var x = document.getElementById("demo");
    var y = document.getElementById("console");

    function showPosition(position) {
    x.innerHTML = "Latitude: " + position.coords.latitude +
        "<br>Longitude: " + position.coords.longitude;
    current[0] = position.coords.latitude;
    current[1] = position.coords.longitude;
    console.log(true);
}

    function display() {
    if (navigator.geolocation) {
    navigator.geolocation.watchPosition(showPosition);
} else {
    x.innerHTML = "Geolocation is not supported by this browser.";
}
}
    //if within the range then return true;
    function withinRange(bad, me){
    return Math.abs(bad - me) < .0001;
}

    function tooClose(lat, long){
    y.innerHTML = true;
    return (withinRange(lat, current[0]) || withinRange(long, current[1]);
}
    //if too close then send emaii.
    go(){
    if(tooClose(badLat, badLat)){
    //send email;
}
}



    while(true){
    var badLat = [40.8507];
    var badLong = [-73.9295];
    display();
    go();
    wait(2000);
}





    /*https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=dunkin’%20donuts&inputtype=textquery&fields=geometry,formatted_address,name&locationbias=circle:10@40.850746, -73.929300&key= AIzaSyB1dk_3_uBczLLqROrA54wGbrJOnqXY3LA*/

    // $.getJSON("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=dunkin’%20donuts&inputtype=textquery&fields=geometry,formatted_address,name&locationbias=circle:10@40.850746, -73.929300&key= AIzaSyB1dk_3_uBczLLqROrA54wGbrJOnqXY3LA", function(results){
//   alert(results.candidates);
//   $.each(results, function(i, field){
//       $.each(a, function(i, a)){
//         alert(a);
//       }
//   });
//   console.log(results.candidates.text);
//   alert(results);
    // var json = $.parseJSON(results);
// $(results).each(function (i, val) {
//   $.each(val, function (k, v) {
//     console.log(k + " : " + v);
//   });
// });
    //y.innerHTML = results;
    // console.log(json);
    // $.each(results, function(index, element) {
    //           console.log(element);
    //       });

// });


// $.getJSON('https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=dunkin’%20donuts&inputtype=textquery&fields=geometry,formatted_address,name&locationbias=circle:10@40.850746, -73.929300&key= AIzaSyB1dk_3_uBczLLqROrA54wGbrJOnqXY3LA', { get_param: 'value' }, function(data) {
//     $.each(data, function(index, element) {
//         $('body').append($('<div>', {
//             text: element.name
//         }));
//     });
// });

// var xhr = new XMLHttpRequest();
// xhr.onreadystatechange = function(){
//   console.log("yayayayyayay");
// }
// xhr.open('GET','https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=dunkin’%20donuts&inputtype=textquery&fields=geometry,formatted_address,name&locationbias=circle:10@40.850746, -73.929300&key= AIzaSyB1dk_3_uBczLLqROrA54wGbrJOnqXY3LA' );
// xhr.send(null);

// var user = gapi.auth2.getAuthInstance().currentUser.get();
// var oauthToken = user.getAuthResponse().access_token;
// var xhr = new XMLHttpRequest();
// xhr.open('GET',
//   'https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=dunkin’%20donuts&inputtype=textquery&fields=geometry,formatted_address,name&locationbias=circle:10@40.850746, -73.929300&key= AIzaSyB1dk_3_uBczLLqROrA54wGbrJOnqXY3LA');
// xhr.setRequestHeader('Authorization',
//   'Bearer ' + oauthToken);
// xhr.send();


// var user = gapi.auth2.getAuthInstance().currentUser.get();
// var oauthToken = user.getAuthResponse().access_token;
// var xhr = new XMLHttpRequest();
// xhr.open('GET',
//   'https://people.googleapis.com/v1/people/me/connections' +
//   '?access_token=' + encodeURIComponent(oauthToken));
// xhr.send();

    </script>
    </body>
    </html>