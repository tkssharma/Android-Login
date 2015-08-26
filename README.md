# Android layout for login screen 

Custom login registeration screen with complete Validation and storing data on remote and locally using sqllite database 

- SqlDBHelper is used to save data locally for user
- Calling external service to update DB on PHP server 
- validate user login using external call 

# Android Studio App 

> Material Design Specifications[Navigation Drawer](http://blog.teamtreehouse.com/add-navigation-drawer-android) 
> Creating Apps with Material Design[Material Design](http://developer.android.com/training/material/index.html) 

## Running Locally
Make sure you have [Android studio/Eclipse ADB](http://developer.android.com/tools/studio/index.html) 

```sh
https://github.com/tkssharma/worldLibrary.git
import project in app studio
run gradle build 
```

## Create a Drawer Layout

Custom Library are used to show data on UI View , LoopJ is to make Async calls and Picasso is best way of image caching and loading on UI 
```sh
       compile 'com.android.support:appcompat-v7:23.0.0'
    compile 'com.android.support:design:23.0.0'
    compile 'com.mcxiaoke.volley:library:1.0.17'
    compile 'com.jakewharton:butterknife:6.1.0'
    compile 'com.loopj.android:android-async-http:1.4.8'
    compile 'com.android.support:support-v4:23.0.0'
```


```sh
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:fitsSystemWindows="true"
    android:background="@color/primary">

    <LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingTop="56dp"
        android:paddingLeft="24dp"
        android:paddingRight="24dp">

        <ImageView android:src="@drawable/logo"
            android:layout_width="wrap_content"
            android:layout_height="72dp"
            android:layout_marginBottom="24dp"
            android:layout_gravity="center_horizontal" />

        <!--  Email Label -->
        <android.support.design.widget.TextInputLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:layout_marginBottom="8dp">
            <EditText android:id="@+id/input_email"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textEmailAddress"
                android:hint="Email"
                android:textColor="#ffffff" />
        </android.support.design.widget.TextInputLayout>

        <!--  Password Label -->
        <android.support.design.widget.TextInputLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:layout_marginBottom="8dp">
            <EditText android:id="@+id/input_password"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textPassword"
                android:hint="Password"
                android:textColor="#ffffff" />
        </android.support.design.widget.TextInputLayout>

        <android.support.v7.widget.AppCompatButton
            android:id="@+id/btn_login"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:layout_marginBottom="24dp"
            android:padding="12dp"
            android:text="Login"
            android:background="@color/primary_dark"
            android:textColor="#ffffff" />

        <android.support.v7.widget.AppCompatButton
            android:id="@+id/btn_facebook_login"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:layout_marginBottom="24dp"
            android:padding="12dp"
            android:text="Login Facebook"
            android:background="@color/primary_dark"
            android:textColor="#ffffff" />

        <TextView android:id="@+id/link_signup"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="24dp"
            android:text="No account yet? Create one"
            android:gravity="center"
            android:textSize="16dip"
            android:textColor="#ffffff" />

    </LinearLayout>
</ScrollView>
```

```sh

<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:background="@color/primary"
    android:fitsSystemWindows="true">

    <LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingTop="56dp"
        android:paddingLeft="24dp"
        android:paddingRight="24dp">

        <ImageView android:src="@drawable/logo"
            android:layout_width="wrap_content"
            android:layout_height="72dp"
            android:layout_marginBottom="24dp"
            android:layout_gravity="center_horizontal" />

        <!--  Name Label -->
        <android.support.design.widget.TextInputLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:layout_marginBottom="8dp">
            <EditText android:id="@+id/input_name"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textCapWords"
                android:hint="Name"
                android:textColor="@color/accent" />
        </android.support.design.widget.TextInputLayout>

        <!-- Email Label -->
        <android.support.design.widget.TextInputLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:layout_marginBottom="8dp">
            <EditText android:id="@+id/input_email"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textEmailAddress"
                android:hint="Email"
                android:textColor="@color/accent" />
        </android.support.design.widget.TextInputLayout>

        <!-- Password Label -->
        <android.support.design.widget.TextInputLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:layout_marginBottom="8dp">
            <EditText android:id="@+id/input_password"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textPassword"
                android:hint="Password"
                android:textColor="@color/accent" />
        </android.support.design.widget.TextInputLayout>

        <!-- Signup Button -->
        <android.support.v7.widget.AppCompatButton
            android:id="@+id/btn_signup"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:layout_marginBottom="24dp"
            android:padding="12dp"
            android:text="Create Account"
            android:textColor="@color/accent"
            android:background="@color/primary_dark" />

        <TextView android:id="@+id/link_login"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="24dp"
            android:text="Already a member? Login"
            android:gravity="center"
            android:textSize="16dip"
            android:textColor="@color/accent" />

    </LinearLayout>
</ScrollView>
```