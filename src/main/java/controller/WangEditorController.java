package controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tool.WangEditorTool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


/**
 * @author 志军
 * 用来测试WangEditor插件
 */

@Controller
@RequestMapping("")
public class WangEditorController {

    @RequestMapping(value = "/Editor/index",method = RequestMethod.GET)
    public String index(){
        return "wangEditor";
    }


    @RequestMapping("/Editor/updateImage")
    public void updateImage(HttpServletRequest request, HttpServletResponse response) throws FileUploadException, InterruptedException, IOException {
        WangEditorTool tool = new WangEditorTool(request);
        tool.execRequest();
        String fileName = "http://localhost:8033/Spring/" + tool.getMap().get("FilePath").toString().substring(78);

        String href = "<img src='"+ fileName + "' style=\"max-width: 100%;\"></p><p><br></p>";
        System.out.print(href);

        //直接返回会报错,因为富文本不接受这样格式的报文返回
        // sendResponse(response,href);
        sendResponseJson(response,href);
        System.out.println("富文本图片进来了");
    }

    @RequestMapping("/Editor/sendText")
    public void sendText(HttpServletRequest request, HttpServletResponse response, @RequestBody String body) throws UnsupportedEncodingException {

        //通过过在MvcComponentLoder配置了一个编码过滤器，所以这里解析是正常的。我们可以把这个东西保存到数据库
        System.out.println(request.getParameter("data"));

        System.out.println("富文本上传数据进来了");
    }

    @RequestMapping("/Editor/getDataAndShow")
    public void getDataAndShow(HttpServletResponse response,HttpServletRequest request){
        String data = "<p>欢迎使用 <b>wangEditor</b> 富文本编辑器<img src=\"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=714624164,1707887834&amp;fm=27&amp;gp=0.jpg\" style=\"max-width: 100%;\"></p><p><br></p>";

        sendResponse(response,data);

    }

    /**
     * 发送数据到前端 -- 通过设置response的编码保证正确的返回
     */
    public void sendResponse(HttpServletResponse response,String data) {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print(data);
        }catch(Exception ex){
            //logger输出堆栈信息
        }finally{
            assert writer != null;
            writer.flush();
            writer.close();
        }

    }

    /**
     * 发送数据到前端 -- 通过设置response的编码保证正确的返回
     */
    public void sendResponseJson(HttpServletResponse response,String data) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errno","0");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(data);
        jsonObject.put("data",jsonArray);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();

            writer.print(jsonObject);
        } catch(Exception ex){
            ex.printStackTrace();
        }finally{
            assert writer != null;
            writer.flush();
            writer.close();
        }

    }
}
