import Constants.Companion.REED_PASSWORD
import Constants.Companion.REED_USERNAME
import api.network.NetworkRequests
import api.network.responses.ReedJobObject
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


var jobsList = mutableListOf<JobObject>()
var reedJobsList = listOf<ReedJobObject>()
lateinit var driver: ChromeDriver
lateinit var wait: WebDriverWait

public fun main(args: Array<String>){
    setup()
    setupWebDriver()
    logonToReed()
    applyForJobsThroughLoop()
}

fun setup(){
    val result = NetworkRequests().getSearchApi()

    if (!result.isNullOrEmpty()){
        reedJobsList = result
    }

}

fun setupWebDriver(){
    //Open Chrome
    System.setProperty("webdriver.chrome.driver","C:\\Selenium\\selenium-java-3.141.59\\chromedriver_win32\\chromedriver.exe" )
    driver = ChromeDriver()
    wait = WebDriverWait(driver, 20)
}

fun applyForJobsThroughLoop(){

    reedJobsList.forEach {
        try {
            driver.get(it.jobUrl)
            applyForJob(it)

        }catch (e: Exception){
            println("\n" + e.toString() + "\n")
        }
    }
}

fun logonToReed(){
    driver.get("https://www.reed.co.uk/account/signin?returnUrl=%2F#&card=signin")

    //wait for page to load
    val lastElementToLoad = driver.findElementById("signin-button")
    wait.until(ExpectedConditions.elementToBeClickable(lastElementToLoad))

    //insert credentials and sign in
    driver.findElementByXPath("//*[@id=\"Credentials_Email\"]").sendKeys(REED_USERNAME)
    driver.findElementByXPath("//*[@id=\"Credentials_Password\"]").sendKeys(REED_PASSWORD)
    lastElementToLoad.click()

    //wait for page to load
    val jobSearchEditText = driver.findElementByXPath("//*[@id=\"keywords\"]")
    wait.until(ExpectedConditions.elementToBeClickable(jobSearchEditText))
}

fun applyForJob(jobObject: ReedJobObject){
    val appliedBefore = driver.findElementsByXPath("//*[@id=\"content\"]/div/div[2]/article/div/div[1]/div")

    if (appliedBefore.isNullOrEmpty()){
        println("${jobObject.jobId} has not been applied")
        //find apply button
        val applyNow = driver.findElementsByXPath("//*[@id=\"applyButtonSide\"]")

        if (!applyNow.isNullOrEmpty()){
            //click apply
            val index = if (applyNow.size > 1){ 1 }else{ 0 }
            applyNow[index].click()

            try{
//                val successfulApplied = driver.findElementByCssSelector("div.alert.alert-success alert-borderless")
                wait.until{
                    driver.executeScript("return document.readyState") == "complete"
                }

            }catch (e: Exception){
                println("\n" + e.toString() + "\n")
            }
        }
    }else{
        println("${jobObject.jobId} has been applied")
    }



}

