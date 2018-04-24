# typeofmood-ime-master
This is just an example of latinIME implementation for android studio. 
Originaly the project was uploaded from ctrl-alt-del(https://github.com/ctrl-alt-del/) but it doesn't exist anymore.
All i did was modifications at the package name and added more prebuild binary dictionaries.

If u want to try it out, without building the whole aosp tree feel free to clone it.
If u want to change the package names, in order for the keyboard to work u will need also to change any reference to previous package name including the .cpp and .h files that are used to build the libjni_latinime.so libraries.
If i you want to use the latest AOSP LatinIME files, build this project once after u have moderated your prefered package name and then u can take the libjni_latinime.so files (for all platforms) from this location \app\build\intermediates\cmake\debug\obj and follow this guide https://github.com/iwo/LatinIME-Android-Studio .
