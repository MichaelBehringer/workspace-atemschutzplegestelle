package program;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class Background extends PdfPageEventHelper {
	com.itextpdf.text.Image headerImg;
	
	public Background(Image headerImg) {
		this.headerImg = headerImg;
	}
	
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContentUnder();
        Image image = Image.getInstance(headerImg);
        image.scaleAbsoluteHeight(PageSize.A4.getHeight());
        image.scaleAbsoluteWidth(PageSize.A4.getWidth());
       // image.scaleAbsolute(PageSize.A4.rotate());
        image.setAbsolutePosition(0, 0);
       canvas.saveState();
        PdfGState state = new PdfGState();
        //state.setFillOpacity(0.6f);
        canvas.setGState(state);
        try {
			canvas.addImage(image);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        canvas.restoreState();
    }
}