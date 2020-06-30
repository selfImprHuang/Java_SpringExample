package tool;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import util.Base64Util;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 志军
 * 配合Wangeditor一起处理的工具类
 */
public class WangEditorTool {
    //对request请求进行保存
    private HttpServletRequest request;
    //保存文件名称
    private String fileName;
    //保存文件的后缀
    private String suffix;
    //保存完整的文件名
    private String completeFileName;
    //用来存储FileItemStream的信息，看最后这些东西是什么
    private ArrayList<String> fileItemStreamNames = new ArrayList<String>();
    //用来保存传过来的参数？
    private HashMap<String, Object> map = new HashMap<String, Object>();
    //用来缓存文件流的 --发现说流是缓存不下来的，因为流一旦读取完毕就是close状态
    private InputStream inputStream;
    //通过list来缓存byte，因为在文件读取的时候这个byte大小可能不一定全是那么大就会大致，写入失败
    private List<byte[]> byteList = new ArrayList<byte[]>();


    public WangEditorTool() {
    }

    public WangEditorTool(boolean b) {
        if (!b) {
            this.completeFileName = "我怀疑你是一个小胖子";
        }
    }

    public WangEditorTool(HttpServletRequest request) {
        System.out.println(request.getParameter("name"));
        System.out.println(request.getRequestURI());
        this.request = request;

    }

    //参考地址：https://commons.apache.org/proper/commons-fileupload/streaming.html
    /**
     * 通过spring提供的类来实现request的解析，读取他的流和流中对应的数据
     * 请注意field和非field的区别
     */
    public WangEditorTool execRequest() throws FileUploadException, IOException, InterruptedException {

        //判断是不是ajax请求？
        boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;
        //判断是不是文件请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            return new WangEditorTool(false);
        }
        //spring提供的对于request请求携带文件信息的处理工具类
        ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
        //设置头部编码？
        if (isAjaxUpload) {
            servletFileUpload.setHeaderEncoding("UTF-8");
        }
        //解析获取request中的流
        FileItemIterator iterator = servletFileUpload.getItemIterator(request);
        //用来判断是否没有数据和接收数据
        FileItemStream fileItemStream = null;

