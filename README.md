# ChatyChaty

#### Android chats application built using kotlin.

## Motivation
The app is a part of my [Portfolio](https://alialbaali.com) projects. It showcases my skills regarding developing Android Apps.

## Screenshots

![ScreenShot](screenshots/Screenshot1.png)
![ScreenShot](screenshots/Screenshot2.png)

## Features

* Dark Mode
* Archive Chats 
* Minimal Design

## Libraries

* Android Architecture Components(LiveData, ViewModel, etc...)
* Kotlin
* Data Binding
* Room (DB)
* Koin (DI)
* Retrofit
* Moshi
* Okhttp

## Architecture

> The app uses Clean Architecture with MVVM. It's splitted into 3 Main Layers. 

#### Domain
###### Contains all the Business Logic. It's splitted into 3 packages.

* ##### Intreactor
    Contains Use Cases for each action that can be triggered and used directly through ViewModels in the `Presentation` Layer.

* ##### Model
    Contains app Models.

* ##### Repository 
    Contains repositories interfaces which are used by the Use Cases and implemented in the `Data` Layer. 
    
#### Data
######  Contains repositories implementations and Data Sources interfaces. It's splitted into 2 pacakges. 

* ##### Local
    Contains interfaces to preform actions locally and implementend in the Local Module.
    
* ##### Remote
    Contains interfaces to preform actions remotely and implementend in the Remote Module.
    

#### Presentation (app)
######  Contains all the UI Logic.

#### DI (Dependency Injection)
###### Contains Koin DI modules.

#### BuildSrc
###### Contains Gradle dependencies and app configuration.

## Requirements

* JDK 1.8
* [Android SDK](https://developer.android.com/studio/index.html)
* Android L (API 21)
* Latest Android SDK Tools and build tools.

## Running

```
./gradlew
```

## License
ChatyChaty is distributed under the terms of the Apache License (Version 2.0). See [License](LICENSE.md) for details.
