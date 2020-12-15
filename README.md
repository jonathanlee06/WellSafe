# WellSafe :blue_heart:
[<img src="https://github.com/jonathanlee06/WellSafe/blob/master/app/src/main/res/drawable/wellsafe.png" alt="drawing" width="200"/>](https://github.com/jonathanlee06/WellSafe/blob/master/app/src/main/res/drawable/wellsafe.png)
<!--- ![alt text](https://github.com/jonathanlee06/WellSafe/blob/master/app/src/main/res/drawable/wellsafe.png =200x200) --->
WellSafe is a COVID-19 Safety Measures Monitoring App developed by [Jonathan Lee](https://www.github.com/jonathanlee06) using Android Studio. The aim of the application is to help reduce human contact during check-in process at any public location.


> If you see anything you like, a :star: will be much appreciated!

## :arrow_down: Download
[v0.1 Alpha](https://github.com/jonathanlee06/WellSafe/releases/tag/v0.1-alpha)

## :bulb: Features
| Name | Description | Status | Note |
| :-------------: | :----------: | :-------------: | :----------: |
| User Authentication | In order to use this application, every user must register and log in | :white_check_mark: | Completed |
| Social Distancing Alert | Use Bluetooth scanning to alert user when social distancing is not practised | :arrows_counterclockwise: | TODOs |
| QR Code Location Check-In | Check-in by scanning QR code |:white_check_mark: | Completed |
| COVID-19 Stistics | Latest updates on COVID-19 numbers in Malaysia | :white_check_mark: | Completed |

#### User Authentication
* User authentication using Firebase

#### Social Distancing Alert
* This feature will require location & bluetooth access in order to work
* `v0.1 Alpha` utilizes Bluetooth discovery scanning, will alert user when nearby bluetooth device(s) is detected

#### QR Code Location Check-In
* This feature will require camera access in order to work
* `v0.1 Alpha` utilizes a custom QR-Code pre-generated by me to check-in (Currently it is generated by me and not the businesses)
* The system will only recognize QR-Code with the format as shown below
    ```
    WS-CI
    Petronas Twin Towers, Kuala Lumpur, Malaysia
    ```

#### COVID-19 Statistics
* This feature utilizes API by [Nuttaphat Arunoprayoch](https://github.com/nat236919)
* The source data of the API is from [Johns Hopkins University](https://github.com/CSSEGISandData/COVID-19)

#### Future New Implementation
* Will look into using Bluetooth beacons and UUID for social distancing alert
* Will consider adding global COVID-19 statistics
* Will plan to launch an web application to accompany the mobile application solely for businesses to register for their unique QR-Code 


## :iphone: Hardware Requirements
* Android smartphone with Android 5.0 Lollipop and above
    >This application was developed and tested on Android 10



## :hammer: Building the project
This project is built using Android Studio 4.0 & Java. You can view the source code of the project using Android Studio by following these steps:
1. Clone this project to your desired location
2. Open the project folder using Android Studio

## :computer: Technologies
1. Android Studio
2. Firebase Realtime Database

## :books: External Libraries

| Name                                                                              | Usage                                               |
| :-------------                                                                    | :----------:                                        |
|  [Country Code Picker Library](https://github.com/hbb20/CountryCodePickerProject) | Choose phone number country code for registration   |
|  [ZXing QR-Code Scanner Library](https://github.com/zxing/zxing)                  | For check-in QR code scanning                       |
|  [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)                      | To generate graphs for COVID-19 statictics          |

## :briefcase: API
* [COVID-19 API](https://github.com/nat236919/covid19-api) (This API was chosen because it offers Malaysian statistics & update daily)

## :page_with_curl: Note
Although this just an assignment project, I'm planning to continue maintaining it. Feel free to fork this repository to improve this application. If you have any questions regarding this repository, please do not hesitate to contact me [here](mailto:jonathanlee06@outlook.com)

## :octocat: Author
[Jonathan Lee](https://github.com/jonathanlee06)

## :bookmark_tabs: Licenses
This project is licensed under the MIT license. See the [LICENSE](https://github.com/jonathanlee06/WellSafe/blob/master/LICENSE) file for more info.
