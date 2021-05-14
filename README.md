# Description
The app is intended for finding movies. It is based on the REST API of https://themoviedb.org service.
App consists of 3 screens: Main, Collection, Detail.

The Main screen shows four movie categories among which Now playing, Upcoming, Popular and Top rated categories, the movies of which are displayed in the horizontal recyclerviews with pagination. Also categories are placed in the recyclerview.

The Collection screen displays movies of selected category in a recyclerview with pagination.

The Detail screen depicts the detailed information about selected movie. On this screen you can see trailer, description, crew and actors. The trailer is showed by means of exoplayer.

The app uses several libraries including Dagger, Navigation, Exoplayer, RxJava, Retrofit + Gson + OkHttp, Glide, Room, Paging 3, firebase messaging
- All asynchronous operations are performed with the help of RxJava. 
- Http requests are peformed by means of Retrofit + Gson + OkHttp
- Glide for image loading
- Room for requests caching
- Firebase messaging is used for delivering push notifications.
- All screen transition performed by means of Navigation component. All transition are animated.
- Some dependencies are provided by Dagger. It has two scopes App scope and Screen scope to efficiently manage memory of the app.

# Apk (version 3)
[![](https://github.com/vkharapaev/find-movie/blob/master/art/small_icon.png?raw=true)](https://github.com/vkharapaev/find-movie/blob/master/versions/find-movie-3.apk?raw=true)

# App screens
![](https://github.com/vkharapaev/find-movie/blob/master/art/screen1.jpg?raw=true)
![](https://github.com/vkharapaev/find-movie/blob/master/art/screen2.jpg?raw=true)
![](https://github.com/vkharapaev/find-movie/blob/master/art/screen3.jpg?raw=true)

# Video
[![](https://img.youtube.com/vi/qgaM4dIOGTE/0.jpg)](https://youtu.be/qgaM4dIOGTE)
