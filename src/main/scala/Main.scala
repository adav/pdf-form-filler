import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.interactive.form.{PDAcroForm, PDTextField}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream, PDResources}

import scala.collection.JavaConversions._

object Main extends App {

  val document = new PDDocument()
  val page = new PDPage(PDRectangle.A4)
  document.addPage(page)

  val font = PDType1Font.HELVETICA

  val resources = new PDResources()
  resources.put(COSName.getPDFName("Helv"), font)

  val form = new PDAcroForm(document)
  document.getDocumentCatalog.setAcroForm(form)

  form.setDefaultResources(resources)
  form.setDefaultAppearance("/Helv 0 Tf 0 g")



  //  new PDRectangle(100, 750, 200, 50)

  val f1 = FilledField("grapes", Rect(100, 750, 200, 50))
  val f2 = FilledField("lemon", Rect(100, 600, 200, 50))

  addField(f1, f2)
  writeText("moo", 10, 10)

  document.save("target/test.pdf")

  document.close()


  def addField(fields: FilledField*): Unit = fields.foreach { field =>
    val textBox = new PDTextField(form)
    form.getFields.add(textBox)

    val widget = textBox.getWidgets.head
    widget.setRectangle(field.rect.asPDRectangle)
    widget.setPage(page)
    widget.setPrinted(true)

    textBox.setValue(field.value)

    page.getAnnotations.add(widget)
  }

  def writeText(value: String, x: Float, y: Float): Unit = {
    val pageContent = new PDPageContentStream(document, page)
    pageContent.beginText()
    pageContent.setFont(font, 12)
    pageContent.newLineAtOffset(x, y)
    pageContent.showText(value)
    pageContent.endText()
    pageContent.close()
  }

}

case class Rect(x: Float, y: Float, width: Float, height: Float) {
  def asPDRectangle = new PDRectangle(x, y, width, height)
}
case class FilledField(value: String, rect: Rect)
/*

 */
