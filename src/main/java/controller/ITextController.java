package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import util.itext.PDFUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ITextController {

    //itext 生成pdf的 工具类
    private PDFUtils pdfUtils = new PDFUtils();

    /**        将一个html转移成String
     String htmlString  = StringEscapeUtils.unescapeHtml(html);
     * 参数介绍:
     * html:从页面获取的html信息(获取到的html是没有<html></html>标签的,需要后期加上)
     * cssName:从页面获取的css的名字
     * cssPath:css对应的绝对地址
     * htmlReadyToPdf:将html的标签加上,补充完整的html文件
     * waterMarkImage:水印的图片的名字
     * waterMarkImagePath:水印图片地址
     * templePdfname:pdf模板的名字
     * templePdfPath:pdf模板的地址
     *
     * pdfFileDirs:存放pdf文件的路径
     * pdfFileSavePath:生成pdf文件存储路径的文件
     *
     * pdfFileName:没有水印之前的pdf名字
     * pdfFilePath:没有水印的pdf的路径
     * pdfFile:没有水印的pdf文件
     * pdfFileWithWaterMarkName:加了水印的pdf文件名
     * pdfFileWithWaterMarkPath:加了水印的pdf的地址
     * pdfFileWithWaterMark:加了水印的pdf的文件
     *
     * programPath:工程地址
     *
     * responseUrl:返回到页面的工程地址
     **/
    @RequestMapping(value = "/ITextTest", method = RequestMethod.POST)
    public void itextTest(HttpServletResponse response, HttpServletRequest request, @RequestBody String body) throws Exception {

        String html = request.getParameter("html");
        String cssName = request.getParameter("cssName");
        String htmlReadyToPdf = pdfUtils.transToHtml(html);
        String waterMarkImage = "joint_theme_pdf_stamp.png";
        String templePdfname = "joint_theme_pdf_template.pdf";
        String pdfFileName = new SimpleDateFormat("YYYYMMddHHmmss").format(new Date()) + "temporary";
        String pdfFileWithWaterMarkName = new SimpleDateFormat("YYYYMMddHHmmss").format(new Date());

        //工程地址
        String programPath = request.getSession().getServletContext().getRealPath("");

        //生成css的绝对地址
        String cssPath = (programPath + "css\\" + cssName).replace("\\", System.getProperty("file.separator"));

        //生成水印的地址
        String waterMarkImagePath = (programPath + "WEB-INF\\pdf\\" + waterMarkImage).replace("\\", System.getProperty("file.separator"));

        //生成模板地址
        String templePdfPath = (programPath + "WEB-INF\\pdf\\" + templePdfname).replace("\\", System.getProperty("file.separator"));

        //生成存放PDF的地址
        String pdfFileDirs = programPath + "pdf" + System.getProperty("file.separator") + new SimpleDateFormat("YYYYMMdd").format(new Date());
        File pdfFileSavePath = new File(pdfFileDirs);
        if (!pdfFileSavePath.exists()) {
            pdfFileSavePath.mkdirs();
        }

        //生成临时pdf(没有水印)和有水印的pdf的路径
        String pdfFilePath = pdfFileDirs + System.getProperty("file.separator") + pdfFileName + ".pdf";
        String pdfFileWithWaterMarkPath = pdfFileDirs + System.getProperty("file.separator") + pdfFileWithWaterMarkName + ".pdf";

        //获取在js中显示的pdf地址
        String responseUrl = ("pdf" + "\\" + new SimpleDateFormat("YYYYMMdd").format(new Date()) + "\\" + pdfFileWithWaterMarkName + ".pdf").replace("\\", "/");

        //生成临时pdf和最终pdf的文件
        File pdfFile = new File(pdfFilePath);
        File pdfFileWithWaterMark = new File(pdfFileWithWaterMarkPath);

        //调用PDFUtils生成pdf文件
        PDFUtils.generalPDF(templePdfPath, pdfFilePath, cssPath, htmlReadyToPdf);


        //进行水印操作
        PDFUtils.sealDocument(pdfFilePath, pdfFileWithWaterMarkPath, waterMarkImagePath, "小胖子是谁：陈毅聪啊", "出具时间", "");
        //把临时pdf删掉
        pdfFile.delete();

        PrintWriter writer = response.getWriter();
        //输出产生的PDF地址
        writer.write(responseUrl);
        writer.flush();
        writer.close();
    }
}
