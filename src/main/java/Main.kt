import Constants.Companion.REED_PASSWORD
import Constants.Companion.REED_USERNAME
import api.network.NetworkRequests
import api.network.responses.ReedJobObject
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.system.exitProcess


var appliedJobsList = mutableListOf<ReedJobObject>()
var reedJobsList = listOf<ReedJobObject>()
lateinit var driver: ChromeDriver
lateinit var wait: WebDriverWait

const val driverPath = "C:\\Selenium\\selenium-java-3.141.59\\chromedriver_win32\\chromedriver.exe"
public fun main(args: Array<String>){
    getJobsFromReedApi()
    setupWebDriver()
    logonToReed()
    applyForJobsThroughLoop {
        appliedJobsList.writeToOut()
        driver.close()
        exitProcess(2)
    }
}

fun getJobsFromReedApi(){
    val result = NetworkRequests().getSearchApi()
    result?.let {
        reedJobsList = it
        return
    }
    // No results found so exit
    exitProcess(2)
}

fun setupWebDriver(){
    //Open Chrome
    System.setProperty("webdriver.chrome.driver", driverPath)
    driver = ChromeDriver()
    wait = WebDriverWait(driver, 20)
}

fun logonToReed(){
    driver.get("https://www.reed.co.uk/account/signin?returnUrl=%2F#&card=signin")
    waitForPageToLoad()

    //insert credentials and sign in
    driver.findElementByXPath("//*[@id=\"Credentials_Email\"]").sendKeys(REED_USERNAME)
    driver.findElementByXPath("//*[@id=\"Credentials_Password\"]").sendKeys(REED_PASSWORD)

    // Click login and wait for page to load
    driver.findElementById("signin-button").click()
    waitForPageToLoad()
}

fun applyForJobsThroughLoop(
        complete: () -> Unit
){
    reedJobsList.forEach {
        try {
            // load url
            driver.get(it.jobUrl)
            // load [age
            waitForPageToLoad()
            // apply for job
            applyForJob(it)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    complete()
}

fun applyForJob(jobObject: ReedJobObject){

    if (hasUserNotAppliedBefore()){
        println("${jobObject.jobId} has not been applied")
        //find apply button
        val applyNow = driver.findElementsByXPath("//*[@id=\"applyButtonSide\"]")

        if (!applyNow.isNullOrEmpty()){
            //click apply
            val index = if (applyNow.size > 1){ 1 }else{ 0 }
            applyNow[index].click()
            waitForPageToLoad()
            println("${jobObject.jobId} has now been applied for")
            appliedJobsList.add(jobObject)
            return
        }
        println("${jobObject.jobId} could not be applied for")
    }else{
        println("${jobObject.jobId} has been applied for previously")
    }
}

// Checks to see if user has applied before
fun hasUserNotAppliedBefore(): Boolean{
    // find "applied before" text
    val appliedBefore = driver.findElementsByXPath("//*[@id=\"content\"]/div/div[2]/article/div/div[1]/div")
    return appliedBefore.isNullOrEmpty()
}

fun waitForPageToLoad(){
    //wait for page to load
    wait.until{driv ->
        (driv as JavascriptExecutor).executeScript("return document.readyState") == "complete"
    }
}
