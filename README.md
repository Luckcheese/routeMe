## B2W Android Challenge


App RouteMe

The app means to the be a simplified version of the Google Maps app. So, it will allow user to check his current position, search by places and locations and ultimately draw the best route between him and the searched location.


### Libraries used

Despite the usual Android libraries, it was also used other libraries to add the required funcionalities.

- [Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk/overview?hl=en_US)
  
    `implementation 'com.google.android.gms:play-services-maps:17.0.0'`

    Used to show the map.

- [Places SDK for Android](https://developers.google.com/places/android-sdk/overview?hl=en_US)

    `implementation 'com.google.android.libraries.places:places:2.4.0'`

    Used to allow the user to search for places and locations

- [Google Maps Service (Java client)](https://github.com/googlemaps/google-maps-services-java)

    `implementation 'com.google.maps:google-maps-services:0.15.0'`

    User to draw the best route between the user and searched location

    This library is not intented to be used by an Android application. The correct use would be to have a server side application calling the Directions API service and returning the data to the application. But, as this server application was not in the scope of the challenge, it was decided to embed this library on the app and make it do the API calls.

    This create a problem, as this Directions API can only be used by servers with known IP addresses. This means that this app, as it is, can't be launched as each user would have a different IP and the API would restrict them.

- [Java time backport](com.github.seratch:java-time-backport:1.0.0)

    `implementation 'com.github.seratch:java-time-backport:1.0.0'`

    As the **Google Maps Service (Java client)** library is not intented to be used by an Android application it depends on some classes that the Android Java doesn't embed. So, this library was used to overcome this limitation (as recommeded byt this [comment](https://github.com/googlemaps/google-maps-services-java/issues/544#issuecomment-482838799).


### Needed permissions

This app only needs the `ACCESS_FINE_LOCATION` permission, without it app won't be able to check user location and draw the route for the searched location.


### Delivery status

For what was requested on the challenge, I believe everything was made. But following are some considerations:

- About the Google API keys

    They are not stored on the project. One can create them following this [tutorial](https://github.com/Luckcheese/routeMe/wiki/Google-Api-Keys). Please, get in touch if you need any help.

- About the limitations of the route feature

    Maybe it was a lake of further research, but for a challenge project with no intent to be launched, I think it is a minor drawback. That could be easily overcome with a simple lambda function.

- About layout
    
    I belive the User eXperience is very good, but the layout could be improved. I mainly used the default images from Android applying customizations when possible. But it was not the focus during the development.

- About automated tests

    As almost everything is based on simple calls of third party frameworks and APIs, I didn't code much logic that could be tested. Almost everything depends that the third party library will work as espected, but writing automated tests to check them could create a problem as most of these can generate a bill.

- About the biggest challenge on the project

    As a simple Android project the code side was very simple, so the biggest challenge award goes to the integration and configuration of the Directions API. 

    The Maps and Places APIs integration was very easy as the documentation is strait forward and the libraries gives a good interface. For the Directions API, there is only a web interface that would need a lot more code to handle it. So, I decided to use the server side client that fixed most of the problems but created one: Android was not supposed to use it and so it was not easy to make it work.

    There were two main problems: a new **Google API key** was needed an Android java doesn't have all needed classes.

    First error was a little boring to fix as there is no documentation about it and a new key takes a few minutes to work, so even after everything was fixed a strange error was still popping up, but it was solved waiting. And the second error needed some research until I found a new library that should be embeded on the project.