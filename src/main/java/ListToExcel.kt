import api.network.responses.ReedJobObject
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import kotlin.reflect.full.memberProperties


fun List<ReedJobObject>.writeToOut(){
    try {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("sheet1") // creating a blank sheet

        val headerList = takeKClass<ReedJobObject>()
        val topRow = sheet.createRow(0)
        ReedJobObject::class.memberProperties.forEachIndexed { headerPos, item ->
            topRow.createCell(headerPos).setCellValue(headerList[headerPos])
        }

       this.forEachIndexed { index, reedJobObject ->
           val row = sheet.createRow(index + 1)

           ReedJobObject::class.memberProperties.forEachIndexed { headerPos, item ->
               row.createCell(headerPos).setCellValue(item.get(reedJobObject).toString())
           }
       }

        val dateTime = LocalDate.now().toString()
        val file = File("E:\\Reed search output\\Jobs applied - ${dateTime}.xlsx")
        val out = FileOutputStream(file) // file name with path
        workbook.write(out)
        out.close()
        val desktop = Desktop.getDesktop()
        if (file.exists()) //checks file exists or not
            desktop.open(file) //opens the specified file
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

//inline fun <reified T : Any> List<T>.writeToExcel() {
//    try {
//        val workbook = XSSFWorkbook()
//        val sheet = workbook.createSheet("sheet1") // creating a blank sheet
//        var rownum = 0
//
//        val headerList = takeKClass<T>()
//
//
//        for (user in this) {
//
//            val row: Row = sheet.createRow(rownum++)
//            createList(user, row)
//        }
//        val out = FileOutputStream(File("NewFile.xlsx")) // file name with path
//        workbook.write(out)
//        out.close()
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}


inline fun <reified T: Any> takeKClass(): List<String> {
    val reflection = T::class

    return reflection.members.map { it.name }
}

inline fun <reified T: Any> List<T>.getElements(): List<List<String>> {
    val reflection = T::class

    return this.map {
        reflection.memberProperties.map { kProperty1 ->
            kProperty1.get(it).toString()
        }
    }

}
