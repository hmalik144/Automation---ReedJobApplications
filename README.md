# Automated Reed Job Applicator

Automated job applicator for reed.co.uk. 

## Installation

Clone project

```bash
git@github.com:hmalik144/Automation---ReedJobApplications.git
```

## Usage

Create a file "Constants.kt" in the java directory
```kotlin
class Constants {
    companion object{
        val REED_USERNAME = "" // Your reed username
        val REED_PASSWORD = "" // Your reed password
        val REED_KEYWORDS = "Android Engineer" 
        val REED_MINIMUM_SALARY = "" // minimum salary

        val REED_LOCATION = "" // Location (ideally post code)
        val REED_API_KEY = "" // Your reed api key
    }
}
```
## Built With

* [Retrofit](https://github.com/square/retrofit) - Type-safe HTTP client for Android and Java by Square, Inc
* [Apache POI](https://poi.apache.org/) - Apache POI - the Java API for Microsoft Documents

## Authors

* **Haider Malik** - *Android Developer* 

## License
[MIT](https://choosealicense.com/licenses/mit/)
