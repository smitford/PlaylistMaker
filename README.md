# Description

The application that helps you build playlists with the music base of Apple Music. The project is developed within a study at Yandex.Practicum.
Also, the last build of the application you can download [here](https://github.com/smitford/PlaylistMaker/raw/dev/app-debug.apk)

## Device requerment
Compatible from Android 8 (Android API 26)

## Application featurse

### Track search

Stable track searching of any text request. Application store 10 of the last audio samples that the user chose for detailed 
information observation during the search. 


https://github.com/smitford/PlaylistMaker/assets/17237585/0b01c1af-1d3c-4fd0-aa53-063cc758b0ae


### Playlist creation

The User has two possibilities for playlist creation:

- form the main screen "Media" of the application
  
- from screen with a track detail information at the moment of it add to a playlist

A playlist has one required field that must be chosen the name of the Playlist and two extras a playlist placeholder and a description.


https://github.com/smitford/PlaylistMaker/assets/17237585/4400213f-867e-4214-ab99-95f1d018a810


### Track player and description

The User may choose the sought-after track for detailed information. It contains the following data:
- name of album
- length of track
- the year of creation
- genre
- country of creation

On the same screen, the User may add the track to the list of favorites or the created playlists.

![Screenshot_20240522-184059](https://github.com/smitford/PlaylistMaker/assets/17237585/b33d3d93-19d1-4204-b1e9-e10baba68a0d)

### Favorites tracks

In the Media screen located tracks that have been added previously.

![Screenshot_20240522-183637](https://github.com/smitford/PlaylistMaker/assets/17237585/0e998f88-1aa7-4815-88f5-8a36b129a4a3)

### Playlist editing



https://github.com/smitford/PlaylistMaker/assets/17237585/7a895e5c-6516-4f83-b5d5-fef03f1d5b62


### Technology stack

Kotlin, MVVM, Kotlin Coroutines, Koin, Navigation Component, Retrofit2 (for interaction with iTunes API), ViewModel, LiveData, RecyclerView, SharedPreferences, MediaPlayer, BottomNavigationView, Fragment, ViewPager2, TabLayout, ConstrainLayout, Git, Android SDK, Gson, Room, Glide.

