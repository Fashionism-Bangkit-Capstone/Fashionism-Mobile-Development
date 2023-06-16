<img src="https://raw.githubusercontent.com/Fashionism-Bangkit-Capstone/Fashionism-Mobile-Development/Fashionism-UMKM-App/app/src/main/ic_launcher_new-playstore.png" width="100" height="100" align="right" />

# Fashionism User App
Product based capstone project Bangkit 2023

This application is designed for users who want a fashion recommendation application from local products based on their style.

---

## Features

- **Sign In and Sign Up** 
- **Homepage that displays recommendations for local fashion products** 
- **Fashion Local Product Filter Based on Category**
- **Recommendation Fashion Local Product Based on Images You Have/Take Pictures Directly**
- **Purchase Product (Coming soon)**
- **Favorite Product**
- **Share Product to Others**
- **Change Profile Data Account** 

---

## Getting Started

To get started with the Fashionism User App, follow these steps:

1. Click sign up text to create your account
2. If you already have an account, you can proceed to sign in
3. In the homepage, you can choose from many fashion product recommendations, and there is also a category feature to group fashion products that appear.
4. In the detail fashion product, you can see detailed information about the fashion product, and you can share the detailed product with others by clicking the share button
5. You can get recommendations for local fashion products other than those displayed on the homepage based on images you have/take pictures directly by clicking the search by image button on the homepage.
6. In the top right corner of the homepage, you can directly navigate to your profile without clicking through the settings.
7. In the settings menu, there are other menu that is profile, my favorite, and change password.
8. In the profile menu, you can see and update your profile data, such as photo, name, email, address, and phone number.
9. In the my favorites, you can see which products you have clicked as favorites.
10. In the change password menu, you can change your password. Make sure to input your older password and match the new password with the confirm new password

---

## Screenshots
<img src="https://github.com/Fashionism-Bangkit-Capstone/Fashionism-Mobile-Development/assets/97342935/80f6b890-7357-497e-8614-e8ec4dab7462" align="center" />

---

## Libraries We Use

| Library name  | Usages        | Dependency    |
| ------------- | ------------- | ------------- |
| [Retrofit2](https://square.github.io/retrofit/) | Request API and convert json response into an object | implementation "com.squareup.retrofit2:retrofit:2.9.0" <br> implementation "com.squareup.retrofit2:converter-gson:2.9.0" |
| [OkHttp](https://square.github.io/okhttp/) | Make a data request to the server | implementation "com.squareup.okhttp3:logging-interceptor:4.9.0" |
| [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle?hl=id) | Connecting frontend and backend | implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1" <br> implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.6.1" <br> implementation 'androidx.activity:activity-ktx:1.7.0' |
| [Navigation component](https://developer.android.com/guide/navigation)| Navigation between pages | implementation "androidx.navigation:navigation-fragment-ktx:2.5.3" <br> implementation "androidx.navigation:navigation-ui-ktx:2.5.3" |
| [Circle imageview](https://github.com/hdodenhof/CircleImageView)| Custom circle imageview | implementation 'de.hdodenhof:circleimageview:3.1.0' |
| [Glide](https://github.com/bumptech/glide)| Media management and image loading  | implementation 'com.github.bumptech.glide:glide:4.15.1' |
| [Lottie](https://github.com/airbnb/lottie-android)| Animation icon effect | implementation "com.airbnb.android:lottie:6.0.0" |
| [Local data store](https://developer.android.com/topic/libraries/architecture/datastore)| Local storage data | implementation "androidx.datastore:datastore-preferences:1.0.0" |
| [Smooth bottombar](https://github.com/ibrahimsn98/SmoothBottomBar)| Custom bottombar  | implementation 'com.github.ibrahimsn98:SmoothBottomBar:1.7.9' |
| [Material Design](https://github.com/material-components/material-components-android) | Better UI Component | implementation 'com.google.android.material:material:1.9.0' |
