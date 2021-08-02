# ChatyChaty

#### Android chats application built using kotlin.

## Motivation

The app is a part of my [Portfolio](https://alialbaali.com) projects. It showcases my skills regarding developing Android apps.

## Screenshots

![ScreenShot](screenshots/Screenshot1.png)
![ScreenShot](screenshots/Screenshot2.png)

## Features

* Dark mode
* Archive chats
* Minimal design
* Real-time communication using websockets

## Libraries

* Android Architecture Components
* Kotlin
* Data Binding
* Room (DB)
* Koin (DI)
* Retrofit
* Moshi
* Okhttp

## Architecture

> The app uses Clean Architecture with MVVM design pattern. It's splitted into 3 main modules.

#### Domain

###### Contains all the models Logic. It's splitted into 2 packages.

* ##### Model
  Contains app models.

* ##### Repository
  Contains repositories interfaces which are used by the Use Cases and implemented in the `Data` Layer.

#### Data

###### Contains repositories implementations and Data Sources interfaces. It's splitted into 2 pacakges.

* ##### Local
  Contains interfaces to preform actions locally and implementend in the Local Module.

* ##### Remote
  Contains interfaces to preform actions remotely and implementend in the Remote Module.

#### Presentation (app)

###### Contains all the UI and business logic.

#### DI (Dependency Injection)

###### Contains Koin DI modules.

#### BuildSrc

###### Contains gradle dependencies and app configuration.

## Requirements

* JDK 11
* [Android SDK](https://developer.android.com/studio/index.html)
* Android N (API 23)
* Latest Android SDK Tools and build tools.

## Running

```
./gradlew
```

## License

ChatyChaty is distributed under the terms of the Apache License (Version 2.0). See [License](LICENSE.md) for details.
