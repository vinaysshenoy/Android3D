# Android3D

Sample Application for mixing Android and [Libgdx](https://github.com/libgdx/libgdx). This project is geared towards integrating 3D interactions and Android UI, and **NOT** towards building complex 3D application.

## Structure
There are three main modules in the project
1. libcore3d: A pure Java module that contains any 3D scenes and animations we want to show
2. desktop-app: A pure Java desktop application(depends on libcore3d) that can be run to test your 3D scenes without needing to build the Android application
3. app: The Android application(depends on libcore3d)

## Usage
1. Import into Android Studio using Gradle and let it set up the initial project
2. In your `Run Configurations`, along with the usual Android build, a `Run Desktop Application` should have been created that will let you execute the desktop app

## Note
1. [Core3d](https://github.com/vinaysshenoy/Android3D/blob/master/libcore3d/src/main/java/com/vinaysshenoy/core3d/Core3d.java) is where the bulk of the 3D application lies. This includes setup of the scene, lighting, materials, models etc as well as exposed methods to manipulate whatever specifically is needed for the animation. Both the Desktop and Android applications will create an instance of this class and use it to render in their respective `View`
2. [CubeAnimationFragment](https://github.com/vinaysshenoy/Android3D/blob/master/app/src/main/java/com/vinaysshenoy/android3d/CubeAnimationFragment.java) is the Android `Fragment` which is used to display whatver 3D code is set up in `Core3D`
3. Whenever `libgdx` version is updated, run the gradle task `app:copyNativeLibs` to copy the updated native libraries into the Android jniLubs as well
4. All the native libs together use a little more than 4MB in the final APK. This can be reduced in two ways - If you're not planning to have 3D animations or manipulations on text, remove the `freetype` dependencies since they will not be needed. On top of this, you can employ ABI splits, or opt to not include since target platforms to reduce your build size. Check out the `copyNativeLibs` gradle task in `app/build.gradle` to remove whatever platforms you don't want to support

## Future improvements
1. Make this a general framework that can be used to implement 3D animations in Android
2. Tie this to Android Views and ViewGroups to automatically create powerful View level 3D animations
3. See if a custom, lightweight scene graph library can be used to replace libgdx
