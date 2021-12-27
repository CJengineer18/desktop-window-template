# DesktopWindowTemplate
JFrame for dummies. Creates desktop windows for Java.

This allows to create a new JFrame for any Java application. Includes factory to create Windows-style status bar and different factories to create menus and popup menus.

## How to use
You only create a new class and extend for ```JGenericWindow``` class. In the constructor, call the function ```loadWorkArea()```, implement the method ```workArea()``` with you content, and the window is ready to show!

## Asynchronous utilities
From version v1.0.2, you can now process asynchronous processes and tasks using the ```AsyncProcessLoading``` and ```AsyncTask``` utility classes. See the documentation for more info.

## Changelog
- 25-12-2021: Added exception support for AsyncProcessLoading.
- 12-05-2020: Added new functions in JGenericWindow and AsyncTask classes.
- 08-01-2020: Added buttons default locale text.
- 07-01-2020: Added indeterminate mode, fixed worker null in AsyncTask.java
- 03-06-2019: Added async task utility class.
- 25-05-2019: Added async process utility class.
- 29-03-2019: Reduce minimum size (640x480 -> 200x200)
- 05-11-2018: First public version (v1.1-alpha) 

Documentation: https://cjengineer18.github.io/desktop-window-template/

## License

MIT