        while (iterator.hasNext()) {
            //对迭代器进行读取，这里的读取是跟浏览器进行信息交互，所以流打开和读取是一次性的，为了多次读取，试试能不能把流缓存在本地
            fileItemStream = iterator.next();
            fileItemStreamNames.add("name = " + fileItemStream.getName());
            fileItemStreamNames.add("fieldNames = " + fileItemStream.getFieldName());
            //判断是不是表单数据
            if (fileItemStream.isFormField()) {
                //打开流
                InputStream inputStream = fileItemStream.openStream();
                String id = fileItemStream.getFieldName();
                String value = Streams.asString(inputStream, "UTF-8");
                //进行表单数据的缓存 -- 该流已经结束读取 和浏览器之间的读取
                map.put(id, value);
                //关闭流
                inputStream.close();
            }
            //不是表单数据，是文件？进行读取？ --文件不是表单数据
            if (!fileItemStream.isFormField()) {
                //文件名
                String fileName = fileItemStream.getName();
                //文件类型
                String ContentType = fileItemStream.getContentType();
                map.put("fileName", fileName);
                map.put("ContentType", ContentType);

                InputStream inputStream = fileItemStream.openStream();
                //文件大小？
                int size = inputStream.available();
                map.put("size", size);
                map.put("inputStream", inputStream);
                this.inputStream = inputStream;
                //读取文件的字节
                byte[] bytes = new byte[size];
                //用来读取的字节大小
                byte[] bytes1 = new byte[1024];

                int read;
                String path = getRandomFilePath(request, bytes, fileName);
                map.put("FilePath",path);
                BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(new File(path)));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                StringBuilder bytesS = new StringBuilder();
                //放在外面读而不是把字节存下来读取，可以保证顺序存储不出错
                while ((read = inputStream.read(bytes1)) != -1) {
                    bof.write(bytes1,0,read);
                    byteList.add(bytes1);
                    bytesS.append(Arrays.toString(bytes1));
                    byteArrayOutputStream.write(bytes1, 0, read);
                }
                map.put("byteList",byteList);
                map.put("bytes", bytesS.toString());
                byte[] fileByteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();

                //也就是说我不可能缓存流，但是可以把流中的字节缓存下来。
                map.put("fileByteArray",fileByteArray);
                fileByteArray = (byte[]) map.get("fileByteArray");

                //将字节转为String？类似在数据库中的存储
                String fileBase64 = Base64Util.encode(fileByteArray);
                inputStream.close();
                System.out.println(fileBase64);

                Thread.sleep(1000);
                //对流进行转换并读写
                writeStringToByte(fileBase64,fileName);

                Thread.sleep(1000);
                //对流进行转换并读写  ---不进行转码
                writeStringToByte(fileName,fileByteArray);
            }
        }

        //如果遍历没给值，说明为空
        if (fileItemStream == null) {
            return new WangEditorTool(false);
        }

        return new WangEditorTool(true);
    }

    /**
     * 通过转成base64的String 模拟进数据库，在回头去把数据decode回来成byte[]，然后写成文件
     */
    public void writeStringToByte(String fileBase64,String fileName) throws IOException {
        String path = getRandomFilePath(request, new byte[0], fileName);
        BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(new File(path)));
        byte[] bytes = Base64Util.decodeByte(fileBase64);

        bof.write(bytes);
        bof.flush();
        bof.close();
    }

    /**
     * 通过byteArrayOutputStream来获取这个byte流的信息，然后直接传进来读取
     * 实验一下如果缓存byte在本地可以读取 的操作
     */
    public void writeStringToByte(String fileName,byte[] unicodeByte) throws IOException {
        String path = getRandomFilePath(request, new byte[0], fileName);

        BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(new File(path)));
        InputStream fis = new ByteArrayInputStream(unicodeByte);
        byte[] bytes = new byte[1024];

        int read;
        while((read = fis.read(bytes) )!=-1){
            bof.write(bytes,0,read);
        }

        bof.flush();
        fis.close();
        bof.close();
    }

    /**
     * 流存不下来，他会自动关闭掉，那我应该怎么办？？？ -- 我尝试用缓存的byte来做？？？
     */
    public void getStreamAndRead(String fileName) throws IOException {
        //用来读取的字节大小
        byte[] bytes1 = new byte[1024];

        String bytesS = (String) map.get("bytes");

        String path = getRandomFilePath(request, bytes1, fileName);
        BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(new File(path)));

        byte[] bytes2 = bytesS.getBytes(StandardCharsets.UTF_8);
        System.out.println(bytesS);

        bof.write(bytes2);
    }

    /**
     * 进行缓存参数的输出
     */
    public void getThingsInRequest() {
        System.out.println("list数据");
        for (int i = 0; i < fileItemStreamNames.size(); i++) {
            System.out.println((i + 1) + ":" + fileItemStreamNames.get(i) + ";");
        }
        System.out.println("map的数据");
        for (Map.Entry<String, Object> esi : map.entrySet()) {
            System.out.println(esi.getKey() + ":" + esi.getValue());
        }
    }

    /**
     * 获取文件名和文件地址
     */
    public String getRandomFilePath(HttpServletRequest request, byte[] bytes, String fileName) throws IOException {
        String springPath;
        springPath = request.getServletContext().getRealPath("/");
        SimpleDateFormat.getInstance().format(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dir = simpleDateFormat.format(new Date());
        System.out.println(dir);
        //通过正确的间隔符来标识文件路径 + File.separator
        springPath = springPath + dir + File.separator;
        System.out.println("文件路径" + springPath);
        File file1 = new File(springPath);
        if (file1.exists() && file1.isDirectory()) {
        } else {
            file1.mkdirs();
        }

        springPath = springPath + File.separator + fileName;
        File file = new File(springPath);
        if (!file.exists()) {
            file.createNewFile();
        }

        return springPath;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HashMap getMap(){
        return this.map;
    }
}
