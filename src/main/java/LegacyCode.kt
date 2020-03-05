import Constants.Companion.REED_KEYWORDS
import Constants.Companion.REED_LOCATION
import Constants.Companion.REED_PASSWORD
import Constants.Companion.REED_USERNAME
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.math.roundToInt

fun StartSearch(){

    //Open Chrome
    System.setProperty("webdriver.chrome.driver","C:\\Selenium\\selenium-java-3.141.59\\chromedriver_win32\\chromedriver.exe" )
    val driver = ChromeDriver()

    //open reed website login
    driver.get("https://www.reed.co.uk/account/signin?returnUrl=%2F#&card=signin")
    val wait = WebDriverWait(driver, 20)

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

//    //submit search
    jobSearchEditText.sendKeys(REED_KEYWORDS)
    driver.findElementByXPath("//*[@id=\"location\"]").sendKeys(REED_LOCATION)
    driver.findElementByXPath("//*[@id=\"main-search\"]/div[1]/div[3]/button").click()

    //todo: change to wait
    Thread.sleep(1500)

    val ad = driver.findElementByXPath("//*[@id=\"content\"]/div[1]/div[2]/h1")
    wait.until(ExpectedConditions.elementToBeClickable(ad))

    //find number of pages
    val text = driver.findElementByCssSelector("div.page-counter").text /* eg. 1 - 25 of 99 jobs */
    print(text)
    val count = text.toTotalCount()
    val pages = count.getNumberOfPages()

    //loop through pages of search
    for (i in 1..pages){

        //open page by number on search
        //todo: change this url builder
        driver.get("https://www.reed.co.uk/jobs/android-developer-jobs-in-kilburn-london?pageno=$i")
        Thread.sleep(2500)

        //elements list of jobs on page
        val list = driver.findElementsByCssSelector("div.col-sm-12.col-md-9.col-lg-10.details")

        //turn list into global list job object
        list.forEach {
            val badge = it.findElement(By.cssSelector("div.badge-container"))
            //check if there is a badge element
            if (badge.isDisplayed){
                //see if applied is in badge
                val applied = badge.findElements(By.cssSelector("span.label.label-applied"))
                //if applied doesnt exist then add to global list of jobs
                if (applied.isNullOrEmpty()){
                    val jobObject = it.toJobObject()
                    jobsList.add(jobObject)
                }

            }else{
                //no badge exists so add to list of jobs declared at the top
                val jobObject = it.toJobObject()
                jobsList.add(jobObject)
            }
        }
    }

    //loop through the jobs collected
    jobsList.forEach{
        //open the URl
        driver.get(it.url)

        val title = driver.findElementByXPath("//*[@id=\"content\"]/div/div[2]/article")
        wait.until(ExpectedConditions.elementToBeClickable(title))

        //check for external apply element
        val applyExternal = driver.findElementsByCssSelector("span.external-app-caption")

        //if external apply is empty then apply for job
        if (applyExternal.isNullOrEmpty()){

            print(it.jobTitle + " ${it.url} \n" )


            //find apply button
            val applyNow = driver.findElementsByXPath("//*[@id=\"applyButtonSide\"]")

            if (!applyNow.isNullOrEmpty()){

                //click apply
                applyNow[1].click()

                try{
                    val successfulApplied = driver.findElementByXPath("//*[@id=\"content\"]/div/div[1]/a")
                    wait.until(ExpectedConditions.visibilityOf(successfulApplied))
                }catch (e: Exception){
                    println(it.jobId + " did not apply")
                    println("\n" + e.toString() + "\n")
                }
            }
        }
    }

}

fun String.toTotalCount() : Int = this.substringAfter("of ").substringBefore( " jobs").toInt()

fun Int.getNumberOfPages():Int = if (this % 25 ==0){
    this/25;
}else{
    (this.toDouble()/25).roundToInt()
}

fun WebElement.toJobObject():JobObject {
    val attribute = this.findElement(By.tagName("a"))

    val id = attribute.getAttribute("data-id")
    val url = attribute.getAttribute("href")
    val jobtitle = attribute.getAttribute("title")

    val location = this.findElement(By.xpath("//*[@id=\"jobSection${id}\"]/div[1]/div[1]/ul[2]/li")).text
    val company = this.findElement(By.xpath("//*[@id=\"jobSection${id}\"]/div[1]/header/div[2]/a")).text

    return JobObject("reed-${id}","Reed.co.uk",jobtitle,location,company,url)

}